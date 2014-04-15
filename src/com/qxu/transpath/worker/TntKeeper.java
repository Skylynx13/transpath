/**
 * Copyright (c) 2014,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.worker
 * File Name:TntKeeper.java
 * Date:2014-4-12 下午11:07:12
 * 
 */
package com.qxu.transpath.worker;

import java.io.InputStream;

import javax.swing.tree.TreeNode;

import com.qxu.transpath.tree.NodeTree;
import com.qxu.transpath.utils.TranspathConstants;

 /**
 * ClassName: TntKeeper <br/>
 * Description: TODO <br/>
 * Date: 2014-4-12 下午11:07:12 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class TntKeeper implements Keeper {
    private String tntFileName = "resource/tflib001.tnt";
    
    public String listSomething(NodeTree pTree) {
        String list = "<";
        list += pTree.getNodeId() + "|" + pTree.getNodeName();
        for (NodeTree child:pTree.getChildren()) {
            if (!child.isLeaf()) {
                list += listSomething(child);
            } else {
                list += "<" + child.getNodeId() + "|" + child.getNodeName() + ">";
            }
        }
        list += ">";
        return list;
    }
    
    public String listAll(NodeTree pTree) {
        String list = pTree.getNodePathName();
        list += TranspathConstants.LINE_LINKER;
        if (!pTree.isLeaf()) {
            for (NodeTree child:pTree.getChildren()) {
                list += listAll(child);
            }
        }
        return list;
    }

    @Override
    public TreeNode buildTree() {
        //InputStream fiStream = new InputStream(tntFileName);
        return null;
   }

    @Override
    public void keepTree() {
        // TODO Auto-generated method stub
    }

}
