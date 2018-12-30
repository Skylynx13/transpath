package com.skylynx13.transpath.utils;

import java.io.*;
import java.net.URLConnection;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.CRC32;

import org.apache.commons.codec.binary.Hex;

import com.skylynx13.transpath.log.TransLog;

/**
 * ClassName: FileUtils
 * Description: Utilities for file processing.
 * Date: 2015-02-17 10:18:37
 */
public class FileUtils {
    private static final int READ_BUFFER_SIZE = 1024 * 128;
    private static final HashMap<String, String> mFileTypes = new HashMap<String, String>();

    static {
        // images
        mFileTypes.put("FFD8FF", "jpg");
        mFileTypes.put("89504E47", "png");
        mFileTypes.put("47494638", "gif");
        mFileTypes.put("49492A00", "tif");
        mFileTypes.put("424D", "bmp");
        // other
        mFileTypes.put("41433130", "dwg"); //CAD
        mFileTypes.put("38425053", "psd");
        mFileTypes.put("7B5C727466", "rtf");
        mFileTypes.put("3C3F786D6C", "xml");
        mFileTypes.put("68746D6C3E", "html");
        mFileTypes.put("44656C69766572792D646174653A", "eml");
        mFileTypes.put("D0CF11E0", "doc");
        mFileTypes.put("5374616E64617264204A", "mdb");
        mFileTypes.put("252150532D41646F6265", "ps");
        mFileTypes.put("255044462D312E", "pdf");
        mFileTypes.put("504B0304", "docx");
        mFileTypes.put("52617221", "rar");
        mFileTypes.put("57415645", "wav");
        mFileTypes.put("41564920", "avi");
        mFileTypes.put("2E524D46", "rm");
        mFileTypes.put("000001BA", "mpg");
        mFileTypes.put("000001B3", "mpg");
        mFileTypes.put("6D6F6F76", "mov");
        mFileTypes.put("3026B2758E66CF11", "asf");
        mFileTypes.put("4D546864", "mid");
        mFileTypes.put("1F8B08", "gz");
    }

    public static String getFileType(InputStream inputStream) {
        return mFileTypes.get(getFileHeader(inputStream));
    }

