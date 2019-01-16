package com.skylynx13.transpath.utils;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class CompressUtilsTest {
//    String testRes = "src/test/resources/";
    String testRes = "D:\\workspace\\transpath\\src\\test\\resources\\";
    File rarFile = new File(testRes + "testFiles.rar");
    File zipFile = new File(testRes + "testFiles.zip");
    CompressUtils compressor = new CompressUtils();

    @Test
    public void testRarTest() {
        compressor.testRar(rarFile);
    }

}
