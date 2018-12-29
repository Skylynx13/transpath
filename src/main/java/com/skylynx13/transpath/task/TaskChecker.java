package com.skylynx13.transpath.task;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.utils.FileUtils;
import com.skylynx13.transpath.utils.TransConst;
import com.skylynx13.transpath.utils.TransProp;

/**
 * ClassName: TaskChecker
 * Description: To rename file with a regular name by replace template.
 * Date: 2015-02-03 11:08:20
 */
public class TaskChecker {
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
            {"#", ""},
            {" v1 ", " v01 "},
            {" v2 ", " v02 "},
            {" v3 ", " v03 "},
            {" v4 ", " v04 "},
            {" v5 ", " v05 "},
            {" v6 ", " v06 "},
            {" Of ", " of "},
            {" of The", " of the"},
            {" From ", " from "},
            {" And ", " and "},
            {" VS ", " vs "},
            {" Vs ", " vs "},
            {" Vs\\. ", " vs "},
            {" vs\\. ", " vs "},
            {"\\.zip", ".cbz"},
            {"\\.rar", ".cbr"},
            {"\\.CBZ", ".cbz"},
            {"\\.CBR", ".cbr"},
            {"([-+'A-Za-z0-9 ]+) (\\d{2}) ([-A-Za-z0-9 \\(\\)\\.]+)", "$1 0$2 $3"}
    };

    private String getRootDir() {
        return TransProp.get(TransConst.LOC_TRANS);
    }

    public TaskChecker() {
    }

    private boolean renameFileByRenameList(String renameListName) {
        return renameRootPathFile(readRenameList(renameListName));
    }

    private boolean renameFileByTemplate() {
        return renameRootPathFile(this.replaceTemplates);
    }

    private boolean renameFileOnce(String[][] replaceOnce) {
        return renameRootPathFile(replaceOnce);
    }

    private boolean renameRootPathFile(String[][] replaceList) {
        int totalFile = 0;
        int procFile = 0;
        String rootDir = getRootDir();
        File dirRoot = new File(rootDir);
        if (!dirRoot.isDirectory()) {
            TransLog.getLogger().info("Path name error.");
            return false;
        }

        for (File aFile : Objects.requireNonNull(dirRoot.listFiles())) {
            if (aFile.isFile()) {
                String replacedName = getReplacedName(replaceList, aFile.getName());
                totalFile++;
                if (!aFile.getName().equals(replacedName)) {
                    procFile++;
                    if (!aFile.renameTo(new File(rootDir + replacedName))) {
                        TransLog.getLogger().info(aFile.getName() + " -e> " + replacedName);
                        return false;
                    }
                    TransLog.getLogger().info(aFile.getName() + " -> " + replacedName);
                }
                checkChars(aFile.getName());
            }
        }
        TransLog.getLogger().info(procFile + " of " + totalFile + " files renamed.");
        return true;
    }

    private void checkChars(String fileName) {
        if (!fileName.matches(TransConst.NAME_CHARS)) {
            TransLog.getLogger().info("Check Characters: " + fileName);
        }
    }

    String getReplacedName(String[][] replaceList, String originalName) {
        String replacedName = originalName;
        for (String[] repTempl : replaceList) {
            replacedName = replacedName.replaceAll(repTempl[0], repTempl[1]);
        }
        return replacedName;
    }

    @SuppressWarnings("unused")
    private static void renameInitUpper() {
        int totalFile = 0;
        int procFile = 0;
        String pRoot = TransProp.get(TransConst.LOC_TRANS);
        File dirRoot = new File(pRoot);
        if (!dirRoot.isDirectory()) {
            TransLog.getLogger().info("Path name error.");
            TransLog.getLogger().info("Result: False.");
        }

        for (File aFile : Objects.requireNonNull(dirRoot.listFiles())) {
            if (aFile.isFile()) {
                char[] replacedChars = aFile.getName().toLowerCase().toCharArray();
                for (int iChar = 0; iChar < replacedChars.length; iChar++) {
                    if (((iChar == 0) || (replacedChars[iChar - 1] == 32))
                            && ((96 < replacedChars[iChar]) && (replacedChars[iChar] < 123))) {
                        replacedChars[iChar] -= 32;
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
        TransLog.getLogger().info(procFile + " of " + totalFile + " files renamed.");
        TransLog.getLogger().info("Result: True.");
    }

    static String[][] readRenameList(String renameListName) {

        Scanner in;
        ArrayList<String[]> namePairs = new ArrayList<>();
        try {
            in = new Scanner(new FileReader(renameListName));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        while (in.hasNext()) {
            String[] namePair = in.nextLine().split(TransConst.COLON, -1);
            namePairs.add(namePair);
        }
        in.close();
        return namePairs.toArray(new String[namePairs.size()][2]);
    }

    public boolean renamePathFileSwapDate() {
        int totalFile = 0;
        int procFile = 0;
        String rootDir = getRootDir();
        File dirRoot = new File(rootDir);
        if (!dirRoot.isDirectory()) {
            TransLog.getLogger().info("Path name error.");
            return false;
        }

        for (File aFile : Objects.requireNonNull(dirRoot.listFiles())) {
            if (aFile.isFile()) {
                String oldName = aFile.getName();
                String[] oldSubStr = oldName.split(" ");
                int slen = oldSubStr.length;
                StringBuilder newName = new StringBuilder(oldSubStr[0] + " " + oldSubStr[2] + " " + oldSubStr[1]);
                for (int i = 3; i < slen; i++) {
                    newName.append(" ").append(oldSubStr[i]);
                }
                totalFile++;
                if (!oldName.equals(newName.toString())) {
                    procFile++;
                    if (!aFile.renameTo(new File(rootDir + newName))) {
                        TransLog.getLogger().info(aFile.getName() + " -e> " + newName);
                        return false;
                    }
                    TransLog.getLogger().info(oldName + " -> " + newName);
                }
            }
        }
        TransLog.getLogger().info(procFile + " of " + totalFile + " files renamed.");
        return true;
    }

    private boolean renamePathFilePushDate() {
        int totalFile = 0;
        int procFile = 0;
        String rootDir = getRootDir();
        File dirRoot = new File(rootDir);
        if (!dirRoot.isDirectory()) {
            TransLog.getLogger().info("Path name error.");
            return false;
        }

        for (File aFile : Objects.requireNonNull(dirRoot.listFiles())) {
            if (aFile.isFile()) {
                String oldName = aFile.getName();
                int idxFirstBlank = oldName.indexOf(' ');
                int idxLastPoint = oldName.lastIndexOf('.');
                int idxFirstLeftBracket = oldName.indexOf('(');
                int idxInsert = idxLastPoint;
                if (idxFirstLeftBracket > 1) {
                    idxInsert = idxFirstLeftBracket - 1;
                }
                String newName = oldName.substring(idxFirstBlank + 1, idxInsert) + " ("
                        + oldName.substring(0, idxFirstBlank) + ")" + oldName.substring(idxInsert);
                totalFile++;
                if (!oldName.equals(newName)) {
                    procFile++;
                    if (!aFile.renameTo(new File(rootDir + newName))) {
                        TransLog.getLogger().info(aFile.getName() + " -e> " + newName);
                        return false;
                    }
                    TransLog.getLogger().info(oldName + " -> " + newName);
                }
            }
        }
        TransLog.getLogger().info(procFile + " of " + totalFile + " files renamed.");
        return true;
    }

    private boolean renameCutHead() {
        int totalFile = 0;
        int procFile = 0;
        String rootDir = getRootDir();
        File dirRoot = new File(rootDir);
        if (!dirRoot.isDirectory()) {
            TransLog.getLogger().info("Path name error.");
            return false;
        }

        for (File aFile : Objects.requireNonNull(dirRoot.listFiles())) {
            if (aFile.isFile()) {
                String oldName = aFile.getName();
                int idxFirstBlank = oldName.indexOf(' ');
                String newName = oldName.substring(idxFirstBlank + 1);
                totalFile++;
                if (!oldName.equals(newName)) {
                    procFile++;
                    if (!aFile.renameTo(new File(rootDir + newName))) {
                        TransLog.getLogger().info(aFile.getName() + " -e> " + newName);
                        return false;
                    }
                    TransLog.getLogger().info(oldName + " -> " + newName);
                }
            }
        }
        TransLog.getLogger().info(procFile + " of " + totalFile + " files renamed.");
        return true;
    }

    private boolean renamePathFileUnPushDate() {
        int totalFile = 0;
        int procFile = 0;
        String rootDir = getRootDir();
        File dirRoot = new File(rootDir);
        if (!dirRoot.isDirectory()) {
            TransLog.getLogger().info("Path name error.");
            return false;
        }

        for (File aFile : Objects.requireNonNull(dirRoot.listFiles())) {
            if (aFile.isFile()) {
                String oldName = aFile.getName();
                int idxFirstBlank = oldName.lastIndexOf('(');
                int idxLastPoint = oldName.lastIndexOf(')');
                String newName = oldName.substring(idxFirstBlank + 1, idxLastPoint) + " "
                        + oldName.substring(0, idxFirstBlank - 1) + oldName.substring(idxLastPoint + 1);
                totalFile++;
                if (!oldName.equals(newName)) {
                    procFile++;
                    if (!aFile.renameTo(new File(rootDir + newName))) {
                        TransLog.getLogger().info(aFile.getName() + " -e> " + newName);
                        return false;
                    }
                    TransLog.getLogger().info(oldName + " -> " + newName);
                }
            }
        }
        TransLog.getLogger().info(procFile + " of " + totalFile + " files renamed.");
        return true;
    }

    public boolean reformatNumber() {
        int totalFile = 0;
        int procFile = 0;
        String rootDir = getRootDir();
        File dirRoot = new File(rootDir);
        if (!dirRoot.isDirectory())
            return false;

        for (File aFile : Objects.requireNonNull(dirRoot.listFiles())) {
            if (aFile.isFile()) {
                String replacedName = aFile.getName().replaceAll("#", " ");
                if (!replacedName.contains(",")) {
                    continue;
                }
                String rpl1s = ", ";
                String rpl1t = " " + String.format("%03d", Integer.parseInt(replacedName.substring(replacedName.indexOf('(') + 1, replacedName.indexOf(')')).trim())) + " (";
                String rpl2s = replacedName.substring(replacedName.indexOf('(') - 1, replacedName.indexOf(')') + 1).replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)");
                String rpl2t = ")";
                TransLog.getLogger().info(":" + rpl1t + ":" + rpl2s + ":");
                replacedName = replacedName.replaceAll(rpl1s, rpl1t).replaceAll(rpl2s, rpl2t);
                totalFile++;
                if (!aFile.getName().equals(replacedName)) {
                    procFile++;
                    if (!aFile.renameTo(new File(rootDir + replacedName))) {
                        TransLog.getLogger().info(aFile.getName() + " -e> " + replacedName);
                        return false;
                    }
                    TransLog.getLogger().info(aFile.getName() + " -> " + replacedName);
                }
            }
        }
        TransLog.getLogger().info(procFile + " of " + totalFile + " files renamed.");
        return true;
    }

    public boolean moveFileToRoot() {
        return movePathFileToRoot(getRootDir(), getRootDir());
    }

    private boolean movePathFileToRoot(String pPath, String pRoot) {
        File dir = new File(pPath);
        if (!dir.isDirectory()) {
            TransLog.getLogger().info("Error: " + pPath);
            return false;
        }
        for (File aFile : Objects.requireNonNull(dir.listFiles())) {
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
        TransLog.getLogger().info("Result: " + new TaskChecker().renamePathFilePushDate() + ".");
    }

    public static void renameUnPushDate() {
        TransLog.getLogger().info("Result: " + new TaskChecker().renamePathFileUnPushDate() + ".");
    }

    public static void renameNormalize() {
        TransLog.getLogger().info("Result: " + new TaskChecker().renameFileByTemplate() + ".");
    }

    public static void rename() {
        TransLog.getLogger().info("Result: " + new TaskChecker().renameFileByRenameList(TransProp.get(TransConst.LOC_CONFIG) + TransConst.LIST_RENAME) + ".");
    }

    public static void renameComic07() {
        String[][] replaceOnce = {
                {" \\(2016\\) ",
                        " "},
                {"\\.cbr",
                        " (2016).cbr"},
                {" NMM \\(2016\\).cbr",
                        " (2016) (NMM).cbr"},
        };

        TransLog.getLogger().info("Result: " + new TaskChecker().renameFileOnce(replaceOnce) + ".");
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

        TransLog.getLogger().info("Result: " + new TaskChecker().renameFileOnce(replaceOnce) + ".");
    }

    public static void cutHead() {
        TransLog.getLogger().info("Result: " + new TaskChecker().renameCutHead() + ".");
    }

    public static void renameTest() {
        System.out.println(
                "Star Trek_ The Original Series - 0 - Mission to Horatius - Mack Reynolds.epub"
                        .replaceAll("Star Trek_ ([A-Za-z0-9' ]+) - (\\d{1,3}) - ([A-Za-z0-9' ]+) - ([A-Za-z&. ]+)\\.(epub|mobi)",
                                "Star Trek - $1 $2 - $3 ($4).$5"));
    }

    public static void main(String[] args) {
        //cutHead();

        //renameNormalize();
        //renameSpecialReplace();

        //renameInitUpper();
        //renameComic07();
        //renamePushDate();
        //renameUnPushDate();
        //renameTest();
        String[][] abc = readRenameList(TransProp.get(TransConst.LOC_CONFIG) + TransConst.LIST_RENAME);
        System.out.println(abc != null ? abc.length : 0);
        for (String[] aaa : Objects.requireNonNull(abc)) {
            System.out.println(aaa[0] + ":::::" + aaa[1]);
        }
        TaskChecker.rename();
    }

    public static void checkPackages() {
        TransLog.getLogger().info("Result: " + new TaskChecker().checkPackageTrans() + ".");

    }

    private String checkPackageTrans() {
        String rootDirStr = getRootDir();
        File rootDir = new File(rootDirStr);
        if (!rootDir.isDirectory()) {
            return "Invalid root: " + rootDirStr;
        }
        if (Objects.requireNonNull(rootDir.listFiles()).length == 0) {
            return "No files in: " + rootDirStr;
        }
        List<String> checkFileList = new ArrayList<>();
        for (File checkFile : Objects.requireNonNull(rootDir.listFiles())) {
            checkFileList.add(checkFile.getPath());
        }
        return FileUtils.checkPackages(checkFileList);
    }
}

