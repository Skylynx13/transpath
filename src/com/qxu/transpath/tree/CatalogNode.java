/**
 * Copyright (c) 2016,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:CatalogNode.java
 * Date:2016-5-16 下午2:07:42
 * 
 */
package com.qxu.transpath.tree;

import java.util.ArrayList;

import com.qxu.transpath.utils.TransConst;

/**
 * ClassName: CatalogNode <br/>
 * Description: TODO <br/>
 * Date: 2016-5-16 下午2:07:42 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 *         Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 *          <cid>:<sid>:<cpath>:<cname>
 */

public class CatalogNode extends Node {
    public ArrayList<String> tagList = new ArrayList<String>();

    public CatalogNode(String cEntry) {
        String[] cItems = cEntry.split(TransConst.COLON);
        id = Integer.parseInt(cItems[0]);
        path = cItems[1];
        name = cItems[2];
    }

    @Override
    public String keepNode() {
        return new StringBuffer(String.format(TransConst.FORMAT_INT_08, id))
                        .append(TransConst.COLON)
                        .append(path)
                        .append(TransConst.COLON)
                        .append(name)
                        .toString();
    }

}
