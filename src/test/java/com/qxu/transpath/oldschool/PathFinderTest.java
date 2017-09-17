/**
 * Copyright (c) 2014,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.utest
 * File Name:PathFinderTest.java
 * Date:2014-3-29 下午8:44:54
 * 
 */
package com.qxu.transpath.oldschool;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.qxu.transpath.worker.PathFinder;

 /**
 * ClassName: PathFinderTest <br/>
 * Description: TODO <br/>
 * Date: 2014-3-29 下午8:44:54 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class PathFinderTest {

    @Test
    public void testGetFileNameList() {
        PathFinder pf = new PathFinder();
        ArrayList<String> fl = pf.getFileNameList("src/test/resources/qtest");
        assertEquals("src\\test\\resources\\qtest\\tdir001", fl.get(0));
//        assertEquals("qtest\\tdir002", fl.get(1));
//        assertEquals("qtest\\tdir003", fl.get(2));
    }

//    @Test
//    public void testGetFileList() {
//    }

}
