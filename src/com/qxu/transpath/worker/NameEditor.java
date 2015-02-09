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
    private String[][] replaceOnce = {{"Timewalker ","Timewalker 0"}};
    
    public NameEditor(String root) {
        this.rootDir=root;
    }
    
    public boolean replaceByTemplate() {
        return replaceDir(this.rootDir, this.replaceTemplates);
    }
    
    public boolean replaceOnce() {
        return replaceDir(this.rootDir, this.replaceOnce);
    }
    
    public boolean replaceDir(String pRoot, String[][] replaceList) {
        File dirRoot = new File(pRoot);
        if (!dirRoot.isDirectory())
            return false;
        
        for (File aFile: dirRoot.listFiles()) {
            if (aFile.isFile()) {
                String replacedName = aFile.getName();
                for (String[] repTempl: replaceList){
                    replacedName = replacedName.replaceAll(repTempl[0], repTempl[1]);
                }
                System.out.println(aFile.getName() + " -> " + replacedName);
                aFile.renameTo(new File(pRoot + replacedName));
            }
        }
        return true;
    }
    public static void main(String[] args) {
//        String root = "D:\\temp\\qtest\\";
        String root = "D:\\Book\\TFLib\\new\\full\\";
        System.out.println("Result: " + new NameEditor(root).replaceByTemplate() + ".");
        //System.out.println("Result: " + new NameEditor(root).replaceOnce() + ".");
    }

}

