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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    public static void clearFile(String pFileName) {
        try {
            new FileOutputStream(pFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean copyFileBytes(String srcFileName, String tarFileName) {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            inBuff = new BufferedInputStream(new FileInputStream(srcFileName));
            outBuff = new BufferedOutputStream(new FileOutputStream(tarFileName));
            
            byte[] b = new byte[1024*5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            try {
                if (inBuff != null) {
                    inBuff.close();
                } 
                if (outBuff != null) {
                    outBuff.close();
                } 
            }
            catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    
    public static boolean appendFileBytes(String srcFileName, String tarFileName) {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            inBuff = new BufferedInputStream(new FileInputStream(srcFileName));
            outBuff = new BufferedOutputStream(new FileOutputStream(tarFileName, true));
            
            byte[] b = new byte[1024*5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            try {
                if (inBuff != null) {
                    inBuff.close();
                } 
                if (outBuff != null) {
                    outBuff.close();
                } 
            }
            catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static boolean catFileBytes(String srcFileName1, String srcFileName2, String tarFileName) {
        BufferedInputStream inBuff1 = null;
        BufferedInputStream inBuff2 = null;
        BufferedOutputStream outBuff = null;
        try {
            inBuff1 = new BufferedInputStream(new FileInputStream(srcFileName1));
            inBuff2 = new BufferedInputStream(new FileInputStream(srcFileName2));
            outBuff = new BufferedOutputStream(new FileOutputStream(tarFileName));
            
            byte[] b = new byte[1024*5];
            int len;
            while ((len = inBuff1.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            while ((len = inBuff2.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            try {
                if (inBuff1 != null) {
                    inBuff1.close();
                } 
                if (inBuff2 != null) {
                    inBuff2.close();
                } 
                if (outBuff != null) {
                    outBuff.close();
                } 
            }
            catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

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

    public static long getFileSize(String pFileName) {
        if (pFileName.equals(TranspathConstants.EMPTY)) {
            return 0;
        }
        return getFileSize(new File(pFileName));
    }
    
    public static long getFileSize(File pFile) {
        if (pFile == null) {
            return 0;
        }
        if (pFile.isFile()) {
            return pFile.length();
        }
        long size = 0;
        for (File sub : pFile.listFiles()) {
            size += getFileSize(sub);
        }
        return size;
    }

    public static String getFileMimeType(String pFileName) {
        if (pFileName.equals(TranspathConstants.EMPTY)) {
            return TranspathConstants.EMPTY;
        }
        return URLConnection.getFileNameMap().getContentTypeFor(pFileName); 
    }
    
    public static String getLastModifiedString(String pFileName) {
        if (pFileName.equals(TranspathConstants.EMPTY)) {
            return TranspathConstants.EMPTY;
        }
        long time = new File(pFileName).lastModified();

        //An alternative way for a string in default format.
        //String fmtStr = new Timestamp(time).toString();
        String fmtStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(time));
        
        return fmtStr;
    }
    
    public static void main (String[] args) {
        System.out.println(FileUtils.getFileSize(new File("resource/tst/ArrangerTest_task_000.txt")));
        System.out.println(FileUtils.getFileSize(new File("resource/tst/ArrangerTest_task_002.txt")));
        System.out.println(FileUtils.getFileSize(new File("resource/tst")));

    }
}
