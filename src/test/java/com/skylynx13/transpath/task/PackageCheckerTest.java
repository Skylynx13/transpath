package com.skylynx13.transpath.task;

import com.skylynx13.transpath.utils.TransConst;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

/**
 * ClassName: PackageCheckerTest
 * Description: Task checker test
 * Date: 2017-09-30 13:47:47
 */
public class PackageCheckerTest {
    private static final String TEST_RESOURCES_LOC = "src/test/resources/";
    private String testResource(String fileName) {
        return (TEST_RESOURCES_LOC + fileName);
    }
    private PackageChecker packageChecker = new PackageChecker();

    @Test
    public void isIgnorableTest() {
        String[] filesOk = {"testfile.txt", "testfile.pdf", "test.epub", "test.mobi"};
        String[] filesErr = {"testfile.pda", "testfile"};

        for (String fn : filesOk){
            assertTrue(packageChecker.isIgnorable(fn));
        }
        for (String fn : filesErr) {
            assertFalse(packageChecker.isIgnorable(fn));
        }
    }

    @Test
    public void checkPackageTest() {
        long t0 = System.currentTimeMillis();
        Map<String, String> statusInfo;

        statusInfo = packageChecker.checkPackage(testResource("testFiles.rar"));
        System.out.println(statusInfo.toString());
        assertTrue(statusInfo.isEmpty());

        statusInfo = packageChecker.checkPackage(testResource("testZipFakeRar.rar"));
        System.out.println(statusInfo.toString());
        assertEquals(TransConst.PKG_TYPE_MISMATCH, statusInfo.get(testResource("testZipFakeRar.rar")));

        statusInfo = packageChecker.checkPackage(testResource("testEmpty.rar"));
        System.out.println(statusInfo.toString());
        assertEquals(TransConst.PKG_ALL_FAILED, statusInfo.get(testResource("testEmpty.rar")));

        statusInfo = packageChecker.checkPackage(testResource("testFiles.cbr"));
        System.out.println(statusInfo.toString());
        assertTrue(statusInfo.isEmpty());

        statusInfo = packageChecker.checkPackage(testResource("testFiles.zip"));
        System.out.println(statusInfo.toString());
        assertTrue(statusInfo.isEmpty());

        statusInfo = packageChecker.checkPackage(testResource("testRarFakeZip.zip"));
        System.out.println(statusInfo.toString());
        assertEquals(TransConst.PKG_TYPE_MISMATCH, statusInfo.get(testResource("testRarFakeZip.zip")));

        statusInfo = packageChecker.checkPackage(testResource("testEmpty.zip"));
        System.out.println(statusInfo.toString());
        assertEquals(TransConst.PKG_ALL_FAILED, statusInfo.get(testResource("testEmpty.zip")));

        statusInfo = packageChecker.checkPackage(testResource("testFiles.cbz"));
        System.out.println(statusInfo.toString());
        assertTrue(statusInfo.isEmpty());

        statusInfo = packageChecker.checkPackage(testResource("testWrongType.tmp"));
        System.out.println(statusInfo.toString());
        assertEquals(TransConst.PKG_TYPE_NOT_RECOGNIZED, statusInfo.get(testResource("testWrongType.tmp")));

        statusInfo = packageChecker.checkPackage(testResource("testIgnorable.pdf"));
        System.out.println(statusInfo.toString());
        assertNull(statusInfo.get(testResource("testIgnorable.pdf")));

        System.out.println("Multiple Time = " + (System.currentTimeMillis() - t0));
    }

    @Test
    public void checkPackageListTest() {

        long t0 = System.currentTimeMillis();
        List<File> testFiles = new ArrayList<>();
        testFiles.add(new File(testResource("testFiles.rar")));
        testFiles.add(new File(testResource("testZipFakeRar.rar")));
        testFiles.add(new File(testResource("testEmpty.rar")));
        testFiles.add(new File(testResource("testFiles.cbr")));
        testFiles.add(new File(testResource("testFiles.zip")));
        testFiles.add(new File(testResource("testRarFakeZip.zip")));
        testFiles.add(new File(testResource("testEmpty.zip")));
        testFiles.add(new File(testResource("testFiles.cbz")));
        testFiles.add(new File(testResource("testWrongType.tmp")));
        testFiles.add(new File(testResource("testIgnorable.pdf")));
        String result = packageChecker.check(testFiles.toArray(new File[0])).toString();
        System.out.println(result);
        assertEquals("Result: 5 error(s) found.\r\n" +
                "src/test/resources/testEmpty.rar : All checkers failed.\r\n" +
                "src/test/resources/testEmpty.zip : All checkers failed.\r\n" +
                "src/test/resources/testRarFakeZip.zip : Type mismatch.\r\n" +
                "src/test/resources/testWrongType.tmp : File type not recognized.\r\n" +
                "src/test/resources/testZipFakeRar.rar : Type mismatch.\r\n" +
//                "src/test/resources/testIgnorable.pdf : File type ignorable.\r\n" +
                "", result);
//        assertEquals(5, result.size());
//        assertEquals("No files to extract", result.get(testResource("testZipFakeRar.rar")));
//        assertEquals("No files to extract", result.get(testResource("testEmpty.rar")));
//        assertTrue(result.get(testResource("testRarFakeZip.zip")).startsWith("zip error: "));
//        assertTrue(result.get(testResource("testEmpty.zip")).startsWith("zip error: "));
//        assertEquals(TransConst.PKG_TYPE_NOT_RECOGNIZED, result.get(testResource("testWrongType.txt")));
        System.out.println("Multiple Time = " + (System.currentTimeMillis() - t0));
    }

}
