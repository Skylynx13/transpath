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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

import com.qxu.transpath.utils.DateUtils;
import com.qxu.transpath.utils.TransConst;

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
    ArrayList<StoreNode> storeList;

    public StoreList() {
        version = DateUtils.formatDateTimeLongToday();
        pidMin = 0;
        pidMax = 0;
        storeList = new ArrayList<StoreNode>();
    }

    public StoreList(StoreList pStoreList) {
        version = DateUtils.formatDateTimeLongToday();
        pidMin = pStoreList.pidMin;
        pidMax = pStoreList.pidMax;
        storeList = new ArrayList<StoreNode>(pStoreList.storeList);
    }

    public StoreList(ArrayList<StoreNode> pArrayList) {
        version = DateUtils.formatDateTimeLongToday();
        storeList = new ArrayList<StoreNode>(pArrayList);
        recapPidMinMax();
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
            version = loadVersion(aScan.nextLine());
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
        recapPidMinMax();
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

    public void recapPidMinMax() {
        pidMin = Integer.MAX_VALUE;
        pidMax = 0;
        for (StoreNode aNode : storeList) {
            if (aNode.id < pidMin) {
                pidMin = aNode.id;
            }
            if (aNode.id > pidMax) {
                pidMax = aNode.id;
            }
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

    public void addNode(StoreNode pStoreNode) {
        if (0 == size()) {
            pidMin = 1;
        }
        pStoreNode.id = ++pidMax;
        storeList.add(pStoreNode);
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
        recapPidMinMax();
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
        recapPidMinMax();
        return aMap;
    }

    public HashMap<Integer, Integer> orderByPathAndName() {
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
        return reorgPid();
    }
    
    public String loadVersion(String pLine) {
        return pLine.split(TransConst.COLON)[0];
    }

    public String keepHeader() {
        return new StringBuffer(version).append(TransConst.COLON)
                .append(String.format(TransConst.FORMAT_INT_08, pidMin)).append(TransConst.COLON)
                .append(String.format(TransConst.FORMAT_INT_08, pidMax)).append(TransConst.COLON)
                .append(String.format(TransConst.FORMAT_INT_08, size())).toString();

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

    public String toString() {
        StringBuffer strBuff = new StringBuffer(keepHeader());
        strBuff.append(TransConst.CRLN);
        for (StoreNode aNode : storeList) {
            strBuff.append(aNode.keepNode()).append(TransConst.CRLN);
        }
        return strBuff.toString();
    }

    public static void buildList() {
        int stotal = 0;
        int ttotal = 0;
        
        int startNum = 1102;
        int endNum = 1228;

        for (int bNum = startNum; bNum <= endNum; bNum++) {
            long t0 = System.currentTimeMillis();
            StoreList aList = new StoreList();

            String aTag = "2014";
            String bTag = String.format(TransConst.FORMAT_INT_04, bNum);

            aList.build("F:\\Book\\TFLib\\", "\\A" + aTag + "\\B" + bTag + "\\");
            aList.keepFile("D:\\Qdata\\update\\TFLib_A" + aTag + "_B" + bTag + ".txt");

//            aList.build("F:\\Book\\TFLib\\", "\\A2014\\V0101\\");
//            aList.keepFile("D:\\Qdata\\update\\TFLib_A2014_V0101.txt");

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
    
    public static void combineList() {
        String srcName = "D:\\Qdata\\update\\storelist\\B\\";
        String tarName = "D:\\Qdata\\update\\storelist\\A\\A2013.txt";
        
        File srcPath = new File(srcName);
        StoreList aList = new StoreList();
        
        if (!srcPath.isDirectory()) {
            return;
        }
        
        for (File aBlock : srcPath.listFiles()) {
            StoreList bList = new StoreList();
            bList.load(aBlock);
            aList.attachList(bList);
        }
        aList.reorgPid();
        aList.keepFile(tarName);
    }
    
    public static void main(String[] args) {
        System.out.println("Test Start...");
        //buildList();
        combineList();

        System.out.println("Test Done.");
    }

}
