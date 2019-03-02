package com.skylynx13.transpath.task;

import com.skylynx13.transpath.Transpath;
import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.utils.*;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * ClassName: PackageChecker
 * Description: To rename file with a regular name by replace template.
 * Date: 2015-02-03 11:08:20
 */
public class PackageChecker extends SwingWorker<StringBuilder, ProgressData> {
    private ProgressParam progressParam = new ProgressParam();
    private ProgressData progressData = new ProgressData();

    @Override
    protected StringBuilder doInBackground() {
        String rootDirStr = TransProp.get(TransConst.LOC_TRANS);
        File rootDir = new File(rootDirStr);
        if (!rootDir.isDirectory()) {
            return new StringBuilder("Invalid root: ").append(rootDirStr);
        }
        if (Objects.requireNonNull(rootDir.listFiles()).length == 0) {
            return new StringBuilder("No files in: ").append(rootDirStr);
        }

        return check(Objects.requireNonNull(rootDir.listFiles()));
    }

    @Override
    protected void process(List<ProgressData> progressDataList) {
        ProgressData lastProgressData = progressDataList.get(progressDataList.size()-1);
        Transpath.getProgressBar().setValue(lastProgressData.getProgress());
        Transpath.getStatusLabel().setText(lastProgressData.getLine());
    }

    @Override
    protected void done() {
        try {
            StringBuilder result = get();
            TransLog.getLogger().info(result.toString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    StringBuilder check(File[] checkFiles) {
        Map<String, String> errorInfos = new HashMap<>();
        long totalSize = 0;
        for (File checkFile : checkFiles) {
            totalSize += checkFile.length();
        }
        progressParam.reset(totalSize, checkFiles.length);
        publishCheckPackageList();
        for (File checkFile : checkFiles) {
            errorInfos.putAll(checkPackage(checkFile.getPath()));
            progressParam.update(checkFile.length());
            publishCheckPackageList();
        }
        StringBuilder errInfoStr = new StringBuilder("Result: ");
        errInfoStr.append(errorInfos.size()).append(" error(s) found.").append(TransConst.CRLN);
        List<String> keyList = new ArrayList<>(Arrays.asList(errorInfos.keySet().toArray(new String[0])));
        Collections.sort(keyList);
        for (String key : keyList) {
              errInfoStr.append(FileUtils.toStandardPath(key)).append(" : ")
                  .append(errorInfos.get(key)).append(TransConst.CRLN);
        }
        return errInfoStr;
    }

    private void publishCheckPackageList() {
        progressData.setProgress(progressParam.calcProgressSize());
        progressData.setLine("Checking packages: " + progressParam.reportOfCount() + " files processed.        "
                + progressParam.reportTimeLeftBySize());
        publish(progressData);
    }

    Map<String, String> checkPackage(String fileName) {
        Map<String, String> errorInfos = new HashMap<>();
        List<Checker> checkers = new ArrayList<>();
        checkers.add(new RarChecker());
        checkers.add(new ZipChecker());

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.redirectErrorStream(true);

        if (isIgnorable(fileName)) {
            TransLog.getLogger().info(TransConst.PKG_IGNORE + ": " + fileName);
            return errorInfos;
        }

        if (!isValid(fileName) || new File(fileName).isDirectory()) {
            errorInfos.put(fileName, TransConst.PKG_TYPE_NOT_RECOGNIZED);
            return errorInfos;
        }

        String result;
        for (Checker checker : checkers) {
            processBuilder.command(checker.checkCommand(fileName));
            TransLog.getLogger().info("Command line: "
                    + String.join(" ", processBuilder.command()));
            result = processCommand(processBuilder);
            if (checker.checkOk(result)) {
                if (!checker.checkType(fileName)) {
                    errorInfos.put(fileName, TransConst.PKG_TYPE_MISMATCH);
                }
                return errorInfos;
            }
        }
        errorInfos.put(fileName, TransConst.PKG_ALL_FAILED);
        return errorInfos;
    }

    private static String processCommand(ProcessBuilder processBuilder) {
        String lastLine = "";

        try {
            Process process = processBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lastLine = line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        TransLog.getLogger().info("Package Status: " + lastLine);
        return lastLine;
    }

    interface Checker {
        boolean checkType(String fileName);
        String[] checkCommand(String fileName);
        boolean checkOk(String result);
    }

    class RarChecker implements Checker {
        @Override
        public boolean checkType(String fileName) {
            return isRar(fileName);
        }

        @Override
        public String[] checkCommand(String fileName) {
            return new String[]{"rar", "t", fileName};
        }

        @Override
        public boolean checkOk(String result) {
            return result.equalsIgnoreCase("All OK");
        }
    }

    class ZipChecker implements Checker {
        @Override
        public boolean checkType(String fileName) {
            return isZip(fileName);
        }

        @Override
        public String[] checkCommand(String fileName) {
            return new String[]{"unzip", "-t", fileName};
        }

        @Override
        public boolean checkOk(String result) {
            return result.startsWith("No errors detected in compressed data of");
        }
    }

    private static boolean isValid(String fileName) {
        return isRar(fileName) || isZip(fileName);
    }

    private static boolean isRar(String fileName) {
        final String[] ARRAY_SUFFIX_RAR = {"rar", "cbr"};

        return Arrays.asList(ARRAY_SUFFIX_RAR).contains(getSuffix(fileName));
    }

    private static boolean isZip(String fileName) {
        final String[] ARRAY_SUFFIX_ZIP = {"zip", "cbz"};

        return Arrays.asList(ARRAY_SUFFIX_ZIP).contains(getSuffix(fileName));
    }

    boolean isIgnorable(String fileName) {
        final String[] ARRAY_SUFFIX_IGNORABLE = {"pdf", "txt", "epub", "mobi"};

        return Arrays.asList(ARRAY_SUFFIX_IGNORABLE).contains(getSuffix(fileName));
    }

    private static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
}

