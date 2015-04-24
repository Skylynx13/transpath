/**
 * Copyright (c) 2015,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:NodeListTest.java
 * Date:2015-2-11 下午2:30:59
 * 
 */
package com.qxu.transpath.tree;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

import com.qxu.transpath.utils.TranspathConstants;

 /**
 * ClassName: NodeListTest <br/>
 * Description: TODO <br/>
 * Date: 2015-2-11 下午2:30:59 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class NodeListTest {

    @Test
    public void buildFromRootTest() {
        ArrayList<Node> nodes = NodeList.buildFromRoot("resource/qtest");
        assertEquals("tfile004.txt", nodes.get(0).getName());
        assertEquals("/tdir001/tdir102/", nodes.get(0).getBranch(TranspathConstants.BRANCH_1ST));
        assertEquals("tfile005.txt", nodes.get(1).getName());
        assertEquals("/tdir001/tdir102/", nodes.get(1).getBranch(TranspathConstants.BRANCH_1ST));
        assertEquals("tfile006.txt", nodes.get(2).getName());
        assertEquals("/tdir002/tdir201/", nodes.get(2).getBranch(TranspathConstants.BRANCH_1ST));
        assertEquals("tfile007.txt", nodes.get(3).getName());
        assertEquals("/tdir002/tdir201/", nodes.get(3).getBranch(TranspathConstants.BRANCH_1ST));
        assertEquals("tfile008.txt", nodes.get(4).getName());
        assertEquals("/tdir003/", nodes.get(4).getBranch(TranspathConstants.BRANCH_1ST));
        assertEquals("tfile001.txt", nodes.get(5).getName());
        assertEquals("/", nodes.get(5).getBranch(TranspathConstants.BRANCH_1ST));
        assertEquals("tfile002.txt", nodes.get(6).getName());
        assertEquals("/", nodes.get(6).getBranch(TranspathConstants.BRANCH_1ST));
        assertEquals("tfile003.txt", nodes.get(7).getName());
        assertEquals("/", nodes.get(7).getBranch(TranspathConstants.BRANCH_1ST));
    }
    
    @Test
    public void buildFromFileTest() {
        ArrayList<Node> nodes = NodeList.buildFromFile("resource/tst/NodeListTest_buildFromFileTest001.txt");
        assertEquals("ABCDEFG.cbr", nodes.get(0).getName());
        assertEquals("/abc/def/ghijklmn/", nodes.get(0).getBranch(TranspathConstants.BRANCH_1ST));
        assertEquals("HIJKLMN.cbz", nodes.get(1).getName());
        assertEquals("/abc/opq/rstuvwxyz/", nodes.get(1).getBranch(TranspathConstants.BRANCH_1ST));
    }
    
    @Test
    public void buildFromFileTest_Double_Colon() {
        assertEquals(null, NodeList.buildFromFile("resource/tst/NodeListTest_buildFromFileTest002.txt"));
    }
    
    @Test
    public void buildFromFileTest_Triple_Colon() {
        assertEquals(null, NodeList.buildFromFile("resource/tst/NodeListTest_buildFromFileTest005.txt"));
    }
    
    @Test
    public void buildFromFileTest_File_Corrupted() {
        assertEquals(null, NodeList.buildFromFile("resource/tst/NodeListTest_buildFromFileTest003.txt"));
    }
    
    @Test
    public void buildFromFileTest_No_Branches() {
        ArrayList<Node> nodes = NodeList.buildFromFile("resource/tst/NodeListTest_buildFromFileTest004.txt");
        assertEquals("ABCDEFG.cbr", nodes.get(0).getName());
        assertEquals(null, nodes.get(0).getBranch(TranspathConstants.BRANCH_1ST));
        assertEquals("HIJKLMN.cbz", nodes.get(1).getName());
        assertEquals(null, nodes.get(1).getBranch(TranspathConstants.BRANCH_1ST));
    }
    
    @Test
    public void storageKeepingTest() {
        String root = "resource/qtest/";
        NodeList.keepList("resource/tst/pflist.txt", NodeList.buildFromRoot(root));
        NodeList.keepList("resource/tst/pflist1st.txt", NodeList.buildFromFile("resource/tst/pflist.txt"));
        Scanner sc1 = null;
        Scanner sc2 = null;
        try {
            sc1 = new Scanner(new FileReader("resource/tst/pflist.txt"));
            sc2 = new Scanner(new FileReader("resource/tst/pflist1st.txt"));
            int cnt = 0;
            while (sc1.hasNext() && sc2.hasNext()) {
                String line1 = sc1.nextLine().trim();
                String line2 = sc2.nextLine().trim();
                assertEquals(true, line1.equals(line2));
                cnt++;
            }
            assertEquals(24, cnt);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            sc1.close();
            sc2.close();
        }
    }
    
    @Test 
    public void findNodeByNameTest() {
        Node node = NodeList.findNodeByName(
                NodeList.buildFromFile("resource/tst/NodeListTest_Default.txt"), 
                "HIJKLMN.cbz");
        assertEquals(2, node.getId());
        assertEquals("HIJKLMN.cbz", node.getName());
        assertEquals("/abc/opq/rstuvwxyz/", node.getBranch(TranspathConstants.BRANCH_1ST));        
    }
    
    @Test
    public void checkDuplicatedNodeTest() {
        List<String> dupCheck = NodeList.checkDuplicatedNode(NodeList.buildFromFile("resource/tst/checkDuplicatedNodeTest_3.txt")); 
        assertEquals(3, dupCheck.size());
        assertEquals("dup1", dupCheck.get(0));
        assertEquals("dup2", dupCheck.get(1));
        assertEquals("dup3", dupCheck.get(2));
        dupCheck = NodeList.checkDuplicatedNode(NodeList.buildFromFile("resource/tst/checkDuplicatedNodeTest_0.txt"));
        assertEquals(0, dupCheck.size());
    }
    
    @Test
    public void combineTest_AHAJ_AHBI() {
        ArrayList<Node> nl1 = new ArrayList<Node>(); 
        ArrayList<Node> nl2 = new ArrayList<Node>(); 
        Node node = null; 
        node = new Node(1, "ABCDEFG.cbr");
        node.putBranch("1ST", "/abc/def/ghijklmn/");
        nl1.add(node);
        node = new Node(2, "HIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz/");
        nl1.add(node);
        node = new Node(3, "AIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz/");
        nl1.add(node);
        node = new Node(4, "JIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz/");
        nl1.add(node);

        node = new Node(1, "ABCDEFG.cbr");
        node.putBranch("2ND", "/abc2/def/ghijklmn/");
        nl2.add(node);
        node = new Node(2, "HIJKLMN.cbz");
        node.putBranch("2ND", "/abc2/opq/rstuvwxyz/");
        nl2.add(node);
        node = new Node(3, "BIJKLMN.cbz");
        node.putBranch("2ND", "/abc3/opq/rstuvwxyz/");
        nl2.add(node);
        node = new Node(4, "IIJKLMN.cbz");
        node.putBranch("2ND", "/abc3/iopq/rstuvwxyz/");
        nl2.add(node);

        ArrayList<Node> nl3 = NodeList.combine(nl1, nl2);
        assertEquals(6, nl3.size());
        assertEquals("ABCDEFG.cbr", nl3.get(0).getName());
        assertEquals("AIJKLMN.cbz", nl3.get(1).getName());
        assertEquals("BIJKLMN.cbz", nl3.get(2).getName());
        assertEquals("HIJKLMN.cbz", nl3.get(3).getName());
        assertEquals("IIJKLMN.cbz", nl3.get(4).getName());
        assertEquals("JIJKLMN.cbz", nl3.get(5).getName());
        assertEquals("/abc/def/ghijklmn/", nl3.get(0).getBranch("1ST"));
        assertEquals("/abc/opq/rstuvwxyz/", nl3.get(1).getBranch("1ST"));
        assertEquals(null, nl3.get(2).getBranch("1ST"));
        assertEquals("/abc/opq/rstuvwxyz/", nl3.get(3).getBranch("1ST"));
        assertEquals(null, nl3.get(4).getBranch("1ST"));
        assertEquals("/abc/opq/rstuvwxyz/", nl3.get(5).getBranch("1ST"));
        assertEquals("/abc2/def/ghijklmn/", nl3.get(0).getBranch("2ND"));
        assertEquals(null, nl3.get(1).getBranch("2ND"));
        assertEquals("/abc3/opq/rstuvwxyz/", nl3.get(2).getBranch("2ND"));
        assertEquals("/abc2/opq/rstuvwxyz/", nl3.get(3).getBranch("2ND"));
        assertEquals("/abc3/iopq/rstuvwxyz/", nl3.get(4).getBranch("2ND"));
        assertEquals(null, nl3.get(5).getBranch("2ND"));
    }
    
    @Test
    public void combineTest_HAJ_AHBI() {
        ArrayList<Node> nl1 = new ArrayList<Node>(); 
        ArrayList<Node> nl2 = new ArrayList<Node>(); 
        Node node = null; 
        node = new Node(1, "HIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz/");
        nl1.add(node);
        node = new Node(2, "AIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz/");
        nl1.add(node);
        node = new Node(3, "JIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz/");
        nl1.add(node);

        node = new Node(1, "ABCDEFG.cbr");
        node.putBranch("2ND", "/abc2/def/ghijklmn/");
        nl2.add(node);
        node = new Node(2, "HIJKLMN.cbz");
        node.putBranch("2ND", "/abc2/opq/rstuvwxyz/");
        nl2.add(node);
        node = new Node(3, "BIJKLMN.cbz");
        node.putBranch("2ND", "/abc3/opq/rstuvwxyz/");
        nl2.add(node);
        node = new Node(4, "IIJKLMN.cbz");
        node.putBranch("2ND", "/abc3/iopq/rstuvwxyz/");
        nl2.add(node);

        ArrayList<Node> nl3 = NodeList.combine(nl1, nl2);
        assertEquals(6, nl3.size());
        assertEquals("ABCDEFG.cbr", nl3.get(0).getName());
        assertEquals("AIJKLMN.cbz", nl3.get(1).getName());
        assertEquals("BIJKLMN.cbz", nl3.get(2).getName());
        assertEquals("HIJKLMN.cbz", nl3.get(3).getName());
        assertEquals("IIJKLMN.cbz", nl3.get(4).getName());
        assertEquals("JIJKLMN.cbz", nl3.get(5).getName());
        assertEquals(null, nl3.get(0).getBranch("1ST"));
        assertEquals("/abc/opq/rstuvwxyz/", nl3.get(1).getBranch("1ST"));
        assertEquals(null, nl3.get(2).getBranch("1ST"));
        assertEquals("/abc/opq/rstuvwxyz/", nl3.get(3).getBranch("1ST"));
        assertEquals(null, nl3.get(4).getBranch("1ST"));
        assertEquals("/abc/opq/rstuvwxyz/", nl3.get(5).getBranch("1ST"));
        assertEquals("/abc2/def/ghijklmn/", nl3.get(0).getBranch("2ND"));
        assertEquals(null, nl3.get(1).getBranch("2ND"));
        assertEquals("/abc3/opq/rstuvwxyz/", nl3.get(2).getBranch("2ND"));
        assertEquals("/abc2/opq/rstuvwxyz/", nl3.get(3).getBranch("2ND"));
        assertEquals("/abc3/iopq/rstuvwxyz/", nl3.get(4).getBranch("2ND"));
        assertEquals(null, nl3.get(5).getBranch("2ND"));
    }
    
    @Test
    public void combineTest_AHA_AHBI() {
        ArrayList<Node> nl1 = new ArrayList<Node>(); 
        ArrayList<Node> nl2 = new ArrayList<Node>(); 
        Node node = null; 
        node = new Node(1, "ABCDEFG.cbr");
        node.putBranch("1ST", "/abc/def/ghijklmn/");
        nl1.add(node);
        node = new Node(2, "HIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz/");
        nl1.add(node);
        node = new Node(3, "AIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz/");
        nl1.add(node);

        node = new Node(1, "ABCDEFG.cbr");
        node.putBranch("2ND", "/abc2/def/ghijklmn/");
        nl2.add(node);
        node = new Node(2, "HIJKLMN.cbz");
        node.putBranch("2ND", "/abc2/opq/rstuvwxyz/");
        nl2.add(node);
        node = new Node(3, "BIJKLMN.cbz");
        node.putBranch("2ND", "/abc3/opq/rstuvwxyz/");
        nl2.add(node);
        node = new Node(4, "IIJKLMN.cbz");
        node.putBranch("2ND", "/abc3/iopq/rstuvwxyz/");
        nl2.add(node);

        ArrayList<Node> nl3 = NodeList.combine(nl1, nl2);
        assertEquals(5, nl3.size());
        assertEquals("ABCDEFG.cbr", nl3.get(0).getName());
        assertEquals("AIJKLMN.cbz", nl3.get(1).getName());
        assertEquals("BIJKLMN.cbz", nl3.get(2).getName());
        assertEquals("HIJKLMN.cbz", nl3.get(3).getName());
        assertEquals("IIJKLMN.cbz", nl3.get(4).getName());
        assertEquals("/abc/def/ghijklmn/", nl3.get(0).getBranch("1ST"));
        assertEquals("/abc/opq/rstuvwxyz/", nl3.get(1).getBranch("1ST"));
        assertEquals(null, nl3.get(2).getBranch("1ST"));
        assertEquals("/abc/opq/rstuvwxyz/", nl3.get(3).getBranch("1ST"));
        assertEquals(null, nl3.get(4).getBranch("1ST"));
        assertEquals("/abc2/def/ghijklmn/", nl3.get(0).getBranch("2ND"));
        assertEquals(null, nl3.get(1).getBranch("2ND"));
        assertEquals("/abc3/opq/rstuvwxyz/", nl3.get(2).getBranch("2ND"));
        assertEquals("/abc2/opq/rstuvwxyz/", nl3.get(3).getBranch("2ND"));
        assertEquals("/abc3/iopq/rstuvwxyz/", nl3.get(4).getBranch("2ND"));
    }

    @Test
    public void combineTest_AHA_AHB() {
        ArrayList<Node> nl1 = new ArrayList<Node>(); 
        ArrayList<Node> nl2 = new ArrayList<Node>(); 
        Node node = null; 
        node = new Node(1, "ABCDEFG.cbr");
        node.putBranch("1ST", "/abc/def/ghijklmn/");
        nl1.add(node);
        node = new Node(2, "HIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz/");
        nl1.add(node);
        node = new Node(3, "AIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz/");
        nl1.add(node);

        node = new Node(1, "ABCDEFG.cbr");
        node.putBranch("2ND", "/abc2/def/ghijklmn/");
        nl2.add(node);
        node = new Node(2, "HIJKLMN.cbz");
        node.putBranch("2ND", "/abc2/opq/rstuvwxyz/");
        nl2.add(node);
        node = new Node(3, "BIJKLMN.cbz");
        node.putBranch("2ND", "/abc3/opq/rstuvwxyz/");
        nl2.add(node);

        ArrayList<Node> nl3 = NodeList.combine(nl1, nl2);
        assertEquals(4, nl3.size());
        assertEquals("ABCDEFG.cbr", nl3.get(0).getName());
        assertEquals("AIJKLMN.cbz", nl3.get(1).getName());
        assertEquals("BIJKLMN.cbz", nl3.get(2).getName());
        assertEquals("HIJKLMN.cbz", nl3.get(3).getName());
        assertEquals("/abc/def/ghijklmn/", nl3.get(0).getBranch("1ST"));
        assertEquals("/abc/opq/rstuvwxyz/", nl3.get(1).getBranch("1ST"));
        assertEquals(null, nl3.get(2).getBranch("1ST"));
        assertEquals("/abc/opq/rstuvwxyz/", nl3.get(3).getBranch("1ST"));
        assertEquals("/abc2/def/ghijklmn/", nl3.get(0).getBranch("2ND"));
        assertEquals(null, nl3.get(1).getBranch("2ND"));
        assertEquals("/abc3/opq/rstuvwxyz/", nl3.get(2).getBranch("2ND"));
        assertEquals("/abc2/opq/rstuvwxyz/", nl3.get(3).getBranch("2ND"));
    }
    
    @Test
    public void combineTest_AHJ_BI() {
        ArrayList<Node> nl1 = new ArrayList<Node>(); 
        ArrayList<Node> nl2 = new ArrayList<Node>(); 
        Node node = null; 
        node = new Node(1, "ABCDEFG.cbr");
        node.putBranch("1ST", "/abc/def/ghijklmn/");
        nl1.add(node);
        node = new Node(2, "HIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz/");
        nl1.add(node);
        node = new Node(3, "JIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz/");
        nl1.add(node);

        node = new Node(1, "BIJKLMN.cbz");
        node.putBranch("2ND", "/abc3/opq/rstuvwxyz/");
        nl2.add(node);
        node = new Node(2, "IIJKLMN.cbz");
        node.putBranch("2ND", "/abc3/iopq/rstuvwxyz/");
        nl2.add(node);

        ArrayList<Node> nl3 = NodeList.combine(nl1, nl2);
        assertEquals(5, nl3.size());
        assertEquals("ABCDEFG.cbr", nl3.get(0).getName());
        assertEquals("BIJKLMN.cbz", nl3.get(1).getName());
        assertEquals("HIJKLMN.cbz", nl3.get(2).getName());
        assertEquals("IIJKLMN.cbz", nl3.get(3).getName());
        assertEquals("JIJKLMN.cbz", nl3.get(4).getName());
        assertEquals("/abc/def/ghijklmn/", nl3.get(0).getBranch("1ST"));
        assertEquals(null, nl3.get(1).getBranch("1ST"));
        assertEquals("/abc/opq/rstuvwxyz/", nl3.get(2).getBranch("1ST"));
        assertEquals(null, nl3.get(3).getBranch("1ST"));
        assertEquals("/abc/opq/rstuvwxyz/", nl3.get(4).getBranch("1ST"));
        assertEquals(null, nl3.get(0).getBranch("2ND"));
        assertEquals("/abc3/opq/rstuvwxyz/", nl3.get(1).getBranch("2ND"));
        assertEquals(null, nl3.get(2).getBranch("2ND"));
        assertEquals("/abc3/iopq/rstuvwxyz/", nl3.get(3).getBranch("2ND"));
        assertEquals(null, nl3.get(4).getBranch("2ND"));
    }
    
    @Test
    public void sortByNodeNameTest() {
        ArrayList<Node> nl1 = new ArrayList<Node>(); 
        Node node = null; 
        node = new Node(1, "ABCDEFG.cbr");
        node.putBranch("1ST", "/abc/def/zghijklmn/");
        node.putBranch("2ND", "/abc2/def/ghijklmn/");
        nl1.add(node);
        node = new Node(2, "HIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz3/");
        node.putBranch("2ND", "/abc2/def/ghijklmn/");
        nl1.add(node);
        node = new Node(3, "AIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz2/");
        node.putBranch("2ND", "/abc2/def/ghijklmn/");
        nl1.add(node);
        node = new Node(4, "JIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz1/");
        node.putBranch("2ND", "/abc2/def/ghijklmn/");
        nl1.add(node);
        node = new Node(5, "ABCDEFG.cbr");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz2/");
        node.putBranch("2ND", "/abc2/def/ghijklmn/");
        nl1.add(node);
        node = new Node(6, "HIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz2/");
        node.putBranch("2ND", "/abc2/opq/rstuvwxyz/");
        nl1.add(node);
        node = new Node(7, "BIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz2/");
        node.putBranch("2ND", "/abc3/opq/rstuvwxyz/");
        nl1.add(node);
        node = new Node(8, "IIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz2/");
        node.putBranch("2ND", "/abc3/iopq/rstuvwxyz/");
        nl1.add(node);
       
        NodeList.sortByNodeName(nl1);
        assertEquals(8, nl1.size());
        assertEquals("ABCDEFG.cbr", nl1.get(0).getName());
        assertEquals("/abc/def/zghijklmn/", nl1.get(0).getBranch("1ST"));
        assertEquals("/abc2/def/ghijklmn/", nl1.get(0).getBranch("2ND"));

        assertEquals("ABCDEFG.cbr", nl1.get(1).getName());
        assertEquals("/abc/opq/rstuvwxyz2/", nl1.get(1).getBranch("1ST"));
        assertEquals("/abc2/def/ghijklmn/", nl1.get(1).getBranch("2ND"));
        
        assertEquals("AIJKLMN.cbz", nl1.get(2).getName());
        assertEquals("/abc/opq/rstuvwxyz2/", nl1.get(2).getBranch("1ST"));
        assertEquals("/abc2/def/ghijklmn/", nl1.get(2).getBranch("2ND"));
        
        assertEquals("BIJKLMN.cbz", nl1.get(3).getName());
        assertEquals("/abc/opq/rstuvwxyz2/", nl1.get(3).getBranch("1ST"));
        assertEquals("/abc3/opq/rstuvwxyz/", nl1.get(3).getBranch("2ND"));
        
        assertEquals("HIJKLMN.cbz", nl1.get(4).getName());
        assertEquals("/abc/opq/rstuvwxyz3/", nl1.get(4).getBranch("1ST"));
        assertEquals("/abc2/def/ghijklmn/", nl1.get(4).getBranch("2ND"));
        
        assertEquals("HIJKLMN.cbz", nl1.get(5).getName());
        assertEquals("/abc/opq/rstuvwxyz2/", nl1.get(5).getBranch("1ST"));
        assertEquals("/abc2/opq/rstuvwxyz/", nl1.get(5).getBranch("2ND"));
        
        assertEquals("IIJKLMN.cbz", nl1.get(6).getName());
        assertEquals("/abc/opq/rstuvwxyz2/", nl1.get(6).getBranch("1ST"));
        assertEquals("/abc3/iopq/rstuvwxyz/", nl1.get(6).getBranch("2ND"));
        
        assertEquals("JIJKLMN.cbz", nl1.get(7).getName());
        assertEquals("/abc/opq/rstuvwxyz1/", nl1.get(7).getBranch("1ST"));
        assertEquals("/abc2/def/ghijklmn/", nl1.get(7).getBranch("2ND"));
    }

    @Test
    public void sortByBranchNameTest_1st() {
        ArrayList<Node> nl1 = new ArrayList<Node>(); 
        Node node = null; 
        node = new Node(1, "ABCDEFG.cbr");
        node.putBranch("1ST", "/abc/def/zghijklmn/");
        node.putBranch("2ND", "/abc2/def/ghijklmn/");
        nl1.add(node);
        node = new Node(2, "HIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz3/");
        node.putBranch("2ND", "/abc2/def/ghijklmn/");
        nl1.add(node);
        node = new Node(3, "AIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz2/");
        node.putBranch("2ND", "/abc2/def/ghijklmn/");
        nl1.add(node);
        node = new Node(4, "JIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz1/");
        node.putBranch("2ND", "/abc2/def/ghijklmn/");
        nl1.add(node);
        node = new Node(5, "ABCDEFG.cbr");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz2/");
        node.putBranch("2ND", "/abc2/def/ghijklmn/");
        nl1.add(node);
        node = new Node(6, "HIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz2/");
        node.putBranch("2ND", "/abc2/opq/rstuvwxyz/");
        nl1.add(node);
        node = new Node(7, "BIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz2/");
        node.putBranch("2ND", "/abc3/opq/rstuvwxyz/");
        nl1.add(node);
        node = new Node(8, "IIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz2/");
        node.putBranch("2ND", "/abc3/iopq/rstuvwxyz/");
        nl1.add(node);
       
        NodeList.sortByBranchName(nl1, "1ST");
        assertEquals(8, nl1.size());
        assertEquals("ABCDEFG.cbr", nl1.get(0).getName());
        assertEquals("/abc/def/zghijklmn/", nl1.get(0).getBranch("1ST"));
        assertEquals("/abc2/def/ghijklmn/", nl1.get(0).getBranch("2ND"));

        assertEquals("JIJKLMN.cbz", nl1.get(1).getName());
        assertEquals("/abc/opq/rstuvwxyz1/", nl1.get(1).getBranch("1ST"));
        assertEquals("/abc2/def/ghijklmn/", nl1.get(1).getBranch("2ND"));

        assertEquals("AIJKLMN.cbz", nl1.get(2).getName());
        assertEquals("/abc/opq/rstuvwxyz2/", nl1.get(2).getBranch("1ST"));
        assertEquals("/abc2/def/ghijklmn/", nl1.get(2).getBranch("2ND"));
        
        assertEquals("ABCDEFG.cbr", nl1.get(3).getName());
        assertEquals("/abc/opq/rstuvwxyz2/", nl1.get(3).getBranch("1ST"));
        assertEquals("/abc2/def/ghijklmn/", nl1.get(3).getBranch("2ND"));
        
        assertEquals("HIJKLMN.cbz", nl1.get(4).getName());
        assertEquals("/abc/opq/rstuvwxyz2/", nl1.get(4).getBranch("1ST"));
        assertEquals("/abc2/opq/rstuvwxyz/", nl1.get(4).getBranch("2ND"));
        
        assertEquals("BIJKLMN.cbz", nl1.get(5).getName());
        assertEquals("/abc/opq/rstuvwxyz2/", nl1.get(5).getBranch("1ST"));
        assertEquals("/abc3/opq/rstuvwxyz/", nl1.get(5).getBranch("2ND"));
        
        assertEquals("IIJKLMN.cbz", nl1.get(6).getName());
        assertEquals("/abc/opq/rstuvwxyz2/", nl1.get(6).getBranch("1ST"));
        assertEquals("/abc3/iopq/rstuvwxyz/", nl1.get(6).getBranch("2ND"));
        
        assertEquals("HIJKLMN.cbz", nl1.get(7).getName());
        assertEquals("/abc/opq/rstuvwxyz3/", nl1.get(7).getBranch("1ST"));
        assertEquals("/abc2/def/ghijklmn/", nl1.get(7).getBranch("2ND"));        
    }
    
    @Test
    public void sortByBranchNameTest_2nd() {
        ArrayList<Node> nl1 = new ArrayList<Node>(); 
        Node node = null; 
        node = new Node(1, "ABCDEFG.cbr");
        node.putBranch("1ST", "/abc/def/zghijklmn/");
        node.putBranch("2ND", "/abc2/def/ghijklmn/");
        nl1.add(node);
        node = new Node(2, "HIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz3/");
        node.putBranch("2ND", "/abc2/def/ghijklmn/");
        nl1.add(node);
        node = new Node(3, "AIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz2/");
        node.putBranch("2ND", "/abc2/def/ghijklmn/");
        nl1.add(node);
        node = new Node(4, "JIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz1/");
        node.putBranch("2ND", "/abc2/def/ghijklmn/");
        nl1.add(node);
        node = new Node(5, "ABCDEFG.cbr");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz2/");
        node.putBranch("2ND", "/abc2/def/ghijklmn/");
        nl1.add(node);
        node = new Node(6, "HIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz2/");
        node.putBranch("2ND", "/abc2/opq/rstuvwxyz/");
        nl1.add(node);
        node = new Node(7, "BIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz2/");
        node.putBranch("2ND", "/abc3/opq/rstuvwxyz/");
        nl1.add(node);
        node = new Node(8, "IIJKLMN.cbz");
        node.putBranch("1ST", "/abc/opq/rstuvwxyz2/");
        node.putBranch("2ND", "/abc3/iopq/rstuvwxyz/");
        nl1.add(node);
       
        NodeList.sortByBranchName(nl1, "2ND");
        assertEquals(8, nl1.size());
        assertEquals("ABCDEFG.cbr", nl1.get(0).getName());
        assertEquals("/abc/def/zghijklmn/", nl1.get(0).getBranch("1ST"));
        assertEquals("/abc2/def/ghijklmn/", nl1.get(0).getBranch("2ND"));

        assertEquals("HIJKLMN.cbz", nl1.get(1).getName());
        assertEquals("/abc/opq/rstuvwxyz3/", nl1.get(1).getBranch("1ST"));
        assertEquals("/abc2/def/ghijklmn/", nl1.get(1).getBranch("2ND"));
        
        assertEquals("AIJKLMN.cbz", nl1.get(2).getName());
        assertEquals("/abc/opq/rstuvwxyz2/", nl1.get(2).getBranch("1ST"));
        assertEquals("/abc2/def/ghijklmn/", nl1.get(2).getBranch("2ND"));
        
        assertEquals("JIJKLMN.cbz", nl1.get(3).getName());
        assertEquals("/abc/opq/rstuvwxyz1/", nl1.get(3).getBranch("1ST"));
        assertEquals("/abc2/def/ghijklmn/", nl1.get(3).getBranch("2ND"));

        assertEquals("ABCDEFG.cbr", nl1.get(4).getName());
        assertEquals("/abc/opq/rstuvwxyz2/", nl1.get(4).getBranch("1ST"));
        assertEquals("/abc2/def/ghijklmn/", nl1.get(4).getBranch("2ND"));
        
        assertEquals("HIJKLMN.cbz", nl1.get(5).getName());
        assertEquals("/abc/opq/rstuvwxyz2/", nl1.get(5).getBranch("1ST"));
        assertEquals("/abc2/opq/rstuvwxyz/", nl1.get(5).getBranch("2ND"));
        
        assertEquals("IIJKLMN.cbz", nl1.get(6).getName());
        assertEquals("/abc/opq/rstuvwxyz2/", nl1.get(6).getBranch("1ST"));
        assertEquals("/abc3/iopq/rstuvwxyz/", nl1.get(6).getBranch("2ND"));
        
        assertEquals("BIJKLMN.cbz", nl1.get(7).getName());
        assertEquals("/abc/opq/rstuvwxyz2/", nl1.get(7).getBranch("1ST"));
        assertEquals("/abc3/opq/rstuvwxyz/", nl1.get(7).getBranch("2ND"));
        
    }

}
