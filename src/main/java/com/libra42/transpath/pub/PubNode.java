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
package com.libra42.transpath.pub;

import com.libra42.transpath.store.StoreNode;
import com.libra42.transpath.tree.Node;
import com.libra42.transpath.utils.TransConst;

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
    
    public PubNode(StoreNode sNode) {
        id = 0;
        order = 0;
        path = TransConst.PUB_PATH_DEFAULT;
        name = sNode.name.replaceAll("( \\(.*\\))*\\.cb[rz]", "");
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

    public Object[] toRow() {
        Object[] row = {id, order, path, name};
        return row;
    }

}
