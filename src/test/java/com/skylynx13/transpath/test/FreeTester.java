package com.skylynx13.transpath.test;

import java.awt.*;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.skylynx13.transpath.store.StoreNode;
import com.skylynx13.transpath.task.TaskArranger;
import com.skylynx13.transpath.task.TaskEntry;
import com.skylynx13.transpath.utils.DateUtils;
import com.skylynx13.transpath.utils.TransConst;
import com.skylynx13.transpath.utils.TransProp;

/**
 * ClassName: FreeTester
 * Description: Free tester
 * Date: 2014-04-14 22:35:28
 */
public class FreeTester {
    public class SortByName implements Comparator<String> {
        public int compare (String s1, String s2) {
            if (s1.compareTo(s2) > 0) {
                return 1;
            } else if (s1.compareTo(s2) < 0) {
                return -1;
            }
            return 0;
        }
    }

    public void testList() {
        ArrayList<String> aStrList = new ArrayList<>();
        aStrList.add("abcde");
        aStrList.add("1234");
        aStrList.add("12345");
        System.out.println(aStrList.toString());
        Collections.sort(aStrList);
        System.out.println(aStrList.toString());
        aStrList.sort(new SortByName());
        System.out.println(aStrList.toString());
    }
    public void testArrangerBasic() {
        TaskEntry cde1 = new TaskEntry("name1");
        //cde1.addComment("comment11");
        cde1.addLink("link11");
        TaskEntry cde2 = new TaskEntry("name2", "comment2", "link2");
        cde2.addComment("comment21");
        cde2.addLink("link21");
        TaskEntry cde3 = new TaskEntry("name3", "comment3", "link3");
        cde3.addComment("comment31");
        cde3.addLink("link32");
        ArrayList<TaskEntry> cdEntries = new ArrayList<>();
        cdEntries.add(cde3);
        cdEntries.add(cde1);
        cdEntries.add(cde2);
        cde3 = new TaskEntry("name4", "comment4", "link4");
        cde3.addComment("comment41");
        cde3.addLink("link42");
        cdEntries.add(cde3);
        TaskArranger argr = new TaskArranger(cdEntries);
        System.out.println(argr.toString());
        System.out.println(argr.toOutput());
        
        System.out.println(argr.sort().toString());
        System.out.println(argr.toOutput());
        System.out.println(argr.checkIgnorableLine("    aCode: "));
        System.out.println("Link: " + argr.checkLinkLine("http://  "));
        System.out.println("Comment: " + argr.checkCommentLine("//  "));
        System.out.println("[" + "       abcd    ".trim() + "]");
    }
    
    public void testArranger() {
        TaskArranger argr = new TaskArranger();
        try {
            int n = argr.readFromFile("src/test/resources/raw.txt").sort().merge().writeToFile("src/test/resources/task.txt");
            System.out.println("Totally " + n + " entries processed.");
        } catch (NullPointerException e) {
            System.out.println(argr.getStatus());
        }
    }
    
    public void testReplaceStr() {
        String str0 = "abcd__efg___hi___test)(2014___digital___jkl___mn_empire_.ccc";
        String str1 = str0.replaceAll("___", ") (");
        String str2 = str1.replaceAll("__", " (");
        String str3 = str2.replaceAll("_\\.", ").");
        String str4 = str3.replaceAll("_", " ");
        String str5 = str4.replaceAll("\\(", " (");
        String str6 = str5.replaceAll(" {2}\\(", " (");
        String str7 = str6.replaceAll("\\(digital\\)", "(Digital)");
        String str8 = str7.replaceAll(" Empire\\)", "-Empire)");
        System.out.println(str0 + "\r -> " + str1  + "\r -> " + str2
                + "\r -> " + str3  + "\r -> " + str4  + "\r -> " + str5
                + "\r -> " + str6 + "\r -> " + str7 + "\r -> " + str8);
    }
    
    public void testReformat() {
        String str0 = "Uncle Sam and the Freedom Fighters, 2006-08-00 (_02) (digital) (OkC.O.M.P.U.T.O.-Novus-HD).cbz";
        System.out.println(str0);
        System.out.println(this.reformat(str0));
    }
    
    private String reformat(String str) {
        String ret = str.replaceAll("_", " ");
        ret.indexOf(',');
        ret.indexOf('(');
        //get number
        String rpl1s = ", ";
        String rpl1t = " " + String.format("%03d", Integer.parseInt(ret.substring(ret.indexOf('(')+1, ret.indexOf(')')).trim())) + " (";
        String rpl2s = ret.substring(ret.indexOf('(')-1, ret.indexOf(')')+1).replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)");
        String rpl2t = ")";
        System.out.println(":" + rpl1t + ":" + rpl2s + ":");
        ret = ret.replaceAll(rpl1s, rpl1t).replaceAll(rpl2s, rpl2t);
        
