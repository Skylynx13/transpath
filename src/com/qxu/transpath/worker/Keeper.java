/**
 * Copyright (c) 2015,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.worker
 * File Name:StorageKeeper.java
 * Date:2015-1-30 下午2:47:13
 * 
 */
package com.qxu.transpath.worker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import com.qxu.transpath.tree.Node;
import com.qxu.transpath.tree.NodeTree;
import com.qxu.transpath.utils.TranspathConstants;

 /**
 * ClassName: StorageKeeper <br/>
 * Description: Marked as 1st. <br/>
 * Date: 2015-1-30 下午2:47:13 <br/>
 * <br/>
 * 
 * @author skylynx
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class Keeper{
    public static ArrayList<Node> buildListFromStorage(String pRoot) {
        if (pRoot.endsWith("\\") || pRoot.endsWith("/")) {
            pRoot=pRoot.substring(0,pRoot.length()-1);
        }
        return Keeper.buildListFromARoot(pRoot, pRoot);
    }
    
    public static ArrayList<Node> buildListFromFile(String pFile) {
        return Keeper.parseListFile(pFile);
    }

    public static NodeTree buildTreeFromList(ArrayList<Node> nodes) {
        NodeTree nTree = new NodeTree();
        for (Node aNode : nodes) {
            nTree.add1stBranchNode(aNode);
        }
        return nTree;
    }

    public static boolean keepList(String outFile, ArrayList<Node> nodes) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(outFile);
            for (Node aNode : nodes) {
                out.println(TranspathConstants.LIST_TAG_ID + aNode.getId() + TranspathConstants.COLON + aNode.getName());
                if (aNode.getBranch1st()!=null && !aNode.getBranch1st().isEmpty()) {
                    out.println(TranspathConstants.BRANCH_TAG_1ST + TranspathConstants.COLON + aNode.getBranch1st());
                }
                if (aNode.getBranch2nd()!=null && !aNode.getBranch2nd().isEmpty()) {
                    out.println(TranspathConstants.BRANCH_TAG_2ND + TranspathConstants.COLON + aNode.getBranch2nd());
                }
                if (aNode.getBranch3rd()!=null && !aNode.getBranch3rd().isEmpty()) {
                    out.println(TranspathConstants.BRANCH_TAG_3RD + TranspathConstants.COLON + aNode.getBranch3rd());
                }
                if (aNode.getBranch4th()!=null && !aNode.getBranch4th().isEmpty()) {
                    out.println(TranspathConstants.BRANCH_TAG_4TH + TranspathConstants.COLON + aNode.getBranch4th());
                }
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    private static ArrayList<Node> parseListFile(String pFile) {
        ArrayList<Node> aNodes = new ArrayList<Node>();
        Scanner aScan = null;
        String aLine = "";
        Node aNode = null;
        try {
            aScan = new Scanner(new FileReader(pFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        while (aScan.hasNext()) {
            aLine = aScan.nextLine().trim();
            if (aLine.startsWith(TranspathConstants.LIST_TAG_ID)) {
                if (aNode != null) {
                    aNodes.add(aNode);
                }
                aNode = new Node(Integer.parseInt(aLine.substring(TranspathConstants.LIST_TAG_ID.length(), aLine.indexOf(TranspathConstants.COLON))), 
                                 aLine.substring(aLine.indexOf(TranspathConstants.COLON)+TranspathConstants.COLON.length()));
                continue;
            }
            if (aLine.startsWith(TranspathConstants.BRANCH_TAG_1ST)) {
                if (aNode == null) {
                    System.out.println("File Corrupted.");
                    return null;
                }
                aNode.setBranch1st(aLine.substring(aLine.indexOf(TranspathConstants.COLON)+ TranspathConstants.COLON.length()));
            }
        }
        if (aNode != null) {
            aNodes.add(aNode);
        }
        aScan.close();

        return aNodes;
    }

    private static ArrayList<Node> buildListFromARoot(String pRoot, String pBaseRoot) {
        ArrayList<Node> nodeList = new ArrayList<Node>();
        File dirRoot = new File(pRoot);
        if (!dirRoot.isDirectory())
            return nodeList;
        
        for (File aFile: dirRoot.listFiles()) {
            if (aFile.isFile()) {
                Node aNode = new Node(0, aFile.getName()); 
                aNode.setBranch1st(aFile.getParent().replaceAll(pBaseRoot.replaceAll("\\\\", "\\\\\\\\"), "")
                                           .replaceAll("\\\\", TranspathConstants.SLASH) + TranspathConstants.SLASH);
                nodeList.add(aNode);
            }
            if (aFile.isDirectory()) {
                nodeList.addAll(Keeper.buildListFromARoot(aFile.getPath(), pBaseRoot));
            }
        }
        return nodeList;
    }
    
    private ArrayList<Node> sortByNodeName(ArrayList<Node> pArrayList) {
        @SuppressWarnings("unchecked")
        ArrayList<Node> sortedArrayList = (ArrayList<Node>) pArrayList.clone();
        Collections.sort(sortedArrayList, new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                return n1.getName().compareTo(n2.getName());
            }
        });
        return sortedArrayList;
    }

    private ArrayList<Node> sortByPathName(ArrayList<Node> pArrayList) {
        @SuppressWarnings("unchecked")
        ArrayList<Node> sortedArrayList = (ArrayList<Node>) pArrayList.clone();
        Collections.sort(sortedArrayList, new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                return n1.getBranch1st().compareTo(n2.getBranch1st());
            }
        });
        return sortedArrayList;       
    }

    public static void main(String[] args) {
        String root = "D:\\Book\\TFLib\\";
        Keeper.keepList("resource/pflist.txt", Keeper.buildListFromStorage(root));
        System.out.println("buildListFromStorage ok");
        Keeper.keepList("resource/pflist1st.txt", Keeper.buildListFromFile("resource/pflist.txt"));
        System.out.println("buildListFromFile ok");
        //NodeTree.buildTreeFromPath("/abcdefg/hijk/lmno/pq/rstuv/wxyz");
    }

}
