/**
 * Copyright (c) 2014,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:Tree.java
 * Date:2014-4-5 下午10:32:45
 * 
 */
package com.qxu.transpath.oldschool;

import java.util.ArrayList;
import java.util.List;

 /**
 * ClassName: Tree <br/>
 * Description: 一棵树。 <br/>
 * Date: 2014-4-5 下午10:32:45 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class Tree {
    private Tree parent;
    private List<Tree> children;

    public Tree() {
        this.setParent(null);
        this.setChildren(null);
    }
    
    public Tree getParent() {
        return parent;
    }

    public void setParent(Tree parent) {
        this.parent = parent;
    }

    public List<Tree> getChildren() {
        return children;
    }

    public void setChildren(List<Tree> children) {
        this.children = children;
    }

    public void addChild(Tree tree) {
        if (null == this.children) {
            this.children = new ArrayList<Tree>();
        }
        this.children.add(tree);
        tree.setParent(this);
    }
    
    public Tree getChild(int index) {
        if (null == this.children) {
            return null;
        }
        return this.children.get(index);
    }
        
    public int numberOfChild() {
        if (null == this.children) {
            return 0;
        }
        return children.size();
    }
    
    public boolean isLeaf() {
        return (null == this.children);
    }
    
    public boolean isRoot() {
        return (null == this.parent);
    }
    
    public boolean isBranch() {
        return !this.isLeaf()&&!this.isBranch();
    }
}
