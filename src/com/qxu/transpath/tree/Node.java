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
    private String branch1st;
    private String branch2nd;
    private String branch3rd;
    private String branch4th;
    
    public Node() {
        this.id = 0;
        this.name = "";
    }
    
    public Node(int id, String name) {
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

    public String getBranch1st() {
        return branch1st;
    }
    
    public void setBranch1st(String branch1st) {
        this.branch1st = branch1st;
    }

    public String getBranch2nd() {
        return branch2nd;
    }

    public void setBranch2nd(String branch2nd) {
        this.branch2nd = branch2nd;
    }

    public String getBranch3rd() {
        return branch3rd;
    }

    public void setBranch3rd(String branch3rd) {
        this.branch3rd = branch3rd;
    }

    public String getBranch4th() {
        return branch4th;
    }

    public void setBranch4th(String branch4th) {
        this.branch4th = branch4th;
    }

}
