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

import com.qxu.transpath.utils.TransLog;

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
    boolean CHANGE = true;
    private static final String FULL_ROOT = "D:\\Book\\TFLib\\new\\full\\";
    //private static final String FULL_ROOT = "G:\\Book\\TFLib\\A2013\\B0015\\";
    private String rootDir;
    private String[][] replaceTemplates = {
            {"\\+", " "},
            {"%28", "("},
            {"%29", ")"},
            {"%23", "#"},
            {"_2C ", ", "},
            {"_\\.", ")."},
            {"___", ") ("},
            {"__", " ("},
            {"_", " "}, 
            {"\\(digital\\)", "(Digital)"}, 
            {"\\(", " ("}, 
            {"  \\(", " ("}, 
            {" Vol\\. ", " v0"}, 
            {" Vol\\.", " v0"}, 
            {" Vol ", " v0"},
            {" v00", " v0"},
            { "#", "" }, 
            { " v1 ", " v01 " }, 
            { " v2 ", " v02 " }, 
            { " v3 ", " v03 " }, 
            { " v4 ", " v04 " }, 
            { " v5 ", " v05 " }, 
            { " v6 ", " v06 " }, 
//            { " v01 ", " v01 - " }, 
//            { " v02 ", " v02 - " }, 
//            { " v03 ", " v03 - " }, 
//            { " v04 ", " v04 - " }, 
//            { " v05 ", " v05 - " }, 
//            { " v06 ", " v06 - " }, 
//            { " v01 - - ", " v01 - " }, 
//            { " v02 - - ", " v02 - " }, 
//            { " v03 - - ", " v03 - " }, 
//            { " v04 - - ", " v04 - " }, 
//            { " v05 - - ", " v05 - " }, 
//            { " v06 - - ", " v06 - " }, 
            { " Of ", " of " }, 
            { " of The", " of the" },
            { " From ", " from " }, 
            { " And ", " and " }, 
            { " VS ", " vs " },
            { " Vs ", " vs " },
            { " Vs\\. ", " vs " },
            { " vs\\. ", " vs " },
            { "\\.zip", ".cbz" }, 
            { "\\.rar", ".cbr" }, 
            { "\\.CBZ", ".cbz" }, 
            { "\\.CBR", ".cbr" }, 
            { "02 of 02 covers", "(2 covers)" },
            { "03 of 03 covers", "(3 covers)" },
            { "04 of 04 covers", "(4 covers)" },
            { "05 of 05 covers", "(5 covers)" },
            { "06 of 06 covers", "(6 covers)" },
            { "07 of 07 covers", "(7 covers)" },
            { "08 of 08 covers", "(8 covers)" },
            { "09 of 09 covers", "(9 covers)" },
            { " 01\\.cbr", " 001.cbr" },
            { " 02\\.cbr", " 002.cbr" },
            { " 03\\.cbr", " 003.cbr" },
            { " 04\\.cbr", " 004.cbr" }, 
            { " 05\\.cbr", " 005.cbr" }, 
            { " 06\\.cbr", " 006.cbr" }, 
            { " 01\\.cbz", " 001.cbz" }, 
            { " 02\\.cbz", " 002.cbz" }, 
            { " 03\\.cbz", " 003.cbz" }, 
            { " 04\\.cbz", " 004.cbz" }, 
            { " 05\\.cbz", " 005.cbz" }, 
            { " 06\\.cbz", " 006.cbz" },
            { "([-+'A-Za-z0-9 ]+) (\\d{2}) ([-A-Za-z0-9 \\(\\)\\.]+)", "$1 0$2 $3" }
        };
        
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
            TransLog.getLogger().info("Path name error.");
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
                    if (CHANGE && !aFile.renameTo(new File(pRoot + replacedName))) {
                        TransLog.getLogger().info(aFile.getName() + " -e> " + replacedName);
                        return false;
                    }
                    TransLog.getLogger().info(aFile.getName() + " -> " + replacedName);
                }
            }
        }
        TransLog.getLogger().info(Integer.toString(procFile) + " of " + totalFile + " files renamed.");
        return true;
    }
    
    @SuppressWarnings("unused")
    private static void renameInitUpper() {
        int totalFile = 0;
        int procFile = 0;
        String pRoot = NameEditor.FULL_ROOT;
        File dirRoot = new File(pRoot);
        if (!dirRoot.isDirectory()) {
            TransLog.getLogger().info("Path name error.");
            TransLog.getLogger().info("Result: False.");
        }
        
        for (File aFile : dirRoot.listFiles()) {
            if (aFile.isFile()) {
                char[] replacedChars = aFile.getName().toLowerCase().toCharArray();
                for (int iChar = 0; iChar < replacedChars.length; iChar++) {
                    if (((iChar == 0) || (replacedChars[iChar-1] == 32))
                            &&((96<replacedChars[iChar]) && (replacedChars[iChar]<123))) {
                        replacedChars[iChar]-=32;
                    }
                }
                String replacedName = String.valueOf(replacedChars);
                totalFile++;
                if (!aFile.getName().equals(replacedName)) {
                    procFile++;
                    if (!aFile.renameTo(new File(pRoot + replacedName))) {
                        TransLog.getLogger().info(aFile.getName() + " -e> " + replacedName);
                        TransLog.getLogger().info("Result: False.");
                    }
                    TransLog.getLogger().info(aFile.getName() + " -> " + replacedName);
                }
            }
        }
        TransLog.getLogger().info(Integer.toString(procFile) + " of " + totalFile + " files renamed.");
        TransLog.getLogger().info("Result: True.");
    }
    
    public boolean renamePathFileSwapDate() {
        int totalFile = 0;
        int procFile = 0;
        String pRoot = this.rootDir;
        File dirRoot = new File(pRoot);
        if (!dirRoot.isDirectory()) {
            TransLog.getLogger().info("Path name error.");
            return false;
        }
        
        for (File aFile : dirRoot.listFiles()) {
            if (aFile.isFile()) {
                String oldName = aFile.getName();
                String[] oldSubStr = oldName.split(" ");
                int slen = oldSubStr.length;
                String newName = oldSubStr[0] + " " + oldSubStr[2] + " " + oldSubStr[1];
                for (int i=3; i<slen; i++) {
                    newName += " " + oldSubStr[i];
                }
                totalFile++;
                if (!oldName.equals(newName)) {
                    procFile++;
                    if (!aFile.renameTo(new File(pRoot + newName))) {
                        TransLog.getLogger().info(aFile.getName() + " -e> " + newName);
                        return false;
                    }
                    TransLog.getLogger().info(oldName + " -> " + newName);
                }
            }
        }
        TransLog.getLogger().info(Integer.toString(procFile) + " of " + totalFile + " files renamed.");
        return true;
    }
    
    public boolean renamePathFilePushDate() {
        int totalFile = 0;
        int procFile = 0;
        String pRoot = this.rootDir;
        File dirRoot = new File(pRoot);
        if (!dirRoot.isDirectory()) {
            TransLog.getLogger().info("Path name error.");
            return false;
        }
        
        for (File aFile : dirRoot.listFiles()) {
            if (aFile.isFile()) {
                String oldName = aFile.getName();
                int idxFirstBlank = oldName.indexOf(' ');
                int idxLastPoint = oldName.lastIndexOf('.');
                int idxFirstLeftBracket = oldName.indexOf('(');
                int idxInsert = idxLastPoint;
                if (idxFirstLeftBracket > 1) {
                    idxInsert = idxFirstLeftBracket - 1;
                }
                String newName = oldName.substring(idxFirstBlank+1, idxInsert) + " ("
                        + oldName.substring(0, idxFirstBlank) + ")" + oldName.substring(idxInsert);
                totalFile++;
                if (!oldName.equals(newName)) {
                    procFile++;
                    if (!aFile.renameTo(new File(pRoot + newName))) {
                        TransLog.getLogger().info(aFile.getName() + " -e> " + newName);
                        return false;
                    }
                    TransLog.getLogger().info(oldName + " -> " + newName);
                }
            }
        }
        TransLog.getLogger().info(Integer.toString(procFile) + " of " + totalFile + " files renamed.");
        return true;
    }
    
    private boolean renameCutHead() {
        int totalFile = 0;
        int procFile = 0;
        String pRoot = this.rootDir;
        File dirRoot = new File(pRoot);
        if (!dirRoot.isDirectory()) {
            TransLog.getLogger().info("Path name error.");
            return false;
        }
        
        for (File aFile : dirRoot.listFiles()) {
            if (aFile.isFile()) {
                String oldName = aFile.getName();
                int idxFirstBlank = oldName.indexOf(' ');
                String newName = oldName.substring(idxFirstBlank+1);
                totalFile++;
                if (!oldName.equals(newName)) {
                    procFile++;
                    if (!aFile.renameTo(new File(pRoot + newName))) {
                        TransLog.getLogger().info(aFile.getName() + " -e> " + newName);
                        return false;
                    }
                    TransLog.getLogger().info(oldName + " -> " + newName);
                }
            }
        }
        TransLog.getLogger().info(Integer.toString(procFile) + " of " + totalFile + " files renamed.");
        return true;
    }

    public boolean renamePathFileUnPushDate() {
        int totalFile = 0;
        int procFile = 0;
        String pRoot = this.rootDir;
        File dirRoot = new File(pRoot);
        if (!dirRoot.isDirectory()) {
            TransLog.getLogger().info("Path name error.");
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
                        TransLog.getLogger().info(aFile.getName() + " -e> " + newName);
                        return false;
                    }
                    TransLog.getLogger().info(oldName + " -> " + newName);
                }
            }
        }
        TransLog.getLogger().info(Integer.toString(procFile) + " of " + totalFile + " files renamed.");
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
                TransLog.getLogger().info(":" + rpl1t + ":" + rpl2s + ":");
                replacedName = replacedName.replaceAll(rpl1s, rpl1t).replaceAll(rpl2s, rpl2t);
                totalFile++;
                if (!aFile.getName().equals(replacedName)) {
                    procFile++;
                    if (!aFile.renameTo(new File(pRoot + replacedName))) {
                        TransLog.getLogger().info(aFile.getName() + " -e> " + replacedName);
                        return false;
                    }
                    TransLog.getLogger().info(aFile.getName() + " -> " + replacedName);
                }
            }
        }
        TransLog.getLogger().info(Integer.toString(procFile) + " of " + totalFile + " files renamed.");
        return true;
    }
    
    public boolean moveFileToRoot() {
        return movePathFileToRoot(this.rootDir, this.rootDir);       
    }
    
    private boolean movePathFileToRoot(String pPath, String pRoot) {
        File dir = new File(pPath);
        if (!dir.isDirectory()) {
            TransLog.getLogger().info("Error: " + pPath);
            return false;
        }
        for (File aFile : dir.listFiles()) {
            if (aFile.isFile()) {
                //TransLog.getLogger().info(aFile.getName() + " -> " + aFile.getName());
                if (!aFile.renameTo(new File(pRoot + aFile.getName()))) {
                    TransLog.getLogger().info("Error: " + aFile.getPath());
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
        TransLog.getLogger().info("Result: " + new NameEditor(FULL_ROOT).renamePathFilePushDate() + ".");
    }

    public static void renameUnPushDate() {
        TransLog.getLogger().info("Result: " + new NameEditor(FULL_ROOT).renamePathFileUnPushDate() + ".");
    }

    @Deprecated
    public static void sampleMoveFileToRoot() {
        String root0 = "I:\\Book\\TFLib\\A2013\\B00";
        for (int i = 32; i < 51; i++) {
            String root = root0 + i;
            TransLog.getLogger().info("Processing " + root + " ...");
            if (!root.endsWith("\\")) {
                root += "\\";
            }
            new File(root);
            TransLog.getLogger().info("Result: " + new NameEditor(root).moveFileToRoot());
        }
    }

    @Deprecated
    public static void sampleListFiles() {
        String root = "I:\\Book\\TFLib\\A2013\\B0001\\Antarctic Press\\Robotech\\";
        File a = new File(root);
        for (File b : a.listFiles()) {
            TransLog.getLogger().info("T: " + b.getPath() + "; F: " + b.getName() + "; P: " + b.getParent());
        }
    }

    public static void renameNormalize() {
        TransLog.getLogger().info("Result: " + new NameEditor(FULL_ROOT).renameFileByTemplate() + ".");
        //TransLog.getLogger().info("Result: " + new NameEditor(FULL_ROOT).reformatNumber() + ".");
    }

    public static void renameComic07() {
        String[][] replaceOnce = {
                { " \\(2016\\) ",
                " "},
                { "\\.cbr",
                " (2016).cbr"},
                { " NMM \\(2016\\).cbr",
                " (2016) (NMM).cbr"},
        };

        TransLog.getLogger().info("Result: " + new NameEditor(FULL_ROOT).renameFileOnce(replaceOnce) + ".");
    }
    
    public static void renameSpecialReplace() {
        String[][] replaceOnce = {
//              { "Star Trek_ ([A-Za-z0-9'\\. ]+) (\\d{1,3}) - ([A-Za-z&-\\. ]+)\\.(epub|mobi)", 
//              "Star Trek - $1 00$2 ($3).$4" }, 
//              { "([A-Za-z0-9'\\. ]+) - ([A-Za-z0-9',&!_\\. ]+) \\(([A-Za-z&-\\. ]+)\\)\\.(epub|mobi)", 
//              "$1 ($3).$4" }, 
//                  { "Star Trek Myriad Universes - ([A-Za-z0-9'_!\\- ]+)\\.(epub|mobi)",
//                  "Star Trek - Myriad Universes $1.$2" }, 
//              { "Star Trek_ ([A-Za-z0-9'_!\\- ]+) - ([A-Za-z&-\\. ]+)\\.(epub|mobi)",
//              "Star Trek - $1 ($2).$3" }, 
//              { "\\(([A-Za-z0-9 ]+) - ([A-Za-z0-9 &\\.]+)\\)",
//              "- $1 ($2)" }, 
//                { "For Extreme Heroes v(\\d) (\\d{2})", "for Extreme Heroes v0$1 0$2" },
//                { "Immortal Iron Fist",
//                "The Immortal Iron Fist"},
//                { "Fist",
//                "Fist v05 - Escape from the Eighth City"},
//              { "([A-Za-z- ]+) (\\(\\d{4}\\)) ([\\dof \\(\\)]+)(\\.cbr)", 
//              "$1 $3 $2$4" }, 
//                { "([A-Za-z- ]+), (\\d{4}-\\d{2}-\\d{2}) \\(([ 0-9]+)\\) (\\(Digital\\) \\(Glorith-HD\\)\\.cbz)", 
//                "$1 0$3 ($2) $4" }, 
        };

        TransLog.getLogger().info("Result: " + new NameEditor(FULL_ROOT).renameFileOnce(replaceOnce) + ".");
    }
    
    public static void cutHead() {
        TransLog.getLogger().info("Result: " + new NameEditor(FULL_ROOT).renameCutHead() + ".");
    }
    
    public static void renameTest() {
        System.out.println(
                new String("Star Trek_ The Original Series - 0 - Mission to Horatius - Mack Reynolds.epub")
                .replaceAll("Star Trek_ ([A-Za-z0-9' ]+) - (\\d{1,3}) - ([A-Za-z0-9' ]+) - ([A-Za-z&\\. ]+)\\.(epub|mobi)", 
                        "Star Trek - $1 $2 - $3 ($4).$5"));
    }

    public static void main(String[] args) {
        //cutHead();
        renameNormalize();
        renameSpecialReplace();
        //renameInitUpper();
        //renameComic07();
        //renamePushDate();
        //renameUnPushDate();
        //renameTest();
    }
    
}

