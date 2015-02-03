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

public class ListKeeper{
    private static int id = 100000001;
    private ArrayList<Node> nodes =null;
    
    public static ListKeeper buildListFromStorage(String pRoot) {
        ListKeeper sk = new ListKeeper();
        sk.nodes = buildListFromARoot(pRoot, pRoot);
        return sk;
    }
    
    public static ListKeeper buildListFromFile(String pFile) {
        ListKeeper sk = new ListKeeper();
        sk.nodes = parseListFile(pFile);
        return sk;
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
            if (aLine.startsWith(TranspathConstants.BRANCH_TAG_1ST) ||
                aLine.startsWith(TranspathConstants.BRANCH_TAG_2ND) ||
                aLine.startsWith(TranspathConstants.BRANCH_TAG_3RD) ||
                aLine.startsWith(TranspathConstants.BRANCH_TAG_4TH)) {
                if (aNode == null) {
                    System.out.println("File Corrupted.");
                    return null;
                }
                aNode.addBranch(aLine);
            }
        }
        if (aNode != null) {
            aNodes.add(aNode);
        }
        aScan.close();

        return aNodes;
        // TODO Auto-generated method stub
    }

    private static ArrayList<Node> buildListFromARoot(String pRoot, String pBaseRoot) {
        ArrayList<Node> nodeList = new ArrayList<Node>();
        File dirRoot = new File(pRoot);
        if (!dirRoot.isDirectory())
            return nodeList;
        
        for (File aFile: dirRoot.listFiles()) {
            if (aFile.isFile()) {
                nodeList.add(new Node(ListKeeper.id++, aFile.getName(), 
                        TranspathConstants.BRANCH_TAG_1ST + TranspathConstants.COLON 
                        + aFile.getParent().replaceAll(pBaseRoot.replaceAll("\\\\", "\\\\\\\\"), "")
                                           .replaceAll("\\\\", "/")));
            }
            if (aFile.isDirectory()) {
                nodeList.addAll(ListKeeper.buildListFromARoot(aFile.getPath(), pBaseRoot));
            }
        }
        return nodeList;
    }

    public NodeTree build1stTreeFromList() {
        NodeTree nTree = new NodeTree();
        for (Node aNode:this.nodes) {
            nTree.add1stBranchNode(aNode);
        }
        return nTree;
    }
    
    public NodeTree build2ndTreeFromList() {
        NodeTree nTree = new NodeTree();
        for (Node aNode:this.nodes) {
            nTree.add2ndBranchNode(aNode);
        }
        return nTree;
    }
    
    public boolean keepList(String outFile) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(Node aNode: this.nodes) {
            out.println(TranspathConstants.LIST_TAG_ID + aNode.getId() + TranspathConstants.COLON + aNode.getName());
            out.println(aNode.get1stBranch());
        }
        out.close();
        return true;
    }
    
    public static void main(String[] args) {
        String root = "D:\\Book\\TFLib";
        //ListKeeper.buildListFromStorage(root).keepList("resource/pflist.txt");
        System.out.println("buildListFromStorage ok");
        ListKeeper.buildListFromFile("resource/pflist.txt").keepList("resource/pflist1st.txt");
        System.out.println("buildListFromFile ok");
        //NodeTree.buildTreeFromPath("/abcdefg/hijk/lmno/pq/rstuv/wxyz");
    }

}
