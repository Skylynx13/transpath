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
    private ArrayList<String> branches;
    
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

        if (this.branches == null)
            this.branches = new ArrayList<String>();
        this.branches.add(branch);
    }
    
    public void setNode(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    @SuppressWarnings("unchecked")
    public void setNode(int id, String name, ArrayList<String> branches) {
        this.id = id;
        this.name = name;
        if (this.branches == null)
            this.branches = new ArrayList<String>();
        this.branches = (ArrayList<String>) branches.clone();
    }
    
    public String get1stBranch() {
        for (String aBranch: this.branches){
            if (aBranch.startsWith(TranspathConstants.BRANCH_TAG_1ST))
                return aBranch;
        }
        return "";
    }
    
    public String get1stBranchPath() {
        if (this.get1stBranch().equals(""))
            return "";
        return this.get1stBranch().substring(TranspathConstants.BRANCH_TAG_1ST.length()+TranspathConstants.COLON.length());
    }
    
    public String get2ndBranch() {
        for (String aBranch: this.branches){
            if (aBranch.startsWith(TranspathConstants.BRANCH_TAG_2ND))
                return aBranch;
        }
        return "";
    }
    
    public String get2ndBranchPath() {
        if (this.get2ndBranch().equals(""))
            return "";
        return this.get2ndBranch().substring(TranspathConstants.BRANCH_TAG_2ND.length()+TranspathConstants.COLON.length());
    }
    
    public String get3rdBranch() {
        for (String aBranch: this.branches){
            if (aBranch.startsWith(TranspathConstants.BRANCH_TAG_3RD))
                return aBranch;
        }
        return "";
    }
    
    public String get4thBranch() {
        for (String aBranch: this.branches){
            if (aBranch.startsWith(TranspathConstants.BRANCH_TAG_4TH))
                return aBranch;
        }
        return "";
    }
    
    public void addBranch(String branch) {
        if (this.branches == null)
            this.branches = new ArrayList<String>();
        this.branches.add(branch);
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
