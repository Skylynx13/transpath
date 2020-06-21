package com.skylynx13.transpath.task;

import com.skylynx13.transpath.utils.StringUtils;

import java.util.ArrayList;

/**
 * ClassName: CdEntry
 * Description: Content destination entry
 * Date: 2014-04-23 23:38:09
 * @author skylynx
 */
public class TaskEntry implements Comparable<TaskEntry>{
    private String name;
    private ArrayList<String> comments;
    private ArrayList<String> links;
    private TaskEntry() {
        this.name = "";
        this.comments = new ArrayList<>();
        this.links = new ArrayList<>();
    }
    public TaskEntry(String name) {
        this.name = name;
        this.comments = new ArrayList<>();
        this.links = new ArrayList<>();
    }
    public TaskEntry(String name, String comment, String link) {
        this.name = name;
        this.comments = new ArrayList<>();
        this.comments.add(comment);
        this.links = new ArrayList<>();
        this.links.add(link);
    }

    TaskEntry copy() {
        TaskEntry newCde = new TaskEntry();
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
            this.comments = new ArrayList<>();
        }
        this.comments.addAll(pComments);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    boolean hasNoName() {
        return (StringUtils.isEmpty(this.name));
    }
    ArrayList<String> getComments() {
        return comments;
    }
    ArrayList<String> getLinks() {
        return links;
    }
    public void addComment(String comment) {
        if (this.comments == null) {
            this.comments = new ArrayList<>();
        }
        this.comments.add(comment);
    }
    public void addLink(String link) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(link);
    }
    void addUniqueLink(String link) {
        if (this.links == null) {
            this.links = new ArrayList<>();
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

    @Override
    public int compareTo(TaskEntry cde) {
        return this.name.compareTo(cde.name);
    }
    
    @Override
    public String toString() {
        return name + this.comments.toString() + this.links.toString();
    }
    
    TaskEntry mergeEntry(TaskEntry otherEntry) {
        TaskEntry newCde = this.copy();
        newCde.addComments(otherEntry.getComments());
        newCde.addUniqueLinks(otherEntry.getLinks());
        return newCde;
    }
    
    public boolean equals(TaskEntry another) {
        if (!this.name.equals(another.name)) {
            return false;
        }
        if (this.comments.size() != another.comments.size()) {
            return false;
        }
        if (this.links.size() != another.links.size()) {
            return false;
        }
        for (int iLen = 0; iLen < this.comments.size(); iLen++) {
            String thisComment = this.comments.get(iLen);
            String anotherComment = another.comments.get(iLen);
            if (!thisComment.equals(anotherComment)) {
                return false;
            }
        }
        for (int iLen = 0; iLen < this.links.size(); iLen++) {
            String thisLink = this.links.get(iLen);
            String anotherLink = another.links.get(iLen);
            if (!thisLink.equals(anotherLink)) {
                return false;
            }
        }
        return true;
    }
    
    boolean matches(String[] keys) {
        for (String key : keys) {
            key = "(?i).*" + key + ".*";
            if (this.name.matches(key)) {
                return true;
            }
            for (String comment : this.comments) {
                if (comment.matches(key)) {
                    return true;
                }
            }
            for (String link : this.links) {
                if (link.matches(key)) {
                    return true;
                }
            }
        }
        return false;
    }
}
