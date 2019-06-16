package com.skylynx13.transpath.store;

import com.skylynx13.transpath.Transpath;
import com.skylynx13.transpath.error.StoreListException;
import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.utils.*;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoreCombiner extends SwingWorker<StringBuilder, ProgressReport> {
    private boolean updateList;
    private final static String STORE_ROOT = TransProp.get(TransConst.LOC_STORE);
    private final static String REGEX_PATH_FULL =
            "^(A\\d{4})/B(\\d{4})(-(\\d{4}))?(,((A\\d{4})/)?B(\\d{4})(-(\\d{4}))?)*?$";
    private final static String REGEX_PATH_UNIT = ",?(A(\\d{4})/)?B(\\d{4})(-(\\d{4}))?";
    private final static Pattern PATTERN_PATH_UNIT = Pattern.compile(REGEX_PATH_UNIT);
    private final static int GROUP_A = 2;
    private final static int GROUP_B = 3;
    private final static int GROUP_S = 5;
    private ProgressTracer progressTracer = new ProgressTracer();

    public StoreCombiner(boolean updateList) {
        this.updateList = updateList;
    }

    @Override
    protected StringBuilder doInBackground() {
        try {
            TransLog.getLogger().info("Store combiner started...");
            StoreList oldStoreList = buildOldStoreList();
            StoreList newStoreList = buildNewStoreList();
            StoreList combinedList = combine(oldStoreList, newStoreList);
            if (updateList) {
                oldStoreList.backup();
                combinedList.keepFile();
                Transpath.getTranspathFrame().reloadStore();
            }
            return new StringBuilder("Store combiner done.")
                    .append(" Old: ").append(oldStoreList.size())
                    .append(" New: ").append(newStoreList.size())
                    .append(" Combined: ").append(combinedList.size())
                    .append(" Combined file size: ").append(combinedList.getStoreSize());
        } catch (StoreListException e) {
            return new StringBuilder(e.getMessage());
        }
    }

    private StoreList buildOldStoreList() {
        TransLog.getLogger().info("Building old store list...");
        long t0 = System.currentTimeMillis();
        StoreList oldList = new StoreList().loadCurrent();
        TransLog.getLogger().info("Old store list built in " + (System.currentTimeMillis() - t0) + "ms.");
        return oldList;
    }

    private StoreList buildNewStoreList() throws StoreListException {
        TransLog.getLogger().info("Building new store list...");
        long t0 = System.currentTimeMillis();
        List<String> storePathList = buildStorePathList();
        resetProgress(calcStoreFileSize(storePathList), calcStoreFileCount(storePathList),
                "Building new list");
        StoreList newList = new StoreList();

        for (String storePathName : storePathList) {
            File storePath = new File(storePathName);
            if (!storePath.isDirectory() || storePath.listFiles() == null) {
                TransLog.getLogger().warn("Store path ignored: " + storePathName);
                continue;
            }
            newList.attachList(buildStoreListByPath(storePath));
        }
        TransLog.getLogger().info("New store list built.");
        logTimeElapsed(t0);
        return newList;
    }

    private StoreList buildStoreListByPath(File storePath) {
        StoreList storeList = new StoreList();
        for (File aPath : Objects.requireNonNull(storePath.listFiles())) {
            if (aPath.isFile()) {
                StoreNode aNode = new StoreNode(STORE_ROOT, aPath);
                storeList.addNode(aNode);
                TransLog.getLogger().info(aNode.keepNode());
                updateProgress(aNode.getLength());
            }
            if (aPath.isDirectory() && aPath.listFiles() != null) {
                storeList.attachList(buildStoreListByPath(aPath));
            }
        }
        return storeList;
    }

    private void resetProgress(long totalSize, long totalCount, String header) {
        progressTracer.reset(totalSize, totalCount, header);
        publish(progressTracer.report());
    }

    private void updateProgress(long nSize) {
        progressTracer.update(nSize);
        publish(progressTracer.report());
    }

    private long calcStoreFileSize(List<String> storePathList) throws StoreListException {
        long tSize = 0;
        for (String storePathName : storePathList) {
            tSize += FileUtils.getFileSize(storePathName);
        }
        if (tSize == 0) {
            throw new StoreListException("Total size is 0");
        }
        return tSize;
    }

    private long calcStoreFileCount(List<String> storePathList) throws StoreListException {
        long tCount = 0;
        for (String storePathName : storePathList) {
            tCount += FileUtils.getFileCount(storePathName);
        }
        if (tCount == 0) {
            throw new StoreListException("Total count is 0");
        }
        return tCount;
    }

    private List<String> buildStorePathList() throws StoreListException {
        if (updateList) {
            return buildList(TransProp.get(TransConst.PATH_BRANCH));
        } else {
            return buildList(TransProp.get(TransConst.PATH_DRILL));
        }
    }

    List<String> buildList(String branchPath) throws StoreListException {
        if (!branchPath.matches(REGEX_PATH_FULL)) {
            if (new File(branchPath).exists()) {
                List<String> singlePathList = new ArrayList<>();
                singlePathList.add(branchPath);
                TransLog.getLogger().info(singlePathList.toString());
                return singlePathList;
            }
            TransLog.getLogger().error("Error branch path: " + branchPath);
            throw new StoreListException("Error branch path.");
        }
        return checkExistList(parseList(branchPath));
    }

    List<String> parseList(String branchPath) throws StoreListException {
        List<String> parsedList = new ArrayList<>();
        Matcher branchMatcher = PATTERN_PATH_UNIT.matcher(branchPath);
        int numberA = 0;
        while (branchMatcher.find()) {
            TransLog.getLogger()
                    .info("count: " + branchMatcher.groupCount() + ", group: " + branchMatcher.group());

            if (branchMatcher.group(GROUP_A) != null) {
                numberA = Integer.parseInt(branchMatcher.group(GROUP_A));
            }

            int numberB = Integer.parseInt(branchMatcher.group(GROUP_B));

            int numberS = numberB;
            if (branchMatcher.group(GROUP_S) != null) {
                numberS = Integer.parseInt(branchMatcher.group(GROUP_S));
            }

            if (numberB > numberS) {
                TransLog.getLogger().error("Error branch group: " + branchMatcher.group());
                throw new StoreListException("Error branch group.");
            }

            for (int numberI = numberB; numberI <= numberS; numberI++) {
                parsedList.add(String.format("A%04d/B%04d", numberA, numberI));
            }
        }
        TransLog.getLogger().info(parsedList.toString());
        return parsedList;
    }

    private List<String> checkExistList(List<String> parsedList) throws StoreListException {
        ArrayList<String> existList = new ArrayList<>();
        for (String relativePath : parsedList) {
            String fullPath = FileUtils.regulateSysPath((STORE_ROOT + relativePath));
            if (new File(fullPath).exists()) {
                existList.add(fullPath);
            }
        }
        if (existList.size() == 0) {
            throw new StoreListException("Exist list is empty.");
        }
        TransLog.getLogger().info("Existing: " + existList.toString());
        return existList;
    }

    private long logTimeElapsed(long t0) {
        long t1 = System.currentTimeMillis();
        TransLog.getLogger().info("Time elapsed " + (t1-t0) + "ms.");
        return t1;
    }

    private StoreList combine(StoreList oldStoreList, StoreList newStoreList) {
        TransLog.getLogger().info("Combining...");
        long t0 = System.currentTimeMillis();

        StoreList overallList = new StoreList();
        overallList.attachList(oldStoreList);
        overallList.attachList(newStoreList);

        TransLog.getLogger().info("Attach done.");
        t0 = logTimeElapsed(t0);

        overallList.orderByMd5();

        TransLog.getLogger().info("List ordered by MD5.");
        t0 = logTimeElapsed(t0);

        StoreNode dNode = null;
        StoreList reservedList = new StoreList();
        StoreList duplicatedList = new StoreList();
        StoreList removedList = new StoreList();

        resetProgress(overallList.getStoreSize(), overallList.size(), "Combining list");
        for (StoreNode aNode : overallList.storeList) {
            if (aNode == null) {
                continue;
            }
            if (aNode.checkDupStoreNode(dNode)) {
                if (!duplicatedList.hasNode(dNode)) {
                    duplicatedList.storeList.add(dNode);
                }
                duplicatedList.storeList.add(aNode);
                removedList.storeList.add(aNode);
            } else {
                dNode = aNode;
                reservedList.storeList.add(aNode);
            }
            updateProgress(aNode.getLength());
        }
        TransLog.getLogger().info("Duplicated list.");
        TransLog.getLogger().info(duplicatedList.toString());
        TransLog.getLogger().info("Duplicated count: " + (duplicatedList.size() - removedList.size()));
        TransLog.getLogger().info("Removed count: " + removedList.size());
        TransLog.getLogger().info("Duplication checked.");
        t0 = logTimeElapsed(t0);

        removedList.orderByPathAndName();
        removedList.recap();
        keepDelList(removedList);
        TransLog.getLogger().info("Overall count: " + overallList.size());
        TransLog.getLogger().info("Reserved count: " + reservedList.size());
        TransLog.getLogger().info("Removed count: " + removedList.size());
        TransLog.getLogger().info("Removed size: " + removedList.getStoreSize());
        TransLog.getLogger().info("Removal scripted.");
        t0 = logTimeElapsed(t0);

        reservedList.orderByPathAndName();
        reservedList.recap();
        reservedList.reorgId();
        TransLog.getLogger().info("List ordered by name.");
        logTimeElapsed(t0);

        TransLog.getLogger().info("Store combined.");
        return reservedList;
    }

    private static void keepDelList(StoreList delList) {
        if (FileUtils.isWindows()) {
            keepDelBatch(delList, new File(TransProp.get(TransConst.LOC_LIST) + "ToDel.bat"));
        } else {
            keepDelShell(delList, new File(TransProp.get(TransConst.LOC_LIST) + "todel.sh"));
        }
    }

    private static void keepDelShell(StoreList delList, File delFile) {
        if (0 == delList.size()) {
            return;
        }
        try {
            PrintWriter out = new PrintWriter(delFile);
            for (StoreNode aNode : delList.storeList) {
                out.println("rm \"" + TransProp.get(TransConst.LOC_STORE)
                        + aNode.getPath().substring(1)
                        + aNode.getName() + "\"");
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void keepDelBatch(StoreList delList, File delFile) {
        if (0 == delList.size()) {
            return;
        }
        try {
            PrintWriter out = new PrintWriter(delFile);
            for (StoreNode aNode : delList.storeList) {
                out.println("del \"" + TransProp.get(TransConst.LOC_STORE)
                        + FileUtils.regulateSysPath(aNode.getPath().substring(1))
                        + aNode.getName() + "\"");
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
            Transpath.getStatusLabel().setText(result.toString());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
