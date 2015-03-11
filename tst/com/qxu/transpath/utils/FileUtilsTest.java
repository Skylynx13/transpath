/**
 * Copyright (c) 2015,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.utils
 * File Name:FileUtilsTest.java
 * Date:2015-2-17 下午6:11:37
 * 
 */
package com.qxu.transpath.utils;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

 /**
 * ClassName: FileUtilsTest <br/>
 * Description: TODO <br/>
 * Date: 2015-2-17 下午6:11:37 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class FileUtilsTest {

    @Test
    public void compareFileBytesTest_same_path() {
        assertEquals(true, FileUtils.compareFileBytes(
                "resource/tst/ArrangerTest_task_000.txt",
                "resource/tst/ArrangerTest_task_000.txt"));
    }

    @Test
    public void compareFileBytesTest_same_contents() {
        assertEquals(true, FileUtils.compareFileBytes(
                "resource/tst/ArrangerTest_task_000.txt",
                "resource/tst/ArrangerTest_task_001.txt"));
    }

    @Test
    public void compareFileBytesTest_different_length() {
        assertEquals(false, FileUtils.compareFileBytes(
                "resource/tst/ArrangerTest_task_000.txt",
                "resource/tst/ArrangerTest_task_002.txt"));
    }

    @Test
    public void compareFileBytesTest_different_contents() {
        assertEquals(false, FileUtils.compareFileBytes(
                "resource/tst/ArrangerTest_task_000.txt",
                "resource/tst/ArrangerTest_task_003.txt"));
    }

    @Test
    public void getFileSizeTest_file_size() {
        assertEquals(9078, FileUtils.getFileSize(new File("resource/tst/ArrangerTest_raw.txt")));
        assertEquals(8047, FileUtils.getFileSize(new File("resource/tst/ArrangerTest_task_000.txt")));
        assertEquals(7715, FileUtils.getFileSize(new File("resource/tst/ArrangerTest_task_002.txt")));
        assertEquals( 183, FileUtils.getFileSize(new File("resource/tst/mergeTest_source_001.txt")));
    }
    @Test
    public void getFileSizeTest_dir_size() {
        assertEquals(51203, FileUtils.getFileSize(new File("resource/tst")));
    }
}
