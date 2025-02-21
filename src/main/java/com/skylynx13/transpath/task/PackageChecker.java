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
 * @author skylynx
 */
public class PackageChecker extends SwingWorker<StringBuilder, ProgressReport> {
    private final ProgressTracer progressTracer = new ProgressTracer();

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
    protected void process(List<ProgressReport> progressReportList) {
        ProgressReport lastProgressReport = progressReportList.get(progressReportList.size()-1);
        Transpath.getProgressBar().setValue(lastProgressReport.getProgress());
        Transpath.getStatusLabel().setText(lastProgressReport.getReportLine());
    }

    @Override
    protected void done() {
        try {
            StringBuilder result = get();
            TransLog.getLogger().info(result.toString());
        } catch (InterruptedException | ExecutionException e) {
            TransLog.getLogger().error("", e);
        }
    }

    StringBuilder check(File[] checkFiles) {
        long totalSize = 0;
        for (File checkFile : checkFiles) {
            totalSize += checkFile.length();
        }
        resetProgress(totalSize, checkFiles.length);

        Map<String, String> errorInfos = new HashMap<>(16);
        for (File checkFile : checkFiles) {
            errorInfos.putAll(checkPackage(checkFile.getPath()));
            updateProgress(checkFile.length());
        }
        StringBuilder errInfoStr = new StringBuilder("=== Package Check Result: ");
        errInfoStr.append(errorInfos.size()).append(" error(s) found. ===").append(TransConst.CRLN);
        List<String> keyList = new ArrayList<>(Arrays.asList(errorInfos.keySet().toArray(new String[0])));
        Collections.sort(keyList);
        for (String key : keyList) {
              errInfoStr.append(FileUtils.toStandardPath(key)).append(" : ")
                  .append(errorInfos.get(key)).append(TransConst.CRLN);
        }
        return errInfoStr;
    }

    private void resetProgress(long totalSize, long totalCount) {
        progressTracer.reset(totalSize, totalCount, "Checking packages");
        publish(progressTracer.report());
    }

    private void updateProgress(long nSize) {
        progressTracer.update(nSize);
        publish(progressTracer.report());
    }

    Map<String, String> checkPackage(String fileName) {
        Map<String, String> errorInfos = new HashMap<>(16);
        List<Checker> checkers = new ArrayList<>();
        checkers.add(new RarChecker());
        checkers.add(new ZipChecker());
        checkers.add(new SevenZChecker());

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.redirectErrorStream(true);

        if (isIgnorable(fileName)) {
            TransLog.getLogger().info(TransConst.PKG_IGNORE + ": {}", fileName);
            return errorInfos;
        }

        if (!isValid(fileName) || new File(fileName).isDirectory()) {
            errorInfos.put(fileName, TransConst.PKG_TYPE_NOT_RECOGNIZED);
            return errorInfos;
        }

        String result;
        for (Checker checker : checkers) {
            processBuilder.command(checker.checkCommand(fileName));
            TransLog.getLogger().info("Command line: {}", String.join(" ", processBuilder.command()));
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
        StringBuilder lastLine = new StringBuilder();

        try {
            Process process = processBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lastLine.append(line);
            }
        } catch (IOException e) {
            TransLog.getLogger().error("", e);
        }

        TransLog.getLogger().info("Package Status: {}", lastLine);
        return lastLine.toString();
    }

    /**
     * Interface for methods of all kind of compressors.
     */
    interface Checker {
        /**
         * Check if match the type of compressor;
         * @param fileName file to check
         * @return match or not
         */
        boolean checkType(String fileName);

        /**
         * Get command for compressor;
         * @param fileName file to check
         * @return command list
         */
        String[] checkCommand(String fileName);

        /**
         * Check status ok or not;
         * @param result result of compressor command
         * @return ok or not
         */
        boolean checkOk(String result);
    }

    static class RarChecker implements Checker {
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
            return result.contains("All OK");
        }
    }

    static class ZipChecker implements Checker {
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
            return result.contains("No errors detected in compressed data of");
        }
    }

    static class SevenZChecker implements Checker {
        @Override
        public boolean checkType(String fileName) {
            return isZip(fileName);
        }

        @Override
        public String[] checkCommand(String fileName) {
            return new String[]{"7z", "t", fileName};
        }

        @Override
        public boolean checkOk(String result) {
            return result.contains("Everything is Ok");
        }
    }

    private static boolean isValid(String fileName) {
        return isRar(fileName) || isZip(fileName);
    }

    private static boolean isRar(String fileName) {
        final String[] arraySuffixRar = {"rar", "cbr"};

        return Arrays.asList(arraySuffixRar).contains(getSuffix(fileName));
    }

    private static boolean isZip(String fileName) {
        final String[] arraySuffixZip = {"zip", "cbz"};

        return Arrays.asList(arraySuffixZip).contains(getSuffix(fileName));
    }

    boolean isIgnorable(String fileName) {
        final String[] arraySuffixIgnorable =
                {"txt", "pdf", "pdb", "epub", "mobi", "azw3", "jpg", "gif", "mp3", "fb2", "rtf"};
        return Arrays.asList(arraySuffixIgnorable).contains(getSuffix(fileName));
    }

    private static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
}