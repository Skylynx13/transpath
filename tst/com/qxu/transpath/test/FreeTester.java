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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Properties;

import com.qxu.transpath.tree.Node;
import com.qxu.transpath.tree.NodeTree;
import com.qxu.transpath.utils.CdEntry;
import com.qxu.transpath.utils.StrUtils;
import com.qxu.transpath.utils.TransConst;
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
    public void testArrangerBasic() {
        CdEntry cde1 = new CdEntry("name1");
        //cde1.addComment("comment11");
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
        System.out.println(argr.checkIgnorableLine("    aCode: "));
        System.out.println("Link: " + argr.checkLinkLine("http://  "));
        System.out.println("Comment: " + argr.checkCommentLine("//  "));
        System.out.println("[" + new String("       abcd    ").trim() + "]");
    }
    
    public void testArranger() {
        Arranger argr = new Arranger();
        try {
            int n = argr.readFromFile("resource/raw.txt").sort().merge().writeToFile("resource/task.txt");
            System.out.println("Totally " + n + " entries processed.");
        } catch (NullPointerException e) {
            System.out.println(argr.getStatus());
        }
    }
    
    public void testReplaceStr() {
        String str0 = new String("abcd__efg___hi___test)(2014___digital___jkl___mn_empire_.ccc");
        String str1 = str0.replaceAll("___", ") (");
        String str2 = str1.replaceAll("__", " (");
        String str3 = str2.replaceAll("_\\.", ").");
        String str4 = str3.replaceAll("_", " ");
        String str5 = str4.replaceAll("\\(", " (");
        String str6 = str5.replaceAll("  \\(", " (");
        String str7 = str6.replaceAll("\\(digital\\)", "(Digital)");
        String str8 = str7.replaceAll(" Empire\\)", "-Empire)");
        System.out.println(str0 + "\r -> " + str1  + "\r -> " + str2
                + "\r -> " + str3  + "\r -> " + str4  + "\r -> " + str5
                + "\r -> " + str6 + "\r -> " + str7 + "\r -> " + str8);
    }
    
    public void testHashMap() {
        HashMap<String, String> thm = new HashMap<String, String>();
        thm.put("abc", "def");
    }
    
    public void testArrayListAddAll() {
        ArrayList<Node> nl1 = null;
        ArrayList<Node> nl2 = new ArrayList<Node>();
        nl2.add(new Node(0, "a"));
        ArrayList<Node> nl3 = new ArrayList<Node>();
        nl3.addAll(nl1);
        nl3.addAll(nl2);
        System.out.println("ok" + nl3);
    }
    public void testReformat() {
        String str0 = "Uncle Sam and the Freedom Fighters, 2006-08-00 (_02) (digital) (OkC.O.M.P.U.T.O.-Novus-HD).cbz";
        System.out.println(str0);
        System.out.println(this.reformat(str0));
    }
    
    public String reformat(String str) {
        String ret = str.replaceAll("_", " ");
        int idxComma = ret.indexOf(',');
        int idx1stBracket = ret.indexOf('(');
        //get number
        String rpl1s = ", ";
        String rpl1t = " " + String.format("%03d", Integer.parseInt(ret.substring(ret.indexOf('(')+1, ret.indexOf(')')).trim())) + " (";
        String rpl2s = ret.substring(ret.indexOf('(')-1, ret.indexOf(')')+1).replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)");
        String rpl2t = ")";
        System.out.println(":" + rpl1t + ":" + rpl2s + ":");
        ret = ret.replaceAll(rpl1s, rpl1t).replaceAll(rpl2s, rpl2t);
        
        System.out.println(str);
        System.out.println(ret);
        return ret;
    }
    
    private void testProps() {
        String aProp = "abc";
        Properties aProps = new Properties();
        try {
            aProps.load(new FileInputStream(TransConst.TP_PROPS));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        aProp = aProps.getProperty("test");
        System.out.println("free props tests: [" + aProp + "]");
        
    }
    
    public static void main(String args[]) {
        FreeTester ft = new FreeTester();
        //ft.testNodeTree();
        //ft.testList();
        //ft.testArranger();
        //ft.testReplaceStr();
        //ft.testReformat();
        
        //System.out.println(StrUtils.getSimpleName("G.I. Joe Action Force Mini Comic.cbr"));
        
        ft.testProps();
        System.out.println(new String(" Week of 02/04/2016  ").matches("^\\s*Week of \\d{2}/\\d{2}/\\d{4}\\s*$"));
        String rex = "(?i).*(\\d{3}(\\.\\d{1,2}){0,1} MB|\\d{1,2}(\\.\\d{1,2}){0,1} GB).*";
        System.out.println(new String("gkk233.2 MB").matches(rex));
    }
}

