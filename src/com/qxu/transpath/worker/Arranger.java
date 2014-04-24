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

import java.util.ArrayList;
import java.util.Collections;

import com.qxu.transpath.utils.CdEntry;
import com.qxu.transpath.utils.TranspathConstants;

 /**
 * ClassName: Arranger <br/>
 * Description: TODO <br/>
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
        this.entries.add(new CdEntry(name));
    }
    
    public Arranger readFromFile(String fileName) {
        return this;
    }
    
    public int writeToFile(String fileName) {
        
        return entries.size();
    }
    
    public Arranger sort() {
        Collections.sort(entries);
        return this;
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
}
