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
package com.libra42.transpath.worker;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import com.libra42.transpath.log.TransLog;
import com.libra42.transpath.utils.CdEntry;
import com.libra42.transpath.utils.TransConst;

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
    
    public void clear() {
        this.entries = null;
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
                    in.close();
                    return null;
                }
                entry.addUniqueLink(line);
            } else if (this.checkCommentLine(line)) {
                if (this.entry == null && !this.entry.hasName()) {
                    status = 200000 + cnt; // Error: Entry no name at line cnt.
                    in.close();
                    return null;
                }
                entry.addComment(line);                    
            } else {
                this.addEntry(line);
            }
            //TransLog.getLogger().info(line);
            cnt++;
        }
        //TransLog.getLogger().info(cnt);
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
    
    public int appendToFile(String fileName) {
        if (entries.size() == 0) {
            return 0;
        }
        try {
            PrintWriter out = new PrintWriter(new FileWriter(fileName,true));
            out.println(this.toOutput());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
    
    public Arranger merge(Arranger otherArranger) {
        Arranger newArranger = new Arranger();
        newArranger.entries.addAll(this.entries);
        newArranger.entries.addAll(otherArranger.entries);
        return newArranger.sort().merge();
    }
    
    public String toString() {
        return entries.toString();
    }
    
    public String toOutput() {
        String str = "";
        if (this.entries == null) 
            return str;
        for (CdEntry cde:this.entries) {
            str += cde.getName() + TransConst.LN;
            for (String cmt: cde.getComments()) {
                str += cmt + TransConst.LN;
            }
            for (String lnk: cde.getLinks()) {
                str += lnk + TransConst.LN;
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
                TransLog.getLogger().info("Error: End line out of window!");
                TransLog.getLogger().info("Last End Line " + iLastClose + ": " + sLastClose);
                TransLog.getLogger().info("This End Line " + iInLine + ": " + inLine);
            }
            if (!isInWindow && checkLinkLine(inLine)) {
                TransLog.getLogger().info("Error: Link line out of window!");
                TransLog.getLogger().info("Last End Line: " + iLastClose + ": " + sLastClose);
                TransLog.getLogger().info("This Link Line " + iInLine + ": " + inLine);
            }
            if (isInWindow && checkStartLine(inLine)) {
                TransLog.getLogger().info("Error: Start line inside window!");
                TransLog.getLogger().info("Last Start Line: " + iLastOpen + ": " + sLastOpen);
                TransLog.getLogger().info("This Start Line " + iInLine + ": " + inLine);
            }
            if (!isInWindow && checkStartLine(inLine)) {
                isInWindow = true;
                iLastOpen = iInLine;
                sLastOpen = inLine;
                //TransLog.getLogger().info("Line " + iInLine + " window opened.");
           } 
            if (isInWindow && checkEndLine(inLine)) {
                isInWindow = false;
                iLastClose = iInLine;
                sLastClose = inLine;
                //TransLog.getLogger().info("Line " + iInLine + " window closed.");
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
    
    public boolean checkStartLine (String line) {
        return line.matches("^(Default|Video|Post) Re: .*$") 
                || line.matches("^Default$")
                || line.matches("^(Hari ini|Kemarin|Today|Yesterday|\\d{2}-\\d{2}-\\d{4}) \\d{2}:\\d{2}$");
    }

    public boolean checkEndLine (String line) {
        return line.matches("^\\w* is offline\\s*Reply With Quote$") 
                || line.matches("^\\s*Multi Quote\\s*Quote$")
                //|| line.matches("^Week of \\d{2}/\\d{2}/\\d{4}$") // Open this line if want to ignore weekly list from the dump.
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
                || line.matches("^\\s*Week of \\d{2}/\\d{2}/\\d{4}\\s*$")
                || line.matches("^\\s*This image has been resized.*$");
    }
    
    public Arranger applyFilter(String[] keys) {
        Arranger newArranger = new Arranger();
        for (CdEntry currEntry : this.entries) {
            if (currEntry.matches(keys))
                newArranger.addEntry(currEntry);
        }
        return newArranger;
    }
    
    public Arranger applyFilterReversely(String[] keys) {
        Arranger newArranger = new Arranger();
        for (CdEntry currEntry : this.entries) {
            if (!currEntry.matches(keys))
                newArranger.addEntry(currEntry);
        }
        return newArranger;
    }
    
    public boolean equals(Arranger another) {
        if (this.entries.size() != another.entries.size())
            return false;

        for (int iLength = 0; iLength < this.entries.size(); iLength++) {
            CdEntry thisEntry = this.entries.get(iLength);
            CdEntry anotherEntry = another.entries.get(iLength);
            if (!thisEntry.equals(anotherEntry))
                return false;
        }
        return true;
    }

}
