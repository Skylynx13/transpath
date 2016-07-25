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
import java.util.Collections;
import java.util.HashMap;

import com.qxu.transpath.tree.LinkList;
import com.qxu.transpath.tree.Node;
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
        PubList resList = new PubList();
        resList.load(TransProp.get("SL_HOME") + "PubList_" + currVer + ".txt");
        resList.keepFile(TransProp.get("SL_HOME") 
                + "PubList_" + currVer + "_" 
                + DateUtils.formatDateTimeLongToday()
                + ".txt");

        LinkList lnkList = new LinkList();
        lnkList.load(TransProp.get("SL_HOME") + "LinkList_" + currVer + ".txt");
        lnkList.keepFile(TransProp.get("SL_HOME") 
                + "LinkList_" + currVer + "_" 
                + DateUtils.formatDateTimeLongToday()
                + ".txt");

        resList.orderByPathAndName();
        lnkList.refreshPubId(resList.reorgId());
        resList.reorgOrder();

        resList.keepFile(TransProp.get("SL_HOME") + "PubList_" + currVer + ".txt");
        lnkList.keepFile(TransProp.get("SL_HOME") + "LinkList_" + currVer + ".txt");
    }
    
    public static void showStoreByPubId(ArrayList<Integer> pPubIdList) {
        String currVer = TransProp.get("CURR_VER");
        LinkList lnkList = new LinkList();
        lnkList.load(TransProp.get("SL_HOME") + "LinkList_" + currVer + ".txt");
        StoreList strList = new StoreList();
        strList.load(TransProp.get("SL_HOME") + "StoreList_" + currVer + ".txt");
        
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

        String pVer = TransProp.get("CURR_VER");
        PubList pList = new PubList();
        pList.load(FileUtils.pubNameOfVersion(pVer));
        
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
        resList.keepFile(FileUtils.pubNameOfVersion(pVer + "_res"));

        dupList.recap();
        dupList.keepFile(FileUtils.pubNameOfVersion(pVer + "_dup"));
        
        delList.recap();
        delList.keepFile(FileUtils.pubNameOfVersion(pVer + "_del"));

        System.out.println("Current Length: " + pList.size());
        System.out.println("Reserve Length: " + resList.size());
        System.out.println("Removal Length: " + delList.size());
        
        System.out.println(dupMap.toString());
        
        System.out.println("CheckDup done in " + (System.currentTimeMillis() - t0) + "ms.");
    }
    
    public static void checkLinkOrphan() {
        
    }
    
    public static void main(String[] args) {
        System.out.println("PubKeeper starts...");
        //System.out.println();
        // pubInit();
        // pubNameEdit();
        // findPath();
        //refreshPubList();
        ArrayList<Integer> idList = new ArrayList<Integer>();
        Integer[] aaa = {233, 234};
        Collections.addAll(idList, aaa);
        //idList.add(233);
        //idList.add(234);
        showStoreByPubId(idList);
        //checkDup();
        System.out.println("PubKeeper ends.");
    }
}
