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
package com.skylynx13.transpath.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.skylynx13.transpath.store.StoreNode;
import com.skylynx13.transpath.task.TaskArranger;
import com.skylynx13.transpath.task.TaskEntry;
import com.skylynx13.transpath.tree.Node;
import com.skylynx13.transpath.tree.NodeTree;
import com.skylynx13.transpath.tree.SimpleNode;
import com.skylynx13.transpath.utils.DateUtils;
import com.skylynx13.transpath.utils.TransConst;
import com.skylynx13.transpath.utils.TransProp;

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
        NodeTree tree1 = new NodeTree(new SimpleNode("tree1"));
        NodeTree node1 = new NodeTree(new SimpleNode("node1"));
        tree1.addChild(node1);
        NodeTree node2 = new NodeTree(new SimpleNode("node2"));
        tree1.addChild(node2);
        NodeTree node3 = new NodeTree(new SimpleNode("node3"));
        NodeTree tree2 = new NodeTree(new SimpleNode("tree2"));
        tree2.addChild(node3);
        tree1.addChild(tree2);
        System.out.println(tree1.getNodePathName());
        System.out.println(tree2.getNodePathName());
        System.out.println(node1.getNodePathName());
        System.out.println(node2.getNodePathName());
        System.out.println(node3.getNodePathName());
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
        TaskEntry cde1 = new TaskEntry("name1");
        //cde1.addComment("comment11");
        cde1.addLink("link11");
        TaskEntry cde2 = new TaskEntry("name2", "comment2", "link2");
        cde2.addComment("comment21");
        cde2.addLink("link21");
        TaskEntry cde3 = new TaskEntry("name3", "comment3", "link3");
        cde3.addComment("comment31");
        cde3.addLink("link32");
        ArrayList<TaskEntry> cdEntries = new ArrayList<TaskEntry>();
        cdEntries.add(cde3);
        cdEntries.add(cde1);
        cdEntries.add(cde2);
        cde3 = new TaskEntry("name4", "comment4", "link4");
        cde3.addComment("comment41");
        cde3.addLink("link42");
        cdEntries.add(cde3);
        TaskArranger argr = new TaskArranger(cdEntries);
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
        TaskArranger argr = new TaskArranger();
        try {
            int n = argr.readFromFile("src/test/resources/raw.txt").sort().merge().writeToFile("src/test/resources/task.txt");
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
        nl2.add(new SimpleNode("a"));
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
        ret.indexOf(',');
        ret.indexOf('(');
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
    
    private static int addOne(final int i) {
        return i + 1;
    }

    private void workoutAbc() {
        System.out.println("abc ready");
        for (int a = 1; a<1000; a++) {
            for (int b=1; b<1000; b++) {
                for (int c=1; c<1000; c++) {
                    //float d = a/(b+c)+b/(a+c)+c/(a+b);
                    //System.out.println(d);
                    //if (Math.abs(d-4) <0.001) {
                    boolean d = (a*(a+c)*(a+b)+b*(b+c)*(a+b)+c*(a+c)*(b+c)) == (4*(a+b)*(b+c)*(a+c));
                    if (d) {
                        System.out.println("your abc: " + a + "," + b + "," + c + "," + d);
                    }
                }
            }
        }
        System.out.println("abc done");
    }

    public static void main(String[] args) {
        double timeTag = System.currentTimeMillis();
        FreeTester ft = new FreeTester();
        //ft.testNodeTree();
        //ft.testList();
        //ft.testArranger();
        //ft.testReplaceStr();
        //ft.testReformat();
        
        //System.out.println(StringUtils.getSimpleName("G.I. Joe Action Force Mini Comic.cbr"));
        
        ft.testProps();
        System.out.println(new String(" Week of 02/04/2016  ").matches("^\\s*Week of \\d{2}/\\d{2}/\\d{4}\\s*$"));
        String rex = "(?i).*(\\d{3}(\\.\\d{1,2}){0,1} MB|\\d{1,2}(\\.\\d{1,2}){0,1} GB).*";
        System.out.println(new String("gkk233.2 MB").matches(rex));
        System.out.println(new String("/a2016/b1234/").replaceAll("/", "\\\\"));
        int aaa = 3;
        int bbb = 3;
        System.out.println(((Integer)aaa).compareTo(bbb));
        
        System.out.println(FreeTester.addOne(5));
        
        System.out.println(TransProp.get(TransConst.LOC_CONFIG));

        System.out.println("Time elapsed: " + (System.currentTimeMillis()-timeTag) + " ms.");
        System.out.println(System.getProperty("qxu.test"));
        long lastM = 1508560299855L;
        System.out.println("Long: " + lastM + "; Date: " + new Date(lastM) + "; Formatted: " + DateUtils.formatDateTimeLong(lastM));
        // Construct the current date
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String tsDate10 = ts.toString().substring(0, 10);
        String tsDate16 = ts.toString().substring(0, 16);
        System.out.println(tsDate10 + ":::" + tsDate16);

//        ft.workoutAbc();

        System.out.println("Time elapsed: " + (System.currentTimeMillis()-timeTag) + " ms.");
        String inputString = "A8123PEKAA";
        String regex = "(\\w{2})(\\d{3,4})([A-Z]{0,1})(\\w{3})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputString);
        boolean matched = matcher.find();
        if (matched) {
            String airline = matcher.group(1);
            String flightNumber = matcher.group(2);
            String suffix = matcher.group(3).equals("")?"^":matcher.group(3);
            String more = matcher.group(4);
            System.out.println("airline is [" + airline + "]; flightNumber is [" + flightNumber + "]; suffix is [" + suffix + "]; more is [" + more + "]");
            System.out.println("Time elapsed: " + (System.currentTimeMillis()-timeTag) + " ms.");
        }

        int a = 1;
        int b = a;
        b = 2;
        System.out.println("a="+a+"; b=" + b);

        String sa = "1";
        String sb = new String(sa);
        sb="2";
        System.out.println("a="+sa+"; b=" + sb);

        StoreNode na = new StoreNode();
        na.id=1;
        na.name = "namea";
        na.path = "patha";

        StoreNode nb = na;
        nb.id=2;
        System.out.println("na=" + na.id + "; nb=" + nb.id);
    }
}
