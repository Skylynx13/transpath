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
        for (String aLink: pLinks) {
            this.addLink(aLink);
        }
    }
    private void addUniqueLinks(ArrayList<String> pLinks) {
        for (String aLink: pLinks) {
            this.addUniqueLink(aLink);
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
    public void addUniqueLink(String link) {
        if (this.links == null) {
            this.links = new ArrayList<String>();
        }
        boolean linkFound = false;
        for (String exLink: this.links){
            if (link.equals(exLink)) {
                linkFound = true;
                break;
            }
        }
        if (!linkFound) {
            this.links.add(link);            
        }
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
        newCde.addUniqueLinks(otherEntry.getLinks());
        return newCde;
    }
    
    public boolean equals(CdEntry another) {
        if (!this.name.equals(another.name))
            return false;
        if (this.comments.size() != another.comments.size())
            return false;
        if (this.links.size() != another.links.size())
            return false;
        for (int iLen = 0; iLen < this.comments.size(); iLen++) {
            String thisComment = this.comments.get(iLen);
            String anotherComment = another.comments.get(iLen);
            if (!thisComment.equals(anotherComment))
                return false;
        }
        for (int iLen = 0; iLen < this.links.size(); iLen++) {
            String thisLink = this.links.get(iLen);
            String anotherLink = another.links.get(iLen);
            if (!thisLink.equals(anotherLink))
                return false;
        }
        return true;
    }
    
    public void clear() {
        this.name = "";
        this.comments.clear();
        this.links.clear();
    }
    public boolean matches(String[] keys) {
        for (String key : keys) {
            key = "(?i).*" + key + ".*";
            if (this.name.matches(key)) 
                return true;
            for (String comment : this.comments) {
                if (comment.matches(key))
                    return true;
            }
            for (String link : this.links) {
                if (link.matches(key))
                    return true;
            }
        }
        return false;
    }
}
