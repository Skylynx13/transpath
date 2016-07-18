/**
 * Copyright (c) 2016,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:PubNode.java
 * Date:2016-7-12 下午1:23:56
 * 
 */
package com.qxu.transpath.tree;

import com.qxu.transpath.utils.TransConst;

 /**
 * ClassName: PubNode <br/>
 * Description: TODO <br/>
 * Date: 2016-7-12 下午1:23:56 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class PubNode extends Node {
    public int order;
    
    public PubNode() {
        order = 0;
    }
    
    public PubNode(String sEntry) {
        String[] sItems = sEntry.split(TransConst.COLON);
        id = Integer.parseInt(sItems[0]);
        order = Integer.parseInt(sItems[1]);
        path = sItems[2];
        if (sItems.length == 4) {
            name = sItems[3];
        }
    }

    @Override
    public String keepNode() {
        String seperator = TransConst.COLON;
        return new StringBuffer(String.format(TransConst.FORMAT_INT_08, id))
                        .append(seperator)
                        .append(String.format(TransConst.FORMAT_INT_08, order))
                        .append(seperator)
                        .append(path)
                        .append(seperator)
                        .append(name)
                        .toString();
    }

}
