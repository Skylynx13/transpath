package com.qxu.transpath.utest;

import static org.junit.Assert.*;

import org.junit.Test;

import com.qxu.transpath.path.FilePath;

public class FilePathTest {

	@Test
	public void test_getFullPath() {
		FilePath fp = new FilePath();
		fp.setArchiveName("A2013");
		fp.setBlockName("B011901");
		assertEquals("A2013/B011901", fp.getFullPath());
	}

}
