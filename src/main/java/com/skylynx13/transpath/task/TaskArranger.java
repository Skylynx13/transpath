package com.skylynx13.transpath.task;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;

import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.utils.TransConst;

 /**
 * ClassName: Arranger
 * Description: To arrange raw file and build a task list.
 * Date: 2014-04-23 23:32:59
 */
public class TaskArranger {
    private ArrayList<TaskEntry> entries;
    private TaskEntry entry;
    private int status;
    
    public TaskArranger() {
        this.entries = new ArrayList<>();
    }
    
    public TaskArranger(ArrayList<TaskEntry> entries) {
        this.entries = entries;
    }
    
    void clear() {
        this.entries = null;
    }
    
    void addEntry(String name) {
        if (this.entries == null) {
            this.entries = new ArrayList<>();
        }
        entry = new TaskEntry(name);
        this.entries.add(entry);
    }
    
    void addEntry(TaskEntry cde) {
        if (this.entries == null) {
            this.entries = new ArrayList<>();
        }
        this.entries.add(cde);
    }
    
    public boolean checkLinkLine(String line) {
        return line.matches("[v]?http[s]?://.*");
    }
    
    public boolean checkCommentLine(String line) {
        return line.matches("//.*");
    }
    
    public TaskArranger readFromFile(String fileName) {
        Scanner in;
        String line;
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
                if (this.entry == null || this.entry.hasNoName()) {
                    status = 200000 + cnt; // Error: Entry no name at line cnt.
                    in.close();
                    return null;
                }
                entry.addUniqueLink(line);
            } else if (this.checkCommentLine(line)) {
                if (this.entry == null || this.entry.hasNoName()) {
                    status = 200000 + cnt; // Error: Entry no name at line cnt.
                    in.close();
                    return null;
                }
                entry.addComment(line);                    
            } else {
                this.addEntry(line);
            }
            cnt++;
        }
        in.close();
        return this;
    }
    
    public int writeToFile(String fileName) {
        try {
            PrintWriter out = new PrintWriter(fileName);
            out.print(this.toOutput());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return entries.size();
    }
    
    int appendToFile(String fileName) {
        if (entries.size() == 0) {
            return 0;
        }
        try {
            PrintWriter out = new PrintWriter(new FileWriter(fileName,true));
            out.print(this.toOutput());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entries.size();
    }
    
    public TaskArranger sort() {
        Collections.sort(entries);
        return this;
    }
    
    public TaskArranger merge() {
        TaskArranger newArranger = new TaskArranger();
        TaskEntry newEntry = null;
        for (TaskEntry currEntry:this.entries) {
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
        StringBuilder str = new StringBuilder();
        if (this.entries == null) 
            return str.toString();
        for (TaskEntry cde:this.entries) {
            str.append(cde.getName()).append(TransConst.LN);
            for (String cmt: cde.getComments()) {
                str.append(cmt).append(TransConst.LN);
            }
            for (String lnk: cde.getLinks()) {
                str.append(lnk).append(TransConst.LN);
            }
        }
        return str.toString();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    int trimFile(String inFile, String outFile) {
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
        
        while (Objects.requireNonNull(in).hasNext()) {
            iInLine++;
            String inLine = in.nextLine();
            if (isInWindow && checkIgnorableLine(inLine)) {
                continue;
            }
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
                Objects.requireNonNull(out).println(inLine);
                iOutLine++;
            }
        }

        in.close();
        if (out != null) {
            out.close();
        }
        return iOutLine;
    }
    
    private boolean checkStartLine(String line) {
        return line.matches("^(Default|Video|Post) Re: .*$") 
                || line.matches("^Default$")
                //|| line.matches("^(Hari ini|Kemarin|Today|Yesterday|\\d{2}-\\d{2}-\\d{4}) \\d{2}:\\d{2}$")
                || line.matches("^Kaskus (Donator|Geek|Addict) .*$")
                || line.matches("^Posts: .*$");
    }

    private boolean checkEndLine(String line) {
        return line.matches("^\\w* is offline\\s*Reply With Quote$") 
                || line.matches("^(0)?\\s*Multi Quote\\s*Quote$")
                //|| line.matches("^Week of \\d{2}/\\d{2}/\\d{4}$")
                // Open this line if want to ignore weekly list from the dump.
                || line.matches("^\\s*Last edited by.*$")
                || line.matches("^\\s*Kutip\\s*Balas$");
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
                || line.matches("^\\s*This image has been resized.*$")
                || line.matches("^#\\d+$")
                || line.matches("^\\s*https://kask\\.us/izmSo\\s*$");
    }
    
    TaskArranger applyFilter(String[] keys) {
        TaskArranger newArranger = new TaskArranger();
        for (TaskEntry currEntry : this.entries) {
            if (currEntry.matches(keys))
                newArranger.addEntry(currEntry);
        }
        return newArranger;
    }
    
    public boolean equals(TaskArranger another) {
        if (this.entries.size() != another.entries.size())
            return false;

        for (int iLength = 0; iLength < this.entries.size(); iLength++) {
            TaskEntry thisEntry = this.entries.get(iLength);
            TaskEntry anotherEntry = another.entries.get(iLength);
            if (!thisEntry.equals(anotherEntry))
                return false;
        }
        return true;
    }

}
