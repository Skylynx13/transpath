/**
 * Copyright (c) 2015,qxu.
 * All Rights Reserved.
 * <p>
 * Project Name:transpath
 * Package Name:com.qxu.transpath.worker
 * File Name:StoreKeeper.java
 * Date:2015-11-4 下午4:26:14
 */
package com.skylynx13.transpath.store;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.pub.LinkList;
import com.skylynx13.transpath.pub.LinkNode;
import com.skylynx13.transpath.pub.PubList;
import com.skylynx13.transpath.pub.PubNode;
import com.skylynx13.transpath.tree.Node;
import com.skylynx13.transpath.ui.TranspathFrame;
import com.skylynx13.transpath.utils.FileUtils;
import com.skylynx13.transpath.utils.TransConst;
import com.skylynx13.transpath.utils.TransProp;

/**
 * ClassName: StoreKeeper <br/>
 * Description: TODO <br/>
 * Date: 2015-11-4 下午4:26:14 <br/>
 * <br/>
 *
 * @author qxu@
 *
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 *
 */

public class StoreKeeper {

    private static void keepDelBat(StoreList pList, String pFileName) {
        keepDelBat(pList, new File(pFileName));
    }

    private static void keepDelBat(StoreList pList, File pFile) {
        if (0 == pList.size()) {
            return;
        }
        try {
            PrintWriter out = new PrintWriter(pFile);
            for (Node aNode : pList.nodeList) {
                out.println("del \"" + TransProp.get(TransConst.LOC_STORE)
                        + aNode.path.substring(1).replaceAll(TransConst.SLASH, TransConst.BACK_SLASH_4)
                        + aNode.name + "\"");
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void buildList(String aTag, String[] bTags) {
        int stotal = 0;
        int ttotal = 0;

        for (String bTag : bTags) {
            long t0 = System.currentTimeMillis();
            StoreList aList = new StoreList();

            aList.build(TransProp.get(TransConst.LOC_STORE), "\\" + aTag + "\\" + bTag + "\\");
            aList.keepFile(FileUtils.storeNameOfTag(aTag, bTag));

            int s1 = aList.size();
            long t1 = System.currentTimeMillis() - t0;
            TransLog.getLogger().info("Store Listed: " + s1);
            TransLog.getLogger().info("Store file size: " + aList.fileSize);
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
        genPubLink(oldVer, resList);

        TransLog.getLogger().info("Combine done in " + (System.currentTimeMillis() - t0) + "ms.");
        return resList.version;
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

        for (Node aNode : pList.nodeList) {
            if (null == aNode) {
                continue;
            } else if (!((StoreNode) aNode).checkDupNode(dNode)) {
                dNode = ((StoreNode) aNode);
                resList.nodeList.add(aNode);
                continue;
            } else {
                if (!dupList.hasNode(dNode)) {
                    dupList.nodeList.add(dNode);
                }
                dupList.nodeList.add(aNode);
                delList.nodeList.add(aNode);
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
        keepDelBat(delList, TransProp.get(TransConst.LOC_LIST) + "ToDel.bat");

        TransLog.getLogger().info("Current Length: " + pList.size());
        TransLog.getLogger().info("Reserve Length: " + resList.size());
        TransLog.getLogger().info("Removal Length: " + delList.size());
        TransLog.getLogger().info("Removal file size: " + delList.fileSize);

        TransLog.getLogger().info("CheckDup done in " + (System.currentTimeMillis() - t0) + "ms.");
        return resList;
    }

    public static void checkDupTest(StoreList pList) {
        long t0 = System.currentTimeMillis();
        TransLog.getLogger().info("CheckDupInPath started...");

        pList.orderByMd5();

        StoreNode dNode = null;
        StoreList resList = new StoreList();
        StoreList dupList = new StoreList();
        StoreList delList = new StoreList();

        for (Node aNode : pList.nodeList) {
            if (null == aNode) {
                continue;
            } else if (!((StoreNode) aNode).checkDupNode(dNode)) {
                dNode = ((StoreNode) aNode);
                resList.nodeList.add(aNode);
                continue;
            } else {
                if (!dupList.hasNode(dNode)) {
                    dupList.nodeList.add(dNode);
                }
                dupList.nodeList.add(aNode);
                delList.nodeList.add(aNode);
            }
        }
        TransLog.getLogger().info("Duplicated Number: " + (dupList.size() - delList.size()));
        TransLog.getLogger().info("Redundant Number: " + delList.size());

        resList.recap();

        dupList.recap();
        TransLog.getLogger().info(dupList.toString());

        delList.recap();
        delList.orderByPathAndName();
        keepDelBat(delList, TransProp.get(TransConst.LOC_LIST) + "ToDel.bat");

        TransLog.getLogger().info("Current Length: " + pList.size());
        TransLog.getLogger().info("Reserve Length: " + resList.size());
        TransLog.getLogger().info("Removal Length: " + delList.size());
        TransLog.getLogger().info("Removal file size: " + delList.fileSize);

        TransLog.getLogger().info("CheckDupInPath done in " + (System.currentTimeMillis() - t0) + "ms.");
    }

    private static void genPubLink(String oldVer, StoreList resList) {
        long t0 = System.currentTimeMillis();
        TransLog.getLogger().info("GenPubLink started...");

        PubList pubList = new PubList();
        pubList.load(FileUtils.pubNameOfVersion(oldVer));

        LinkList linkList = new LinkList();
        linkList.load(FileUtils.linkNameOfVersion(oldVer));

        ArrayList<Integer> oldStoreIdList = linkList.getStoreIdList();

        for (Node aNode : resList.nodeList) {
            StoreNode sNode = (StoreNode) aNode;
            if (!oldStoreIdList.contains(sNode.id)) {
                PubNode pNode = new PubNode(sNode);
                int pubId = pubList.addNode(pNode);
                linkList.addNode(new LinkNode(sNode.id, pubId));
            }
        }

        resList.orderByPathAndName();
        linkList.refreshStoreId(resList.reorgId());

        pubList.hitShelf(FileUtils.hitShelfList());
        pubList.orderByPathAndName();
        linkList.refreshPubId(pubList.reorgId());
        pubList.reorder();

        linkList.orderByStoreId();
        linkList.reorgId();

        String currVer = resList.version;
        pubList.version = currVer;
        linkList.version = currVer;
        resList.keepFile(FileUtils.storeNameOfVersion(currVer));
        pubList.keepFile(FileUtils.pubNameOfVersion(currVer));
        linkList.keepFile(FileUtils.linkNameOfVersion(currVer));

        TransLog.getLogger().info("GenPubLink done in " + (System.currentTimeMillis() - t0) + "ms.");
    }

    @Deprecated
    public static void buildListInit() {
        int stotal = 0;
        int ttotal = 0;

        int startNum = 619;
        int endNum = 619;

        for (int bNum = startNum; bNum <= endNum; bNum++) {
            long t0 = System.currentTimeMillis();
            StoreList aList = new StoreList();

            String aTag = "2016";
            String bTag = String.format(TransConst.FORMAT_INT_04, bNum);

            aList.build("F:\\Book\\TFLib\\", "\\A" + aTag + "\\B" + bTag + "\\");
            aList.keepFile("D:\\Qdata\\update\\TFLib_A" + aTag + "_B" + bTag + ".txt");

            int s1 = aList.size();
            long t1 = System.currentTimeMillis() - t0;
            TransLog.getLogger().info("Store Listed: " + s1);
            TransLog.getLogger().info("Time Used: " + t1);
            TransLog.getLogger().info("Avg Speed: " + s1 / (t1 * 0.001) + "item/s.");
            stotal += s1;
            ttotal += t1;
        }
        TransLog.getLogger().info("Total Store Listed: " + stotal + ". From " + startNum + " to " + endNum + ".");
        TransLog.getLogger().info("Total Time Used: " + ttotal + " ms.");
        TransLog.getLogger().info("Total Avg Speed: " + (stotal) * 1000000 / ttotal * 0.001 + " items/s.");
    }

    @Deprecated
    public static void combineListInit() {
        TransLog.getLogger().info("combine Start...");
        String srcName = "D:\\Qdata\\update\\storelist\\A\\";

        File srcPath = new File(srcName);
        StoreList aList = new StoreList();

        String tarName = "D:\\Qdata\\update\\storelist\\StoreList_" + aList.version + ".txt";

        if (!srcPath.isDirectory()) {
            return;
        }

        for (File aBlock : srcPath.listFiles(new FilenameFilter() {
            public boolean accept(File file, String name) {
                return name.startsWith("");
            }
        })) {
            StoreList bList = new StoreList();
            bList.load(aBlock);
            aList.attachList(bList);
            TransLog.getLogger().info(aBlock.getName());
        }
        aList.reorgId();
        aList.keepFile(tarName);
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

    /**
     * main:<br/>
     * bin>java com.qxu.transpath.worker.StoreKeeper storehis<br/>
     * bin>java com.qxu.transpath.worker.StoreKeeper b1112 istore<br/>
     *
     * @param args
     */
    public static void main(String[] args) {
        StoreList aList = new StoreList();
        aList.build("D:\\Book\\TFLib\\New\\full", "\\");
        checkDupTest(aList);
    }

}
