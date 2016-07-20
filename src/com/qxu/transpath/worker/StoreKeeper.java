/**
 * Copyright (c) 2015,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.worker
 * File Name:StoreKeeper.java
 * Date:2015-11-4 下午4:26:14
 * 
 */
package com.qxu.transpath.worker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.qxu.transpath.tree.LinkList;
import com.qxu.transpath.tree.LinkNode;
import com.qxu.transpath.tree.Node;
import com.qxu.transpath.tree.PubList;
import com.qxu.transpath.tree.PubNode;
import com.qxu.transpath.tree.StoreList;
import com.qxu.transpath.tree.StoreNode;
import com.qxu.transpath.utils.TransConst;
import com.qxu.transpath.utils.TransProp;

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

    private static String storeNameOfVersion(String version) {
        return TransProp.get("SL_HOME") + "StoreList_" + version + ".txt";
    }
    
    private static String pubNameOfVersion(String version) {
        return TransProp.get("SL_HOME") + "PubList_" + version + ".txt";
    }
    
    private static String linkNameOfVersion(String version) {
        return TransProp.get("SL_HOME") + "LinkList_" + version + ".txt";
    }
    
    private static String storeNameOfTag(String aTag, String bTag) {
        return TransProp.get("SL_HOME") + "N\\TFLib_" + aTag + "_" + bTag + ".txt";
    }
    
    public static void keepDelBat(StoreList pList, String pFileName) {
        keepDelBat(pList, new File(pFileName));
    }

    public static void keepDelBat(StoreList pList, File pFile) {
        if (0 == pList.size()) {
            return;
        }
        try {
            PrintWriter out = new PrintWriter(pFile);
            for (Node aNode : pList.nodeList) {
                out.println("del \"" + TransProp.get("ST_HOME")
                        + aNode.path.substring(1).replaceAll(TransConst.SLASH, TransConst.BACK_SLASH_4)
                        + aNode.name + "\"");
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void buildList(String aTag, String[] bTags) {
        int stotal = 0;
        int ttotal = 0;
        
        for (String bTag: bTags) {
            long t0 = System.currentTimeMillis();
            StoreList aList = new StoreList();

            aList.build(TransProp.get("ST_HOME"), "\\" + aTag + "\\" + bTag + "\\");
            aList.keepFile(storeNameOfTag(aTag, bTag));

            int s1 = aList.size();
            long t1 = System.currentTimeMillis() - t0;
            System.out.println("Store Listed: " + s1);
            System.out.println("Store file size: " + aList.fileSize);
            System.out.println("Time Used: " + t1);
            System.out.println("Avg Speed: " + s1 / (t1 * 0.001) + "item/s.");
            stotal += s1;
            ttotal += t1;
        }
        System.out.println("Total Store Listed: " + stotal + " : " + Arrays.toString(bTags));
        System.out.println("Total Time Used: " + ttotal + " ms.");
        System.out.println("Total Avg Speed: " + (stotal) * 1000000 / ttotal * 0.001 + " items/s.");        
    }
    
    public static String combineList(String oldVer, String aTag, String[] bTags) {
        long t0 = System.currentTimeMillis();
        System.out.println("Combine started...");

        StoreList aList = attachTags(oldVer, aTag, bTags);
        StoreList resList = checkDup(oldVer, aList);
        genPubLink(oldVer, resList);
        
        System.out.println("Combine done in " + (System.currentTimeMillis() - t0) + "ms.");
        return resList.version;
    }

    private static StoreList attachTags(String oldVer, String aTag, String[] bTags) {
        long t0 = System.currentTimeMillis();
        System.out.println("AttachTags started...");
        StoreList aList = new StoreList();
        aList.load(storeNameOfVersion(oldVer));
        
        for (String bTag : bTags) {
            String bTagName = storeNameOfTag(aTag, bTag);
            StoreList bList = new StoreList();
            bList.load(bTagName);
            aList.attachList(bList);
            System.out.println(bTagName);
        }
        System.out.println("AttachTags done in " + (System.currentTimeMillis() - t0) + "ms.");
        return aList;
    }
    
    private static StoreList checkDup(String pVer, StoreList pList) {
        long t0 = System.currentTimeMillis();
        System.out.println("CheckDup started...");

        String resName = storeNameOfVersion(pVer + "_res");
        String dupName = storeNameOfVersion(pVer + "_dup");
        String delName = storeNameOfVersion(pVer + "_del");
        String batName = TransProp.get("SL_HOME") + "ToDel.bat";
        
        pList.orderByMd5();

        StoreNode dNode = null;
        StoreList resList = new StoreList();
        StoreList dupList = new StoreList();
        StoreList delList = new StoreList();
        
        for (Node aNode : pList.nodeList) {
            if (null == aNode) {
                continue;
            } else if (!((StoreNode)aNode).checkDupNode(dNode)) {
                dNode = ((StoreNode)aNode);
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
        System.out.println("Duplicated Number: " + (dupList.size()-delList.size()));
        System.out.println("Redundant Number: " + delList.size());
        
        resList.recap();
        resList.keepFile(resName);

        dupList.recap();
        dupList.keepFile(dupName);
        
        delList.recap();
        delList.keepFile(delName);

        delList.orderByPathAndName();
        keepDelBat(delList, batName);

        System.out.println("Current Length: " + pList.size());
        System.out.println("Reserve Length: " + resList.size());
        System.out.println("Removal Length: " + delList.size());
        System.out.println("Removal file size: " + delList.fileSize);
        
        System.out.println("CheckDup done in " + (System.currentTimeMillis() - t0) + "ms.");
        return resList;
    }
    
    private static void genPubLink(String oldVer, StoreList resList) {
        long t0 = System.currentTimeMillis();
        System.out.println("GenPubLink started...");

        PubList pubList = new PubList();
        pubList.load(pubNameOfVersion(oldVer));
        
        LinkList linkList = new LinkList();
        linkList.load(linkNameOfVersion(oldVer));
        
        ArrayList<Integer> oldStoreIdList = new ArrayList<Integer>();
        for (Node aNode : linkList.nodeList) {
            LinkNode lNode = (LinkNode)aNode;
            oldStoreIdList.add(lNode.storeId);
        }
        for (Node aNode : resList.nodeList) {
            StoreNode sNode = (StoreNode)aNode;
            if (!oldStoreIdList.contains(sNode.id)){
                PubNode pNode = new PubNode(sNode);
                int pubId = pubList.addNode(pNode);
                linkList.addNode(new LinkNode(sNode.id, pubId));
            }
        }
        
        resList.orderByPathAndName();
        linkList.refreshStoreId(resList.reorgId());
        
        pubList.orderByPathAndName();
        linkList.refreshPubId(pubList.reorgId());
        
        linkList.orderByStoreId();
        linkList.reorgId();
        
        String currVer = resList.version;
        pubList.version = currVer;
        linkList.version = currVer;
        resList.keepFile(storeNameOfVersion(currVer));
        pubList.keepFile(pubNameOfVersion(currVer));
        linkList.keepFile(linkNameOfVersion(currVer));
        
        System.out.println("GenPubLink done in " + (System.currentTimeMillis() - t0) + "ms.");
    }

    @SuppressWarnings("unused")
    private static void printMap(String storeNameOfVersion, HashMap<Integer, Integer> reorgId) {
        try {
            PrintWriter out = new PrintWriter(storeNameOfVersion);
            out.println(reorgId.toString());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
            System.out.println("Store Listed: " + s1);
            System.out.println("Time Used: " + t1);
            System.out.println("Avg Speed: " + s1 / (t1 * 0.001) + "item/s.");
            stotal += s1;
            ttotal += t1;
        }
        System.out.println("Total Store Listed: " + stotal + ". From " + startNum + " to " + endNum + ".");
        System.out.println("Total Time Used: " + ttotal + " ms.");
        System.out.println("Total Avg Speed: " + (stotal) * 1000000 / ttotal * 0.001 + " items/s.");        
    }

    @Deprecated
    public static void combineListInit() {
        System.out.println("combine Start...");
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
            System.out.println(aBlock.getName());
        }
        aList.reorgId();
        aList.keepFile(tarName);
    }

    /**
     * main:<br/>
     * bin>java com.qxu.transpath.worker.StoreKeeper storehis<br/>
     * bin>java com.qxu.transpath.worker.StoreKeeper b1112 istore<br/>
     * 
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("StoreKeeper starts...");
        
        String aTag = TransProp.get("A_TAG");
        String[] bTags = TransProp.get("B_TAGS").split(",");
        String oldVer = TransProp.get("CURR_VER");
        
        for(int bIndex = 0; bIndex < bTags.length; bIndex++) {
            bTags[bIndex] = bTags[bIndex].trim();
        }
        
        System.out.println("A-Tag: " + aTag);
        System.out.println("B-Tag: " + Arrays.toString(bTags));
        System.out.println("Old version: " + oldVer);
        
//        buildList(aTag, bTags);
        String comVer = combineList(oldVer, aTag, bTags);

        System.out.println("New version: " + comVer + ".");
        
        System.out.println("StoreKeeper done.");
    }

}
