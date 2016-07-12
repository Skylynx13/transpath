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
import java.util.Arrays;

import com.qxu.transpath.tree.Node;
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

    private static String fileNameOfVersion(String version) {
        return TransProp.get("SL_HOME") + "StoreList_" + version + ".txt";
    }
    
    private static String fileNameOfTag(String aTag, String bTag) {
        return TransProp.get("SL_HOME") + "\\N\\TFLib_" + aTag + "_" + bTag + ".txt";
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
                //System.out.println(aNode.storePath);
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
            aList.keepFile(fileNameOfTag(aTag, bTag));

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
            String bName = fileNameOfTag(aTag, bTag);
            StoreList bList = new StoreList();
            bList.load(bName);
            aList.attachList(bList);
            System.out.println(bName);
        }
        aList.orderByPathAndName();
        aList.reorgId();
        aList.recap();
        aList.refreshVersion();
        aList.keepFile(fileNameOfVersion(aList.version));
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
        aList.reorgId();
        aList.recap();
        aList.keepFile(tarName);
    }
    
    public static String checkDup(String currVer) {
        System.out.println("checkDup Start...");
        String srcName = fileNameOfVersion(currVer);
        String dupName = fileNameOfVersion(currVer + "_dup");
        String resName = fileNameOfVersion(currVer + "_res");
        String delName = fileNameOfVersion(currVer + "_del");
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
        
        for (Node aNode : aList.nodeList) {
            if (null == aNode) {
                continue;
            } else if (!((StoreNode)aNode).checkDupNode(dNode)) {
                dNode = ((StoreNode)aNode);
                resList.nodeList.add(aNode);
                continue;
            } else {
                if (!dupList.hasNode(dNode)) {
                    dupList.nodeList.add(dNode);
                    dNum++;
                }
                dupList.nodeList.add(aNode);
                delList.nodeList.add(aNode);
                aNum++;
            }
        }
        //System.out.println(dupList.toString());
        System.out.println("D Number: " + dNum);
        System.out.println("A Number: " + aNum);
//        aList.reorgPid();
        dupList.recap();
        dupList.keepFile(dupName);
        
        resList.recap();
        resList.keepFile(resName);
        resList.orderByPathAndName();
        resList.reorgId();
        resList.recap();
        resList.keepFile(fileNameOfVersion(resList.version));
        
        delList.recap();
        delList.keepFile(delName);
        delList.orderByPathAndName();
        delList.reorgId();
        delList.recap();
        keepDelBat(delList, batName);
        System.out.println("Current Length: " + aList.size());
        System.out.println("Reserve Length: " + resList.size());
        System.out.println("Removal Length: " + delList.size());
        System.out.println("Removal file size: " + delList.fileSize);
        
        return resList.version;
    }
    
    /**
     * main:<br/>
     * bin>java com.qxu.transpath.worker.StoreKeeper storehis<br/>
     * bin>java com.qxu.transpath.worker.StoreKeeper b1112 istore<br/>
     * 
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("StoreKeeper on job...");
        
        String aTag = "A2016";
        String[] bTags = {"B0626", "B0703", "B0740"};
        String oldVer = "20160705164244895";
        String comVer = null;
        String newVer = null;
        
//        buildList(aTag, bTags);
        comVer = combineList(oldVer, aTag, bTags);
        newVer = checkDup(comVer);
        System.out.println("New version is: " + newVer + ".");
        
        System.out.println("StoreKeeper job done.");
    }

}
