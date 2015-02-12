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
        if (pRoot.endsWith("\\") || pRoot.endsWith("/")) {
            pRoot=pRoot.substring(0,pRoot.length()-1);
        }
        if (new File(pRoot).isFile()) {
            return null;
        }
        return NodeList.buildFromPath(pRoot, pRoot);
    }
    
    private static ArrayList<Node> buildFromPath(String pPath, String pRoot) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        File dirRoot = new File(pPath);
        if (dirRoot.isFile()) {
            return null;
        }
        
        for (File aFile: dirRoot.listFiles()) {
            if (aFile.isFile()) {
                Node aNode = new Node(0, aFile.getName()); 
                aNode.setBranch1st(aFile.getParent().replaceAll(pRoot.replaceAll("\\\\", "\\\\\\\\"), "")
                                           .replaceAll("\\\\", TranspathConstants.SLASH) + TranspathConstants.SLASH);
                nodes.add(aNode);
            }
            if (aFile.isDirectory()) {
                nodes.addAll(NodeList.buildFromPath(aFile.getPath(), pRoot));
            }
        }
        return nodes;
    }

    public static void sortByNodeName(ArrayList<Node> nodes) {
        Collections.sort(nodes, new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                return n1.getName().compareTo(n2.getName());
            }
        });
    }

    public static void sortByNodeBranch1st(ArrayList<Node> nodes) {
        Collections.sort(nodes, new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                return n1.getBranch1st().compareTo(n2.getBranch1st());
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
        Node node = null;
        while (aScan.hasNext()) {
            aLine = aScan.nextLine().trim();
            if (aLine.startsWith(TranspathConstants.LIST_TAG_ID)) {
                if (node != null) {
                    nodes.add(node);
                }
                node = new Node(Integer.parseInt(aLine.substring(TranspathConstants.LIST_TAG_ID.length(), aLine.indexOf(TranspathConstants.COLON))), 
                                 aLine.substring(aLine.indexOf(TranspathConstants.COLON)+TranspathConstants.COLON.length()));
                continue;
            }
            if (aLine.startsWith(TranspathConstants.BRANCH_TAG_1ST)) {
                if (node == null) {
                    System.out.println("File Corrupted.");
                    return null;
                }
                node.setBranch1st(aLine.substring(aLine.indexOf(TranspathConstants.COLON)+ TranspathConstants.COLON.length()));
            }
        }
        if (node != null) {
            nodes.add(node);
        }
        aScan.close();

        return nodes;
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


}
