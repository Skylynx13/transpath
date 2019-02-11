package com.skylynx13.transpath.store;

import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.utils.FileUtils;
import com.skylynx13.transpath.utils.TransConst;
import com.skylynx13.transpath.utils.TransProp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * ClassName: StoreKeeper
 * Description: Store keeper
 * Date: 2015-11-04 16:26:14
 */
public class StoreKeeper {

    private static void keepDelList(StoreList delList) {
        if (isWindow()) {
            keepDelBatch(delList, new File(TransProp.get(TransConst.LOC_LIST) + "ToDel.bat"));
        } else {
            keepDelShell(delList, new File(TransProp.get(TransConst.LOC_LIST) + "todel.sh"));
        }
    }

    private static boolean isWindow() {
        return TransProp.get(TransConst.SYS_TYPE).equalsIgnoreCase(TransConst.SYS_WINDOWS);
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
                        + FileUtils.regulatePath(aNode.getPath().substring(1))
                        + aNode.getName() + "\"");
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void buildList(String aTag, String[] bTags) {
        int stotal = 0;
        int ttotal = 0;

        String SEPARATOR = "/";
        if(isWindow()) {
            SEPARATOR = "\\";
        }

        for (String bTag : bTags) {
            long t0 = System.currentTimeMillis();
            StoreList aList = new StoreList();

            aList.build(TransProp.get(TransConst.LOC_STORE),
                    SEPARATOR + aTag + SEPARATOR + bTag + SEPARATOR);
            aList.keepFile(FileUtils.storeNameOfTag(aTag, bTag));

            int s1 = aList.size();
            long t1 = System.currentTimeMillis() - t0;
            TransLog.getLogger().info("Store Listed: " + s1);
            TransLog.getLogger().info("Store file size: " + aList.getStoreSize());
            TransLog.getLogger().info("Time Used: " + t1);
            TransLog.getLogger().info("Avg Speed: " + s1 / (t1 * 0.001) + "item/s.");
            stotal += s1;
            ttotal += t1;
        }
        TransLog.getLogger().info("Total Store Listed: " + stotal + " : " + Arrays.toString(bTags));
        TransLog.getLogger().info("Total Time Used: " + ttotal + " ms.");
        TransLog.getLogger().info("Total Avg Speed: " + (stotal) * 1000000 / ttotal * 0.001 + " items/s.");
    }

    private static String combineList(String oldVer, String aTag, String[] bTags) {
        long t0 = System.currentTimeMillis();
        TransLog.getLogger().info("Combine started...");

        StoreList aList = attachTags(oldVer, aTag, bTags);
        StoreList resList = checkDup(oldVer, aList);
        saveResList(resList);

        TransLog.getLogger().info("Combine done in " + (System.currentTimeMillis() - t0) + "ms.");
        return resList.getVersion();
    }

    private static void combineTest(String oldVer, String aTag, String[] bTags) {
        long t0 = System.currentTimeMillis();
        TransLog.getLogger().info("Combine Test started...");

        StoreList aList = attachTags(oldVer, aTag, bTags);
        checkDupTest(aList);

        TransLog.getLogger().info("Combine Test done in " + (System.currentTimeMillis() - t0) + "ms.");
    }

    private static StoreList attachTags(String oldVer, String aTag, String[] bTags) {
        long t0 = System.currentTimeMillis();
        TransLog.getLogger().info("AttachTags started...");
        StoreList aList = new StoreList();
        aList.load(FileUtils.storeNameOfVersion(oldVer));

        for (String bTag : bTags) {
            String bTagName = FileUtils.storeNameOfTag(aTag, bTag);
            StoreList bList = new StoreList();
            bList.load(bTagName);
            aList.attachList(bList);
            TransLog.getLogger().info(bTagName);
        }
        TransLog.getLogger().info("AttachTags done in " + (System.currentTimeMillis() - t0) + "ms.");
        return aList;
    }

