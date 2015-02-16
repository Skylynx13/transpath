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
import java.util.Collection;
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
        pRoot = pRoot.replaceAll(TranspathConstants.BACK_SLASH_4, TranspathConstants.SLASH);
        if (pRoot.endsWith(TranspathConstants.SLASH)) {
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
                aNode.putBranch(TranspathConstants.BRANCH_1ST,
                        aFile.getParent().replaceAll(TranspathConstants.BACK_SLASH_4, TranspathConstants.SLASH)
                        .replaceAll(pRoot, TranspathConstants.EMPTY_STRING) + TranspathConstants.SLASH);
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
        Node node = null;
        while (aScan.hasNext()) {
            aLine = aScan.nextLine().trim();
            if (aLine.startsWith(TranspathConstants.NODE_ID)) {
                String[] iLine = aLine.split(TranspathConstants.COLON);
                if (iLine.length != TranspathConstants.BRANCH_FIELDS) {
                    System.out.println("File Corrupted.");
                    return null;
                }
                nodes.add(new Node(Integer.parseInt(iLine[1]), iLine[2]));
                continue;
            }
            if (aLine.indexOf(TranspathConstants.COLON) > 0 ) {
                String[] bLine = aLine.split(TranspathConstants.COLON);
                if (bLine.length != TranspathConstants.INFO_FIELDS || nodes == null || nodes.size() < 1) {
                    System.out.println("File Corrupted.");
                    return null;
                }
                nodes.get(nodes.size()-1).putBranch(bLine[0], bLine[1]);
            }
        }
        aScan.close();

        return nodes;
    }

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
    
    @SuppressWarnings("unchecked")
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
        //NodeList.keepList("D:\\_TF\\_Update\\TFLib_A2013.txt", NodeList.buildFromRoot("F:\\Book\\TFLib\\"));
    }

}
