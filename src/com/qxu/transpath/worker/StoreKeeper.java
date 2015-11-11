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
import java.io.PrintWriter;
import java.util.ArrayList;

import com.qxu.transpath.tree.Node;
import com.qxu.transpath.tree.NodeList;

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
            PrintWriter out = new PrintWriter("D:\\_TF\\_Update\\TFLib_A2013_1st_2nd_fin.txt");
            for (Node aNode : NodeList.buildFromFile("D:\\_TF\\_Update\\TFLib_A2013_1st_2nd++.txt")) {
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
        ArrayList<Node> nodes = NodeList.buildFromFile("D:\\_TF\\_Update\\TFLib_A2013_1st_2nd_fin.txt");
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
                System.out.println("Combining storehis...");
                StoreKeeper.combineLists("resource/storehis.txt", 
                        "D:\\_TF\\_Update\\TFLib_A2013_0_1_2.txt",
                        "D:\\_TF\\_Update\\TFLib_A2014_0_1_2.txt");
                System.out.println("Done.");
            } else if (arg.startsWith("b")) {
                String newBlock = "D:\\_TF\\_Update\\TFLib_A2015_" + arg + ".txt";
                System.out.println("Keeping block...");
                StoreKeeper.keepBlockFromRoot(newBlock, "\\A2015\\", "F:\\Book\\TFLib\\");
                System.out.println("Combining store...");
                StoreKeeper.combineLists("resource/store.txt", "resource/storehis.txt", newBlock);
                System.out.println("Done.");
            } else if (arg.equals("istore")) {
                System.out.println("Keeping store index...");
                StoreKeeper.keepStoreIndex("resource/store.txt", 
                        "resource/istore.txt");
                System.out.println("Done.");
            } 
        }
        System.out.println("StoreKeeper job done.");
        
        // StoreKeeper.keepListFromRoot("D:\\_TF\\_Update\\TFLib_A2013_1st.txt",
        // "I:\\Book\\TFLib\\");

        // StoreKeeper.combineLists("D:\\_TF\\_Update\\TFLib_A2013_0_1_2.txt",
        // "D:\\_TF\\_Update\\TFLib_A2013_1st_2nd_fin.txt",
        // "D:\\_TF\\_Update\\TFLib_A2013_fin.txt");

    }

}