        System.out.println(str);
        System.out.println(ret);
        return ret;
    }
    
    private void testProps() {
        String aProp;
        Properties aProps = new Properties();
        try {
            aProps.load(new FileInputStream(TransConst.TP_PROPS));
        } catch (IOException e) {
            e.printStackTrace();
        }
        aProp = aProps.getProperty("test");
        System.out.println("free props tests: [" + aProp + "]");
        
    }
    
    private void workoutAbc() {
        System.out.println("abc ready");
        for (int a = 1; a<1000; a++) {
            for (int b=1; b<1000; b++) {
                for (int c=1; c<1000; c++) {
                    //float d = a/(b+c)+b/(a+c)+c/(a+b);
                    //System.out.println(d);
                    //if (Math.abs(d-4) <0.001) {
                    boolean d = (a*(a+c)*(a+b)+b*(b+c)*(a+b)+c*(a+c)*(b+c)) == (4*(a+b)*(b+c)*(a+c));
                    if (d) {
                        System.out.println("your abc: " + a + "," + b + "," + c + "," + d);
                    }
                }
            }
        }
        System.out.println("abc done");
    }

    public static void main(String[] args) {
        fileTester();
    }

    private static void fileTester() {
        int rev = 0;
        String version = "Teestt";
        File file = new File(String.format("D:\\\\temp\\\\StoreList_%s_%03d.txt", version, rev));
        while (file.exists()) {
            rev ++;
            file = new File(String.format("D:\\\\temp\\\\StoreList_%s_%03d.txt", version, rev));
        }
        try {
            PrintWriter out = new PrintWriter(file);
            out.println("ok");
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static void openFileByDefaultProgram() {
        try {
            Desktop.getDesktop().open(new File("/home/qxu/Book/TFLib/A2018/B0912/G.I. Joe - A Real American Hero 256 (2018) (Digital) (Thornn-Empire).cbr"));
            //Desktop.getDesktop().open(new File("/home/qxu/workspace/transpath/star.sh"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void pathNameDisplay() {
        File path = new File("/home/qxu/temp/ok.txt");
        try {
            System.out.println(path.getName() + " | " + path.getPath() + " | " + path.getAbsolutePath() + " | " + path.getAbsoluteFile()
                    + " | " + path.getParent() + " | " + path.getCanonicalPath() + " | " + path.getCanonicalFile() + " | " + path.getParentFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void mapDisplay() {
        Map<String, String> disMap = new HashMap<>();
        disMap.put("aaa", "bbb");
        disMap.put("333", "444");
        System.out.println(disMap.toString());

    }

    private static void runLinuxCommand() {
        String sandboxPath = "/home/qxu/sandbox";
        File packPath = new File(sandboxPath);
        if (!packPath.isDirectory()) {
            System.out.println(packPath.getName() + " is not a directory.");
            return;
        }
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(packPath);
        processBuilder.redirectErrorStream();
        String[] rarCmd = new String[3];
        rarCmd[0] = "rar";
        rarCmd[1] = "t";
        for (File packFile : Objects.requireNonNull(packPath.listFiles())) {
//            System.out.println("Checking " + packFile.getName());
            if (packFile.isDirectory()) {
                System.out.println(packFile.getName() + " is a directory.");
                continue;
            }
            String fileName = packFile.getName();
            String suffix = fileName.substring(fileName.lastIndexOf('.')+1);
            if (suffix.equalsIgnoreCase("rar") ||suffix.equalsIgnoreCase("cbr")) {
                //rarCmd[2] = "\'" + fileName.replaceAll(" ", "\\\\ ").replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)") + "\'";
                //rarCmd[2] = packPath + "/" + fileName.replaceAll(" ", "\\\\ ").replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)");
                //rarCmd[2] = "\'" + packPath + "/" + fileName + "\'";
                rarCmd[2] = packPath + "/" + fileName;
                processBuilder.command(rarCmd);
                System.out.println("Command line: " + String.join(" ", processBuilder.command()));
                try {
                    Process process = processBuilder.start();
                    //process.waitFor();
                    InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        System.out.println("Output: " + line);
                    }
                    InputStreamReader errStreamReader = new InputStreamReader(process.getErrorStream());
                    BufferedReader errbufferedReader = new BufferedReader(errStreamReader);
                    String eline;
                    while ((eline = errbufferedReader.readLine()) != null) {
                        System.out.println("Error: " + eline);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                //} catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            System.out.println("The type of file [" + fileName + "] is [" + suffix + "].");
        }
}

    public static void simpleExec() {
        String fileName = "/home/qxu/sandbox/Amazing Spider-Man 005 (2018) (digital) (Oroboros-DCP).cbr";
        //"/home/qxu/sandbox/Amazing\\ Spider-Man\\ 005\\ \\(2018\\)\\ \\(digital\\)\\ \\(Oroboros-DCP\\).cbr";
        ProcessBuilder processBuilder = new ProcessBuilder().command("rar", "t", fileName);//.inheritIO();
        processBuilder.redirectErrorStream(true);
        System.out.println("Command line: " + String.join(" ", processBuilder.command()));
        try {
            Process process = processBuilder.start();
            process.waitFor();
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println("Output: " + line);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void someTest() {
        double timeTag = System.currentTimeMillis();
        FreeTester ft = new FreeTester();
        //ft.testNodeTree();
        //ft.testList();
        //ft.testArranger();
        //ft.testReplaceStr();
        //ft.testReformat();
        
        //System.out.println(StringUtils.getSimpleName("G.I. Joe Action Force Mini Comic.cbr"));
        
        ft.testProps();
        System.out.println(" Week of 02/04/2016  ".matches("^\\s*Week of \\d{2}/\\d{2}/\\d{4}\\s*$"));
        String rex = "(?i).*(\\d{3}(\\.\\d{1,2})? MB|\\d{1,2}(\\.\\d{1,2})? GB).*";
        System.out.println("gkk233.2 MB".matches(rex));
        System.out.println("/a2016/b1234/".replaceAll("/", "\\\\"));
        int aaa = 3;
        int bbb = 3;
        System.out.println(Integer.compare(aaa, bbb));
        
        System.out.println(TransProp.get(TransConst.LOC_CONFIG));

        System.out.println("Time elapsed: " + (System.currentTimeMillis()-timeTag) + " ms.");
        System.out.println(System.getProperty("qxu.test"));
        long lastM = 1508560299855L;
        System.out.println("Long: " + lastM + "; Date: " + new Date(lastM) + "; Formatted: " + DateUtils.formatDateTimeLong(lastM));
        // Construct the current date
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String tsDate10 = ts.toString().substring(0, 10);
        String tsDate16 = ts.toString().substring(0, 16);
        System.out.println(tsDate10 + ":::" + tsDate16);

//        ft.workoutAbc();

        System.out.println("Time elapsed: " + (System.currentTimeMillis()-timeTag) + " ms.");
        String inputString = "A8123PEKAA";
        String regex = "(\\w{2})(\\d{3,4})([A-Z]?)(\\w{3})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputString);
        boolean matched = matcher.find();
        if (matched) {
            String airline = matcher.group(1);
            String flightNumber = matcher.group(2);
            String suffix = matcher.group(3).equals("")?"^":matcher.group(3);
            String more = matcher.group(4);
            System.out.println("airline is [" + airline + "]; flightNumber is [" + flightNumber + "]; suffix is [" + suffix + "]; more is [" + more + "]");
            System.out.println("Time elapsed: " + (System.currentTimeMillis()-timeTag) + " ms.");
        }

        int a = 1;
        int b = 2;
        System.out.println("a="+a+"; b=" + b);

        String sa = "1";
        String sb = "2";
        System.out.println("a="+sa+"; b=" + sb);

        StoreNode na = new StoreNode();
        na.setId(1);
        na.setName("namea");
        na.setPath("patha");

        StoreNode nb = na;
        nb.setId(2);
        System.out.println("na=" + na.getId() + "; nb=" + nb.getId());

        try {
            Process proc = Runtime.getRuntime().exec("cmd /c chcp 437|copy D:\\tesp\\head110.jpg D:\\temp\\head120.jpg" );
            BufferedReader iReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(proc.getInputStream())));
            BufferedReader eReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(proc.getErrorStream())));
            String inLine;
            while ((inLine = iReader.readLine()) != null) {
                System.out.println("Return: " + inLine);
            }
            while ((inLine = eReader.readLine()) != null) {
                System.out.println("Error: " + inLine);
            }
            if (proc.waitFor() != 0) {
                if (proc.exitValue() == 1) {
                    System.err.println("Error executing.");
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
