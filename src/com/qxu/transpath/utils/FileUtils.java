/**
 * Copyright (c) 2015,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.utils
 * File Name:FileUtils.java
 * Date:2015-2-17 上午10:18:37
 * 
 */
package com.qxu.transpath.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

 /**
 * ClassName: FileUtils <br/>
 * Description: Utilities for file processing. <br/>
 * Date: 2015-2-17 上午10:18:37 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class FileUtils {
    public static boolean compareFileBytes(String pFileName1, String pFileName2) {
        if (pFileName1.equals(pFileName2)) {
            return true;
        }
        FileInputStream inStream1 = null;
        FileInputStream inStream2 = null;
        try {
            inStream1 = new FileInputStream(pFileName1);
            inStream2 = new FileInputStream(pFileName2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        try {
            if (inStream1.available() != inStream2.available()) {
                inStream1.close();
                inStream2.close();
                return false;
            }
            int inByte1 = 0;
            int inByte2 = 0;
            do {
                inByte1 = inStream1.read();
                inByte2 = inStream2.read();
            } while(inByte1 == inByte2 && inByte1 != -1);
            
            inStream1.close();
            inStream2.close();
            return (inByte1 == inByte2);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main (String[] args) {
        System.out.println("Files have a same path-name should be true: " + 
                FileUtils.compareFileBytes("resource/tst/ArrangerTest_task_000.txt", 
                        "resource/tst/ArrangerTest_task_000.txt"));
        System.out.println("Files have same contents should be true: " + 
                FileUtils.compareFileBytes("resource/tst/ArrangerTest_task_000.txt", 
                        "resource/tst/ArrangerTest_task_001.txt"));
        System.out.println("Files have different length should be false: " + 
                FileUtils.compareFileBytes("resource/tst/ArrangerTest_task_000.txt", 
                        "resource/tst/ArrangerTest_task_002.txt"));
        System.out.println("Files have different contents should be false: " + 
                FileUtils.compareFileBytes("resource/tst/ArrangerTest_task_000.txt", 
                        "resource/tst/ArrangerTest_task_003.txt"));
    }
}
