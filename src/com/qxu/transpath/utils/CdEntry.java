/**
 * Copyright (c) 2014,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.utils
 * File Name:CdEntry.java
 * Date:Apr 23, 2014 11:38:09 PM
 * 
 */
package com.qxu.transpath.utils;

import java.util.ArrayList;

 /**
 * ClassName: CdEntry <br/>
 * Description: Content destination entry <br/>
 * Date: Apr 23, 2014 11:38:09 PM <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class CdEntry implements Comparable<CdEntry>{
    private String name;
    private ArrayList<String> comments;
    private ArrayList<String> links;
    public CdEntry() {
        this.name = "";
        this.comments = new ArrayList<String>();
        this.links = new ArrayList<String>();
    }
    public CdEntry(String name) {
        this.name = name;
        this.comments = new ArrayList<String>();
        this.links = new ArrayList<String>();
    }
    public CdEntry(String name, String comment, String link) {
        this.name = name;
        this.comments = new ArrayList<String>();
        this.comments.add(comment);
        this.links = new ArrayList<String>();
        this.links.add(link);
    }
    public CdEntry copy() {
        CdEntry newCde = new CdEntry();
        newCde.setName(this.name);
        newCde.addComments(this.comments);
        newCde.addLinks(this.links);
        return newCde;
    }
    
    private void addLinks(ArrayList<String> pLinks) {
        if (this.links == null) {
            this.links = new ArrayList<String>();
        }
        for (String aLink: pLinks) {
            this.links.add(aLink);
        }
    }
    private void addComments(ArrayList<String> pComments) {
        if (this.comments == null) {
            this.comments = new ArrayList<String>();
        }
        for (String aComment: pComments) {
            this.comments.add(aComment);
        }
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean hasName() {
        return ((this.name != null) && (this.name != ""));
    }
    public ArrayList<String> getComments() {
        return comments;
    }
    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }
    public ArrayList<String> getLinks() {
        return links;
    }
    public void setLinks(ArrayList<String> links) {
        this.links = links;
    }
    public void addComment(String comment) {
        if (this.comments == null) {
            this.comments = new ArrayList<String>();
        }
        this.comments.add(comment);
    }
    public void addLink(String link) {
        if (this.links == null) {
            this.links = new ArrayList<String>();
        }
        this.links.add(link);
    }
    public int getLinksSize() {
        if (this.links == null) {
            return 0;
        }
        return this.links.size();
    }
    
    public int getCommentsSize() {
        if (this.comments == null) {
            return 0;
        }
        return this.comments.size();
    }
    
    @Override
    public int compareTo(CdEntry cde) {
        return this.name.compareTo(cde.name);
    }
    
    public String toString() {
        return name + this.comments.toString() + this.links.toString();
    }
    
    public CdEntry mergeEntry(CdEntry otherEntry) {
        CdEntry newCde = this.copy();
        newCde.addComments(otherEntry.getComments());
        newCde.addLinks(otherEntry.getLinks());
        return newCde;
    }
}
