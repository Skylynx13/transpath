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
import java.io.PrintWriter;
import java.util.ArrayList;

 /**
 * ClassName: StoreList <br/>
 * Description: TODO <br/>
 * Date: 2016-6-17 上午12:05:40 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class StoreList {
    int pidMin;
    int pidMax;
    int length;
    ArrayList<StoreNode> storeList;
        
    public StoreList() {
        pidMin = 0;
        pidMax = 0;
        length = 0;
        storeList = new ArrayList<StoreNode>();
    }
    
    public StoreList(StoreList pStoreList) {
        pidMin = pStoreList.pidMin;
        pidMax = pStoreList.pidMax;
        length = pStoreList.length;
        storeList = new ArrayList<StoreNode>(pStoreList.storeList);
    }
    
    public void addNode(StoreNode pStoreNode) {
        pStoreNode.id = ++pidMax;
        storeList.add(pStoreNode);
        length = storeList.size();
    }
    
    public StoreNode get(int index) {
        return storeList.get(index);
    }
    
    public void persistFile(File pFile) {
        try {
            PrintWriter out = new PrintWriter(pFile);
            for (StoreNode aNode : storeList) {
                out.println(aNode.keepNode());
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static void main (String[] args) {
        System.out.println("Test Start...");
        System.out.println("Test Done.");
    }
    
}
