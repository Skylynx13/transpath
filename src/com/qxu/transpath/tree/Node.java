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
 * Description: Information of the node of the tree. <br/>
 * Date: 2014-4-2 下午10:34:48 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class Node {
    private int id;
    private String name;
    
    public Node(int id, String name) {
        this.setNode(id, name);
    }
    
    public void setNode(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
