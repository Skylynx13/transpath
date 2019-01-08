package com.skylynx13.transpath.task;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * ClassName: PackageCheckerTest
 * Description: Task checker test
 * Date: 2017-09-30 13:47:47
 */
public class PackageCheckerTest {
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
}
