/**
 * Copyright (c) 2016,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.utest
 * File Name:StoreListTest.java
 * Date:2016-6-17 上午12:29:56
 * 
 */
package com.qxu.transpath.tree;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import com.qxu.transpath.tree.StoreList;
import com.qxu.transpath.tree.StoreNode;

/**
 * ClassName: StoreListTest <br/>
 * Description: TODO <br/>
 * Date: 2016-6-17 上午12:29:56 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 *         Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class StoreListTest {

    @Test
    public void testAddNode_add1() {
        StoreList sl = new StoreList();
        StoreNode sn = new StoreNode();
        assertEquals(0, sn.id);
        assertEquals(0, sl.pidMax);
        assertEquals(0, sl.size());
        sl.addNode(sn);
        assertEquals(1, sn.id);
        assertEquals(1, sl.get(0).id);
        assertEquals(1, sl.pidMax);
        assertEquals(1, sl.size());
    }

    @Test
    public void testHasNode() {
        StoreList sl = new StoreList();
        StoreNode sn1 = new StoreNode();
        StoreNode sn2 = new StoreNode();
        assertEquals(0, sl.size());
        sl.addNode(sn1);
        assertEquals(true, sl.hasNode(sn1));
        assertEquals(false, sl.hasNode(sn2));        
    }
    
    @Test
    public void testAttachList() {
        StoreList sl1 = new StoreList();
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc11"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc12"));
        StoreList sl2 = new StoreList();
        sl2.addNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc21"));
        sl2.addNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc22"));
        sl2.addNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc23"));
        sl2.addNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc24"));
        sl1.attachList(sl2);
        String expResult = "00000001:00000006:00000000000000000738:00000006\r\n" + "00000001:0000000000123:456:1:2:3:/aaa/bbb/:ccc11\r\n"
                + "00000002:0000000000123:456:1:2:3:/aaa/bbb/:ccc12\r\n"
                + "00000003:0000000000123:456:1:2:3:/aaa/bbb/:ccc21\r\n"
                + "00000004:0000000000123:456:1:2:3:/aaa/bbb/:ccc22\r\n"
                + "00000005:0000000000123:456:1:2:3:/aaa/bbb/:ccc23\r\n"
                + "00000006:0000000000123:456:1:2:3:/aaa/bbb/:ccc24\r\n";
        assertEquals(expResult, sl1.toString().substring(sl1.toString().indexOf(':')+1));
    }

    @Test
    public void testAttachList1() {
        StoreList sl1 = new StoreList();
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc11"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc12"));
        StoreList sl2 = new StoreList();
        sl2.addNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc21"));
        sl2.addNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc22"));
        sl2.addNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc23"));
        sl2.addNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc24"));
        sl1.attachList1(sl2);
        String expResult = "00000001:00000006:00000000000000000246:00000006\r\n" + "00000001:0000000000123:456:1:2:3:/aaa/bbb/:ccc11\r\n"
                + "00000002:0000000000123:456:1:2:3:/aaa/bbb/:ccc12\r\n"
                + "00000003:0000000000123:456:1:2:3:/aaa/bbb/:ccc21\r\n"
                + "00000004:0000000000123:456:1:2:3:/aaa/bbb/:ccc22\r\n"
                + "00000005:0000000000123:456:1:2:3:/aaa/bbb/:ccc23\r\n"
                + "00000006:0000000000123:456:1:2:3:/aaa/bbb/:ccc24\r\n";
        assertEquals(expResult, sl1.toString().substring(sl1.toString().indexOf(':')+1));
    }
    
    @Test
    public void testRemoveByPath_middle() {
        StoreList sl1 = new StoreList();
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc11"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc12"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/ddd/:ccc21"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/ddd/:ccc22"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/ddd/:ccc23"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/fff/:ccc24"));
        sl1.removeByPath("/aaa/ddd/");
        String expResult = "00000001:00000006:00000000000000000369:00000003\r\n" + "00000001:0000000000123:456:1:2:3:/aaa/bbb/:ccc11\r\n"
                + "00000002:0000000000123:456:1:2:3:/aaa/bbb/:ccc12\r\n"
                + "00000006:0000000000123:456:1:2:3:/aaa/fff/:ccc24\r\n";
        assertEquals(expResult, sl1.toString().substring(sl1.toString().indexOf(':')+1));
    }
    @Test
    public void testRemoveByPath_head() {
        StoreList sl1 = new StoreList();
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc11"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc12"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/ddd/:ccc21"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/ddd/:ccc22"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/ddd/:ccc23"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/fff/:ccc24"));
        sl1.removeByPath("/aaa/bbb/");
        String expResult = "00000003:00000006:00000000000000000492:00000004\r\n"
                + "00000003:0000000000123:456:1:2:3:/aaa/ddd/:ccc21\r\n"
                + "00000004:0000000000123:456:1:2:3:/aaa/ddd/:ccc22\r\n"
                + "00000005:0000000000123:456:1:2:3:/aaa/ddd/:ccc23\r\n"
                + "00000006:0000000000123:456:1:2:3:/aaa/fff/:ccc24\r\n";
        assertEquals(expResult, sl1.toString().substring(sl1.toString().indexOf(':')+1));
    }
    @Test
    public void testRemoveByPath_tail() {
        StoreList sl1 = new StoreList();
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc11"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc12"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/ddd/:ccc21"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/ddd/:ccc22"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/ddd/:ccc23"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/fff/:ccc24"));
        sl1.removeByPath("/aaa/fff/");
        String expResult = "00000001:00000005:00000000000000000615:00000005\r\n" + "00000001:0000000000123:456:1:2:3:/aaa/bbb/:ccc11\r\n"
                + "00000002:0000000000123:456:1:2:3:/aaa/bbb/:ccc12\r\n"
                + "00000003:0000000000123:456:1:2:3:/aaa/ddd/:ccc21\r\n"
                + "00000004:0000000000123:456:1:2:3:/aaa/ddd/:ccc22\r\n"
                + "00000005:0000000000123:456:1:2:3:/aaa/ddd/:ccc23\r\n";
        assertEquals(expResult, sl1.toString().substring(sl1.toString().indexOf(':')+1));
    }
    
    @Test
    public void testReorgPid() {
        StoreList sl1 = new StoreList();
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc11"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc12"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/ddd/:ccc21"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/ddd/:ccc22"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/ddd/:ccc23"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/fff/:ccc24"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/fff/:ccc25"));
        sl1.removeByPath("/aaa/ddd/");
        HashMap<Integer, Integer> aMap = sl1.reorgPid();
        String expResult = "00000001:00000004:00000000000000000492:00000004\r\n" + "00000001:0000000000123:456:1:2:3:/aaa/bbb/:ccc11\r\n"
                + "00000002:0000000000123:456:1:2:3:/aaa/bbb/:ccc12\r\n"
                + "00000003:0000000000123:456:1:2:3:/aaa/fff/:ccc24\r\n"
                + "00000004:0000000000123:456:1:2:3:/aaa/fff/:ccc25\r\n";
        assertEquals(expResult, sl1.toString().substring(sl1.toString().indexOf(':')+1));
        System.out.println(aMap.toString());
        assertEquals(2, aMap.keySet().size());
        assertEquals(6, aMap.keySet().toArray()[0]);
        assertEquals(7, aMap.keySet().toArray()[1]);
        assertEquals(3, (int)aMap.get(aMap.keySet().toArray()[0]));
        assertEquals(4, (int)aMap.get(aMap.keySet().toArray()[1]));
    }
    @Test
    public void testOrderByPathAndName() {
        StoreList sl1 = new StoreList();
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc11"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/bbb/:ccc12"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/fff/:ccc21"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/fff/:ccc22"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/eee/:ccc25"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/ddd/:ccc24"));
        sl1.addNode(new StoreNode("0:123:456:1:2:3:/aaa/eee/:ccc23"));
        //sl1.removeByPath("/aaa/ddd/");
        sl1.orderByPathAndName();
        HashMap<Integer, Integer> aMap = sl1.reorgPid();
        String expResult = "00000001:00000007:00000000000000000861:00000007\r\n" 
                + "00000001:0000000000123:456:1:2:3:/aaa/bbb/:ccc11\r\n"
                + "00000002:0000000000123:456:1:2:3:/aaa/bbb/:ccc12\r\n"
                + "00000003:0000000000123:456:1:2:3:/aaa/ddd/:ccc24\r\n"
                + "00000004:0000000000123:456:1:2:3:/aaa/eee/:ccc23\r\n"
                + "00000005:0000000000123:456:1:2:3:/aaa/eee/:ccc25\r\n"
                + "00000006:0000000000123:456:1:2:3:/aaa/fff/:ccc21\r\n"
                + "00000007:0000000000123:456:1:2:3:/aaa/fff/:ccc22\r\n";
        assertEquals(expResult, sl1.toString().substring(sl1.toString().indexOf(':')+1));
        System.out.println(aMap.toString());
        assertEquals(4, aMap.keySet().size());
        assertEquals(3, aMap.keySet().toArray()[0]);
        assertEquals(4, aMap.keySet().toArray()[1]);
        assertEquals(6, aMap.keySet().toArray()[2]);
        assertEquals(7, aMap.keySet().toArray()[3]);
        assertEquals(6, (int) aMap.get(aMap.keySet().toArray()[0]));
        assertEquals(7, (int) aMap.get(aMap.keySet().toArray()[1]));
        assertEquals(3, (int) aMap.get(aMap.keySet().toArray()[2]));
        assertEquals(4, (int) aMap.get(aMap.keySet().toArray()[3]));
    }
}
