/**
 * Copyright (c) 2014,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:NodeTree.java
 * Date:2014-4-5 下午10:47:08
 * 
 */
package com.qxu.transpath.tree;

import java.util.ArrayList;
import java.util.List;

import com.qxu.transpath.utils.TranspathConstants;

 /**
 * ClassName: NodeTree <br/>
 * Description: A Tree with a Node. <br/>
 * If this is not enough for use, <br/>
 * reference to Class DefaultMutableTreeNode.<br/>
 * Date: 2014-4-5 下午10:47:08 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class NodeTree {
    private Node node;
    private NodeTree parent;
    private List<NodeTree> children;

    public NodeTree() {
        this.setNode(null);
        this.setParent(null);
        this.setChildren(null);
    }
    
    public NodeTree(Node node) {
        this.setNode(node);
        this.setParent(null);
        this.setChildren(null);
    }
    
    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public NodeTree getParent() {
        return parent;
    }

    public void setParent(NodeTree parent) {
        this.parent = parent;
    }

    public List<NodeTree> getChildren() {
        return children;
    }

    public void setChildren(List<NodeTree> children) {
        this.children = children;
    }

    public void addChild(NodeTree nodeTree) {
        if (null == this.children) {
            this.children = new ArrayList<NodeTree>();
        }
        this.children.add(nodeTree);
        nodeTree.setParent(this);
    }
    
    public NodeTree getChild(int index) {
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
    
    public String getNodeName() {
        return this.node.getName();
    }
    
    public void setNodeName(String name) {
        this.node.setName(name);
    }

    public String getNodePathName() {
        String pathName = "";
        if (null != this.parent) {
            pathName += this.parent.getNodePathName();
        } 
        pathName += TranspathConstants.PATH_LINKER + this.getNodeName();
        return pathName;
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
    
    public String list() {
        String list = "<";
        list += this.node.getId() + "|" + this.getNodeName();
        for (NodeTree child:this.children) {
            if (!child.isLeaf()) {
                list += ((NodeTree) child).list();
            } else {
                list += "<" + child.node.getId() + "|" + child.getNodeName() + ">";
            }
        }
        list += ">";
        return list;
    }
    
    public static void main (String args[]) {
        NodeTree tree1 = new NodeTree(new Node(11, "tree1"));
        NodeTree node1 = new NodeTree(new Node(1, "node1"));
        tree1.addChild(node1);
        NodeTree node2 = new NodeTree(new Node(2, "node2"));
        tree1.addChild(node2);
        NodeTree node3 = new NodeTree(new Node(3, "node3"));
        NodeTree tree2 = new NodeTree(new Node(12, "tree2"));
        tree2.addChild(node3);
        tree1.addChild(tree2);
        System.out.println(tree1.list());
        System.out.println(tree2.list());
        System.out.println(tree1.getNodePathName());
        System.out.println(tree2.getNodePathName());
        System.out.println(node1.getNodePathName());
        System.out.println(node2.getNodePathName());
        System.out.println(node3.getNodePathName());
    }

}
