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
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.tree.TreeNode;

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

public class NodeTree implements TreeNode {
    private Node node;
    private NodeTree parent;
    private List<NodeTree> children;
    private boolean allowsChildren;

    public NodeTree() {
        this.setNode(null);
        this.setParent(null);
        this.setChildren(null);
    }
    
    public boolean isNull() {
        return ((null==this.getNode())&&(null==this.getParent())&&(null==this.getChildren()));
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
        if (null == children) {
            children = new ArrayList<NodeTree>();
        }
        children.add(nodeTree);
        nodeTree.parent = this;
    }
    
    public void addChildNode(Node node) {
        this.addChild(new NodeTree(node));
    }
    
    @Override
    public NodeTree getChildAt(int index) {
        if (null == children) {
            return null;
        }
        return children.get(index);
    }
        
    @Override
    public int getChildCount() {
        if (null == children) {
            return 0;
        }
        return children.size();
    }
    
    public int getNodeId() {
        return node.getId();
    }
    
    public void setNodeId(int id) {
        node.setId(id);
    }
    
    public String getNodeName() {
        return node.getName();
    }
    
    public void setNodeName(String name) {
        node.setName(name);
    }
    
    // For JTree display
    @Override
    public String toString() {
        return this.getNodeName();
    }

    public String getNodePathName() {
        String pathName = TranspathConstants.EMPTY_STRING;
        if (!isRoot()) {
            pathName += this.parent.getNodePathName();
        } 
        pathName += TranspathConstants.PATH_LINKER + this.getNodeName();
        return pathName;
    }
    
    public NodeTree getChildByName(String aName) {
        if (null==this.children)
            return null;
        for(NodeTree aChild:this.children) {
            if (aChild.node.getName().equals(aName))
                return aChild;
        }
        return null;
    }
    
    public NodeTree getChildByNameAnyway(String aName) {
        NodeTree aNodeTree = getChildByName(aName);
        if (null == aNodeTree) {
            aNodeTree = new NodeTree(new Node(0, aName));
            this.addChild(aNodeTree);
        }
        return aNodeTree;
    }
    
    public String getNodePathName_style1() {
        String pathName = TranspathConstants.EMPTY_STRING;
        if (isRoot()) {
            pathName += TranspathConstants.PATH_LINKER;
        } else {
            pathName += this.parent.getNodePathName();
        } 
        pathName += this.getNodeName();
        if (!isLeaf()) {
            pathName += TranspathConstants.PATH_LINKER;
        }
        return pathName;
    }
    
    @Override
    public int getIndex(TreeNode node) {
        if (null == node) {
            throw new IllegalArgumentException("Argument is null.");
        }
        if ((this.isLeaf()) || (node.getParent() != this)) {
            return -1;
        }
        return children.indexOf(node);
    }

    @Override
    public boolean getAllowsChildren() {
        return this.allowsChildren;
    }

    public void setAllowsChildren(boolean allowsChildren) {
        this.allowsChildren = allowsChildren;
    }

    @Override
    public Enumeration<NodeTree> children() {
        if (this.isLeaf()) {
            return new Enumeration<NodeTree>() {
                public boolean hasMoreElements() {
                    return false;
                }
                public NodeTree nextElement() {
                    throw new NoSuchElementException("No elements here.");
                }
            };
        } else {
            return new Enumeration<NodeTree>() {
                Iterator<NodeTree> iter = children.iterator();

                public boolean hasMoreElements() {
                    return iter.hasNext();
                }
                public NodeTree nextElement() throws NoSuchElementException {
                    return iter.next();
                }
            };
        }
    }

    public boolean isLeaf() {
        return (null == this.children);
    }
    
    public boolean isRoot() {
        return (null == this.parent);
    }
    
    public boolean isBranch() {
        return !this.isLeaf()&&!this.isRoot();
    }
    
    public NodeTree addBranch(String pPath) {
        if (this.isNull()) {
            this.setNode(new Node(0, TranspathConstants.ROOT));
        }
        String[] nodeNames = pPath.split(TranspathConstants.SLASH);
        NodeTree bTree = this;
        for (String nodeName: nodeNames) {
            if (null != nodeName && !nodeName.isEmpty()) {
                //System.out.println(nodeName);
                bTree=bTree.getChildByNameAnyway(nodeName);
            }
        }
        return bTree;
    }
    
    public static NodeTree buildFromList(ArrayList<Node> nodes, String branchType) {
        NodeTree aTree = new NodeTree();
        for (Node aNode : nodes) {
            aTree.addBranch(aNode.getBranch(branchType)).addChildNode(aNode);
        }
        return aTree;
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
        System.out.println(tree1.getNodePathName());
        System.out.println(tree2.getNodePathName());
        System.out.println(node1.getNodePathName());
        System.out.println(node2.getNodePathName());
        System.out.println(node3.getNodePathName());
    }

}
