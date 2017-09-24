/**
 * Copyright (c) 2016,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:PubList.java
 * Date:2016-7-12 下午3:08:02
 * 
 */
package com.qxu.transpath.tree;

import java.io.FileReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

import com.qxu.transpath.utils.TransConst;
import com.qxu.transpath.utils.TransLog;

 /**
 * ClassName: PubList <br/>
 * Description: TODO <br/>
 * Date: 2016-7-12 下午3:08:02 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class PubList extends NodeList {
    
    public PubList() {
        
    }
    
    public PubList(PubList pList) {
        super(pList);
    }
    
    public PubList(String fileName) {
        super(fileName);
    }

    @Override
    public Node loadNode(String pLine) {
        return new PubNode(pLine);
    }

    @Override
    public String keepLine(Node pNode) {
        return ((PubNode)pNode).keepNode();
    }

    public void reorgOrder() {
        int newOrder = 0;
        String lastPath = "";
        for (Node aNode : nodeList) {
            if (aNode.path.equals(lastPath)) {
                newOrder++;
            }
            else {
                lastPath = aNode.path;
                newOrder = 0;
            }
            ((PubNode)aNode).order = newOrder;
        }
    }
    
    public void orderByPathAndOrder() {
        Collections.sort(nodeList, new Comparator<Node>() {
            @Override
            public int compare(Node sn1, Node sn2) {
                int cmp = sn1.path.compareTo(sn2.path);
                if (cmp != 0) {
                    return cmp;
                }
                return ((Integer)((PubNode)sn1).order).compareTo(((PubNode)sn2).order);
            }
        });
    }
    
    public void hitShelf(String hitShelfList) {
        Scanner in = null;
        HashMap<String, String> hitMapper = new HashMap<String, String>();
        try {
            in = new Scanner(new FileReader(hitShelfList));
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (in.hasNext()) {
            String[] hitPair= in.nextLine().split(TransConst.COLON);
            hitMapper.put(hitPair[0], hitPair[1]);
        }
        in.close();

        for (Node aNode : nodeList) {
            for (String aKey : hitMapper.keySet()) {
                if (aNode.path.equals(TransConst.PUB_PATH_DEFAULT) && aNode.name.contains(aKey)) {
                    aNode.path = hitMapper.get(aKey);
                    TransLog.getLogger().info(aNode.path + ":" + aNode.name);
                }
            }
        }
    }
    
    public static void main (String[] args) {
        TransLog.getLogger().info("test hit shelf");
        TransLog.getLogger().info("test end");
    }
}
