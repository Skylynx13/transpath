package com.skylynx13.transpath.task;

import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.utils.FileUtils;
import com.skylynx13.transpath.utils.TransConst;
import com.skylynx13.transpath.utils.TransProp;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

/**
 * ClassName: CodeDigger
 * Description: To dig a given code from a source file,
 * get all of the entries containing the code,
 * then write them to a track file.
 * Date: 2016-04-07 19:20:44
 */
class TaskDigger {

    static void digKeywordFileDefault(String src, String target) {
        TaskDigger.digKeywordFile(TransProp.get(TransConst.LOC_CONFIG) + TransConst.LIST_KEYWORDS, 
                                  src, target);
    }

    private static void digKeywordFile(String key, String src, String target) {
        String[] keywords = TaskDigger.readKeywordList(key);
        TransLog.getLogger().info("Digging keywords " + Arrays.toString(keywords));
        TaskDigger.digKeyword(keywords, src, target);
    }

    private static int digKeyword(String[] keywords, String src, String target) {
        int n = new TaskArranger().readFromFile(src).applyFilter(keywords).appendToFile(target);
        TransLog.getLogger().info("" + n + " entries digged from " + src + " are saved to " + target);
        return n;
    }

    private static String[] readKeywordList(String key) {
        Scanner in;
        ArrayList<String> keywords = new ArrayList<>();
        try {
            in = new Scanner(new FileReader(key));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        while (in.hasNext()) {
            keywords.add(in.nextLine());
        }
        in.close();
        return keywords.toArray(new String[0]);
    }
    
    static void digAllFreshSpecific(String[] keys, String target) {
        long t0 = System.currentTimeMillis();
        FileUtils.clearFile(target);
        File path = new File(TransProp.get(TransConst.LOC_TASK));
        int total = 0;
        TransLog.getLogger().info("Digging keywords " + Arrays.toString(keys));
        for (File file : Objects.requireNonNull(path.listFiles((file, name) -> name.matches("fresh_.*.txt")))) {
            total += TaskDigger.digKeyword(keys, file.getAbsolutePath(), target);
            //TransLog.getLogger().info(file.getAbsolutePath());
        }
        TransLog.getLogger().info("Total: " + total);
        TransLog.getLogger().info("Time : " + (System.currentTimeMillis() - t0) + "ms");
    }
}
