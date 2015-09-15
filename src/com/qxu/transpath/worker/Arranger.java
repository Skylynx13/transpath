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
    
    public boolean checkIgnorableLine (String line) {
        return line.matches("\\s*Code:\\s*")||line.matches("\\s*")||
               line.matches("\\s*Quote:\\s*")||line.matches("\\s*//\\s*")||
               line.matches("\\s*Multi Quote Quote\\s*");
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
                entry.addLink(line);
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
    
    public static void main (String[] args) {
        int n = new Arranger().readFromFile("resource/raw.txt").sort().merge().writeToFile("resource/task.txt");
        System.out.println("Totally " + n + " entries processed.");
    }
}
