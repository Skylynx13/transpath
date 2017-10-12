/**
 * Copyright (c) 2017,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.utils
 * File Name:CompressUtils.java
 * Date:2017年10月4日 下午6:28:15
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
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
import com.qxu.transpath.log.TransLog;

/**
 * ClassName: CompressUtils <br/>
 * Description: <br/>
 * Date: 2017年10月4日 下午6:28:15 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 *         Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class CompressUtils {
    private static final int BUFFEREDSIZE = 1024;

    public CompressUtils() {
    }

    @SuppressWarnings("unchecked")
    public synchronized void unzip(String zipFileName, String extPlace) throws Exception {
        ZipFile zipFile = null;
        try {
            (new File(extPlace)).mkdirs();
            File f = new File(zipFileName);
            zipFile = new ZipFile(zipFileName);
            if ((!f.exists()) && (f.length() <= 0)) {
                throw new Exception("File not found!");
            }
            String strPath, gbkPath, strtemp;
            File tempFile = new File(extPlace);
            strPath = tempFile.getAbsolutePath();
            Enumeration<ZipEntry> e = (Enumeration<ZipEntry>) zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry zipEnt = (ZipEntry) e.nextElement();
                gbkPath = zipEnt.getName();
                if (zipEnt.isDirectory()) {
                    strtemp = strPath + File.separator + gbkPath;
                    File dir = new File(strtemp);
                    dir.mkdirs();
                    continue;
                } else {
                    InputStream is = zipFile.getInputStream(zipEnt);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    gbkPath = zipEnt.getName();
                    strtemp = strPath + File.separator + gbkPath;

                    String strsubdir = gbkPath;
                    for (int i = 0; i < strsubdir.length(); i++) {
                        if (strsubdir.substring(i, i + 1).equalsIgnoreCase("/")) {
                            String temp = strPath + File.separator + strsubdir.substring(0, i);
                            File subdir = new File(temp);
                            if (!subdir.exists())
                                subdir.mkdir();
                        }
                    }
                    FileOutputStream fos = new FileOutputStream(strtemp);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    int c;
                    while ((c = bis.read()) != -1) {
                        bos.write((byte) c);
                    }
                    bos.close();
                    fos.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (null != zipFile) {
                zipFile.close();
            }
        }
    }

    public synchronized void zip(String inputFilename, String zipFilename) throws IOException {
        zip(new File(inputFilename), zipFilename);
    }

    public synchronized void zip(File inputFile, String zipFilename) throws IOException {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilename));
        try {
            zip(inputFile, out, "");
        } catch (IOException e) {
            throw e;
        } finally {
            out.close();
        }
    }

    private synchronized void zip(File inputFile, ZipOutputStream out, String base) throws IOException {
        if (inputFile.isDirectory()) {
            File[] inputFiles = inputFile.listFiles();
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < inputFiles.length; i++) {
                zip(inputFiles[i], out, base + inputFiles[i].getName());
            }
        } else {
            if (base.length() > 0) {
                out.putNextEntry(new ZipEntry(base));
            } else {
                out.putNextEntry(new ZipEntry(inputFile.getName()));
            }
            FileInputStream in = new FileInputStream(inputFile);
            try {
                int c;
                byte[] by = new byte[BUFFEREDSIZE];
                while ((c = in.read(by)) != -1) {
                    out.write(by, 0, c);
                }
            } catch (IOException e) {
                throw e;
            } finally {
                in.close();
            }
        }
    }

    public static synchronized void unrar(String rarFileName, String extPlace) {
        (new File(extPlace)).mkdirs();

        Archive arch = null;
        try {
            File rf = new File(rarFileName);
            arch = new Archive(rf);
        } catch (RarException | IOException e) {
            TransLog.getLogger().error("[" + rarFileName + "] package error: " + e.getMessage());
        }
        FileHeader fh = arch.nextFileHeader();
        while (fh != null) {
            FileOutputStream output = null;
            try {
                output = new FileOutputStream(new File(extPlace + fh.getFileNameString()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                arch.extractFile(fh, output);
            } catch (RarException e) {
                e.printStackTrace();
            }
            fh = arch.nextFileHeader();
        }
    }

    public static void main(String[] args) {
        System.out.println(File.separator);
        CompressUtils.unrar("D:/temp/test-rar.rar", "d:/temp/test01/");
    }
}
