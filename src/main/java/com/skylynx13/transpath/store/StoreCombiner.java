package com.skylynx13.transpath.store;

import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.utils.ProgressData;
import com.skylynx13.transpath.utils.TransConst;
import com.skylynx13.transpath.utils.TransProp;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoreCombiner extends SwingWorker<StringBuilder, ProgressData> {
    private boolean updateList;
    private final static String REGEX_PATH_FULL = "^(A\\d{4})/B(\\d{4})(-(\\d{4}))?(,((A\\d{4})/)?B(\\d{4})(-(\\d{4}))?)*?$";
    private final static String REGEX_PATH_UNIT = ",?(A(\\d{4})/)?B(\\d{4})(-(\\d{4}))?";
    private final static Pattern PATTERN_PATH_UNIT = Pattern.compile(REGEX_PATH_UNIT);
    private final static int GROUP_A = 2;
    private final static int GROUP_B = 3;
    private final static int GROUP_S = 5;

    public StoreCombiner(boolean updateList) {
        this.updateList = updateList;
    }

    @Override
    protected StringBuilder doInBackground() {
        List<String> storePathList = parseStorePathList(TransProp.get(TransConst.PATH_BRANCH));

        StoreList newStoreList = new StoreList(storePathList);
        StoreList oldStoreList = new StoreList(getStoreListName());
        oldStoreList.backup();
//        StoreList combinedList = oldStoreList.combine(newStoreList);
//        if (updateList) {
//            combinedList.keepFile("");
//        }
        return null;
    }

    private String getStoreListName() {
        return TransProp.get(TransConst.LOC_LIST) + String.format("StoreList.txt");
    }

    List<String> parseStorePathList(String branchPath) {
        if (!branchPath.matches(REGEX_PATH_FULL)) {
            TransLog.getLogger().error("Error branch path: " + branchPath);
            return null;
        }

        Matcher branchMatcher = PATTERN_PATH_UNIT.matcher(branchPath);
        int numberA = 0;
        List<String> storePathList = new ArrayList<>();
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
                return null;
            }
            for (int numberI = numberB; numberI <= numberS; numberI++) {
                storePathList.add(String.format("A%04d/B%04d", numberA, numberI));
            }
        }
        TransLog.getLogger().info(storePathList.toString());
        return storePathList;
    }


    @Override
    protected void process(List<ProgressData> progressData) {

    }

    @Override
    protected void done() {

    }

    public boolean isUpdateList() {
        return updateList;
    }

    public void setUpdateList(boolean updateList) {
        this.updateList = updateList;
    }
}
