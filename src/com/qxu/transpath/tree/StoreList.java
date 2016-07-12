/**
 * Copyright (c) 2016,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:StoreList.java
 * Date:2016-6-17 上午12:05:40
 * 
 */
package com.qxu.transpath.tree;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import com.qxu.transpath.utils.FileUtils;
import com.qxu.transpath.utils.TransConst;

/**
 * ClassName: StoreList <br/>
 * Description: TODO <br/>
 * Date: 2016-6-17 上午12:05:40 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 *         Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class StoreList extends NodeList{
    public long fileSize; // recap, clear

    public StoreList() {
        fileSize = 0;
    }
    
    @Override
    public void clear() {
        super.clear();
        fileSize = 0;
    }

    @Override
    public void recap() {
        super.recap();
        fileSize = 0;
        for (Node aNode : nodeList) {
            fileSize += ((StoreNode)aNode).length;
        }
    }
    
    public void build(String pRoot, String pPathName) {
        String aRoot = FileUtils.regulateSlash(pRoot);
        String aPathName = aRoot + FileUtils.regulateSlash(pPathName);
        
        if (new File(aRoot).isFile() || new File(aPathName).isFile()) {
            return;
        }

        buildByPath(aRoot, aPathName);
        recap();
    }

    public void buildByPath(String pRoot, String pPathName) {
        buildByPath(pRoot, new File(pPathName));
    }

    public void buildByPath(String pRoot, File pPath) {
        clear();
        if (pPath.isFile()) {
            return;
        }
        if (pPath.listFiles() == null) {
            return;
        }
        System.out.println(pPath);
        for (File aFile : pPath.listFiles()) {
            if (aFile.isFile()) {
                StoreNode aNode = new StoreNode(pRoot, aFile);
                aNode.id = ++maxId;
                System.out.println(aNode.keepNode());
                nodeList.add(aNode);
            }
            if (aFile.isDirectory()) {
                StoreList aList = new StoreList();
                aList.buildByPath(pRoot, aFile.getPath());
                attachList(aList);
            }
        }
    }

    public void orderByMd5() {
        Collections.sort(nodeList, new Comparator<Node>() {
            @Override
            public int compare(Node sn1, Node sn2) {
                return ((StoreNode)sn1).md5.compareTo(((StoreNode)sn2).md5);
            }
        });
    }
    
    @Override
    public String keepHeader() {
        return new StringBuffer(version).append(TransConst.COLON)
                .append(String.format(TransConst.FORMAT_INT_08, minId)).append(TransConst.COLON)
                .append(String.format(TransConst.FORMAT_INT_08, maxId)).append(TransConst.COLON)
                .append(String.format(TransConst.FORMAT_INT_20, fileSize)).append(TransConst.COLON)
                .append(String.format(TransConst.FORMAT_INT_08, size())).toString();

    }

    @Override
    public Node loadNode(String pLine) {
        return new StoreNode(pLine);
    }

    @Override
    public String keepLine(Node pNode) {
        return ((StoreNode)pNode).keepNode();
    }
}
