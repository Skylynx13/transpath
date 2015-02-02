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
    public static int id = 100000001;
    
    public ArrayList<Node> buildListFromFileSystem(String pRoot) {
        ArrayList<Node> nodeList = new ArrayList<Node>();
        //StorageKeeper sk = new StorageKeeper();
        File dirRoot = new File(pRoot);
        if (!dirRoot.isDirectory())
            return nodeList;
        
        
        File[] fileInRoot = dirRoot.listFiles();

//        for (int iFile = 0; iFile<fileInRoot.length; iFile++){
        for (File aFile: fileInRoot) {
            if (aFile.isFile()) {
                
                nodeList.add(new Node(StorageKeeper.id++, aFile.getName(), "1st:" + aFile.getParent()));
            }
            if (aFile.isDirectory()) {
                nodeList.addAll(this.buildListFromFileSystem(aFile.getPath()));
            }
        }

        return nodeList;
        // TODO Auto-generated method stub
    }

    public NodeTree buildTreeFromFile(String pFileName) {
        NodeTree nTree = new NodeTree();
        return nTree;
    }
    
    public boolean keepTree(String outFile, String root, ArrayList<Node> nodes) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        root = root.replaceAll("\\\\", "\\\\\\\\");
        for(Node aNode: nodes) {
            out.println("ID" + aNode.getId() + ":" + aNode.getName());
            out.println(aNode.get1stBranch().replaceAll(root, "").replaceAll("\\\\", "/"));
        }
        out.close();
        return true;
    }
    
    public static void main(String[] args) {
        StorageKeeper sk = new StorageKeeper();
        String root = "D:\\Quest\\WIKI";
        ArrayList<Node> nodes = sk.buildListFromFileSystem(root);
        sk.keepTree("resource/pflist.txt", root, nodes);
        System.out.println("ok");
    }

}
