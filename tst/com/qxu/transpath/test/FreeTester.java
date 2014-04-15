/**
 * Copyright (c) 2014,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.test
 * File Name:FreeTester.java
 * Date:2014-4-14 下午10:35:28
 * 
 */
package com.qxu.transpath.test;

import com.qxu.transpath.tree.Node;
import com.qxu.transpath.tree.NodeTree;
import com.qxu.transpath.worker.TntKeeper;

/**
 * ClassName: FreeTester <br/>
 * Description: TODO <br/>
 * Date: 2014-4-14 下午10:35:28 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 *         Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class FreeTester {
    public static void main(String args[]) {
        NodeTree tree1 = new NodeTree(new Node(11, "tree1"));
        NodeTree node1 = new NodeTree(new Node(1, "node1"));
        tree1.addChild(node1);
        NodeTree node2 = new NodeTree(new Node(2, "node2"));
        tree1.addChild(node2);
        NodeTree node3 = new NodeTree(new Node(3, "node3"));
        NodeTree tree2 = new NodeTree(new Node(12, "tree2"));
        tree2.addChild(node3);
        tree1.addChild(tree2);
        TntKeeper tk1 = new TntKeeper();
        System.out.println(tk1.listSomething(tree1));
        System.out.println(tk1.listSomething(tree2));
        System.out.println(tree1.getNodePathName());
        System.out.println(tree2.getNodePathName());
        System.out.println(node1.getNodePathName());
        System.out.println(node2.getNodePathName());
        System.out.println(node3.getNodePathName());
        System.out.println("===");
        System.out.println(tk1.listAll(tree1));
        System.out.println("===");
    }
}
