package com.skylynx13.transpath.utils;

import com.skylynx13.transpath.log.TransLog;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static org.junit.Assert.*;

 /**
 * ClassName: FileUtilsTest
 * Description: File utils test
 * Date: 2015-02-17 18:11:37
 */
public class FileUtilsTest {
    private static final String TEST_RESOURCES_LOC = "src/test/resources/";
    private String testResource(String fileName) {
        return (TEST_RESOURCES_LOC + fileName);
    }
        
    @Test
    public void compareFileBytesTest_same_path() {
        assertTrue(FileUtils.compareFileBytes(
                testResource("CompareTest_base.txt"),
                testResource("CompareTest_base.txt")));
    }

    @Test
    public void compareFileBytesTest_same_contents() {
        assertTrue(FileUtils.compareFileBytes(
                testResource("CompareTest_base.txt"),
                testResource("CompareTest_same.txt")));
    }

    @Test
    public void compareFileBytesTest_different_length() {
        assertFalse(FileUtils.compareFileBytes(
                testResource("CompareTest_base.txt"),
                testResource("CompareTest_short.txt")));
    }

    @Test
    public void compareFileBytesTest_different_contents() {
        assertFalse(FileUtils.compareFileBytes(
                testResource("CompareTest_base.txt"),
                testResource("CompareTest_change.txt")));
    }

    @Test
    public void getFileSizeTest_file_size() {
        assertEquals(9899, FileUtils.getFileSize(new File(testResource("Raw_ArrangerTest.txt"))));
        assertEquals(10205, FileUtils.getFileSize(new File(testResource("CompareTest_base.txt"))));
        assertEquals(8990, FileUtils.getFileSize(new File(testResource("CompareTest_short.txt"))));
        assertEquals( 10205, FileUtils.getFileSize(new File(testResource("CompareTest_change.txt"))));
    }

    @Test
    public void getFileSizeTest_dir_size() {
        assertEquals(32521529, FileUtils.getFileSize(new File(testResource(""))));
    }

    @Test
    public void clearFileTest() {
        String fn = testResource("ClearTest_000.txt");

        try (PrintWriter pw = new PrintWriter(fn)) {
            pw.println("12345");
        } catch (FileNotFoundException e) {
            TransLog.getLogger().error("", e);
        }
        File f1 = new File(fn);
        assertNotEquals(0, f1.length());
        FileUtils.clearFile(fn);
        assertEquals(0, f1.length());
    }

    @Test
    public void copyFileBytesTest() {
        String fn1 = testResource("CopyTest_001.txt");
        String fn2 = testResource("CopyTest_002.txt");
        File f1 = new File(fn1);
        File f2 = new File(fn2);
        
        FileUtils.clearFile(fn2);
        assertEquals(0, f2.length());

        assertTrue(FileUtils.copyFileBytes(fn1, fn2));
        assertEquals(f1.length(), f2.length());
        assertTrue(FileUtils.compareFileBytes(fn1, fn2));
    }
    
    @Test
    public void catFileBytesTest() {
        String fn1 = testResource("CatTest_001.txt");
        String fn2 = testResource("CatTest_002.txt");
        String fn3 = testResource("CatTest_003.txt");
        File f1 = new File(fn1);
        File f2 = new File(fn2);
        File f3 = new File(fn3);
        
        FileUtils.clearFile(fn3);
        assertEquals(0, f3.length());
        
        long tm1 = System.currentTimeMillis();
        assertTrue(FileUtils.catFileBytes(fn1, fn2, fn3));
        System.out.println("catFileBytesTest: " + (
                System.currentTimeMillis()-tm1) + " ms.");
        
        assertEquals(f1.length() + f2.length(), f3.length());
    }
    
    @Test
    public void catFileBytesTest_copy_append() {
        String fn1 = testResource("CatTest_copy_append_001.txt");
        String fn2 = testResource("CatTest_copy_append_002.txt");
        String fn3 = testResource("CatTest_copy_append_003.txt");
        File f1 = new File(fn1);
        File f2 = new File(fn2);
        File f3 = new File(fn3);

        FileUtils.clearFile(fn3);
        assertEquals(0, f3.length());

        long tm1 = System.currentTimeMillis();
        assertTrue(FileUtils.copyFileBytes(fn1, fn3));
        assertTrue(FileUtils.appendFileBytes(fn2, fn3));
        System.out.println("catFileBytesTest_copy_append: " + (
                System.currentTimeMillis()-tm1) + " ms.");

        assertEquals(f1.length() + f2.length(), f3.length());
    }
}
