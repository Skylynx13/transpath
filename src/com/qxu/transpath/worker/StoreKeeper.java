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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import com.qxu.transpath.oldschool.NodeList;
import com.qxu.transpath.tree.Node;
import com.qxu.transpath.tree.StoreNode;
import com.qxu.transpath.utils.DateUtils;
import com.qxu.transpath.utils.FileUtils;
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
    private static void keepListFromRoot(String listFile, String rootLoc) {
        //Keep list from a root.
        NodeList.keepList(listFile, NodeList.buildFromRoot(rootLoc));
    }

    private static void keepBlockFromRoot(String blockFile, String block, String rootLoc) {
        // Keep a block of list from a root.
        NodeList.keepList(blockFile,
                NodeList.buildBlockFromRoot(block, rootLoc));

    }
    
    private static void keepStoreBlockFromRoot(String blockFile, String block, String rootLoc) {
        // Keep a block of list from a root.
        long t0 = System.currentTimeMillis();
        ArrayList<StoreNode> snList = NodeList.buildStoreBlockFromRoot(block, rootLoc);
        NodeList.keepStoreList(blockFile, snList);
        int s1 = snList.size();
        long t1 = System.currentTimeMillis() - t0;
        System.out.println("Store Listed: " + s1);
        System.out.println("Time Used: " + t1);
        System.out.println("Avg Speed: " + (s1)*1000000/t1*0.001 + "item/s.");
    }
    
    private static void combineLists(String targetFile, String srcFile1, String srcFile2) {
        // Combine two list into a new one.
        NodeList.keepList(targetFile,
                NodeList.combine(NodeList.buildFromFile(srcFile1), 
                        NodeList.buildFromFile(srcFile2), false));
    }
    
    public static void buildStoreBlock(String arg) {
        String year = arg.substring(1,3);
        String newBlock = "D:\\Qdata\\update\\TFLib_A20" + year + "_" + arg + ".txt";
        System.out.println("Keeping block...");
        StoreKeeper.keepBlockFromRoot(newBlock, "\\A20" + year + "\\", "F:\\Book\\TFLib\\");
        System.out.println("Combining store...");
        if (year.equals("15")) {
            StoreKeeper.combineLists(TransProp.get("TP_HOME") + "store15.txt", TransProp.get("TP_HOME") + "storehis.txt", newBlock);
        } else if (year.equals("16")) {
            StoreKeeper.combineLists(TransProp.get("TP_HOME") + "store16.txt", TransProp.get("TP_HOME") + "store15.txt", newBlock);
        }
        System.out.println("Done.");
    }

    public static void buildStoreArchive(String arg) {
        String newBlock = "D:\\Qdata\\update\\TFLib_" + arg + ".txt";
        System.out.println("Keeping block...");
        StoreKeeper.keepBlockFromRoot(newBlock, "\\" + arg + "\\", "F:\\Book\\TFLib\\");
        System.out.println("Backup...");
        FileUtils.copyFileBytes(newBlock, TransProp.get("TP_HOME") + "TFLib_" + arg + "_" + DateUtils.formatDateToday() + ".txt");
        System.out.println("Done.");
    }

    public static void buildStoreHis() {
        System.out.println("Combining storehis...."); 
        StoreKeeper.combineLists(TransProp.get("TP_HOME") + "storehis.txt", 
                "D:\\Qdata\\update\\TFLib_A2013_0_1_2.txt",
                "D:\\Qdata\\update\\TFLib_A2014_0_1_2.txt");
        System.out.println("Done.");
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
        for (String arg : args) {
            if (arg.equals("storehis")) {
                buildStoreHis();
            } else if (arg.startsWith("b")) {
                buildStoreBlock(arg);
            } else if (arg.startsWith("A")) {
                buildStoreArchive(arg);
            } 
        }
        if (args.length == 0) {
            System.out.println("Another job done.");
        }
        System.out.println("StoreKeeper job done.");
    }

}
