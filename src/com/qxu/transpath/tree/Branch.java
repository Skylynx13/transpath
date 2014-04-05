/**
 * Copyright (c) 2014,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:BranchNode.java
 * Date:2014-4-2 下午10:36:25
 * 
 */
package com.qxu.transpath.tree;

 /**
 * ClassName: BranchNode <br/>
 * Description: TODO <br/>
 * Date: 2014-4-2 下午10:36:25 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class Branch {
    public Branch(int id, String name) {
        //super(id, name);
        // TODO Auto-generated constructor stub
    }

    private TNodeTree parent;

    public TNodeTree getParent() {
        return parent;
    }

    public void setParent(TNodeTree parent) {
        this.parent = parent;
    }
}
