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

import com.qxu.transpath.tree.Node;
import com.qxu.transpath.tree.NodeList;
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
    
    private static void keepListIgnoreSingleBranch() {
        //Keep list ignoring single branch node
        int tNum = 0;
        int kNum = 0;
        try {
            PrintWriter out = new PrintWriter("D:\\Qdata\\update\\TFLib_A2013_1st_2nd_fin.txt");
            for (Node aNode : NodeList.buildFromFile("D:\\Qdata\\update\\TFLib_A2013_1st_2nd++.txt")) {
                if (aNode.numberOfBranch() == 2) {
                    out.println(aNode.keepNode());
                    kNum++;
                }
                tNum++;
            }
            System.out.println("Total Nodes: " + tNum);
            System.out.println("Kept Nodes: " + kNum);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void keepStoreIndex(String inFile, String outFile) {
        //Keep simple name of the list.
        int tNum = 0;
        try {
            PrintWriter out = new PrintWriter(outFile);
            for (Node aNode : NodeList.buildFromFile(inFile)) {
                //System.out.println("Debug: " + aNode.getSimpleName());
                out.println(aNode.getName());
                tNum++;
            }
            System.out.println("Total Index Name: " + tNum);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private static void findSingleBranchNode() {
        // Find those who only has one branch.
        ArrayList<Node> nodes = NodeList.buildFromFile("D:\\Qdata\\update\\TFLib_A2013_1st_2nd_fin.txt");
        System.out.println("=========================================================");
        System.out.println("1ST only:");
        for (Node node : nodes) {
            if (node.getBranch("2ND") == null) {
                System.out.println(node.keepNode());
            }
        }
        System.out.println("=========================================================");
        System.out.println("2ND only:");
        for (Node node : nodes) {
            if (node.getBranch("1ST") == null) {
                System.out.println(node.keepNode());
            }
        }
        System.out.println("=========================================================");
        System.out.println("Others:");
        for (Node node : nodes) {
            if (node.numberOfBranch() != 2 && node.numberOfBranch() != 1) {
                System.out.println(node.keepNode());
            }
        }
    }

    public static void buildStoreIndex() {
        System.out.println("Keeping store index...");
        StoreKeeper.keepStoreIndex(TransProp.get("TP_HOME") + "store16.txt", 
                TransProp.get("TP_HOME") + "istore.txt");
        System.out.println("Done.");
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

    public static void setId() {
        String srcFile = TransProp.get("TP_HOME") + "store16.txt";
        String targetFile = TransProp.get("TP_HOME") + "store1601.txt";
        ArrayList<Node> nodeList = NodeList.buildFromFile(srcFile);
        int nodeId = 1;
        for (Node aNode : nodeList) {
            System.out.println(aNode.getId() + " => " + nodeId);
            aNode.setId(nodeId);
            nodeId++;
        }
        NodeList.keepList(targetFile, nodeList);
    }
    public static void fetchVariant() {
        String srcFile = TransProp.get("TP_HOME") + "store16.txt";
        String targetFile = TransProp.get("TP_HOME") + "store1600ri2016.txt";
        ArrayList<Node> nodeList = NodeList.buildFromFile(srcFile);
        ArrayList<Node> newList = new ArrayList<Node>();
        for (Node aNode : nodeList) {
//            if ((aNode.getName().matches("(?i).*poster.*") ||
//                    aNode.getName().matches("(?i).*covers only.*") ||
//                    aNode.getName().matches("(?i).*cover only.*") ||
//                    aNode.getName().matches("(?i).*variant.*")) &&
//                aNode.getBranch("1ST").matches("(?i).*/A2013/.*")) {
                if ((aNode.getName().matches("(?i).*retailer.*") ||
                        aNode.getName().matches("(?i).*incentive.*")) &&
                        aNode.getBranch("1ST").matches("(?i).*/A2016/.*")) {
                newList.add(aNode);
            }
        }
        NodeList.keepList(targetFile, newList);
    }
    public static void get2ndTree() {
        String srcFile = TransProp.get("TP_HOME") + "store16.txt";
        String targetFile = TransProp.get("TP_HOME") + "store1602nd.txt";
        ArrayList<Node> nodeList = NodeList.buildFromFile(srcFile);
        ArrayList<String> secondList = new ArrayList<String>();
        for (Node aNode : nodeList) {
            secondList.add(aNode.getBranch("2ND") + aNode.getName());// + ":" + aNode.getName());
        }
        Collections.sort(secondList);
        try {
            PrintWriter out = new PrintWriter(targetFile);
            for (String entry : secondList) {
                out.println(entry);
            }
            System.out.println("Total Nodes: " + secondList.size());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static void process2nd(){
        Scanner inFile = null;
        PrintWriter outFile = null;

        try {
            inFile = new Scanner(new FileReader(TransProp.get("TP_HOME") + "store1602ndtest001.txt"));
            outFile = new PrintWriter(TransProp.get("TP_HOME") + "store1602ndtest002.txt");
            String aLine = "";
            while (inFile.hasNext()) {
                aLine = inFile.nextLine().trim();
                outFile.println(aLine.replaceAll("\\.cbr", ""));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            inFile.close();
            outFile.close();
        }
    }
    
    private static void convertStore() {
        long t0 = System.currentTimeMillis();
        String srcFile = TransProp.get("TP_HOME") + "store16.txt";
        String targetFile = TransProp.get("TP_HOME") + "store_20160616.txt";
        ArrayList<Node> nodeList = NodeList.buildFromFile(srcFile);
        NodeList.sortByFullStoreName(nodeList);
        try {
            PrintWriter out = new PrintWriter(targetFile);
            for (Node node : nodeList) {
                out.println(new StoreNode(node).keepNode());
            }
            System.out.println("Total Nodes: " + nodeList.size());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        long t1 = System.currentTimeMillis();
        System.out.println("Total Time(ms): " + (t1-t0));
    }

    public static void buildStoreStoreBlock() {
        System.out.println("Keeping block...");
        StoreKeeper.keepStoreBlockFromRoot("D:\\Qdata\\update\\TFLib_A2013_BlockTest.txt", 
                                           "\\A2013\\V0101\\", 
                                           "F:\\Book\\TFLib\\");
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
            } else if (arg.equals("istore")) {
                buildStoreIndex();
            } 
        }
        if (args.length == 0) {
            //buildStoreStoreBlock();
            //convertStore();
            fetchVariant();
            System.out.println("Another job done.");
        }
        System.out.println("StoreKeeper job done.");
    }

}
