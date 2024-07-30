package com.skylynx13.transpath.db;

import com.skylynx13.transpath.store.StoreNode;
import com.skylynx13.transpath.utils.TransConst;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "node")
public class DbNode {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "path")
    private String path;

    @Column(name = "name")
    private String name;

    @Column(name = "rawName")
    private String rawName;

    @Column(name = "length")
    private long length;

    @Column(name = "lastModified")
    private long lastModified;

    @Column(name = "md5")
    private String md5;

    @Column(name = "sha1")
    private String sha1;

    @Column(name = "crc32")
    private String crc32;

    @Column(name = "branch")
    private boolean branch = false;

    public DbNode() {

    }

    public DbNode(StoreNode storeNode) {
        this.id = storeNode.getId();
        this.path = storeNode.getPath();
        this.name = storeNode.getName();
        this.rawName = storeNode.getRawName();
        this.length = storeNode.getLength();
        this.lastModified = storeNode.getLastModified();
        this.md5 = storeNode.getMd5();
        this.sha1 = storeNode.getSha1();
        this.crc32 = storeNode.getCrc32();
        this.branch = storeNode.isBranch();
    }

    public StoreNode toStoreNode() {
        StoreNode storeNode = new StoreNode();
        storeNode.setId(this.id);
        storeNode.setPath(this.path);
        storeNode.setName(this.name);
        storeNode.setRawName(this.rawName);
        storeNode.setLength(this.length);
        storeNode.setLastModified(this.lastModified);
        storeNode.setMd5(this.md5);
        storeNode.setSha1(this.sha1);
        storeNode.setCrc32(this.crc32);
        storeNode.setBranch(this.branch);
        return storeNode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRawName() {
        return rawName;
    }

    public void setRawName(String rawName) {
        this.rawName = rawName;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getCrc32() {
        return crc32;
    }

    public void setCrc32(String crc32) {
        this.crc32 = crc32;
    }

    public boolean isBranch() {
        return branch;
    }

    public void setBranch(boolean branch) {
        this.branch = branch;
    }

    public String toNodeString() {
        return String.format(TransConst.FORMAT_INT_08, id) +
                TransConst.COLON +
                String.format(TransConst.FORMAT_INT_13, length) +
                TransConst.COLON +
                lastModified +
                TransConst.COLON +
                md5 +
                TransConst.COLON +
                sha1 +
                TransConst.COLON +
                crc32 +
                TransConst.COLON +
                path +
                TransConst.COLON +
                name +
                TransConst.COLON +
                rawName;
    }
}
