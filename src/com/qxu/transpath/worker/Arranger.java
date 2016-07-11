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
import com.qxu.transpath.utils.StrUtils;
import com.qxu.transpath.utils.TransConst;

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
            //System.out.println(line);
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
    
    public int writeIndexToFile(String fileName) {
        try {
            PrintWriter out = new PrintWriter(fileName);
            out.println(this.toIndex());
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
            str += cde.getName() + TransConst.LINE_LINKER;
            for (String cmt: cde.getComments()) {
                str += cmt + TransConst.LINE_LINKER;
            }
            for (String lnk: cde.getLinks()) {
                str += lnk + TransConst.LINE_LINKER;
            }
        }
        return str;
    }

    public String toIndex() {
        String str = "";
        if (this.entries == null) 
            return str;
        for (CdEntry cde:this.entries) {
            str += cde.getName() + TransConst.LINE_LINKER;
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
                System.out.println("Last End Line " + iLastClose + ": " + sLastClose);
                System.out.println("This End Line " + iInLine + ": " + inLine);
            }
            if (!isInWindow && checkLinkLine(inLine)) {
                System.out.println("Error: Link line out of window!");
                System.out.println("Last End Line: " + iLastClose + ": " + sLastClose);
                System.out.println("This Link Line " + iInLine + ": " + inLine);
            }
            if (isInWindow && checkStartLine(inLine)) {
                System.out.println("Error: Start line inside window!");
                System.out.println("Last Start Line: " + iLastOpen + ": " + sLastOpen);
                System.out.println("This Start Line " + iInLine + ": " + inLine);
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
    
    public boolean checkStartLine (String line) {
        return line.matches("^(Default|Video) Re: .*$") 
                || line.matches("^Default$")
                || line.matches("^(Today|Yesterday|\\d{2}-\\d{2}-\\d{4}) \\d{2}:\\d{2}$");
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
    
    public String findSameEntries(Arranger otherArranger) {
        StringBuffer result = new StringBuffer();
        int i1=0;
        for (CdEntry cde1 : this.entries) {
            i1++;
            int i2 = 0;
            for (CdEntry cde2 : otherArranger.entries) {
                i2++;
                String simpleName2 = StrUtils.getSimpleName(cde2.getName());
                if (cde1.getName().equalsIgnoreCase(simpleName2) 
                        || cde1.getName().startsWith(simpleName2)
                        || simpleName2.startsWith(cde1.getName())) {
                    result.append("New Line " + i1 + ": " + cde1.getName() + TransConst.LINE_LINKER
                            +"Old Line " + i2 + ": " + cde2.getName() + TransConst.LINE_LINKER); 
                }
                if (i2%10000==0) {
                    System.out.println("i1="+i1+"; i2="+i2);
                }
            }
        }
        return result.toString();
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
