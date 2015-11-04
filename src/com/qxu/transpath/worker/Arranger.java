/**
 * Copyright (c) 2014,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.worker
 * File Name:Arranger.java
 * Date:Apr 23, 2014 11:32:59 PM
 * 
 */
package com.qxu.transpath.worker;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import com.qxu.transpath.utils.CdEntry;
import com.qxu.transpath.utils.FileUtils;
import com.qxu.transpath.utils.TranspathConstants;

 /**
 * ClassName: Arranger <br/>
 * Description: To arrange raw file and build a task list. <br/>
 * Date: Apr 23, 2014 11:32:59 PM <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class Arranger {
    private ArrayList<CdEntry> entries;
    private CdEntry entry;
    private int status;
    
    public Arranger() {
        this.entries = new ArrayList<CdEntry>();
    }
    
    public Arranger(ArrayList<CdEntry> entries) {
        this.entries = entries;
    }
    
    public void addEntry(String name) {
        if (this.entries == null) {
            this.entries = new ArrayList<CdEntry>();
        }
        entry = new CdEntry(name);
        this.entries.add(entry);
    }
    
    public void addEntry(CdEntry cde) {
        if (this.entries == null) {
            this.entries = new ArrayList<CdEntry>();
        }
        this.entries.add(cde);
    }
    
    public boolean checkLinkLine(String line) {
        return line.matches("[v]?http[s]?://.*");
    }
    
    public boolean checkCommentLine(String line) {
        return line.matches("//.*");
    }
    
    public Arranger readFromFile(String fileName) {
        Scanner in = null;
        String line = "";
        try {
            in = new Scanner(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            status = 100000; // File open error;
            return null;
        }
        int cnt = 0;
        while (in.hasNext()) {
            line = in.nextLine().trim();
            if (this.checkIgnorableLine(line)) {
                continue;
            }
            if (this.checkLinkLine(line)) {
                if (this.entry == null || !this.entry.hasName()) {
                    status = 200000 + cnt; // Error: Entry no name at line cnt.
                    return null;
                }
                entry.addUniqueLink(line);
            } else if (this.checkCommentLine(line)) {
                if (this.entry == null && !this.entry.hasName()) {
                    status = 200000 + cnt; // Error: Entry no name at line cnt.
                    return null;
                }
                entry.addComment(line);                    
            } else {
                this.addEntry(line);
            }
            System.out.println(line);
            cnt++;
        }
        System.out.println(cnt);
        in.close();
        return this;
    }
    
    public int writeToFile(String fileName) {
        try {
            PrintWriter out = new PrintWriter(fileName);
            out.println(this.toOutput());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return entries.size();
    }
    
    public Arranger sort() {
        Collections.sort(entries);
        return this;
    }
    
    public Arranger merge() {
        Arranger newArranger = new Arranger();
        CdEntry newEntry = null;
        for (CdEntry currEntry:this.entries) {
            if (newEntry == null) {
                newEntry = currEntry.copy();
                continue;
            }
            if (newEntry.getName().equals(currEntry.getName()) ||
                newEntry.getName().startsWith(currEntry.getName())) {
                newEntry = newEntry.mergeEntry(currEntry);
                continue;
            }
            if (currEntry.getName().startsWith(newEntry.getName())) {
                newEntry.setName(currEntry.getName());
                newEntry = newEntry.mergeEntry(currEntry);
                continue;
            }
            newArranger.addEntry(newEntry);
            newEntry = currEntry.copy();
        }
        if (newEntry != null) {
            newArranger.addEntry(newEntry);
        }
        return newArranger;
    }
    
    public String toString() {
        return entries.toString();
    }
    
    public String toOutput() {
        String str = "";
        if (this.entries == null) 
            return str;
        for (CdEntry cde:this.entries) {
            str += cde.getName() + TranspathConstants.LINE_LINKER;
            for (String cmt: cde.getComments()) {
                str += cmt + TranspathConstants.LINE_LINKER;
            }
            for (String lnk: cde.getLinks()) {
                str += lnk + TranspathConstants.LINE_LINKER;
            }
        }
        return str;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    public int trimFile(String inFile, String outFile) {
        int iInLine = 0;
        int iOutLine = 0;
        int iLastOpen = 0;
        int iLastClose = 0;
        String sLastOpen = null;
        String sLastClose = null;
        Scanner in = null;
        PrintWriter out = null;
        boolean isInWindow = false;
        
        try {
            in = new Scanner(new FileReader(inFile));
            out = new PrintWriter(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        while (in.hasNext()) {
            iInLine++;
            String inLine = in.nextLine();
            if (!isInWindow && checkEndLine(inLine)) {
                System.out.println("Error: End line out of window!");
                System.out.println("Last Close Line " + iLastClose + ": " + sLastClose);
                System.out.println("This Close Line " + iInLine + ": " + inLine);
            }
            if (!isInWindow && checkLinkLine(inLine)) {
                System.out.println("Error: Link line out of window!");
                System.out.println("Last Close Line: " + iLastClose + ": " + sLastClose);
                System.out.println("This Close Line " + iInLine + ": " + inLine);
            }
            if (isInWindow && checkStartLine(inLine)) {
                System.out.println("Error: Start line inside window!");
                System.out.println("Last Open Line: " + iLastOpen + ": " + sLastOpen);
                System.out.println("This Open Line " + iInLine + ": " + inLine);
            }
            if (!isInWindow && checkStartLine(inLine)) {
                isInWindow = true;
                iLastOpen = iInLine;
                sLastOpen = inLine;
                //System.out.println("Line " + iInLine + " window opened.");
           } 
            if (isInWindow && checkEndLine(inLine)) {
                isInWindow = false;
                iLastClose = iInLine;
                sLastClose = inLine;
                //System.out.println("Line " + iInLine + " window closed.");
            }
            if (isInWindow && !checkIgnorableLine(inLine)) {
                out.println(inLine);
                iOutLine++;
            }
        }
        
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
        return iOutLine;
    }
    
    public int countMatch(String inFile, String inRegex) {
        int matchedLine = 0;
        Scanner in = null;
        try {
            in = new Scanner(new FileReader(inFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (in.hasNext()) {
            String inLine = in.nextLine();
            if (inLine.matches(inRegex)) {
                System.out.println(inLine);
                matchedLine++;
            }
        }
        if (in != null) {
            in.close();
        }
        return matchedLine;
    }
    
    public boolean checkStartLine (String line) {
        return line.matches("^(Default|Video) Re: .*$") 
                || line.matches("^Default$")
                || line.matches("^(Today|Yesterday|\\d{2}-\\d{2}-\\d{4}) \\d{2}:\\d{2}$");
    }

    public boolean checkEndLine (String line) {
        return line.matches("^\\w* is offline\\s*Reply With Quote$") 
                || line.matches("^\\s*Multi Quote\\s*Quote$")
                //|| line.matches("^Week of \\d{2}/\\d{2}/\\d{4}$")
                || line.matches("^\\s*Last edited by.*$");
    }

    public boolean checkIgnorableLine (String line) {
        return checkStartLine(line)
                || checkEndLine(line)
                //|| line.matches("\\s*//\\s*") 
                || line.matches("^\\s*$") 
                || line.matches("^\\s*Code:\\s*$") 
                || line.matches("^\\s*Quote:\\s*$")
                || line.matches("^\\s*Download:\\s*$")
                || line.matches("^\\s*This image has been resized.*$");
    }

    public static void countMatchString() {
        int n = new Arranger().countMatch("resource/rawDump.txt", 
                "^\\s*Last edited by.*$");
        System.out.println("Matched Lines: "+ n);        
    }

    public static void arrangeTask() {
        int n = new Arranger().readFromFile("resource/raw.txt").sort().merge().writeToFile("resource/task.txt");
        System.out.println("Totally " + n + " entries processed.");
        
    }
    
    public static void filterRaw() {
        int n = new Arranger().trimFile("resource/rawDump.txt", "resource/rawClean.txt");
        System.out.println("Raw Filtered to "+ n + " lines.");        
    }
    
    public static void main (String[] args) {
        arrangeTask();
        //filterRaw();
        //countMatchString();
    }
}