    private static String getFileHeader(InputStream inputStream) {
        String value = null;
        try {
            byte[] b = new byte[4];
            inputStream.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception ignored) {
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
        return value;
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (byte b : src) {
            hv = Integer.toHexString(b & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }

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

    static String getFileMimeType(String pFileName) {
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

    private static String getLastModifiedString(File pFile) {
        if (pFile == null) {
            return TransConst.EMPTY;
        }
        long time = pFile.lastModified();

        // An alternative way for a string in default format.
        // String fmtStr = new Timestamp(time).toString();

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(time));
    }

    public static int countMatch(String inFile, String inRegex) {
        int matchedLine = 0;
        Scanner in = null;
        try {
            in = new Scanner(new FileReader(inFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (Objects.requireNonNull(in).hasNext()) {
            String inLine = in.nextLine();
            if (inLine.matches(inRegex)) {
                TransLog.getLogger().info(inLine);
                matchedLine++;
            }
        }
        in.close();
        return matchedLine;
    }

    static String extractZipComment(String fileName) {
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
                return new String(buffer, i + 22, Math.min(commentLen, realLen));
            }
        }
        TransLog.getLogger().info("ERROR! ZIP comment NOT found!");
        return null;
    }

    private static String digest(String fileName, String algorithm) {
        byte[] rBytes = new byte[READ_BUFFER_SIZE];

        if (TransConst.CRC32.equals(algorithm)) {
            CRC32 crc32 = new CRC32();
            try {
                BufferedInputStream inBuff = new BufferedInputStream(new FileInputStream(fileName));
                int nLen;
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
        
        MessageDigest md;
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
        return TransProp.get(TransConst.LOC_LIST) + "StoreList_" + version + ".txt";
    }

    public static String pubNameOfVersion(String version) {
        return TransProp.get(TransConst.LOC_LIST) + "PubList_" + version + ".txt";
    }

    public static String linkNameOfVersion(String version) {
        return TransProp.get(TransConst.LOC_LIST) + "LinkList_" + version + ".txt";
    }

    public static String storeNameOfTag(String aTag, String bTag) {
        if (TransProp.get(TransConst.SYS_TYPE).equalsIgnoreCase(TransConst.SYS_WINDOWS)) {
            return TransProp.get(TransConst.LOC_LIST) + "B\\TFLib_" + aTag + "_" + bTag + ".txt";
        }
        return TransProp.get(TransConst.LOC_LIST) + "B/TFLib_" + aTag + "_" + bTag + ".txt";
    }
    
    public static String hitShelfList() {
        return TransProp.get(TransConst.LOC_CONFIG) + TransConst.LIST_HITSHELF;
    }
    
    public static String listFiles(String pathName) {
        File path = new File(pathName);
        if (!path.exists()) {
            return "Target N/A.";
        }
        if (path.isFile()) {
            return path.getName();
        }
        StringBuilder strbuf = new StringBuilder();
        for (File file : Objects.requireNonNull(path.listFiles())) {
            strbuf.append(file.getName()).append(TransConst.CRLN);
        }
        return strbuf.toString();
    }

    public static void main(String[] args) {
    }

    interface Checker {
        boolean checkType(String fileName);
        String[] checkCommand(String fileName);
        boolean checkOk(String result);
    }

    static class RarChecker implements Checker {
        @Override
        public boolean checkType(String fileName) {
            return isRar(fileName);
        }

        @Override
        public String[] checkCommand(String fileName) {
            return new String[]{"rar", "t", fileName};
        }

        @Override
        public boolean checkOk(String result) {
            return result.equalsIgnoreCase("All OK");
        }
    }

    static class ZipChecker implements Checker {
        @Override
        public boolean checkType(String fileName) {
            return isZip(fileName);
        }

        @Override
        public String[] checkCommand(String fileName) {
            return new String[]{"unzip", "-t", fileName};
        }

        @Override
        public boolean checkOk(String result) {
            return result.startsWith("No errors detected in compressed data of");
        }
    }

    private static Map<String, String> checkPackage(String fileName) {
        Map<String, String> errorInfos = new HashMap<>();
        List<Checker> checkers = new ArrayList<>();
        checkers.add(new RarChecker());
        checkers.add(new ZipChecker());

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.redirectErrorStream(true);

        if (isIgnorable(fileName)) {
            errorInfos.put(fileName, TransConst.PKG_IGNORE);
        }
        if (!isValid(fileName) || new File(fileName).isDirectory()) {
            errorInfos.put(fileName, TransConst.PKG_TYPE);
            return errorInfos;
        }

        String result;
        for (Checker checker : checkers) {
            processBuilder.command(checker.checkCommand(fileName));
//            TransLog.getLogger().info("Command line: " + processBuilder.command().stream().collect(Collectors.joining(" ")));
            TransLog.getLogger().info("Command line: " + String.join(" ", processBuilder.command()));
            result = processCommand(processBuilder);
            if (checker.checkOk(result)) {
                errorInfos.clear();
                if (!checker.checkType(fileName)) {
                    errorInfos.put(fileName, "Type mismatch.");
                }
                return errorInfos;
            }
//            errorInfos.put(fileName, result);
        }
        errorInfos.put(fileName, "All checkers failed.");
        return errorInfos;
    }

    private static String processCommand(ProcessBuilder processBuilder) {
        String lastLine = "";

        try {
            Process process = processBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lastLine = line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        TransLog.getLogger().info("Package Status: " + lastLine);
        return lastLine;
    }

    public static String checkPackages(List<String> fileNames) {
        Map<String, String> errorInfos = new HashMap<>();
        for (String fileName : fileNames) {
            errorInfos.putAll(checkPackage(fileName));
        }
        StringBuilder errInfoStr = new StringBuilder();
        errInfoStr.append(errorInfos.size()).append(" error(s) found.").append(TransConst.CRLN);
        for (String key : errorInfos.keySet()) {
            errInfoStr.append(key).append(" : ").append(errorInfos.get(key)).append(TransConst.CRLN);
        }
        return errInfoStr.toString();
    }

    private static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    private static boolean isValid(String fileName) {
        return isRar(fileName) || isZip(fileName);
    }

    private static boolean isRar(String fileName) {
        final String[] ARRAY_SUFFIX_RAR = {"rar", "cbr"};

        return Arrays.asList(ARRAY_SUFFIX_RAR).contains(getSuffix(fileName));
    }

    private static boolean isZip(String fileName) {
        final String[] ARRAY_SUFFIX_ZIP = {"zip", "cbz"};

        return Arrays.asList(ARRAY_SUFFIX_ZIP).contains(getSuffix(fileName));
    }

    static boolean isIgnorable(String fileName) {
        final String[] ARRAY_SUFFIX_IGNORABLE = {"pdf", "txt", "epub", "mobi"};

        return Arrays.asList(ARRAY_SUFFIX_IGNORABLE).contains(getSuffix(fileName));
    }
}
