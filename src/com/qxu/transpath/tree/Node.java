/**
 * Copyright (c) 2014,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:Node.java
 * Date:2014-4-2 下午10:34:48
 * 
 */
package com.qxu.transpath.tree;

/**
 * ClassName: Node <br/>
 * Description: Basic Unit of NodeTree. <br/>
 * Date: 2014-4-2 下午10:34:48 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 *         Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public abstract class Node {
    public int id;
    public String path;
    public String name;

    public Node() {
        this.id = 0;
        this.path = "";
        this.name = "";
    }

    public Node(int pId, String pPath, String pName) {
        this.id = pId;
        this.path = pPath;
        this.name = pName;
    }
    
    public abstract String keepNode();

}
