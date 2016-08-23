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
package com.qxu.transpath.worker;

import java.util.ArrayList;
import java.util.HashMap;

import com.qxu.transpath.tree.LinkList;
import com.qxu.transpath.tree.Node;
import com.qxu.transpath.tree.NodeList;
import com.qxu.transpath.tree.PubList;
import com.qxu.transpath.tree.PubNode;
import com.qxu.transpath.tree.StoreList;
import com.qxu.transpath.tree.StoreNode;
import com.qxu.transpath.utils.DateUtils;
import com.qxu.transpath.utils.FileUtils;
import com.qxu.transpath.utils.TransProp;

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
    @Deprecated
    public static void pubInit() {
        String srcName = TransProp.get("LB_HOME") + "TFLib_L20160715_Ref.txt";
        String targetName = TransProp.get("LB_HOME") + "TFLib_L20160715_Ref_000.txt";
        PubList pubList = new PubList();
        pubList.load(srcName);
        pubList.orderByPathAndName();
        pubList.reorgId();
        pubList.reorgOrder();
        pubList.keepFile(targetName);
    }

    @Deprecated
    public static void pubNameEdit() {
        String srcName = TransProp.get("LB_HOME") + "TFLib_L20160715_Ref_000.txt";
        String targetName = TransProp.get("LB_HOME") + "TFLib_L20160715_Ref_001.txt";
        PubList pubList = new PubList();
        pubList.load(srcName);
        for (Node aNode : pubList.nodeList) {
            aNode.name = aNode.name.replaceAll("( \\(.*\\))*\\.cb[rz]", "");
        }
        pubList.keepFile(targetName);
    }

    @Deprecated
    public static void findPath() {
        PubList refList = new PubList();
        refList.load(TransProp.get("LB_HOME") + "TFLib_L20160715_Ref_001.txt");

        PubList resList = new PubList();
        resList.load(TransProp.get("SL_HOME") + "PubList_20160721102308967.txt");

        LinkList lnkList = new LinkList();
        lnkList.load(TransProp.get("SL_HOME") + "LinkList_20160721102308967.txt");

        for (Node resNode : resList.nodeList) {
            System.out.println(resNode.keepNode());
            for (Node refNode : refList.nodeList) {
                if (resNode.name.equals(refNode.name)) {
                    resNode.path = refNode.path;
                    break;
                }
            }
        }

        resList.orderByPathAndName();
        lnkList.refreshPubId(resList.reorgId());
        resList.reorgOrder();

        resList.keepFile(TransProp.get("SL_HOME") + "PubList_20160721102308967.txt");
        lnkList.keepFile(TransProp.get("SL_HOME") + "LinkList_20160721102308967.txt");
    }

    public static void refreshPubList() {
        String currVer = TransProp.get("CURR_VER");
        PubList pList = new PubList();
        pList.load(FileUtils.pubNameOfVersion(currVer));
        String newVer = DateUtils.formatDateTimeLongToday();
        pList.keepFile(FileUtils.pubNameOfVersion(currVer + "_" + newVer));

        LinkList lList = new LinkList();
        lList.load(FileUtils.linkNameOfVersion(currVer));
        lList.keepFile(FileUtils.linkNameOfVersion(currVer + "_" + newVer));

        pList.orderByPathAndName();
        lList.refreshPubId(pList.reorgId());
        pList.reorgOrder();

        pList.keepFile(FileUtils.pubNameOfVersion(currVer));
        lList.keepFile(FileUtils.linkNameOfVersion(currVer));
    }
    
    public static void mergeDup(HashMap<Integer, Integer> dupMap) {
        String currVer = TransProp.get("CURR_VER");
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
        
        pList.orderByPathAndName();
        lList.refreshPubId(pList.reorgId());
        pList.reorgOrder();
    
        pList.keepFile(FileUtils.pubNameOfVersion(currVer));
        lList.keepFile(FileUtils.linkNameOfVersion(currVer));
    }

    public static void showStoreByPubId(ArrayList<Integer> pPubIdList) {
        String currVer = TransProp.get("CURR_VER");
        LinkList lnkList = new LinkList();
        lnkList.load(FileUtils.linkNameOfVersion(currVer));
        StoreList strList = new StoreList();
        strList.load(FileUtils.storeNameOfVersion(currVer));
        
        ArrayList<Integer> storeIdList = lnkList.getStoreIdList(pPubIdList);
        for (int sId : storeIdList) {
            StoreNode sNode = (StoreNode)strList.getById(sId);
            System.out.println(sNode.name);
            System.out.println(sNode.keepNode());
        }
        
        System.out.println();
    }

    public static void checkDup() {
        long t0 = System.currentTimeMillis();
        System.out.println("CheckDup started...");

        String currVer = TransProp.get("CURR_VER");
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
        System.out.println("Duplicated Number: " + (dupList.size()-delList.size()));
        System.out.println("Redundant Number: " + delList.size());
        
        resList.recap();
        resList.keepFile(FileUtils.pubNameOfVersion(currVer + "_res"));

        dupList.recap();
        dupList.keepFile(FileUtils.pubNameOfVersion(currVer + "_dup"));
        
        delList.recap();
        delList.keepFile(FileUtils.pubNameOfVersion(currVer + "_del"));

        System.out.println("Current Length: " + pList.size());
        System.out.println("Reserve Length: " + resList.size());
        System.out.println("Removal Length: " + delList.size());
        
        System.out.println(dupMap.toString());
        
        System.out.println("CheckDup done in " + (System.currentTimeMillis() - t0) + "ms.");
    }
    
    /**
     * checkLink: To find Free Id and Invalid Id for Store and Pub.<br/>
     */
    public static void checkLink() {
        String pVer = TransProp.get("CURR_VER");
        System.out.println("Checking version: " + pVer);
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
        System.out.println("Free Store: " + NodeList.FindIdOnlyInAList(sList.getIdList(), lList.getStoreIdList()).toString());
    }

    private static void checkInvalidStore(StoreList sList, LinkList lList) {
        System.out.println("Invalid Store: " + NodeList.FindIdOnlyInAList(lList.getStoreIdList(), sList.getIdList()).toString());
    }

    private static void checkFreePub(PubList pList, LinkList lList) {
        System.out.println("Free Pub: " + NodeList.FindIdOnlyInAList(pList.getIdList(), lList.getPubIdList()).toString());
    }

    private static void checkInvalidPub(PubList pList, LinkList lList) {
        System.out.println("Invalid Pub: " + NodeList.FindIdOnlyInAList(lList.getPubIdList(), pList.getIdList()).toString());        
    }

    // To know where a new pub probably belong to.
    public static void findSimilar(ArrayList<Integer> pPubIdList) {
        String pVer = TransProp.get("CURR_VER");
        System.out.println("Current version: " + pVer);
        PubList pList = new PubList();
        pList.load(FileUtils.pubNameOfVersion(pVer));

        System.out.println("Find Similar Pub for: ");
        for (int pubId : pPubIdList) {
            String pubName = pList.getById(pubId).name;
            System.out.println(pubId + " : " + pubName);
            System.out.println("|" + pubName.substring(0, pubName.lastIndexOf(' ')) + "|");
            for (Node aNode : pList.nodeList) {
                if (aNode.name.contains(pubName.substring(0, pubName.lastIndexOf(' ')))) {
                    System.out.println(aNode.path + aNode.name);
                }
            }
        }
    }
    
    public static void main(String[] args) {
        System.out.println("PubKeeper starts...");
        //System.out.println();
        // pubInit();
        // pubNameEdit();
        // findPath();
        //refreshPubList();
        //checkLink();

        //Change intA to intB.
        HashMap<Integer, Integer> dupMap = new HashMap<Integer, Integer>();
        //dupMap.put(61482, 61481);
        mergeDup(dupMap);

//        ArrayList<Integer> idList = new ArrayList<Integer>();
//        idList.add(41289);
//        idList.add(41290);
//        idList.add(41291);
//        idList.add(41292);
//        idList.add(41293);
//        showStoreByPubId(idList);

        //checkDup();
        
        //findSimilar(idList);
        
        System.out.println("PubKeeper ends.");
    }

}
