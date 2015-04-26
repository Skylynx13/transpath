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
    private String rootDir;
    private String[][] replaceTemplates = {
            {"_", " "}, 
            {"\\(digital\\)", "(Digital)"}, 
            {"\\(", " ("}, 
            {"  \\(", " ("}};
        
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
        File dirRoot = new File(pRoot);
        if (!dirRoot.isDirectory())
            return false;
        
        for (File aFile : dirRoot.listFiles()) {
            if (aFile.isFile()) {
                String replacedName = aFile.getName();
                for (String[] repTempl: replaceList){
                    replacedName = replacedName.replaceAll(repTempl[0], repTempl[1]);
                }
                if (!aFile.getName().equals(replacedName)) {
                    System.out.println(aFile.getName() + " -> " + replacedName);
                }
                if (!aFile.renameTo(new File(pRoot + replacedName))) {
                    System.out.println(aFile.getName() + " -e> " + replacedName);
                    return false;
                }
            }
        }
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

    public static void main(String[] args) {
//        String root = "I:\\Book\\TFLib\\A2013\\B0001\\Antarctic Press\\Robotech\\";
//        File a = new File(root);
//        for (File b : a.listFiles()) {
//            System.out.println("T: " + b.getPath() + "; F: " + b.getName() + "; P: " + b.getParent());
//        }
        
//        String root0 = "I:\\Book\\TFLib\\A2013\\B00";
////        String root = "D:\\temp\\qtest\\";
//        for (int i = 32; i < 51; i++) {
//            String root = root0 + i;
//            System.out.println("Processing " + root + " ...");
//        if (!root.endsWith("\\")) {
//            root += "\\";
//        }
//        File a = new File(root);
//        System.out.println("Result: " + new NameEditor(root).moveFileToRoot());
//        }
        
        String root = "D:\\Book\\TFLib\\new\\full\\";
        System.out.println("Result: " + new NameEditor(root).renameFileByTemplate() + ".");
        
//      String root = "D:\\Book\\TFLib\\new\\full\\";
//      String[][] replaceOnce = {{"Eternal Warriors ","Eternal Warriors 0"}};
//      System.out.println("Result: " + new NameEditor(root).renameFileOnce(replaceOnce) + ".");
    }

}

