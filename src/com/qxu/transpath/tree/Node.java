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

import java.util.ArrayList;

 /**
 * ClassName: Node <br/>
 * Description: TODO <br/>
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
    private ArrayList<String> branches = new ArrayList<String>();
    
    public Node() {
        this.id = 0;
        this.name = "";
        this.branches = null;
    }
    
    public Node(int id, String name) {
        this.id = id;
        this.name = name;
        this.branches = null;
    }
    
    public Node(int id, String name, String branch) {
        this.id = id;
        this.name = name;
        this.branches.add(branch);
    }
    
    public void setNode(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public void setNode(int id, String name, ArrayList<String> branches) {
        this.id = id;
        this.name = name;
        this.branches = branches;
    }
    
    public String get1stBranch() {
        return this.branches.get(0);
    }
    
    @SuppressWarnings("unchecked")
    public Node copy(Node pNode) {
        Node aNode = new Node();
        aNode.id = pNode.id;
        aNode.name = pNode.name;
        aNode.branches = (ArrayList<String>) pNode.branches.clone();
        return aNode;
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
