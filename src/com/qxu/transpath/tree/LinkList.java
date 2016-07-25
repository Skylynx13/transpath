/**
 * Copyright (c) 2016,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:IdLinkList.java
 * Date:2016-7-18 下午5:07:33
 * 
 */
package com.qxu.transpath.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

 /**
 * ClassName: IdLinkList <br/>
 * Description: TODO <br/>
 * Date: 2016-7-18 下午5:07:33 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class LinkList extends NodeList{
    
    public LinkList() {
        
    }
    
    public LinkList(LinkList pList) {
        super(pList);
    }
    
    @Override
    public Node loadNode(String pLine) {
        return new LinkNode(pLine);
    }

    @Override
    public String keepLine(Node pNode) {
        return ((LinkNode)pNode).keepNode();
    }
    
    public void refreshPubId(HashMap<Integer, Integer> pMap) {
        LinkList newLinkList = new LinkList(this);
        this.clear();
        for (Node aNode : newLinkList.nodeList) {
            LinkNode lNode = (LinkNode)aNode;
            if (pMap.containsKey(lNode.pubId)) {
                lNode.pubId = pMap.get(lNode.pubId);
            }
            this.addNode(lNode);
        }
    }
    
    public void refreshStoreId(HashMap<Integer, Integer> pMap) {
        LinkList newLinkList = new LinkList(this);
        this.clear();
        for (Node aNode : newLinkList.nodeList) {
            LinkNode lNode = (LinkNode)aNode;
            if (pMap.containsKey(lNode.storeId)) {
                lNode.storeId = pMap.get(lNode.storeId);
            }
            this.addNode(lNode);
        }
    }

    public void orderByStoreId() {
        Collections.sort(nodeList, new Comparator<Node>() {
            @Override
            public int compare(Node sn1, Node sn2) {
                return ((Integer)((LinkNode)sn1).storeId).compareTo(((LinkNode)sn2).storeId);
            }
        });
    }

    public void orderByPubId() {
        Collections.sort(nodeList, new Comparator<Node>() {
            @Override
            public int compare(Node sn1, Node sn2) {
                return ((Integer)((LinkNode)sn1).pubId).compareTo(((LinkNode)sn2).pubId);
            }
        });
    }
    
    public ArrayList<Integer> getStoreIdList(int pPubId) {
        ArrayList<Integer> storeIdList = new ArrayList<Integer>();
        for (Node aNode : nodeList) {
            LinkNode lNode = (LinkNode)aNode;
            if (lNode.pubId == pPubId) {
                storeIdList.add(lNode.storeId);
            }
        }
        return storeIdList;
    }

    public ArrayList<Integer> getStoreIdList(ArrayList<Integer> pPubIdList) {
        ArrayList<Integer> storeIdList = new ArrayList<Integer>();
        for (int pPubId : pPubIdList) {
            storeIdList.addAll(getStoreIdList(pPubId));
        }
        return storeIdList;
    }
    
    public ArrayList<Integer> getPubIdList(int pStoreId) {
        ArrayList<Integer> pubIdList = new ArrayList<Integer>();
        for (Node aNode : nodeList) {
            LinkNode lNode = (LinkNode)aNode;
            if (lNode.storeId == pStoreId) {
                pubIdList.add(lNode.pubId);
            }
        }
        return pubIdList;
    }
    
    public ArrayList<Integer> getPubIdList(ArrayList<Integer> pStoreIdList) {
        ArrayList<Integer> pubIdList = new ArrayList<Integer>();
        for (int pStoreId : pStoreIdList) {
            pubIdList.addAll(getStoreIdList(pStoreId));
        }
        return pubIdList;
    }
    
}
