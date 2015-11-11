/**
 * Copyright (c) 2015,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.worker
 * File Name:NameEditor.java
 * Date:2015-2-3 上午11:08:20
 * 
 */
package com.qxu.transpath.worker;

import java.io.File;

 /**
 * ClassName: NameEditor <br/>
 * Description: To rename file with a regular name by replace template. <br/>
 * Date: 2015-2-3 上午11:08:20 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class NameEditor {
    private static final String FULL_ROOT = "D:\\Book\\TFLib\\new\\full\\";
    private String rootDir;
    private String[][] replaceTemplates = {
            {"_2C ", ", "},
            {"_\\.", ")."},
            {"___", ") ("},
            {"__", " ("},
            {"_", " "}, 
            {"\\(digital\\)", "(Digital)"}, 
            {"\\(", " ("}, 
            {"  \\(", " ("}, 
            {" Vol. ", " v0"}, 
            {" Vol ", " v0"}};
        
    public NameEditor(String root) {
        this.rootDir=root;
    }
    
    public boolean renameFileByTemplate() {
        return renamePathFile(this.rootDir, this.replaceTemplates);
    }
    
    public boolean renameFileOnce(String[][] replaceOnce) {
        return renamePathFile(this.rootDir, replaceOnce);
    }
    
    public boolean renamePathFile(String pRoot, String[][] replaceList) {
        int totalFile = 0;
        int procFile = 0;
        File dirRoot = new File(pRoot);
        if (!dirRoot.isDirectory()) {
            System.out.println("Path name error.");
            return false;
        }
        
        for (File aFile : dirRoot.listFiles()) {
            if (aFile.isFile()) {
                String replacedName = aFile.getName();
                for (String[] repTempl: replaceList){
                    replacedName = replacedName.replaceAll(repTempl[0], repTempl[1]);
                }
                totalFile++;
                if (!aFile.getName().equals(replacedName)) {
                    procFile++;
                    if (!aFile.renameTo(new File(pRoot + replacedName))) {
                        System.out.println(aFile.getName() + " -e> " + replacedName);
                        return false;
                    }
                    System.out.println(aFile.getName() + " -> " + replacedName);
                }
            }
        }
        System.out.println(Integer.toString(procFile) + " of " + totalFile + " files renamed.");
        return true;
    }
    
    public boolean renamePathFilePushDate() {
        int totalFile = 0;
        int procFile = 0;
        String pRoot = this.rootDir;
        File dirRoot = new File(pRoot);
        if (!dirRoot.isDirectory()) {
            System.out.println("Path name error.");
            return false;
        }
        
        for (File aFile : dirRoot.listFiles()) {
            if (aFile.isFile()) {
                String oldName = aFile.getName();
                int idxFirstBlank = oldName.indexOf(' ');
                int idxLastPoint = oldName.lastIndexOf('.');
                String newName = oldName.substring(idxFirstBlank+1, idxLastPoint) + " ("
                        + oldName.substring(0, idxFirstBlank) + ")" + oldName.substring(idxLastPoint);
                totalFile++;
                if (!oldName.equals(newName)) {
                    procFile++;
                    if (!aFile.renameTo(new File(pRoot + newName))) {
                        System.out.println(aFile.getName() + " -e> " + newName);
                        return false;
                    }
                    System.out.println(oldName + " -> " + newName);
                }
            }
        }
        System.out.println(Integer.toString(procFile) + " of " + totalFile + " files renamed.");
        return true;
    }
    
    public boolean renamePathFileUnPushDate() {
        int totalFile = 0;
        int procFile = 0;
        String pRoot = this.rootDir;
        File dirRoot = new File(pRoot);
        if (!dirRoot.isDirectory()) {
            System.out.println("Path name error.");
            return false;
        }
        
        for (File aFile : dirRoot.listFiles()) {
            if (aFile.isFile()) {
                String oldName = aFile.getName();
                int idxFirstBlank = oldName.lastIndexOf('(');
                int idxLastPoint = oldName.lastIndexOf(')');
                String newName = oldName.substring(idxFirstBlank+1, idxLastPoint) + " "
                        + oldName.substring(0, idxFirstBlank-1) + oldName.substring(idxLastPoint+1);
                totalFile++;
                if (!oldName.equals(newName)) {
                    procFile++;
                    if (!aFile.renameTo(new File(pRoot + newName))) {
                        System.out.println(aFile.getName() + " -e> " + newName);
                        return false;
                    }
                    System.out.println(oldName + " -> " + newName);
                }
            }
        }
        System.out.println(Integer.toString(procFile) + " of " + totalFile + " files renamed.");
        return true;
    }
    
    public boolean reformatNumber() {
        int totalFile = 0;
        int procFile = 0;
        String pRoot = this.rootDir;
        File dirRoot = new File(pRoot);
        if (!dirRoot.isDirectory())
            return false;
        
        for (File aFile : dirRoot.listFiles()) {
            if (aFile.isFile()) {
                String replacedName = aFile.getName().replaceAll("#", " ");
                if (replacedName.indexOf(",") < 0) {
                    continue;
                }
                String rpl1s = ", ";
                String rpl1t = " " + String.format("%03d", Integer.parseInt(replacedName.substring(replacedName.indexOf('(')+1, replacedName.indexOf(')')).trim())) + " (";
                String rpl2s = replacedName.substring(replacedName.indexOf('(')-1, replacedName.indexOf(')')+1).replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)");
                String rpl2t = ")";
                System.out.println(":" + rpl1t + ":" + rpl2s + ":");
                replacedName = replacedName.replaceAll(rpl1s, rpl1t).replaceAll(rpl2s, rpl2t);
                totalFile++;
                if (!aFile.getName().equals(replacedName)) {
                    procFile++;
                    if (!aFile.renameTo(new File(pRoot + replacedName))) {
                        System.out.println(aFile.getName() + " -e> " + replacedName);
                        return false;
                    }
                    System.out.println(aFile.getName() + " -> " + replacedName);
                }
            }
        }
        System.out.println(Integer.toString(procFile) + " of " + totalFile + " files renamed.");
        return true;
    }
    
    public boolean moveFileToRoot() {
        return movePathFileToRoot(this.rootDir, this.rootDir);       
    }
    
    private boolean movePathFileToRoot(String pPath, String pRoot) {
        File dir = new File(pPath);
        if (!dir.isDirectory()) {
            System.out.println("Error: " + pPath);
            return false;
        }
        for (File aFile : dir.listFiles()) {
            if (aFile.isFile()) {
                //System.out.println(aFile.getName() + " -> " + aFile.getName());
                if (!aFile.renameTo(new File(pRoot + aFile.getName()))) {
                    System.out.println("Error: " + aFile.getPath());
                    return false;
                }
                
                continue;
            }
            if (aFile.isDirectory() && !movePathFileToRoot(aFile.getPath(), pRoot)) {
                    return false;
            }
        }
        return true;
    }

    public static void renamePushDate() {
        System.out.println("Result: " + new NameEditor(FULL_ROOT).renamePathFilePushDate() + ".");
    }

    public static void renameUnPushDate() {
        System.out.println("Result: " + new NameEditor(FULL_ROOT).renamePathFileUnPushDate() + ".");
    }

    public static void sampleMoveFileToRoot() {
        String root0 = "I:\\Book\\TFLib\\A2013\\B00";
        // String root = "D:\\temp\\qtest\\";
        for (int i = 32; i < 51; i++) {
            String root = root0 + i;
            System.out.println("Processing " + root + " ...");
            if (!root.endsWith("\\")) {
                root += "\\";
            }
            new File(root);
            System.out.println("Result: " + new NameEditor(root).moveFileToRoot());
        }
    }

    public static void sampleListFiles() {
        String root = "I:\\Book\\TFLib\\A2013\\B0001\\Antarctic Press\\Robotech\\";
        File a = new File(root);
        for (File b : a.listFiles()) {
            System.out.println("T: " + b.getPath() + "; F: " + b.getName() + "; P: " + b.getParent());
        }
    }

    public static void renameNormalize() {
        System.out.println("Result: " + new NameEditor(FULL_ROOT).renameFileByTemplate() + ".");
        System.out.println("Result: " + new NameEditor(FULL_ROOT).reformatNumber() + ".");
    }

    public static void renameSpecialReplace() {
        String[][] replaceOnce = { { "peterwatts-", "PeterWatts-" } };
        System.out.println("Result: " + new NameEditor(FULL_ROOT).renameFileOnce(replaceOnce) + ".");
    }

    public static void main(String[] args) {
        //renameNormalize();
        renameSpecialReplace();
        //renamePushDate();
        //renameUnPushDate();
    }
    
}

