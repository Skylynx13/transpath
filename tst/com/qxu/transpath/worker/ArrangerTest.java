/**
 * Copyright (c) 2015,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.worker
 * File Name:ArrangerTest.java
 * Date:2015-2-17 上午10:13:07
 * 
 */
package com.qxu.transpath.worker;

import static org.junit.Assert.*;

import org.junit.Test;

import com.qxu.transpath.utils.FileUtils;

 /**
 * ClassName: ArrangerTest <br/>
 * Description: TODO <br/>
 * Date: 2015-2-17 上午10:13:07 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class ArrangerTest {

    @Test
    public void readSortMergeWriteTest_raw_task() {
        Arranger argr = new Arranger();
        int n = argr.readFromFile("resource/tst/ArrangerTest_raw.txt").sort().merge().writeToFile("resource/tst/ArrangerTest_task.txt");
        assertEquals(31, n);
        assertEquals(true, FileUtils.compareFileBytes("resource/tst/ArrangerTest_task.txt", "resource/tst/ArrangerTest_task_000.txt"));
    }

}
