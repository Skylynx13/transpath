/**
 * Copyright (c) 2014,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:RootNode.java
 * Date:2014-4-2 下午10:45:10
 * 
 */
package com.qxu.transpath.oldschool;

import java.util.ArrayList;
import java.util.List;

 /**
 * ClassName: TNodeTree<TNode> <br/>
 * Description: 一棵以TNode为结点的树。 <br/>
 * Date: 2014-4-2 下午10:45:10 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class TNodeTree<TNode> {
    private TNode node; 
    private TNodeTree<TNode> parent;
    private List<TNodeTree<TNode>> children;

    public TNodeTree(TNode node) {
        this.setNode(node);
        this.setParent(null);
        this.setChildren(null);
    }
    
    public TNode getNode() {
        return node;
    }

    public void setNode(TNode node) {
        this.node = node;
    }

    public TNodeTree<TNode> getParent() {
        return parent;
    }

    public void setParent(TNodeTree<TNode> parent) {
        this.parent = parent;
    }

    public List<TNodeTree<TNode>> getChildren() {
        return children;
    }

    public void setChildren(List<TNodeTree<TNode>> children) {
        this.children = children;
    }

    public void addChild(TNodeTree<TNode> tree) {
        if (null == this.children) {
            this.children = new ArrayList<TNodeTree<TNode>>();
        }
        this.children.add(tree);
        tree.setParent(this);
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
    
    public boolean isBranch() {
        return !this.isLeaf();
    }
}
