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
package com.qxu.transpath.oldschool;

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

import com.qxu.transpath.tree.StoreNode;
import com.qxu.transpath.utils.TransConst;

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
    
    public static ArrayList<BranchesNode> buildFromRoot(String pRoot) {
        String aRoot = pRoot.replaceAll(TransConst.BACK_SLASH_4, TransConst.SLASH);
        if (aRoot.endsWith(TransConst.SLASH)) {
            aRoot=aRoot.substring(0, aRoot.length()-1);
        }
        if (new File(aRoot).isFile()) {
            return null;
        }
        return NodeList.buildFromPath(aRoot, aRoot);
    }
    
    private static ArrayList<BranchesNode> buildFromPath(String pPath, String pRoot) {
        ArrayList<BranchesNode> nodes = new ArrayList<BranchesNode>();
        File dirRoot = new File(pPath);
        if (dirRoot.isFile()) {
            return null;
        }
        
        System.out.println(pPath);
        
        for (File aFile: dirRoot.listFiles()) {
            if (aFile.isFile()) {
                BranchesNode aBranchesNode = new BranchesNode(0, aFile.getName()); 
                aBranchesNode.putBranch(TransConst.BRANCH_2ND, 
                        composeIndexPathDefault());
                aBranchesNode.putBranch(TransConst.BRANCH_1ST,
                        composeStoragePath(pRoot, aFile));
                aBranchesNode.putBranch(TransConst.BRANCH_0MD, 
                        composeMetadata(aFile));
                nodes.add(aBranchesNode);
            }
            if (aFile.isDirectory()) {
                nodes.addAll(NodeList.buildFromPath(aFile.getPath(), pRoot));
            }
        }
        return nodes;
    }

    public static ArrayList<BranchesNode> buildBlockFromRoot(String pBlock, String pRoot) {
        String aRoot = pRoot.replaceAll(TransConst.BACK_SLASH_4, TransConst.SLASH);
        String aBlock = pBlock.replaceAll(TransConst.BACK_SLASH_4, TransConst.SLASH);
        if (aRoot.endsWith(TransConst.SLASH)) {
            aRoot=aRoot.substring(0, aRoot.length()-1);
        }
        if (aBlock.endsWith(TransConst.SLASH)) {
            aBlock=aBlock.substring(0, aBlock.length()-1);
        }
        aBlock = aRoot + aBlock;
        if (new File(aRoot).isFile() || new File(aBlock).isFile()) {
            return null;
        }
        return NodeList.buildFromPath(aBlock, aRoot);
    }
    
    private static ArrayList<StoreNode> buildStoreFromPath(String pPath, String pRoot) {
        ArrayList<StoreNode> nodes = new ArrayList<StoreNode>();
        File dirRoot = new File(pPath);
        if (dirRoot.isFile()) {
            return null;
        }
        
        System.out.println(pPath);
        
        for (File aFile: dirRoot.listFiles()) {
            if (aFile.isFile()) {
                StoreNode aBranchesNode = new StoreNode(pRoot, aFile); 
                System.out.println(aBranchesNode.keepNode());
                nodes.add(aBranchesNode);
            }
            if (aFile.isDirectory()) {
                nodes.addAll(NodeList.buildStoreFromPath(aFile.getPath(), pRoot));
            }
        }
        return nodes;
    }

    public static ArrayList<StoreNode> buildStoreBlockFromRoot(String pBlock, String pRoot) {
        String aRoot = pRoot.replaceAll(TransConst.BACK_SLASH_4, TransConst.SLASH);
        String aBlock = pBlock.replaceAll(TransConst.BACK_SLASH_4, TransConst.SLASH);
        if (aRoot.endsWith(TransConst.SLASH)) {
            aRoot=aRoot.substring(0, aRoot.length()-1);
        }
        if (aBlock.endsWith(TransConst.SLASH)) {
            aBlock=aBlock.substring(0, aBlock.length()-1);
        }
        aBlock = aRoot + aBlock;
        if (new File(aRoot).isFile() || new File(aBlock).isFile()) {
            return null;
        }
        return NodeList.buildStoreFromPath(aBlock, aRoot);
    }
    
    public static void keepList(String outFile, ArrayList<BranchesNode> nodes) {
        try {
            PrintWriter out = new PrintWriter(outFile);
            for (BranchesNode aBranchesNode : nodes) {
                out.println(aBranchesNode.keepNode());
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void keepStoreList(String outFile, ArrayList<StoreNode> nodes) {
        try {
            PrintWriter out = new PrintWriter(outFile);
            for (StoreNode aBranchesNode : nodes) {
                out.println(aBranchesNode.keepNode());
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String composeIndexPathDefault() {
        return TransConst.INDEX_PRE_CATALOG;
    }

    private static String composeMetadata(File pFile) {
        return new StringBuffer(TransConst.SLASH)
            .append(pFile.length())
            .append(TransConst.SLASH)
            .append(pFile.lastModified())
            .toString();
    }

    private static String composeStoragePath(String pRoot, File pFile) {
        return pFile.getParent()
                .replaceAll(TransConst.BACK_SLASH_4, TransConst.SLASH)
                .replaceAll(pRoot, TransConst.EMPTY) + TransConst.SLASH;
    }

    public static void sortByBranchesNodeName(ArrayList<BranchesNode> nodes) {
        Collections.sort(nodes, new Comparator<BranchesNode>() {
            @Override
            public int compare(BranchesNode n1, BranchesNode n2) {
                return n1.getName().compareTo(n2.getName());
            }
        });
    }
    
    public static void sortByBranchName(ArrayList<BranchesNode> nodes, final String key) {
        Collections.sort(nodes, new Comparator<BranchesNode>() {
           @Override
           public int compare(BranchesNode n1, BranchesNode n2) {
               return n1.getBranch(key).compareTo(n2.getBranch(key));
           }
        });
    }

    public static void sortByFullStoreName(ArrayList<BranchesNode> nodes) {
        Collections.sort(nodes, new Comparator<BranchesNode>() {
           @Override
           public int compare(BranchesNode n1, BranchesNode n2) {
               return new String(n1.getBranch("1ST")+n1.getName())
                      .compareTo(n2.getBranch("1ST")+n2.getName());
           }
        });
    }

    public static ArrayList<BranchesNode> buildFromFile(String pFile) {
        ArrayList<BranchesNode> nodes = new ArrayList<BranchesNode>();

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
            String[] iLine = aLine.split(TransConst.COLON);
            if (aLine.startsWith(TransConst.NODE_ID) &&
                iLine.length == TransConst.FIELDS_BRANCH) {
                nodes.add(new BranchesNode(Integer.parseInt(iLine[1]), iLine[2]));
                continue;
            }
            if (iLine.length == TransConst.FIELDS_INFO &&
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
    private static ArrayList<BranchesNode> buildFromFile_SupportedBranchTypes(String pFile) {
        ArrayList<BranchesNode> nodes = new ArrayList<BranchesNode>();

        Scanner aScan = null;
        try {
            aScan = new Scanner(new FileReader(pFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        
        String aLine = "";
        BranchesNode node = null;
        while (aScan.hasNext()) {
            aLine = aScan.nextLine().trim();
            if (aLine.startsWith(TransConst.NODE_ID)) {
                node = new BranchesNode(Integer.parseInt(aLine.substring(TransConst.NODE_ID.length(), aLine.indexOf(TransConst.COLON))), 
                                 aLine.substring(aLine.indexOf(TransConst.COLON)+TransConst.COLON.length()));
                nodes.add(node);
                continue;
            }
            for (String branchType : TransConst.SUPPORTED_BRANCH_TYPES) {
                if (aLine.startsWith(branchType)) {
                    if (nodes == null || nodes.size() < 1) {
                        System.out.println("File Corrupted.");
                        return null;
                    }
                    nodes.get(nodes.size()-1).putBranch(branchType, aLine.substring(aLine.indexOf(TransConst.COLON)+ TransConst.COLON.length()));
                }
            }
        }
        //System.out.println(nodes.size());
        aScan.close();

        return nodes;
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<BranchesNode> combine(ArrayList<BranchesNode> nl1, ArrayList<BranchesNode> nl2, boolean checkDuplicated) {
        if (checkDuplicated) {
            if (NodeList.checkDuplicatedBranchesNode(nl1).size() > 0) {
                System.out.println("Duplicated BranchesNode Names NL1: " + NodeList.checkDuplicatedBranchesNode(nl1));
                return null;
            }
            if (NodeList.checkDuplicatedBranchesNode(nl2).size() > 0) {
                System.out.println("Duplicated BranchesNode Names NL2: " + NodeList.checkDuplicatedBranchesNode(nl2));
                return null;
            }
        }
        if (0 == nl1.size()) {
            return (ArrayList<BranchesNode>) nl2.clone();
        }
        if (0 == nl2.size()) {
            return (ArrayList<BranchesNode>) nl1.clone();
        }

        ArrayList<BranchesNode> nl3 = new ArrayList<BranchesNode>();
        int idx1 = 0;
        int idx2 = 0;

        while(idx1 < nl1.size() && idx2 < nl2.size()) {
            BranchesNode n1 = nl1.get(idx1);
            BranchesNode n2 = nl2.get(idx2);
            
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
    private static ArrayList<BranchesNode> combineIterator(ArrayList<BranchesNode> nl1, ArrayList<BranchesNode> nl2) {
        if (NodeList.checkDuplicatedBranchesNode(nl1).size() > 0) {
            System.out.println("Duplicated BranchesNode Names NL1: " + NodeList.checkDuplicatedBranchesNode(nl1));
            return null;
        }
        if (NodeList.checkDuplicatedBranchesNode(nl2).size() > 0) {
            System.out.println("Duplicated BranchesNode Names NL2: " + NodeList.checkDuplicatedBranchesNode(nl2));
            return null;
        }
        
        Iterator<BranchesNode> iter1 = nl1.iterator();
        Iterator<BranchesNode> iter2 = nl2.iterator();
        if (!iter1.hasNext()) {
            return (ArrayList<BranchesNode>) nl2.clone();
        }
        if (!iter2.hasNext()) {
            return (ArrayList<BranchesNode>) nl1.clone();
        }

        ArrayList<BranchesNode> nl3 = new ArrayList<BranchesNode>();
        BranchesNode n1 = null;
        BranchesNode n2 = null;
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
    
    public static BranchesNode findBranchesNodeByName(ArrayList<BranchesNode> nodes, String nodeName) {
        Iterator<BranchesNode> iter = nodes.iterator();
        while(iter.hasNext()) {
            BranchesNode node = iter.next();
            if (node.getName().equals(nodeName)) {
                return node;
            }
        }
        return null;
    }

    public static List<String> checkDuplicatedBranchesNode(ArrayList<BranchesNode> nodes) {
        List<String> dupStr = new ArrayList<String>();
        NodeList.sortByBranchesNodeName(nodes);
        String lastBranchesNodeName = null;
        for(BranchesNode node: nodes) {
            if (lastBranchesNodeName != null && lastBranchesNodeName.equals(node.getName())) {
                dupStr.add(lastBranchesNodeName);
            }
            lastBranchesNodeName = node.getName();
        }
        return dupStr;
    }

    public static void main(String[] args) {
        System.out.println("Started...");
        NodeList.keepList("D:\\_TF\\_Update\\TFLib_A2015_b1103.txt", NodeList.buildBlockFromRoot("\\A2015\\", "F:\\Book\\TFLib\\"));
        System.out.println("Done.");
    }

}