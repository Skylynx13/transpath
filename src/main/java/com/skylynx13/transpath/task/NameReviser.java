package com.skylynx13.transpath.task;

import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.utils.TransConst;
import com.skylynx13.transpath.utils.TransProp;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author skylynx
 */
public class NameReviser {
    public static void rename() {
        TransLog.getLogger().info("Result: "
                + new NameReviser().renameFileByRenameList(TransProp.get(TransConst.LOC_CONFIG)
                + TransConst.LIST_RENAME) + ".");
    }

    private boolean renameFileByRenameList(String renameListName) {
        return renameRootPathFile(readRenameList(renameListName));
    }

    private boolean renameRootPathFile(String[][] replaceList) {
        int totalFile = 0;
        int procFile = 0;
        String rootDir = TransProp.get(TransConst.LOC_TRANS);
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

    String getReplacedName(String[][] replaceList, String originalName) {
        String replacedName = originalName;
        for (String[] repTempl : replaceList) {
            replacedName = replacedName.replaceAll(repTempl[0], repTempl[1]);
        }
        return replacedName;
    }

    private void checkChars(String fileName) {
        if (!fileName.matches(TransConst.NAME_CHARS)) {
            TransLog.getLogger().info("Check Characters: " + fileName);
        }
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
}
