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
import java.util.HashMap;
import java.util.Iterator;

import com.qxu.transpath.utils.TranspathConstants;

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
    private HashMap<String, String> branches;
    
    public Node() {
        this.id = 0;
        this.name = "";
        this.branches = new HashMap<String, String>();
    }
    
    public Node(int id, String name) {
        this.id = id;
        this.name = name;
        this.branches = new HashMap<String, String>();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Node clone() {
        Node node = new Node(this.id, this.name);
        node.branches = (HashMap<String, String>)this.branches.clone();
        return node;
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

    public HashMap<String, String> getBranches() {
        return branches;
    }

    public void setBranches(HashMap<String, String> branches) {
        this.branches = branches;
    }

    public void putBranch(String key, String value) {
        if (null == this.branches) {
            this.branches = new HashMap<String, String>();
        }
        this.branches.put(key, value);
    }
    
    public String getBranch(String key) {
        return this.branches.get(key);
    }

    public Node merge(Node n1) {
        Node n2 = this.clone();
        
        Iterator<String> iter = n1.getBranches().keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            if (!n2.getBranches().containsKey(key)) {
                n2.putBranch(key, n1.getBranch(key));
            }
        }
        return n2;
    }
    
    public String keepNode() {
        StringBuffer nodeBuf = new StringBuffer();
        nodeBuf.append(TranspathConstants.NODE_ID);
        nodeBuf.append(TranspathConstants.COLON);
        nodeBuf.append(this.getId());
        nodeBuf.append(TranspathConstants.COLON);
        nodeBuf.append(this.getName());
        for(String key: this.branches.keySet()) {
            nodeBuf.append(TranspathConstants.CRLN);
            nodeBuf.append(key);
            nodeBuf.append(TranspathConstants.COLON);
            nodeBuf.append(this.getBranch(key));
        }
        return nodeBuf.toString();
    }

}
