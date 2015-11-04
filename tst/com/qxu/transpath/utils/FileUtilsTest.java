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
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.junit.Ignore;
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
        assertEquals(9445, FileUtils.getFileSize(new File("resource/tst/ArrangerTest_raw.txt")));
        assertEquals(8047, FileUtils.getFileSize(new File("resource/tst/ArrangerTest_task_000.txt")));
        assertEquals(7715, FileUtils.getFileSize(new File("resource/tst/ArrangerTest_task_002.txt")));
        assertEquals( 183, FileUtils.getFileSize(new File("resource/tst/mergeTest_source_001.txt")));
    }
    @Test
    public void getFileSizeTest_dir_size() {
        assertEquals(53706, FileUtils.getFileSize(new File("resource/tst/GetFileSizeTest")));
    }
    @Test
    public void clearFileTest() {
        String fn = "resource/tst/ClearTest_000.txt";
        
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(fn);
            pw.println("12345");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            pw.close();
        }
        File f1 = new File(fn);
        assertEquals(7, f1.length());
        FileUtils.clearFile(fn);
        assertEquals(0, f1.length());
    }
    @Test
    public void copyFileBytesTest() {
        String fn1 = "resource/tst/CopyTest_001.txt";
        String fn2 = "resource/tst/CopyTest_002.txt";
        File f1 = new File(fn1);
        File f2 = new File(fn2);
        
        FileUtils.clearFile(fn2);
        assertEquals(0, f2.length());
        
        assertEquals(true, FileUtils.copyFileBytes(fn1, fn2));
        assertEquals(f1.length(), f2.length());
        assertEquals(true, FileUtils.compareFileBytes(fn1, fn2));
    }
    
    @Test
    public void catFileBytesTest() {
        String fn0 = "resource/tst/CatTest_000.txt";
        String fn1 = "resource/tst/CatTest_001.txt";
        String fn2 = "resource/tst/CatTest_002.txt";
        String fn3 = "resource/tst/CatTest_003.txt";
        File f1 = new File(fn1);
        File f2 = new File(fn2);
        File f3 = new File(fn3);
        
        FileUtils.clearFile(fn3);
        assertEquals(0, f3.length());
        
        long tm1 = System.currentTimeMillis();
        assertEquals(true, FileUtils.catFileBytes(fn1, fn2, fn3));
        System.out.println("catFileBytesTest: " + (
                System.currentTimeMillis()-tm1) + " ms.");
        
        assertEquals(f1.length() + f2.length(), f3.length());
//        assertEquals(true, FileUtils.compareFileBytes(fn0, fn3));
    }
    
    @Test
    public void catFileBytesTest_copy_append() {
        String fn0 = "resource/tst/CatTest_copy_append_000.txt";
        String fn1 = "resource/tst/CatTest_copy_append_001.txt";
        String fn2 = "resource/tst/CatTest_copy_append_002.txt";
        String fn3 = "resource/tst/CatTest_copy_append_003.txt";
        File f1 = new File(fn1);
        File f2 = new File(fn2);
        File f3 = new File(fn3);
        
        FileUtils.clearFile(fn3);
        assertEquals(0, f3.length());
        
        long tm1 = System.currentTimeMillis();
        assertEquals(true, FileUtils.copyFileBytes(fn1, fn3));
        assertEquals(true, FileUtils.appendFileBytes(fn2, fn3));
        System.out.println("catFileBytesTest_copy_append: " + (
                System.currentTimeMillis()-tm1) + " ms.");
        
        assertEquals(f1.length() + f2.length(), f3.length());
//        assertEquals(true, FileUtils.compareFileBytes(fn0, fn3));
    }
    
    @Test
    public void getFileMimeTypeTest() {
        //Look up: ${JRE_HOME}\lib\content-types.properties
        long tm1 = System.currentTimeMillis();
        assertEquals("text/plain", FileUtils.getFileMimeType("resource/tst/getFileMimeType_000.txt"));
        System.out.println("getFileMimeTypeTest URLConnection: " + (
                System.currentTimeMillis()-tm1) + " ms.");
        assertEquals("text/qxu", FileUtils.getFileMimeType("resource/tst/getFileMimeType_001.qxu"));
        assertEquals("archive/roshal", FileUtils.getFileMimeType("resource/tst/getFileMimeType_002.rar"));
        assertEquals("archive/comic", FileUtils.getFileMimeType("resource/tst/getFileMimeType_003.cbr"));
        assertEquals("archive/comic", FileUtils.getFileMimeType("resource/tst/getFileMimeType_004.cbz"));
    }
    
    @Test
    public void getLastModifiedStringTest() {
        assertEquals("2015-03-19 14:11:42.114", FileUtils.getLastModifiedString("resource/tst/getLastModifiedString_000.txt"));
    }
}
