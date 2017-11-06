/**
 * Copyright (c) 2016,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:LinkListTest.java
 * Date:2016-7-20 下午3:28:13
 * 
 */
package com.skylynx13.transpath.pub;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import com.skylynx13.transpath.pub.LinkList;
import com.skylynx13.transpath.pub.LinkNode;

 /**
 * ClassName: LinkListTest <br/>
 * Description: TODO <br/>
 * Date: 2016-7-20 下午3:28:13 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class LinkListTest {

    @Test
    public void testRefreshStoreId() {
        LinkList aList = new LinkList();
        LinkNode aNode = new LinkNode(1, 11);
        aList.addNode(aNode);
        aNode = new LinkNode(2, 12);
        aList.addNode(aNode);
        aNode = new LinkNode(3, 13);
        aList.addNode(aNode);
        aNode = new LinkNode(4, 14);
        aList.addNode(aNode);
        aNode = new LinkNode(5, 15);
        aList.addNode(aNode);
        assertEquals(1, ((LinkNode)aList.get(0)).storeId);
        assertEquals(2, ((LinkNode)aList.get(1)).storeId);
        assertEquals(3, ((LinkNode)aList.get(2)).storeId);
        assertEquals(4, ((LinkNode)aList.get(3)).storeId);
        assertEquals(5, ((LinkNode)aList.get(4)).storeId);
        assertEquals(11, ((LinkNode)aList.get(0)).pubId);
        assertEquals(12, ((LinkNode)aList.get(1)).pubId);
        assertEquals(13, ((LinkNode)aList.get(2)).pubId);
        assertEquals(14, ((LinkNode)aList.get(3)).pubId);
        assertEquals(15, ((LinkNode)aList.get(4)).pubId);
        HashMap<Integer, Integer> aMap = new HashMap<Integer, Integer>();
        aMap.put(11, 15);
        aMap.put(12, 14);
        aMap.put(13, 13);
        aMap.put(14, 12);
        aMap.put(15, 11);
        aList.refreshPubId(aMap);
        assertEquals(1, ((LinkNode)aList.get(0)).storeId);
        assertEquals(2, ((LinkNode)aList.get(1)).storeId);
        assertEquals(3, ((LinkNode)aList.get(2)).storeId);
        assertEquals(4, ((LinkNode)aList.get(3)).storeId);
        assertEquals(5, ((LinkNode)aList.get(4)).storeId);
        assertEquals(15, ((LinkNode)aList.get(0)).pubId);
        assertEquals(14, ((LinkNode)aList.get(1)).pubId);
        assertEquals(13, ((LinkNode)aList.get(2)).pubId);
        assertEquals(12, ((LinkNode)aList.get(3)).pubId);
        assertEquals(11, ((LinkNode)aList.get(4)).pubId);
    }

}
