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

import com.qxu.transpath.tree.Node;
import com.qxu.transpath.tree.PubList;
import com.qxu.transpath.utils.TransProp;

 /**
 * ClassName: PubKeeper <br/>
 * Description: TODO <br/>
 * Date: 2016-7-15 上午11:13:49 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
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
    
    public static void main(String[] args) {
        System.out.println("PubKeeper starts...");
        System.out.println();
        //pubInit();
        pubNameEdit();
        System.out.println("PubKeeper ends.");
    }
}
