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
import java.util.Scanner;

import org.junit.Test;

import com.qxu.transpath.utils.TranspathConstants;
import com.qxu.transpath.worker.Keeper;

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
        ArrayList<Node> nodes = NodeList.buildFromRoot("qtest");
        assertEquals("tfile004.txt", nodes.get(0).getName());
        assertEquals("/tdir001/tdir102/", nodes.get(0).getBranch1st());
        assertEquals("tfile005.txt", nodes.get(1).getName());
        assertEquals("/tdir001/tdir102/", nodes.get(1).getBranch1st());
        assertEquals("tfile006.txt", nodes.get(2).getName());
        assertEquals("/tdir002/tdir201/", nodes.get(2).getBranch1st());
        assertEquals("tfile007.txt", nodes.get(3).getName());
        assertEquals("/tdir002/tdir201/", nodes.get(3).getBranch1st());
        assertEquals("tfile008.txt", nodes.get(4).getName());
        assertEquals("/tdir003/", nodes.get(4).getBranch1st());
        assertEquals("tfile001.txt", nodes.get(5).getName());
        assertEquals("/", nodes.get(5).getBranch1st());
        assertEquals("tfile002.txt", nodes.get(6).getName());
        assertEquals("/", nodes.get(6).getBranch1st());
        assertEquals("tfile003.txt", nodes.get(7).getName());
        assertEquals("/", nodes.get(7).getBranch1st());
    }
    
    @Test
    public void buildFromFileTest() {
        ArrayList<Node> nodes = NodeList.buildFromFile("resource/tst/KeeperTest_buildListFromFileTest001.txt");
        assertEquals("ABCDEFG.cbr", nodes.get(0).getName());
        assertEquals("/abc/def/ghijklmn/", nodes.get(0).getBranch1st());
        assertEquals("HIJKLMN.cbz", nodes.get(1).getName());
        assertEquals("/abc/opq/rstuvwxyz/", nodes.get(1).getBranch1st());
    }
    
    @Test
    public void buildTreeFromListTest() {
        NodeTree aTree = NodeList.buildTreeFromList(NodeList.buildFromRoot("qtest"));
        assertEquals(TranspathConstants.ROOT, aTree.getNodeName());
        assertEquals("tdir001", aTree.getChildAt(0).getNodeName());
        assertEquals("/root/tdir001", aTree.getChildAt(0).getNodePathName());
        assertEquals("tdir102", aTree.getChildAt(0).getChildAt(0).getNodeName());
        assertEquals("/root/tdir001/tdir102", aTree.getChildAt(0).getChildAt(0).getNodePathName());
        assertEquals("tfile004.txt", aTree.getChildAt(0).getChildAt(0).getChildAt(0).getNodeName());
        assertEquals("/root/tdir001/tdir102/tfile004.txt", aTree.getChildAt(0).getChildAt(0).getChildAt(0).getNodePathName());
        assertEquals("tfile005.txt", aTree.getChildAt(0).getChildAt(0).getChildAt(1).getNodeName());
        assertEquals("/root/tdir001/tdir102/tfile005.txt", aTree.getChildAt(0).getChildAt(0).getChildAt(1).getNodePathName());
        assertEquals("tdir002", aTree.getChildAt(1).getNodeName());
        assertEquals("/root/tdir002", aTree.getChildAt(1).getNodePathName());
        assertEquals("tdir201", aTree.getChildAt(1).getChildAt(0).getNodeName());
        assertEquals("/root/tdir002/tdir201", aTree.getChildAt(1).getChildAt(0).getNodePathName());
        assertEquals("tfile006.txt", aTree.getChildAt(1).getChildAt(0).getChildAt(0).getNodeName());
        assertEquals("/root/tdir002/tdir201/tfile006.txt", aTree.getChildAt(1).getChildAt(0).getChildAt(0).getNodePathName());
        assertEquals("tfile007.txt", aTree.getChildAt(1).getChildAt(0).getChildAt(1).getNodeName());
        assertEquals("/root/tdir002/tdir201/tfile007.txt", aTree.getChildAt(1).getChildAt(0).getChildAt(1).getNodePathName());
        assertEquals("tdir003", aTree.getChildAt(2).getNodeName());
        assertEquals("/root/tdir003", aTree.getChildAt(2).getNodePathName());
        assertEquals("tfile008.txt", aTree.getChildAt(2).getChildAt(0).getNodeName());
        assertEquals("/root/tdir003/tfile008.txt", aTree.getChildAt(2).getChildAt(0).getNodePathName());
        assertEquals("tfile001.txt", aTree.getChildAt(3).getNodeName());
        assertEquals("/root/tfile001.txt", aTree.getChildAt(3).getNodePathName());
        assertEquals("tfile002.txt", aTree.getChildAt(4).getNodeName());
        assertEquals("/root/tfile002.txt", aTree.getChildAt(4).getNodePathName());
        assertEquals("tfile003.txt", aTree.getChildAt(5).getNodeName());
        assertEquals("/root/tfile003.txt", aTree.getChildAt(5).getNodePathName());
    }

    @Test
    public void combined_Storage_File_Keep_Test() {
        String root = "qtest/";
        NodeList.keepList("resource/pflist.txt", NodeList.buildFromRoot(root));
        NodeList.keepList("resource/pflist1st.txt", NodeList.buildFromFile("resource/pflist.txt"));
        Scanner sc1 = null;
        Scanner sc2 = null;
        try {
            sc1 = new Scanner(new FileReader("resource/pflist.txt"));
            sc2 = new Scanner(new FileReader("resource/pflist1st.txt"));
            int cnt = 0;
            while (sc1.hasNext() && sc2.hasNext()) {
                String line1 = sc1.nextLine().trim();
                String line2 = sc2.nextLine().trim();
                assertEquals(true, line1.equals(line2));
                cnt++;
            }
            assertEquals(16, cnt);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            sc1.close();
            sc2.close();
        }
    }

}
