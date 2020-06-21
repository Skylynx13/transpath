package com.skylynx13.transpath.utils;

import org.apache.commons.codec.binary.Hex;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.zip.CRC32;

/**
 * ClassName: FileUtils
 * Description: Utilities for file processing.
 * Date: 2015-02-17 10:18:37
 * @author skylynx
 */
public class FileUtils {
    private static final int READ_BUFFER_SIZE = 1024 * 128;

    public static void clearFile(String pFileName) {
        try {
            FileOutputStream file = new FileOutputStream(pFileName);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean copyFileBytes(String srcFileName, String tarFileName) {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        boolean result;
        try {
            inBuff = new BufferedInputStream(new FileInputStream(srcFileName));
            outBuff = new BufferedOutputStream(new FileOutputStream(tarFileName));

            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
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
                result = false;
            }
        }
        return result;
    }

    static boolean appendFileBytes(String srcFileName, String tarFileName) {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        boolean result;
        try {
            inBuff = new BufferedInputStream(new FileInputStream(srcFileName));
            outBuff = new BufferedOutputStream(new FileOutputStream(tarFileName, true));

            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            outBuff.flush();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
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
                result = false;
            }
        }
        return result;
    }

    static boolean catFileBytes(String srcFileName1, String srcFileName2, String tarFileName) {
        BufferedInputStream inBuff1 = null;
        BufferedInputStream inBuff2 = null;
        BufferedOutputStream outBuff = null;
        boolean result;
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
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
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
                result = false;
            }
        }
        return result;
    }

    public static boolean compareFileBytes(String pFileName1, String pFileName2) {
        if (pFileName1.equals(pFileName2)) {
            return true;
        }
        FileInputStream inStream1;
        FileInputStream inStream2;
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
            int inByte1;
            int inByte2;
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

    static long getFileSize(File pFile) {
        if (pFile == null) {
            return 0;
        }
        if (pFile.isFile()) {
            return pFile.length();
        }
        long size = 0;
        for (File sub : Objects.requireNonNull(pFile.listFiles())) {
            size += getFileSize(sub);
        }
        return size;
    }

    public static long getFileCount(String pFileName) {
        if (pFileName == null || pFileName.isEmpty()) {
            return 0;
        }
        return getFileCount(new File(pFileName));
    }

    private static long getFileCount(File pFile) {
        if (pFile == null) {
            return 0;
        }
        if (pFile.isFile()) {
            return 1;
        }
        long count = 0;
        for (File sub : Objects.requireNonNull(pFile.listFiles())) {
            count += getFileCount(sub);
        }
        return count;
    }

    private static String digest(File pFile, String algorithm) {
        byte[] rBytes = new byte[READ_BUFFER_SIZE];

        if (TransConst.CRC32.equals(algorithm)) {
            CRC32 crc32 = new CRC32();
            try {
                BufferedInputStream inBuff = new BufferedInputStream(new FileInputStream(pFile));
                int nLen;
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
        
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(algorithm);

            DigestInputStream dInput = new DigestInputStream(new FileInputStream(pFile), md);
            //noinspection StatementWithEmptyBody
            while (dInput.read(rBytes) != -1) {
            }
            dInput.close();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return new String(Hex.encodeHex(md.digest())).toUpperCase();
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

    public static String regulateRelativePath(String pRoot, File pFile) {
        String aRoot = pRoot;
        if (isWindows()) {
            aRoot = pRoot.replaceAll(TransConst.BACK_SLASH_4, TransConst.BACK_SLASH_8);
        }
        return toStandardPath(pFile.getParent().replaceAll(aRoot, TransConst.SLASH) + TransConst.SLASH);
    }

    public static String regulateSysPath(String path) {
        if (isWindows()) {
            return toWindowsPath(path);
        }
        return toStandardPath(path);
    }

    public static boolean isWindows() {
        return TransProp.get(TransConst.SYS_TYPE).equalsIgnoreCase(TransConst.SYS_WINDOWS);
    }

    private static String toWindowsPath(String path) {
        return path.replaceAll(TransConst.SLASH, TransConst.BACK_SLASH_4);
    }

    public static String toStandardPath(String path) {
        return path.replaceAll(TransConst.BACK_SLASH_4, TransConst.SLASH);
    }
}
