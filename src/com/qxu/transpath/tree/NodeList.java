/**
 * Copyright (c) 2015,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:NodeList.java
 * Date:2015-2-11 下午2:30:48
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
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import com.qxu.transpath.utils.TranspathConstants;

 /**
 * ClassName: NodeList <br/>
 * Description: Tools for NodeList.<br/>
 * Date: 2015-2-11 下午2:30:48 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class NodeList {
    
    public static ArrayList<Node> buildFromRoot(String pRoot) {
        String aRoot = pRoot.replaceAll(TranspathConstants.BACK_SLASH_4, TranspathConstants.SLASH);
        if (aRoot.endsWith(TranspathConstants.SLASH)) {
            aRoot=aRoot.substring(0, aRoot.length()-1);
        }
        if (new File(aRoot).isFile()) {
            return null;
        }
        return NodeList.buildFromPath(aRoot, aRoot);
    }
    
    public static ArrayList<Node> buildBlockFromRoot(String pBlock, String pRoot) {
        String aRoot = pRoot.replaceAll(TranspathConstants.BACK_SLASH_4, TranspathConstants.SLASH);
        String aBlock = pBlock.replaceAll(TranspathConstants.BACK_SLASH_4, TranspathConstants.SLASH);
        if (aRoot.endsWith(TranspathConstants.SLASH)) {
            aRoot=aRoot.substring(0, aRoot.length()-1);
        }
        if (aBlock.endsWith(TranspathConstants.SLASH)) {
            aBlock=aBlock.substring(0, aBlock.length()-1);
        }
        aBlock = aRoot + aBlock;
        if (new File(aRoot).isFile() || new File(aBlock).isFile()) {
            return null;
        }
        return NodeList.buildFromPath(aBlock, aRoot);
    }
    
    private static ArrayList<Node> buildFromPath(String pPath, String pRoot) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        File dirRoot = new File(pPath);
        if (dirRoot.isFile()) {
            return null;
        }
        
        System.out.println(pPath);
        
        for (File aFile: dirRoot.listFiles()) {
            if (aFile.isFile()) {
                Node aNode = new Node(0, aFile.getName()); 
                aNode.putBranch(TranspathConstants.BRANCH_2ND, 
                        composeIndexPathDefault());
                aNode.putBranch(TranspathConstants.BRANCH_1ST,
                        composeStoragePath(pRoot, aFile));
                aNode.putBranch(TranspathConstants.BRANCH_0MD, 
                        composeMetadata(aFile));
                nodes.add(aNode);
            }
            if (aFile.isDirectory()) {
                nodes.addAll(NodeList.buildFromPath(aFile.getPath(), pRoot));
            }
        }
        return nodes;
    }

    private static String composeIndexPathDefault() {
        return TranspathConstants.INDEX_PRE_CATALOG;
    }

    private static String composeMetadata(File pFile) {
        return new StringBuffer(TranspathConstants.SLASH)
            .append(pFile.length())
            .append(TranspathConstants.SLASH)
            .append(pFile.lastModified())
            .toString();
    }

    private static String composeStoragePath(String pRoot, File pFile) {
        return pFile.getParent()
                .replaceAll(TranspathConstants.BACK_SLASH_4, TranspathConstants.SLASH)
                .replaceAll(pRoot, TranspathConstants.EMPTY) + TranspathConstants.SLASH;
    }

    public static void sortByNodeName(ArrayList<Node> nodes) {
        Collections.sort(nodes, new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                return n1.getName().compareTo(n2.getName());
            }
        });
    }
    
    public static void sortByBranchName(ArrayList<Node> nodes, final String key) {
        Collections.sort(nodes, new Comparator<Node>() {
           @Override
           public int compare(Node n1, Node n2) {
               return n1.getBranch(key).compareTo(n2.getBranch(key));
           }
        });
    }

    public static ArrayList<Node> buildFromFile(String pFile) {
        ArrayList<Node> nodes = new ArrayList<Node>();

        Scanner aScan = null;
        try {
            aScan = new Scanner(new FileReader(pFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        
        String aLine = "";
        while (aScan.hasNext()) {
            aLine = aScan.nextLine().trim();
            String[] iLine = aLine.split(TranspathConstants.COLON);
            if (aLine.startsWith(TranspathConstants.NODE_ID) &&
                iLine.length == TranspathConstants.FIELDS_BRANCH) {
                nodes.add(new Node(Integer.parseInt(iLine[1]), iLine[2]));
                continue;
            }
            if (iLine.length == TranspathConstants.FIELDS_INFO &&
                nodes != null && nodes.size() > 0) {
                nodes.get(nodes.size()-1).putBranch(iLine[0], iLine[1]);
                continue;
            }
            System.out.println("File Corrupted.");
            return null;
        }
        aScan.close();

        return nodes;
    }

    @SuppressWarnings("unused")
    private static ArrayList<Node> buildFromFile_SupportedBranchTypes(String pFile) {
        ArrayList<Node> nodes = new ArrayList<Node>();

        Scanner aScan = null;
        try {
            aScan = new Scanner(new FileReader(pFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        
        String aLine = "";
        Node node = null;
        while (aScan.hasNext()) {
            aLine = aScan.nextLine().trim();
            if (aLine.startsWith(TranspathConstants.NODE_ID)) {
                node = new Node(Integer.parseInt(aLine.substring(TranspathConstants.NODE_ID.length(), aLine.indexOf(TranspathConstants.COLON))), 
                                 aLine.substring(aLine.indexOf(TranspathConstants.COLON)+TranspathConstants.COLON.length()));
                nodes.add(node);
                continue;
            }
            for (String branchType : TranspathConstants.SUPPORTED_BRANCH_TYPES) {
                if (aLine.startsWith(branchType)) {
                    if (nodes == null || nodes.size() < 1) {
                        System.out.println("File Corrupted.");
                        return null;
                    }
                    nodes.get(nodes.size()-1).putBranch(branchType, aLine.substring(aLine.indexOf(TranspathConstants.COLON)+ TranspathConstants.COLON.length()));
                }
            }
        }
        //System.out.println(nodes.size());
        aScan.close();

        return nodes;
    }

    public static void keepList(String outFile, ArrayList<Node> nodes) {
        try {
            PrintWriter out = new PrintWriter(outFile);
            for (Node aNode : nodes) {
                out.println(aNode.keepNode());
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Node> combine(ArrayList<Node> nl1, ArrayList<Node> nl2) {
        if (NodeList.checkDuplicatedNode(nl1).size() > 0) {
            System.out.println("Duplicated Node Names NL1: " + NodeList.checkDuplicatedNode(nl1));
            return null;
        }
        if (NodeList.checkDuplicatedNode(nl2).size() > 0) {
            System.out.println("Duplicated Node Names NL2: " + NodeList.checkDuplicatedNode(nl2));
            return null;
        }
        
        if (0 == nl1.size()) {
            return (ArrayList<Node>) nl2.clone();
        }
        if (0 == nl2.size()) {
            return (ArrayList<Node>) nl1.clone();
        }

        ArrayList<Node> nl3 = new ArrayList<Node>();
        int idx1 = 0;
        int idx2 = 0;

        while(idx1 < nl1.size() && idx2 < nl2.size()) {
            Node n1 = nl1.get(idx1);
            Node n2 = nl2.get(idx2);
            
            int comp = n1.getName().compareTo(n2.getName());
            
            if (0 == comp) {
                nl3.add(n1.merge(n2));
                idx1++;
                idx2++;
                continue;
            }
            if (comp < 0) {
                nl3.add(n1.clone());
                idx1++;
                continue;
            }
            if (0 < comp) {
                nl3.add(n2.clone());
                idx2++;
                continue;
            }
        }
        while (idx1 < nl1.size()) {
            nl3.add(nl1.get(idx1++).clone());
        }
        while (idx2 < nl2.size()) {
            nl3.add(nl2.get(idx2++).clone());
        }
        
        return nl3;
    }
    
    @SuppressWarnings({ "unchecked", "unused" })
    private static ArrayList<Node> combineIterator(ArrayList<Node> nl1, ArrayList<Node> nl2) {
        if (NodeList.checkDuplicatedNode(nl1).size() > 0) {
            System.out.println("Duplicated Node Names NL1: " + NodeList.checkDuplicatedNode(nl1));
            return null;
        }
        if (NodeList.checkDuplicatedNode(nl2).size() > 0) {
            System.out.println("Duplicated Node Names NL2: " + NodeList.checkDuplicatedNode(nl2));
            return null;
        }
        
        Iterator<Node> iter1 = nl1.iterator();
        Iterator<Node> iter2 = nl2.iterator();
        if (!iter1.hasNext()) {
            return (ArrayList<Node>) nl2.clone();
        }
        if (!iter2.hasNext()) {
            return (ArrayList<Node>) nl1.clone();
        }

        ArrayList<Node> nl3 = new ArrayList<Node>();
        Node n1 = null;
        Node n2 = null;
        boolean n1next = true;
        boolean n2next = true;
        while (iter1.hasNext() && iter2.hasNext()) {
            if (n1next) {
                n1 = iter1.next();
            }
            if (n2next) {
                n2 = iter2.next();
            }
            //System.out.println("n1: " + n1.getName() + ". n2: " + n2.getName() + ".");
            int comp = n1.getName().compareTo(n2.getName());
            if (0 == comp) {
                nl3.add(n1.merge(n2));
                n1next = true;
                n2next = true;
                continue;
            }
            if (comp < 0) {
                nl3.add(n1.clone());
                n1next = true;
                n2next = false;
                continue;
            }
            if (0 < comp) {
                nl3.add(n2.clone());
                n1next = false;
                n2next = true;
                continue;
            }
        }
        if (!n1next) nl3.add(n1.clone());
        if (!n2next) nl3.add(n2.clone());
        
        while (iter1.hasNext()) {
            nl3.add(iter1.next().clone());
        }
        while (iter2.hasNext()) {
            nl3.add(iter2.next().clone());
        }
        return nl3;
    }
    
    public static Node findNodeByName(ArrayList<Node> nodes, String nodeName) {
        Iterator<Node> iter = nodes.iterator();
        while(iter.hasNext()) {
            Node node = iter.next();
            if (node.getName().equals(nodeName)) {
                return node;
            }
        }
        return null;
    }

    public static List<String> checkDuplicatedNode(ArrayList<Node> nodes) {
        List<String> dupStr = new ArrayList<String>();
        NodeList.sortByNodeName(nodes);
        String lastNodeName = null;
        for(Node node: nodes) {
            if (lastNodeName != null && lastNodeName.equals(node.getName())) {
                dupStr.add(lastNodeName);
            }
            lastNodeName = node.getName();
        }
        return dupStr;
    }

    public static void main(String[] args) {
        System.out.println("Started...");

//      //Keep list from a root.
//      NodeList.keepList("D:\\_TF\\_Update\\TFLib_A2013_1st.txt", NodeList.buildFromRoot("I:\\Book\\TFLib\\"));

      //Keep a block of list from a root.
//        NodeList.keepList("D:\\_TF\\_Update\\TFLib_A2013_fin.txt", NodeList.buildBlockFromRoot("\\A2013\\", "F:\\Book\\TFLib\\"));
//        NodeList.keepList("D:\\_TF\\_Update\\TFLib_A2014_fin.txt", NodeList.buildBlockFromRoot("\\A2014\\", "F:\\Book\\TFLib\\"));
        NodeList.keepList("D:\\_TF\\_Update\\TFLib_A2015_b1103.txt", NodeList.buildBlockFromRoot("\\A2015\\", "F:\\Book\\TFLib\\"));

//        //Combine two list into a new one.
//        NodeList.keepList("D:\\_TF\\_Update\\TFLib_A2013_0_1_2.txt", 
//                NodeList.combine(
//                        NodeList.buildFromFile("D:\\_TF\\_Update\\TFLib_A2013_1st_2nd_fin.txt"), 
//                        NodeList.buildFromFile("D:\\_TF\\_Update\\TFLib_A2013_fin.txt")));
        
//        //Find those who only has one branch.
//        ArrayList<Node> nodes = NodeList.buildFromFile("D:\\_TF\\_Update\\TFLib_A2013_1st_2nd_fin.txt");
//        System.out.println("=========================================================");
//        System.out.println("1ST only:");
//        for (Node node : nodes) {
//            if (node.getBranch("2ND") == null) {
//                System.out.println(node.keepNode());
//            }
//        }
//        System.out.println("=========================================================");
//        System.out.println("2ND only:");
//        for (Node node : nodes) {
//            if (node.getBranch("1ST") == null) {
//                System.out.println(node.keepNode());
//            }
//        }
//        System.out.println("=========================================================");
//        System.out.println("Others:");
//        for (Node node : nodes) {
//            if (node.numberOfBranch() != 2 && node.numberOfBranch() != 1) {
//                System.out.println(node.keepNode());
//            }
//        }
        
//        //Keep list ignoring single branch node
//        int tNum = 0;
//        int kNum = 0;
//        try {
//            PrintWriter out = new PrintWriter("D:\\_TF\\_Update\\TFLib_A2013_1st_2nd_fin.txt");
//            for (Node aNode : NodeList.buildFromFile("D:\\_TF\\_Update\\TFLib_A2013_1st_2nd++.txt")) {
//                if (aNode.numberOfBranch() == 2) {
//                    out.println(aNode.keepNode());
//                    kNum++;
//                }
//                tNum++;
//            }
//            System.out.println("Total Nodes: " + tNum);
//            System.out.println("Kept Nodes: " + kNum);
//            out.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        System.out.println("Done.");
    }

}
