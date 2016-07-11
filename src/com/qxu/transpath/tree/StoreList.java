/**
 * Copyright (c) 2016,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:StoreList.java
 * Date:2016-6-17 上午12:05:40
 * 
 */
package com.qxu.transpath.tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

import com.qxu.transpath.utils.DateUtils;
import com.qxu.transpath.utils.TransConst;
import com.qxu.transpath.utils.TransProp;

/**
 * ClassName: StoreList <br/>
 * Description: TODO <br/>
 * Date: 2016-6-17 上午12:05:40 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 *         Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class StoreList {
    String version;
    int pidMin;
    int pidMax;
    long fileSize;
    ArrayList<StoreNode> storeList;

    public StoreList() {
        version = DateUtils.formatDateTimeLongToday();
        pidMin = 0;
        pidMax = 0;
        fileSize = 0;
        storeList = new ArrayList<StoreNode>();
    }

    public StoreList(StoreList pStoreList) {
        version = DateUtils.formatDateTimeLongToday();
        pidMin = pStoreList.pidMin;
        pidMax = pStoreList.pidMax;
        fileSize = pStoreList.fileSize;
        storeList = new ArrayList<StoreNode>(pStoreList.storeList);
    }

    public StoreList(ArrayList<StoreNode> pArrayList) {
        version = DateUtils.formatDateTimeLongToday();
        storeList = new ArrayList<StoreNode>(pArrayList);
        recapListInfo();
    }

    public void load(String pFileName) {
        load(new File(pFileName));
    }

    public void load(File pFile) {
        clear();
        Scanner aScan = null;
        try {
            aScan = new Scanner(new FileReader(pFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        if (aScan.hasNext()) {
            loadVersion(aScan.nextLine());
        }
        while (aScan.hasNext()) {
            storeList.add(new StoreNode(aScan.nextLine()));
        }
        aScan.close();    
    }

    public void clear() {
        version = DateUtils.formatDateTimeLongToday();
        pidMin = 0;
        pidMax = 0;
        fileSize = 0;
        storeList = new ArrayList<StoreNode>();
    }
    
    private String regulateSlash(String pStr) {
        String aStr = pStr.replaceAll(TransConst.BACK_SLASH_4, TransConst.SLASH);
        if (aStr.endsWith(TransConst.SLASH)) {
            aStr = aStr.substring(0, aStr.length() - 1);
        }
        return aStr;
    }

    public void build(String pRoot, String pPathName) {
        String aRoot = regulateSlash(pRoot);
        String aPathName = aRoot + regulateSlash(pPathName);
        
        if (new File(aRoot).isFile() || new File(aPathName).isFile()) {
            return;
        }

        buildByPath(aRoot, aPathName);
        recapListInfo();
    }

    public void buildByPath(String pRoot, String pPathName) {
        buildByPath(pRoot, new File(pPathName));
    }

    public void buildByPath(String pRoot, File pPath) {
        clear();
        if (pPath.isFile()) {
            return;
        }
        if (pPath.listFiles() == null) {
            return;
        }
        System.out.println(pPath);
        for (File aFile : pPath.listFiles()) {
            if (aFile.isFile()) {
                StoreNode aNode = new StoreNode(pRoot, aFile);
                aNode.id = ++pidMax;
                System.out.println(aNode.keepNode());
                storeList.add(aNode);
            }
            if (aFile.isDirectory()) {
                StoreList aList = new StoreList();
                aList.buildByPath(pRoot, aFile.getPath());
                attachList(aList);
            }
        }
    }

    public int size() {
        return (null == storeList) ? 0 : storeList.size();
    }

    public void recapListInfo() {
        pidMin = Integer.MAX_VALUE;
        pidMax = 0;
        fileSize = 0;
        for (StoreNode aNode : storeList) {
            if (aNode.id < pidMin) {
                pidMin = aNode.id;
            }
            if (aNode.id > pidMax) {
                pidMax = aNode.id;
            }
            fileSize += aNode.length;
        }
    }

    public StoreNode get(int index) {
        return storeList.get(index);
    }

    public StoreNode getByPid(int pPid) {
        for (StoreNode aNode : storeList) {
            if (aNode.id == pPid) {
                return aNode;
            }
        }
        return null;
    }
    
    public boolean hasNode (StoreNode pStoreNode) {
        return storeList.contains(pStoreNode);
    }

    public void addNode(StoreNode pStoreNode) {
        if (0 == size()) {
            pidMin = 1;
        }
        pStoreNode.id = ++pidMax;
        storeList.add(pStoreNode);
        fileSize += pStoreNode.length;
    }

    public void attachList(StoreList pStoreList) {
        for (StoreNode aStoreNode : pStoreList.storeList) {
            addNode(aStoreNode);
        }
    }

    @Deprecated
    public void attachList1(StoreList pStoreList) {
        StoreList tempList = new StoreList(pStoreList);
        for (StoreNode tempNode : tempList.storeList) {
            tempNode.id += pidMax;
        }
        storeList.addAll(tempList.storeList);
        pidMax += tempList.size();
    }

    public void removeByPath(String pPath) {
        ArrayList<StoreNode> removeList = new ArrayList<StoreNode>();
        for (StoreNode aNode : storeList) {
            if (aNode.storePath.equals(pPath)) {
                removeList.add(aNode);
            }
        }
        storeList.removeAll(removeList);
        recapListInfo();
    }

    public HashMap<Integer, Integer> reorgPid() {
        HashMap<Integer, Integer> aMap = new HashMap<Integer, Integer>();
        int newPid = 0;
        for (StoreNode aNode : storeList) {
            newPid++;
            if (aNode.id != newPid) {
                aMap.put(aNode.id, newPid);
                aNode.id = newPid;
            }
        }
        recapListInfo();
        return aMap;
    }

    public void orderByPathAndName() {
        Collections.sort(storeList, new Comparator<StoreNode>() {
            @Override
            public int compare(StoreNode sn1, StoreNode sn2) {
                int cmp = sn1.storePath.compareTo(sn2.storePath);
                if (cmp != 0) {
                    return cmp;
                }
                return sn1.name.compareTo(sn2.name);
            }
        });
    }
    
    public void orderByMd5() {
        Collections.sort(storeList, new Comparator<StoreNode>() {
            @Override
            public int compare(StoreNode sn1, StoreNode sn2) {
                return sn1.md5.compareTo(sn2.md5);
            }
        });
    }
    
    public void loadVersion(String pLine) {
        version = pLine.split(TransConst.COLON)[0];
    }
    
    public void refreshVersion() {
        version = DateUtils.formatDateTimeLongToday();
    }

    public String keepHeader() {
        return new StringBuffer(version).append(TransConst.COLON)
                .append(String.format(TransConst.FORMAT_INT_08, pidMin)).append(TransConst.COLON)
                .append(String.format(TransConst.FORMAT_INT_08, pidMax)).append(TransConst.COLON)
                .append(String.format(TransConst.FORMAT_INT_20, fileSize)).append(TransConst.COLON)
                .append(String.format(TransConst.FORMAT_INT_08, size())).toString();

    }

    public void keepFileByVersion() {
        String aName = TransProp.get("SL_HOME") + "StoreList_" + this.version + ".txt";
        keepFile(aName);
    }
    
    public void keepFileByTag(String aTag, String bTag) {
        String aName = TransProp.get("SL_HOME") + "\\N\\TFLib_" + aTag + "_" + bTag + ".txt";
        keepFile(aName);
    }
    
    public void keepFile(String pFileName) {
        File aFile = new File(pFileName);
        keepFile(aFile);
    }

    public void keepFile(File pFile) {
        if (0 == size()) {
            return;
        }
        try {
            PrintWriter out = new PrintWriter(pFile);
            out.println(keepHeader());
            for (StoreNode aNode : storeList) {
                out.println(aNode.keepNode());
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void keepDelBat(String pFileName) {
        File aFile = new File(pFileName);
        keepDelBat(aFile);
    }

    public void keepDelBat(File pFile) {
        if (0 == size()) {
            return;
        }
        try {
            PrintWriter out = new PrintWriter(pFile);
            for (StoreNode aNode : storeList) {
                //System.out.println(aNode.storePath);
                out.println("del \"" + TransProp.get("ST_HOME")
                        + aNode.storePath.substring(1).replaceAll(TransConst.SLASH, TransConst.BACK_SLASH_4)
                        + aNode.name + "\"");
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        StringBuffer strBuff = new StringBuffer(keepHeader());
        strBuff.append(TransConst.CRLN);
        for (StoreNode aNode : storeList) {
            strBuff.append(aNode.keepNode()).append(TransConst.CRLN);
        }
        return strBuff.toString();
    }

    public static void buildList(String aTag, String[] bTags) {
        int stotal = 0;
        int ttotal = 0;
        
        for (String bTag: bTags) {
            long t0 = System.currentTimeMillis();
            StoreList aList = new StoreList();

            aList.build(TransProp.get("ST_HOME"), "\\" + aTag + "\\" + bTag + "\\");
            aList.keepFileByTag(aTag, bTag);

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

//            aList.build("F:\\Book\\TFLib\\", "\\A" + aTag + "\\V" + bTag + "\\");
//            aList.keepFile("D:\\Qdata\\update\\TFLib_A" + aTag + "_V" + bTag + ".txt");

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
    
    public static String combineList(String oldVer, String aTag, String[] bTags) {
        System.out.println("combine Start...");
        String aName = TransProp.get("SL_HOME") + "StoreList_" + oldVer + ".txt";
        
        StoreList aList = new StoreList();
        aList.load(aName);
        
        for (String bTag : bTags) {
            String bName = TransProp.get("SL_HOME") + "\\N\\TFLib_" + aTag + "_" + bTag + ".txt";
            StoreList bList = new StoreList();
            bList.load(bName);
            aList.attachList(bList);
            System.out.println(bName);
        }
        aList.orderByPathAndName();
        aList.reorgPid();
        aList.refreshVersion();
        aList.keepFileByVersion();
        return aList.version;
    }
    
    @Deprecated
    public static void combineListInit() {
        System.out.println("combine Start...");
//        String srcName = "D:\\Qdata\\update\\storelist\\B\\";
//        String tarName = "D:\\Qdata\\update\\storelist\\A\\A2013.txt";
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
        aList.reorgPid();
        aList.keepFile(tarName);
    }
    
    public static String checkDup(String currVer) {
        System.out.println("checkDup Start...");
        String srcName = TransProp.get("SL_HOME") + "StoreList_" + currVer + ".txt";
        String dupName = TransProp.get("SL_HOME") + "StoreList_" + currVer + "_dup.txt";
        String resName = TransProp.get("SL_HOME") + "StoreList_" + currVer + "_res.txt";
        String delName = TransProp.get("SL_HOME") + "StoreList_" + currVer + "_del.txt";
        String batName = TransProp.get("SL_HOME") + "ToDel.bat";
        
        File srcPath = new File(srcName);
        StoreList aList = new StoreList();
        aList.load(srcPath);

        aList.orderByMd5();

        StoreNode dNode = null;
        StoreList dupList = new StoreList();
        StoreList resList = new StoreList();
        StoreList delList = new StoreList();
        
        int dNum = 0;
        int aNum = 0;
        
        for (StoreNode aNode : aList.storeList) {
            if (null == aNode) {
                continue;
            } else if (!aNode.checkDupNode(dNode)) {
                dNode = aNode;
                resList.storeList.add(aNode);
                continue;
            } else {
                if (!dupList.hasNode(dNode)) {
                    dupList.storeList.add(dNode);
                    dNum++;
                }
                dupList.storeList.add(aNode);
                delList.storeList.add(aNode);
                aNum++;
            }
        }
        //System.out.println(dupList.toString());
        System.out.println("D Number: " + dNum);
        System.out.println("A Number: " + aNum);
//        aList.reorgPid();
        dupList.recapListInfo();
        dupList.keepFile(dupName);
        
        resList.recapListInfo();
        resList.keepFile(resName);
        resList.orderByPathAndName();
        resList.reorgPid();
        resList.recapListInfo();
        resList.keepFileByVersion();
        
        delList.recapListInfo();
        delList.keepFile(delName);
        delList.orderByPathAndName();
        delList.reorgPid();
        delList.recapListInfo();
        delList.keepDelBat(batName);
        System.out.println("Current Length: " + aList.size());
        System.out.println("Reserve Length: " + resList.size());
        System.out.println("Removal Length: " + delList.size());
        System.out.println("Removal file size: " + delList.fileSize);
        
        return resList.version;
    }

    public static void main(String[] args) {
        System.out.println("Test Start...");
        String aTag = "A2016";
        String[] bTags = {"B0626", "B0703", "B0740"};
        String oldVer = "20160705164244895";
        String comVer = null;
        String newVer = null;
        
        buildList(aTag, bTags);
        comVer = combineList(oldVer, aTag, bTags);
        newVer = checkDup(comVer);

        System.out.println("New version is: " + newVer + ".");
        System.out.println("Test Done.");
    }

}
