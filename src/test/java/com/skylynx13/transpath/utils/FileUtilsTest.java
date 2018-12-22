package com.skylynx13.transpath.utils;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static org.junit.Assert.*;

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
        assertEquals(32510140, FileUtils.getFileSize(new File(testResource(""))));
    }

    @Test
    public void clearFileTest() {
        String fn = testResource("ClearTest_000.txt");

        try (PrintWriter pw = new PrintWriter(fn)) {
            pw.println("12345");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

    @Test
    @Ignore
    public void checkPackageTest() {
        long t0 = System.currentTimeMillis();
//        List<String> testFiles = new ArrayList<>();
//        testFiles.add(testResource("testFiles.rar"));
//        testFiles.add(testResource("testZipFakeRar.rar"));
//        testFiles.add(testResource("testEmpty.rar"));
//        testFiles.add(testResource("testFiles.cbr"));
//        testFiles.add(testResource("testFiles.zip"));
//        testFiles.add(testResource("testRarFakeZip.zip"));
//        testFiles.add(testResource("testEmpty.zip"));
//        testFiles.add(testResource("testFiles.cbz"));
//        testFiles.add(testResource("testWrongType.txt"));
//        String result = FileUtils.checkPackages(testFiles);
//        assertEquals(5, result.size());
//        assertEquals("No files to extract", result.get(testResource("testZipFakeRar.rar")));
//        assertEquals("No files to extract", result.get(testResource("testEmpty.rar")));
//        assertTrue(result.get(testResource("testRarFakeZip.zip")).startsWith("zip error: "));
//        assertTrue(result.get(testResource("testEmpty.zip")).startsWith("zip error: "));
//        assertEquals(TransConst.PKG_TYPE, result.get(testResource("testWrongType.txt")));
//        TransLog.getLogger().info("Multiple Time = " + (System.currentTimeMillis() - t0));
        assertTrue(true);
    }
    
    /**
     * To pass this test, add following entries to ${JRE_HOME}\lib\content-types.properties <br/>
     * text/qxu: \
     *     description=QXU text;\
     *     file_extensions=.qxu
     *
     * archive/roshal: \
     *     description=rar package;\
     *     file_extensions=.rar
     *
     * archive/comic: \
     *     description=comic package;\
     *     file_extensions=.cbr,.cbz
     */
    @Test
    public void getFileMimeTypeTest() {
        long tm1 = System.currentTimeMillis();
        assertEquals("text/plain", FileUtils.getFileMimeType(testResource("getFileMimeType_000.txt")));
        System.out.println("getFileMimeTypeTest URLConnection: " + (
                System.currentTimeMillis()-tm1) + " ms.");
//        assertEquals("text/qxu", FileUtils.getFileMimeType(testResource("getFileMimeType_001.qxu")));
//        assertEquals("archive/roshal", FileUtils.getFileMimeType(testResource("getFileMimeType_002.rar")));
//        assertEquals("archive/comic", FileUtils.getFileMimeType(testResource("getFileMimeType_003.cbr")));
//        assertEquals("archive/comic", FileUtils.getFileMimeType(testResource("getFileMimeType_004.cbz")));
    }

    @SuppressWarnings("unused")
    @Test
    @Ignore
    public void getRarCommentTest() {
        String fn1 = testResource("getSetRarCommentTest.rar");
        String fn2 = testResource("getSetZipCommentTest.zip");
        File f1 = new File(fn1);
//        try {
//            ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(fn2));
//            zout.write(new String("ask why").getBytes());
//            zout.flush();
//            zout.setComment("abc");
//            zout.close();
//            ZipInputStream zin = new ZipInputStream(new FileInputStream(fn2));
//            byte[] b = new byte[10];
//            zin.read(b);
//            zin.close();
//            System.out.println(b);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        System.out.println(FileUtils.extractZipComment(fn2));
        assertTrue(true);
    }

    @Test
    public void isIgnorableTest() {
        String[] filesOk = {"testfile.txt", "testfile.pdf", "test.epub", "test.mobi"};
        String[] filesErr = {"testfile.pda", "testfile"};

        for (String fn : filesOk){
            assertTrue(FileUtils.isIgnorable(fn));
        }
        for (String fn : filesErr) {
            assertFalse(FileUtils.isIgnorable(fn));
        }

    }
}
