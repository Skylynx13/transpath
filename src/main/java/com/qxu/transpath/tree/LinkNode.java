/**
 * Copyright (c) 2016,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:DualList.java
 * Date:2016-7-18 下午4:34:48
 * 
 */
package com.qxu.transpath.tree;

import com.qxu.transpath.utils.TransConst;

 /**
 * ClassName: DualList <br/>
 * Description: TODO <br/>
 * Date: 2016-7-18 下午4:34:48 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class LinkNode extends Node{
    public int storeId;
    public int pubId;
    
    public LinkNode() {
        storeId = 0;
        pubId = 0;
    }
    
    public LinkNode(int pStoreId, int pPubId) {
        storeId = pStoreId;
        pubId = pPubId;
    }

    public LinkNode(String pLine) {
        String[] sItems = pLine.split(TransConst.COLON);
        storeId = Integer.parseInt(sItems[0]);
        pubId = Integer.parseInt(sItems[1]);
    }

    @Override
    public String keepNode() {
        String seperator = TransConst.COLON;
        return new StringBuffer(String.format(TransConst.FORMAT_INT_08, storeId))
                        .append(seperator)
                        .append(String.format(TransConst.FORMAT_INT_08, pubId))
                        .toString();
    }
}
