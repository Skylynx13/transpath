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
package com.qxu.transpath.tree;

import java.util.ArrayList;
import java.util.List;

 /**
 * ClassName: RootNode <br/>
 * Description: TODO <br/>
 * Date: 2014-4-2 下午10:45:10 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class Tree extends Node {
    private Tree parent;
    private List<Tree> children;

    public Tree(int id, String name) {
        super(id, name);
        this.children = new ArrayList<Tree>();
    }
    
    
    public List<Tree> getChildren() {
        return children;
    }

    public void setChildren(List<Tree> children) {
        this.children = children;
    }
    
    public void addChild(Tree child) {
        children.add(child);
        child.setParent(this);
    }
    
    public int numberOfChild() {
        return children.size();
    }

    public void clearChild() {
        children.clear();
    }
    
    public String list() {
        String list = "<";
        list += this.getId() + "|" + this.getName();
        for (Node child:this.children) {
            if (child instanceof Tree) {
                list += ((Tree) child).list();
            } else {
                list += "<" + child.getId() + "|" + child.getName() + ">";
            }
        }
        list += ">";
        return list;
    }
    
    @Override
    public String getFullName(){
        if (this.getParent() == null) {
            return "/" + this.getName() + "/";
        }
        return this.getParent().getFullName() + this.getName() + "/";
    }
    
    public static void main (String args[]) {
        Tree tree1 = new Tree(11, "tree1");
        Node node1 = new Node(1, "node1");
        tree1.addChild(node1);
        Node node2 = new Node(2, "node2");
        tree1.addChild(node2);
        Node node3 = new Node(3, "node3");
        Tree tree2 = new Tree(12, "tree2");
        tree2.addChild(node3);
        tree1.addChild(tree2);
        System.out.println(tree1.list());
        System.out.println(tree2.list());
        System.out.println(tree1.getFullName());
        System.out.println(tree2.getFullName());
        System.out.println(node1.getFullName());
        System.out.println(node2.getFullName());
        System.out.println(node3.getFullName());
    }
}
