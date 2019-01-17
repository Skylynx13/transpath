package com.skylynx13.transpath.task;

import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.utils.TransConst;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
        List<String> testFiles = new ArrayList<>();
        testFiles.add(testResource("testFiles.rar"));
        testFiles.add(testResource("testZipFakeRar.rar"));
        testFiles.add(testResource("testEmpty.rar"));
        testFiles.add(testResource("testFiles.cbr"));
        testFiles.add(testResource("testFiles.zip"));
        testFiles.add(testResource("testRarFakeZip.zip"));
        testFiles.add(testResource("testEmpty.zip"));
        testFiles.add(testResource("testFiles.cbz"));
        testFiles.add(testResource("testWrongType.txt"));
        String result = packageChecker.check(testFiles).toString();
        System.out.println(result);
//        assertEquals(5, result.size());
//        assertEquals("No files to extract", result.get(testResource("testZipFakeRar.rar")));
//        assertEquals("No files to extract", result.get(testResource("testEmpty.rar")));
//        assertTrue(result.get(testResource("testRarFakeZip.zip")).startsWith("zip error: "));
//        assertTrue(result.get(testResource("testEmpty.zip")).startsWith("zip error: "));
//        assertEquals(TransConst.PKG_TYPE, result.get(testResource("testWrongType.txt")));
        System.out.println("Multiple Time = " + (System.currentTimeMillis() - t0));
    }

}