    private static StoreList checkDup(String pVer, StoreList pList) {
        long t0 = System.currentTimeMillis();
        TransLog.getLogger().info("CheckDup started...");

        pList.keepFile(FileUtils.storeNameOfVersion(pVer + "_old"));

        pList.orderByMd5();

        StoreNode dNode = null;
        StoreList resList = new StoreList();
        StoreList dupList = new StoreList();
        StoreList delList = new StoreList();

        for (StoreNode aNode : pList.storeList) {
            if (null != aNode) {
                if (aNode.checkDupStoreNode(dNode)) {
                    if (!dupList.hasNode(dNode)) {
                        dupList.storeList.add(dNode);
                    }
                    dupList.storeList.add(aNode);
                    delList.storeList.add(aNode);
                } else {
                    dNode = aNode;
                    resList.storeList.add(aNode);
                }
            }
        }
        TransLog.getLogger().info("Duplicated Number: " + (dupList.size() - delList.size()));
        TransLog.getLogger().info("Redundant Number: " + delList.size());

        resList.recap();
        resList.keepFile(FileUtils.storeNameOfVersion(pVer + "_res"));

        dupList.recap();
        dupList.keepFile(FileUtils.storeNameOfVersion(pVer + "_dup"));
        TransLog.getLogger().info(dupList.toString());

        delList.recap();
        delList.keepFile(FileUtils.storeNameOfVersion(pVer + "_del"));

        delList.orderByPathAndName();
        keepDelList(delList);

        TransLog.getLogger().info("Current Length: " + pList.size());
        TransLog.getLogger().info("Reserve Length: " + resList.size());
        TransLog.getLogger().info("Removal Length: " + delList.size());
        TransLog.getLogger().info("Removal file size: " + delList.getStoreSize());

        TransLog.getLogger().info("CheckDup done in " + (System.currentTimeMillis() - t0) + "ms.");
        return resList;
    }

    private static void checkDupTest(StoreList pList) {
        long t0 = System.currentTimeMillis();
        TransLog.getLogger().info("CheckDupInPath started...");

        pList.orderByMd5();

        StoreNode dNode = null;
        StoreList resList = new StoreList();
        StoreList dupList = new StoreList();
        StoreList delList = new StoreList();

        for (StoreNode aNode : pList.storeList) {
            if (null != aNode) {
                if (aNode.checkDupStoreNode(dNode)) {
                    if (!dupList.hasNode(dNode)) {
                        dupList.storeList.add(dNode);
                    }
                    dupList.storeList.add(aNode);
                    delList.storeList.add(aNode);
                } else {
                    dNode = aNode;
                    resList.storeList.add(aNode);
                }
            }
        }
        TransLog.getLogger().info("Duplicated Number: " + (dupList.size() - delList.size()));
        TransLog.getLogger().info("Redundant Number: " + delList.size());

        resList.recap();

        dupList.recap();
        TransLog.getLogger().info(dupList.toString());

        delList.recap();
        delList.orderByPathAndName();
        keepDelList(delList);

        TransLog.getLogger().info("Current Length: " + pList.size());
        TransLog.getLogger().info("Reserve Length: " + resList.size());
        TransLog.getLogger().info("Removal Length: " + delList.size());
        TransLog.getLogger().info("Removal file size: " + delList.getStoreSize());

        TransLog.getLogger().info("CheckDupInPath done in " + (System.currentTimeMillis() - t0) + "ms.");
    }

    private static void saveResList(StoreList resList) {
        long t0 = System.currentTimeMillis();
        TransLog.getLogger().info("GenPubLink started...");

        resList.orderByPathAndName();
        resList.reorgId();
        String currVer = resList.getVersion();
        resList.keepFile(FileUtils.storeNameOfVersion(currVer));

        TransLog.getLogger().info("GenPubLink done in " + (System.currentTimeMillis() - t0) + "ms.");
    }

    public static void buildCombinedList() {
        TransLog.getLogger().info("StoreKeeper combining...");

        String aTag = TransProp.get(TransConst.TAG_A);
        String[] bTags = TransProp.get(TransConst.TAG_B).split(",");
        String oldVer = TransProp.get(TransConst.VER_CURR);

        for (int bIndex = 0; bIndex < bTags.length; bIndex++) {
            bTags[bIndex] = bTags[bIndex].trim();
        }

        TransLog.getLogger().info("A-Tag: " + aTag);
        TransLog.getLogger().info("B-Tag: " + Arrays.toString(bTags));
        TransLog.getLogger().info("Old version: " + oldVer);

        buildList(aTag, bTags);
        String comVer = combineList(oldVer, aTag, bTags);

        TransLog.getLogger().info("New version: " + comVer + ".");
//        TransLog.getLogger().info("Remember to move N/ files to B/.");
        TransLog.getLogger().info("StoreKeeper done.");
    }

    public static void testCombinedList() {
        TransLog.getLogger().info("StoreKeeper test combining...");

        String aTag = TransProp.get(TransConst.TAG_A);
        String[] bTags = TransProp.get(TransConst.TAG_B).split(",");
        String oldVer = TransProp.get(TransConst.VER_CURR);

        for (int bIndex = 0; bIndex < bTags.length; bIndex++) {
            bTags[bIndex] = bTags[bIndex].trim();
        }

        TransLog.getLogger().info("A-Tag: " + aTag);
        TransLog.getLogger().info("B-Tag: " + Arrays.toString(bTags));
        TransLog.getLogger().info("Old version: " + oldVer);

        buildList(aTag, bTags);
        combineTest(oldVer, aTag, bTags);

        TransLog.getLogger().info("StoreKeeper done.");
    }
}
