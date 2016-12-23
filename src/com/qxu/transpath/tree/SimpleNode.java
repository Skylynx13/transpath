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
package com.qxu.transpath.tree;

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

}
