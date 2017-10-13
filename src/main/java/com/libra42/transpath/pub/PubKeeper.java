/**
 * Copyright (c) 2016,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.worker
 * File Name:PubKeeper.java
 * Date:2016-7-15 上午11:13:49
 * 
 */
package com.libra42.transpath.pub;

import java.util.ArrayList;
import java.util.HashMap;

import com.libra42.transpath.log.TransLog;
import com.libra42.transpath.tree.LinkList;
import com.libra42.transpath.tree.Node;
import com.libra42.transpath.tree.NodeList;
import com.libra42.transpath.tree.PubList;
import com.libra42.transpath.tree.PubNode;
import com.libra42.transpath.tree.StoreList;
import com.libra42.transpath.tree.StoreNode;
import com.libra42.transpath.utils.DateUtils;
import com.libra42.transpath.utils.FileUtils;
import com.libra42.transpath.utils.TransConst;
import com.libra42.transpath.utils.TransProp;

/**
 * ClassName: PubKeeper <br/>
 * Description: TODO <br/>
 * Date: 2016-7-15 上午11:13:49 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 *         Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class PubKeeper {
    public static void refreshPubList() {
        TransLog.getLogger().info("PubKeeper refreshPubList starts...");
        mergeDup(null);
        TransLog.getLogger().info("PubKeeper refreshPubList ends.");
    }
    
    private static void mergeDup(HashMap<Integer, Integer> dupMap) {
        String currVer = TransProp.get(TransConst.VER_CURR);
        PubList pList = new PubList();
        pList.load(FileUtils.pubNameOfVersion(currVer));
        String newVer = DateUtils.formatDateTimeLongToday();
        pList.keepFile(FileUtils.pubNameOfVersion(currVer + "_" + newVer));
        
        LinkList lList = new LinkList();
        lList.load(FileUtils.linkNameOfVersion(currVer));
        lList.keepFile(FileUtils.linkNameOfVersion(currVer + "_" + newVer));
    
        if (dupMap != null && dupMap.size() > 0) {
            lList.refreshPubId(dupMap);
            pList.removeByIdMap(dupMap);
        }
        
        pList.hitShelf(FileUtils.hitShelfList());
        pList.orderByPathAndName();
        lList.refreshPubId(pList.reorgId());
        pList.reorder();
    
        pList.keepFile(FileUtils.pubNameOfVersion(currVer));
        lList.keepFile(FileUtils.linkNameOfVersion(currVer));
    }

    public static void showStoreByPubId(ArrayList<Integer> pPubIdList) {
        String currVer = TransProp.get(TransConst.VER_CURR);
        LinkList lnkList = new LinkList();
        lnkList.load(FileUtils.linkNameOfVersion(currVer));
        StoreList strList = new StoreList();
        strList.load(FileUtils.storeNameOfVersion(currVer));
        
        ArrayList<Integer> storeIdList = lnkList.getStoreIdList(pPubIdList);
        for (int sId : storeIdList) {
            StoreNode sNode = (StoreNode)strList.getById(sId);
            TransLog.getLogger().info(sNode.name);
            TransLog.getLogger().info(sNode.keepNode());
        }
        
        TransLog.getLogger().info("");
    }

    public static void checkDup() {
        long t0 = System.currentTimeMillis();
        TransLog.getLogger().info("CheckDup started...");

        String currVer = TransProp.get(TransConst.VER_CURR);
        PubList pList = new PubList();
        pList.load(FileUtils.pubNameOfVersion(currVer));
        
        PubNode dNode = null;
        PubList resList = new PubList();
        PubList dupList = new PubList();
        PubList delList = new PubList();
        
        HashMap<Integer, Integer> dupMap = new HashMap<Integer, Integer>();
        
        pList.orderByPathAndName();
        
        for (Node aNode : pList.nodeList) {
            if (null == aNode) {
                continue;
            } else if (!((PubNode)aNode).checkDupNode(dNode)) {
                dNode = ((PubNode)aNode);
                resList.nodeList.add(aNode);
                continue;
            } else {
                if (!dupList.hasNode(dNode)) {
                    dupList.nodeList.add(dNode);
                }
                dupList.nodeList.add(aNode);
                delList.nodeList.add(aNode);
                dupMap.put(aNode.id, dNode.id);
            }
        }
        TransLog.getLogger().info("Duplicated Number: " + (dupList.size()-delList.size()));
        TransLog.getLogger().info("Redundant Number: " + delList.size());
        
        resList.recap();
        resList.keepFile(FileUtils.pubNameOfVersion(currVer + "_res"));

        dupList.recap();
        dupList.keepFile(FileUtils.pubNameOfVersion(currVer + "_dup"));
        
        delList.recap();
        delList.keepFile(FileUtils.pubNameOfVersion(currVer + "_del"));

        TransLog.getLogger().info("Current Length: " + pList.size());
        TransLog.getLogger().info("Reserve Length: " + resList.size());
        TransLog.getLogger().info("Removal Length: " + delList.size());
        
        TransLog.getLogger().info(dupMap.toString());
        
        TransLog.getLogger().info("CheckDup done in " + (System.currentTimeMillis() - t0) + "ms.");
    }
    
    /**
     * checkLink: To find Free Id and Invalid Id for Store and Pub.<br/>
     */
    public static void checkLink() {
        String pVer = TransProp.get(TransConst.VER_CURR);
        TransLog.getLogger().info("Checking version: " + pVer);
        StoreList sList = new StoreList();
        sList.load(FileUtils.storeNameOfVersion(pVer));
        PubList pList = new PubList();
        pList.load(FileUtils.pubNameOfVersion(pVer));
        LinkList lList = new LinkList();
        lList.load(FileUtils.linkNameOfVersion(pVer));

        checkFreeStore(sList, lList);
        checkInvalidStore(sList, lList);
        checkFreePub(pList, lList);
        checkInvalidPub(pList, lList);
    }
    
    private static void checkFreeStore(StoreList sList, LinkList lList) {
        TransLog.getLogger().info("Free Store: " + NodeList.FindIdOnlyInAList(sList.getIdList(), lList.getStoreIdList()).toString());
    }

    private static void checkInvalidStore(StoreList sList, LinkList lList) {
        TransLog.getLogger().info("Invalid Store: " + NodeList.FindIdOnlyInAList(lList.getStoreIdList(), sList.getIdList()).toString());
    }

    private static void checkFreePub(PubList pList, LinkList lList) {
        TransLog.getLogger().info("Free Pub: " + NodeList.FindIdOnlyInAList(pList.getIdList(), lList.getPubIdList()).toString());
    }

    private static void checkInvalidPub(PubList pList, LinkList lList) {
        TransLog.getLogger().info("Invalid Pub: " + NodeList.FindIdOnlyInAList(lList.getPubIdList(), pList.getIdList()).toString());        
    }

    // To know where a new pub probably belong to.
    public static void findSimilar(ArrayList<Integer> pPubIdList) {
        String pVer = TransProp.get(TransConst.VER_CURR);
        TransLog.getLogger().info("Current version: " + pVer);
        PubList pList = new PubList();
        pList.load(FileUtils.pubNameOfVersion(pVer));

        TransLog.getLogger().info("Find Similar Pub for: ");
        for (int pubId : pPubIdList) {
            String pubName = pList.getById(pubId).name;
            TransLog.getLogger().info(pubId + " : " + pubName);
            TransLog.getLogger().info("|" + pubName.substring(0, pubName.lastIndexOf(' ')) + "|");
            for (Node aNode : pList.nodeList) {
                if (aNode.name.contains(pubName.substring(0, pubName.lastIndexOf(' ')))) {
                    TransLog.getLogger().info(aNode.path + aNode.name);
                }
            }
        }
    }
}
