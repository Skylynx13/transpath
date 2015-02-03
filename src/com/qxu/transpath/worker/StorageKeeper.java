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
import java.util.Collections;
import java.util.UUID;

import javax.swing.tree.TreeNode;

import org.apache.commons.codec.binary.Base64;

import com.qxu.transpath.tree.Node;
import com.qxu.transpath.tree.NodeTree;

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

public class StorageKeeper{
    private static int id = 100000001;
    private String rootDir;
    public StorageKeeper(String root) {
        this.rootDir = root;
    }
    
    private ArrayList<Node> buildListFromStorage() {
        return buildListFromARoot(this.rootDir);
    }

    public ArrayList<Node> buildListFromARoot(String pRoot) {
        ArrayList<Node> nodeList = new ArrayList<Node>();
        File dirRoot = new File(pRoot);
        if (!dirRoot.isDirectory())
            return nodeList;
        
        for (File aFile: dirRoot.listFiles()) {
            if (aFile.isFile()) {
                
                nodeList.add(new Node(StorageKeeper.id++, aFile.getName(), "1st:" + aFile.getParent()));
            }
            if (aFile.isDirectory()) {
                nodeList.addAll(this.buildListFromARoot(aFile.getPath()));
            }
        }
        return nodeList;
    }

    public NodeTree buildTreeFromFile(String pFileName) {
        NodeTree nTree = new NodeTree();
        return nTree;
    }
    
    public boolean keepTree(String outFile, ArrayList<Node> nodes) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String root = this.rootDir.replaceAll("\\\\", "\\\\\\\\");
        for(Node aNode: nodes) {
            out.println("ID" + aNode.getId() + ":" + aNode.getName());
            out.println(aNode.get1stBranch().replaceAll(root, "").replaceAll("\\\\", "/"));
        }
        out.close();
        return true;
    }
    
    public static void main(String[] args) {
        String root = "D:\\Book\\TFLib";
        StorageKeeper sk = new StorageKeeper(root);
        ArrayList<Node> nodes = sk.buildListFromStorage();
        sk.keepTree("resource/pflist.txt", nodes);
        System.out.println("ok");
    }

}
