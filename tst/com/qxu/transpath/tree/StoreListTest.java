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
 * Change Log:
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
        assertEquals(0, sl.length);
        sl.addNode(sn);
        assertEquals(1, sn.id);
        assertEquals(1, sl.get(0).id);
        assertEquals(1, sl.pidMax);
        assertEquals(1, sl.length);
    }

}
