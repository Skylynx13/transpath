/**
 * Copyright (c) 2015,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:NodeTreeTest.java
 * Date:2015-2-11 下午2:30:30
 * 
 */
package com.qxu.transpath.tree;

import static org.junit.Assert.*;

import org.junit.Test;

import com.qxu.transpath.utils.TransConst;

 /**
 * ClassName: NodeTreeTest <br/>
 * Description: TODO <br/>
 * Date: 2015-2-11 下午2:30:30 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class NodeTreeTest {

    @Test
    public void buildFromListTest() {
        NodeTree aTree = NodeTree.buildFromList(NodeList.buildFromRoot("resource/qtest"), TransConst.BRANCH_1ST);
        assertEquals(TransConst.ROOT, aTree.getNodeName());
        assertEquals("tdir001", aTree.getChildAt(0).getNodeName());
        assertEquals("/TFLib/tdir001", aTree.getChildAt(0).getNodePathName());
        assertEquals("tdir102", aTree.getChildAt(0).getChildAt(0).getNodeName());
        assertEquals("/TFLib/tdir001/tdir102", aTree.getChildAt(0).getChildAt(0).getNodePathName());
        assertEquals("tfile004.txt", aTree.getChildAt(0).getChildAt(0).getChildAt(0).getNodeName());
        assertEquals("/TFLib/tdir001/tdir102/tfile004.txt", aTree.getChildAt(0).getChildAt(0).getChildAt(0).getNodePathName());
        assertEquals("tfile005.txt", aTree.getChildAt(0).getChildAt(0).getChildAt(1).getNodeName());
        assertEquals("/TFLib/tdir001/tdir102/tfile005.txt", aTree.getChildAt(0).getChildAt(0).getChildAt(1).getNodePathName());
        assertEquals("tdir002", aTree.getChildAt(1).getNodeName());
        assertEquals("/TFLib/tdir002", aTree.getChildAt(1).getNodePathName());
        assertEquals("tdir201", aTree.getChildAt(1).getChildAt(0).getNodeName());
        assertEquals("/TFLib/tdir002/tdir201", aTree.getChildAt(1).getChildAt(0).getNodePathName());
        assertEquals("tfile006.txt", aTree.getChildAt(1).getChildAt(0).getChildAt(0).getNodeName());
        assertEquals("/TFLib/tdir002/tdir201/tfile006.txt", aTree.getChildAt(1).getChildAt(0).getChildAt(0).getNodePathName());
        assertEquals("tfile007.txt", aTree.getChildAt(1).getChildAt(0).getChildAt(1).getNodeName());
        assertEquals("/TFLib/tdir002/tdir201/tfile007.txt", aTree.getChildAt(1).getChildAt(0).getChildAt(1).getNodePathName());
        assertEquals("tdir003", aTree.getChildAt(2).getNodeName());
        assertEquals("/TFLib/tdir003", aTree.getChildAt(2).getNodePathName());
        assertEquals("tfile008.txt", aTree.getChildAt(2).getChildAt(0).getNodeName());
        assertEquals("/TFLib/tdir003/tfile008.txt", aTree.getChildAt(2).getChildAt(0).getNodePathName());
        assertEquals("tfile001.txt", aTree.getChildAt(3).getNodeName());
        assertEquals("/TFLib/tfile001.txt", aTree.getChildAt(3).getNodePathName());
        assertEquals("tfile002.txt", aTree.getChildAt(4).getNodeName());
        assertEquals("/TFLib/tfile002.txt", aTree.getChildAt(4).getNodePathName());
        assertEquals("tfile003.txt", aTree.getChildAt(5).getNodeName());
        assertEquals("/TFLib/tfile003.txt", aTree.getChildAt(5).getNodePathName());
    }

}
