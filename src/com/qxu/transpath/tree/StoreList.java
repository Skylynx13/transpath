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
    public String version;
    public int pidMin;
    public int pidMax;
    public long fileSize;
    public ArrayList<StoreNode> storeList;

    public StoreList() {
        version = DateUtils.formatDateTimeLongToday();
        pidMin = 0;
        pidMax = 0;
        fileSize = 0;
        storeList = new ArrayList<StoreNode>();
    }

    public StoreList(StoreList pStoreList) {
        version = DateUtils.formatDateTimeLongToday();
        storeList = new ArrayList<StoreNode>(pStoreList.storeList);
        recap();
    }

    public StoreList(ArrayList<StoreNode> pArrayList) {
        version = DateUtils.formatDateTimeLongToday();
        storeList = new ArrayList<StoreNode>(pArrayList);
        recap();
    }

    public int size() {
        return (null == storeList) ? 0 : storeList.size();
    }

    public void recap() {
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

    public void clear() {
        version = DateUtils.formatDateTimeLongToday();
        pidMin = 0;
        pidMax = 0;
        fileSize = 0;
        storeList = new ArrayList<StoreNode>();
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

    public void removeByPath(String pPath) {
        ArrayList<StoreNode> removeList = new ArrayList<StoreNode>();
        for (StoreNode aNode : storeList) {
            if (aNode.storePath.equals(pPath)) {
                removeList.add(aNode);
            }
        }
        storeList.removeAll(removeList);
        recap();
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
        recap();
        return aMap;
    }

    public void refreshVersion() {
        version = DateUtils.formatDateTimeLongToday();
    }

    private String regulateSlash(String pStr) {
        String aStr = pStr.replaceAll(TransConst.BACK_SLASH_4, TransConst.SLASH);
        if (aStr.endsWith(TransConst.SLASH)) {
            aStr = aStr.substring(0, aStr.length() - 1);
        }
        return aStr;
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

    public void loadVersion(String pLine) {
        version = pLine.split(TransConst.COLON)[0];
    }

    public void build(String pRoot, String pPathName) {
        String aRoot = regulateSlash(pRoot);
        String aPathName = aRoot + regulateSlash(pPathName);
        
        if (new File(aRoot).isFile() || new File(aPathName).isFile()) {
            return;
        }

        buildByPath(aRoot, aPathName);
        recap();
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
    
    public String keepHeader() {
        return new StringBuffer(version).append(TransConst.COLON)
                .append(String.format(TransConst.FORMAT_INT_08, pidMin)).append(TransConst.COLON)
                .append(String.format(TransConst.FORMAT_INT_08, pidMax)).append(TransConst.COLON)
                .append(String.format(TransConst.FORMAT_INT_20, fileSize)).append(TransConst.COLON)
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
}
