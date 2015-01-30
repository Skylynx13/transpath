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
import java.util.ArrayList;

import javax.swing.tree.TreeNode;

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

    public NodeTree buildTreeFromFileSystem(String pFilePath) {
        NodeTree nTree = new NodeTree();

        ArrayList<String> fileNameList = new ArrayList<String>();

        File dirRoot = new File(pFilePath);
        File[] fileInRoot = dirRoot.listFiles();

        for (int iFile = 0; iFile<fileInRoot.length; iFile++){
            fileNameList.add(fileInRoot[iFile].getPath());
            if (fileInRoot[iFile].isDirectory()) {
                //fileNameList.addAll(this.getFileNameList(fileInRoot[iFile].getPath()));
            }
        }

        return nTree;
        // TODO Auto-generated method stub
    }

    public NodeTree buildTreeFromFile(String pFileName) {
        NodeTree nTree = new NodeTree();
        return nTree;
    }
    
    public boolean keepTree() {
        // TODO Auto-generated method stub
        return true;
    }

}
