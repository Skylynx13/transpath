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
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLConnection;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.zip.CRC32;

import org.apache.commons.codec.binary.Hex;

/**
 * ClassName: FileUtils <br/>
 * Description: Utilities for file processing. <br/>
 * Date: 2015-2-17 上午10:18:37 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 *         Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class FileUtils {
    private static final int READ_BUFFER_SIZE = 1024 * 128;

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

            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (inBuff != null) {
                    inBuff.close();
                }
                if (outBuff != null) {
                    outBuff.close();
                }
            } catch (IOException e) {
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

            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (inBuff != null) {
                    inBuff.close();
                }
                if (outBuff != null) {
                    outBuff.close();
                }
            } catch (IOException e) {
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

            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff1.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            while ((len = inBuff2.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
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
            } catch (IOException e) {
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
            } while (inByte1 == inByte2 && inByte1 != -1);

            inStream1.close();
            inStream2.close();
            return (inByte1 == inByte2);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static long getFileSize(String pFileName) {
        if (pFileName == null || pFileName.isEmpty()) {
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
        if (pFileName == null || pFileName.isEmpty()) {
            return TransConst.EMPTY;
        }
        return URLConnection.getFileNameMap().getContentTypeFor(pFileName);
    }

    public static String getFileMimeType(File pFile) {
        if (pFile == null) {
            return TransConst.EMPTY;
        }
        return getFileMimeType(pFile.getName());
    }

    public static String getLastModifiedString(String pFileName) {
        if (pFileName == null || pFileName.isEmpty()) {
            return TransConst.EMPTY;
        }
        return getLastModifiedString(new File(pFileName));
    }

    public static String getLastModifiedString(File pFile) {
        if (pFile == null) {
            return TransConst.EMPTY;
        }
        long time = pFile.lastModified();

        // An alternative way for a string in default format.
        // String fmtStr = new Timestamp(time).toString();
        String fmtStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(time));

        return fmtStr;
    }

    public static int countMatch(String inFile, String inRegex) {
        int matchedLine = 0;
        Scanner in = null;
        try {
            in = new Scanner(new FileReader(inFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (in.hasNext()) {
            String inLine = in.nextLine();
            if (inLine.matches(inRegex)) {
                TransLog.getLogger().info(inLine);
                matchedLine++;
            }
        }
        if (in != null) {
            in.close();
        }
        return matchedLine;
    }

    public static String extractZipComment(String fileName) {
        String retStr = null;
        try {
            File file = new File(fileName);
            int fileLen = (int) file.length();
            FileInputStream in = new FileInputStream(file);
            /*
             * The whole ZIP comment (including the magic byte sequence) MUST
             * fit in the buffer otherwise, the comment will not be recognized
             * correctly
             * 
             * You can safely increase the buffer size if you like
             */
            byte[] buffer = new byte[Math.min(fileLen, 8192)];
            int len;
            in.skip(fileLen - buffer.length);
            if ((len = in.read(buffer)) > 0) {
                retStr = getZipCommentFromBuffer(buffer, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retStr;
    }

    private static String getZipCommentFromBuffer(byte[] buffer, int len) {
        byte[] magicDirEnd = { 0x50, 0x4b, 0x05, 0x06 };
        int buffLen = Math.min(buffer.length, len); // Check the buffer from the
                                                    // end
        for (int i = buffLen - magicDirEnd.length - 22; i >= 0; i--) {
            boolean isMagicStart = true;
            for (int k = 0; k < magicDirEnd.length; k++) {
                if (buffer[i + k] != magicDirEnd[k]) {
                    isMagicStart = false;
                    break;
                }
            }
            if (isMagicStart) { // Magic Start found!
                int commentLen = buffer[i + 20] + buffer[i + 22] * 256;
                int realLen = buffLen - i - 22;
                TransLog.getLogger().info("ZIP comment found at buffer position " + (i + 22) + " with len=" + commentLen
                        + ", good!");
                if (commentLen != realLen) {
                    TransLog.getLogger().info("WARNING! ZIP comment size mismatch: directory says len is " + commentLen
                            + ", but file ends after " + realLen + " bytes!");
                }
                String comment = new String(buffer, i + 22, Math.min(commentLen, realLen));
                return comment;
            }
        }
        TransLog.getLogger().info("ERROR! ZIP comment NOT found!");
        return null;
    }

    public static String digest(String fileName, String algorithm) {
        byte[] rBytes = new byte[READ_BUFFER_SIZE];

        if (TransConst.CRC32.equals(algorithm)) {
            CRC32 crc32 = new CRC32();
            try {
                BufferedInputStream inBuff = new BufferedInputStream(new FileInputStream(fileName));
                int nLen = 0;
                while ((nLen = inBuff.read(rBytes)) != -1) {
                    crc32.update(rBytes, 0, nLen);
                }
                inBuff.close();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
            //return Long.toHexString(crc32.getValue()).toUpperCase();
            return String.format(TransConst.FORMAT_HEX_08, crc32.getValue()).toUpperCase();
        }
        
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);

            DigestInputStream dInput = new DigestInputStream(new FileInputStream(fileName), md);
            while (dInput.read(rBytes) != -1) {
            }
            dInput.close();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        //return new BigInteger(1, md.digest()).toString(16).toUpperCase();
        //return String.format("%040x", new BigInteger(1, md.digest())).toUpperCase();
        return new String(Hex.encodeHex(md.digest())).toUpperCase();
    }

    public static String digest(File pFile, String algorithm) {
        byte[] rBytes = new byte[READ_BUFFER_SIZE];

        if (TransConst.CRC32.equals(algorithm)) {
            CRC32 crc32 = new CRC32();
            try {
                BufferedInputStream inBuff = new BufferedInputStream(new FileInputStream(pFile));
                int nLen = 0;
                while ((nLen = inBuff.read(rBytes)) != -1) {
                    crc32.update(rBytes, 0, nLen);
                }
                inBuff.close();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
            return String.format(TransConst.FORMAT_HEX_08, crc32.getValue()).toUpperCase();
        }
        
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);

            DigestInputStream dInput = new DigestInputStream(new FileInputStream(pFile), md);
            while (dInput.read(rBytes) != -1) {
            }
            dInput.close();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return new String(Hex.encodeHex(md.digest())).toUpperCase();
    }

    public static String digestMd5(String fileName) {
        return digest(fileName, TransConst.MD5);
    }

    public static String digestSha(String fileName) {
        return digest(fileName, TransConst.SHA);
    }

    public static String digestCrc32(String fileName) {
        return digest(fileName, TransConst.CRC32);
    }

    public static String digestMd5(File pFile) {
        return digest(pFile, TransConst.MD5);
    }

    public static String digestSha(File pFile) {
        return digest(pFile, TransConst.SHA);
    }

    public static String digestCrc32(File pFile) {
        return digest(pFile, TransConst.CRC32);
    }

    public static boolean verify(String fileName, String checkCode, String algorithm) {
        return checkCode.equals(digest(fileName, algorithm));
    }
    
    public static String regulateSlash(String pStr) {
        String aStr = pStr.replaceAll(TransConst.BACK_SLASH_4, TransConst.SLASH);
        if (aStr.endsWith(TransConst.SLASH)) {
            aStr = aStr.substring(0, aStr.length() - 1);
        }
        return aStr;
    }

    public static void printMap(String pName, HashMap<Integer, Integer> pMap) {
        try {
            PrintWriter out = new PrintWriter(pName);
            out.println(pMap.toString());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String storeNameOfVersion(String version) {
        return TransProp.get("SL_HOME") + "StoreList_" + version + ".txt";
    }

    public static String pubNameOfVersion(String version) {
        return TransProp.get("SL_HOME") + "PubList_" + version + ".txt";
    }

    public static String linkNameOfVersion(String version) {
        return TransProp.get("SL_HOME") + "LinkList_" + version + ".txt";
    }

    public static String storeNameOfTag(String aTag, String bTag) {
        return TransProp.get("SL_HOME") + "B\\TFLib_" + aTag + "_" + bTag + ".txt";
    }
    
    public static String hitShelfList() {
        return TransProp.get("CONFIG_PATH") + TransConst.TP_HITSHELF;
    }
    
    public static String listFiles(String pathName) {
        File path = new File(pathName);
        if (!path.exists()) {
            return "Target N/A.";
        }
        if (path.isFile()) {
            return path.getName();
        }
        StringBuffer strbuf = new StringBuffer();
        for (File file : path.listFiles()) {
            strbuf.append(file.getName()).append(TransConst.CRLN);
        }
        return strbuf.toString();
    }

    public static void main(String[] args) {
            long t0 = System.currentTimeMillis();
            TransLog.getLogger().info(listFiles("D:\\Quest\\_IC\\doc\\design"));
            TransLog.getLogger().info("Time : " + (System.currentTimeMillis()-t0) + " ms.");
        }
}
