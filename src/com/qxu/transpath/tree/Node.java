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
    
    public Node(int id, String name) {
        this.setNode(id, name);
    }
    
    public void setNode(int id, String name) {
        this.id = id;
        this.name = name;
        this.parent = null;
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
    public Node getParent() {
        return parent;
    }
    public void setParent(Node parent) {
        this.parent = parent;
    }
    public String getFullName() {
        return this.parent.getFullName() + this.name;
    }
    public String list() {
        String list = "<";
        list += this.getId() + "|" + this.getName();
        for (Node child:this.children) {
            if (child instanceof TNodeTree) {
                list += ((TNodeTree) child).list();
            } else {
                list += "<" + child.getId() + "|" + child.getName() + ">";
            }
        }
        list += ">";
        return list;
    }
    
    public String getFullName(){
        if (null == this.parent) {
            return "/" + this.getName() + "/";
        }
        return this.getParent().getFullName() + this.getName() + "/";
    }
    
    public static void main (String args[]) {
        TNodeTree tree1 = new TNodeTree(11, "tree1");
        Node node1 = new Node(1, "node1");
        tree1.addChild(node1);
        Node node2 = new Node(2, "node2");
        tree1.addChild(node2);
        Node node3 = new Node(3, "node3");
        TNodeTree tree2 = new TNodeTree(12, "tree2");
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
