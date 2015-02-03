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
import java.io.PrintWriter;
import java.util.ArrayList;

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
    
    public ListKeeper() {
    }
    
    private static ListKeeper buildListFromStorage(String root) {
        ListKeeper sk = new ListKeeper();
        sk.nodes = buildListFromARoot(root, root);
        return sk;
    }

    public static ArrayList<Node> buildListFromARoot(String pRoot, String pBaseRoot) {
        ArrayList<Node> nodeList = new ArrayList<Node>();
        File dirRoot = new File(pRoot);
        if (!dirRoot.isDirectory())
            return nodeList;
        
        for (File aFile: dirRoot.listFiles()) {
            if (aFile.isFile()) {
                nodeList.add(new Node(ListKeeper.id++, aFile.getName(), 
                        aFile.getParent().replaceAll(pBaseRoot.replaceAll("\\\\", "\\\\\\\\"), "")
                                           .replaceAll("\\\\", "/")));
            }
            if (aFile.isDirectory()) {
                nodeList.addAll(ListKeeper.buildListFromARoot(aFile.getPath(), pBaseRoot));
            }
        }
        return nodeList;
    }

    public NodeTree buildTreeFromFile(String pFileName) {
        NodeTree nTree = new NodeTree();
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
        ListKeeper.buildListFromStorage(root).keepList("resource/pflist.txt");
        System.out.println("ok");
    }

}
