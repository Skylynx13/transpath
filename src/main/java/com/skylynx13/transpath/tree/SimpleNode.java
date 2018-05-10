/**
 * Copyright (c) 2016,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:SimpleNode.java
 * Date:2016-7-12 下午4:47:49
 * 
 */
package com.skylynx13.transpath.tree;

import com.skylynx13.transpath.utils.DateUtils;
import com.skylynx13.transpath.utils.StringUtils;

/**
 * ClassName: SimpleNode <br/>
 * Description: TODO <br/>
 * Date: 2016-7-12 下午4:47:49 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class SimpleNode extends Node {

    public SimpleNode(String pName) {
        name = pName;
    }

    @Override
    public String keepNode() {
        return name;
    }

    public Object[] toRow(long length) {
        Object[] row = {id, path, name, StringUtils.formatLongInt(length)};
        return row;
    }
 }
