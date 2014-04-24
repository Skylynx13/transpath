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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.qxu.transpath.tree.Node;
import com.qxu.transpath.tree.NodeTree;
import com.qxu.transpath.utils.CdEntry;
import com.qxu.transpath.worker.Arranger;
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
    public class SortByName implements Comparator<String> {
        public int compare (String s1, String s2) {
            if (s1.compareTo(s2) > 0) {
                return 1;
            } else if (s1.compareTo(s2) < 0) {
                return -1;
            }
            return 0;
        }
    }

    public void testNodeTree() {
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
    public void testList() {
        ArrayList<String> aStrList = new ArrayList<String>();
        aStrList.add("abcde");
        aStrList.add("1234");
        aStrList.add("12345");
        System.out.println(aStrList.toString());
        Collections.sort(aStrList);
        System.out.println(aStrList.toString());
        Collections.sort(aStrList, new SortByName());
        System.out.println(aStrList.toString());
    }
    public void testArranger() {
        CdEntry cde1 = new CdEntry("name1", "comment1", "link1");
        cde1.addComment("comment11");
        cde1.addLink("link11");
        CdEntry cde2 = new CdEntry("name2", "comment2", "link2");
        cde2.addComment("comment21");
        cde2.addLink("link21");
        CdEntry cde3 = new CdEntry("name3", "comment3", "link3");
        cde3.addComment("comment31");
        cde3.addLink("link32");
        ArrayList<CdEntry> cdEntries = new ArrayList<CdEntry>();
        cdEntries.add(cde3);
        cdEntries.add(cde1);
        cdEntries.add(cde2);
        cde3 = new CdEntry("name4", "comment4", "link4");
        cde3.addComment("comment41");
        cde3.addLink("link42");
        cdEntries.add(cde3);
        Arranger argr = new Arranger(cdEntries);
        System.out.println(argr.toString());
        System.out.println(argr.toOutput());
        
        System.out.println(argr.sort().toString());
        System.out.println(argr.toOutput());
    }
    public static void main(String args[]) {
        FreeTester ft = new FreeTester();
        //ft.testNodeTree();
        //ft.testList();
        ft.testArranger();
    }
}

