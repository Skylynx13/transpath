package com.skylynx13.transpath.store;

import java.io.File;

import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.utils.DateUtils;
import com.skylynx13.transpath.utils.FileUtils;
import com.skylynx13.transpath.utils.StringUtils;
import com.skylynx13.transpath.utils.TransConst;

/**
 * ClassName: StoreNode
 * Description: Store node
 * <sid>:<size>:<time>:<md5>:<sha1>:<crc32>:<spath>:<sname>
 * Date: 2016-05-16 14:04:15
 * @author skylynx
 */
public class StoreNode implements Cloneable {
    private int id;
    private String path;
    private String name;
    private String rawName;

    private long length;

    private long lastModified;
    private String md5;
    private String sha1;
    private String crc32;

    private boolean branch = false;

    public boolean isBranch() {
        return branch;
    }

    public void setBranch(boolean branch) {
        this.branch = branch;
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

    public StoreNode() {
        id = 0;
        path = "";
        name = "";
        rawName = "";
        length = 0;
        lastModified = 0;
        md5 = "";
        sha1 = "";
        crc32 = "";
        branch = false;
    }

    public StoreNode(String sEntry) {
        String[] sItems = sEntry.split(TransConst.COLON);
        id = Integer.parseInt(sItems[0]);
        length = Long.parseLong(sItems[1]);
        lastModified = Long.parseLong(sItems[2]);
        md5 = sItems[3];
        sha1 = sItems[4];
        crc32 = sItems[5];
        path = sItems[6];
        name = sItems[7];
        // Keep if statement
        // while compatibility for old store files is required.
        rawName = "";
        if (sItems.length > 8) {
            rawName = sItems[8];
        }
        branch = false;
    }

    StoreNode(String pRoot, File pFile) {
        id = 0;
        name = pFile.getName();
        rawName = pFile.getName();
        path = FileUtils.regulateRelativePath(pRoot, pFile);
        length = pFile.length();
        lastModified = pFile.lastModified();
        md5 = FileUtils.digestMd5(pFile);
        sha1 = FileUtils.digestSha(pFile);
        crc32 = FileUtils.digestCrc32(pFile);
    }

    StoreNode(String pRoot, File pFile, boolean forBrowse) {
        id = 0;
        name = "";
        rawName = pFile.getName();
        path = FileUtils.regulateRelativePath(pRoot, pFile);
        length = pFile.length();
        lastModified = pFile.lastModified();
        md5 = "";
        sha1 = "";
        crc32 = "";
    }

    static StoreNode newBranchNode(String name, String path) {
        StoreNode branchNode = new StoreNode();
        branchNode.setName(name);
        branchNode.setPath(path);
        branchNode.setBranch(true);
        return branchNode;
    }

    @Override
    public StoreNode clone() {
        StoreNode storeNode = null;
        try {
            storeNode = (StoreNode) super.clone();
        } catch (CloneNotSupportedException e) {
            TransLog.getLogger().error("StoreNode clone error: {}", e.getMessage());
            TransLog.getLogger().error("", e);
        }
        return storeNode;
    }

    boolean checkDupStoreNode(StoreNode pStoreNode) {
        return (null != pStoreNode)
                && (pStoreNode.length == this.length)
                && ((pStoreNode.crc32.equals(this.crc32))
                || (pStoreNode.md5.equals(this.md5))
                || (pStoreNode.sha1.equals(this.sha1)));
    }

    boolean searchName(String searchText) {
        return search(this.name, searchText);
    }

    private boolean search(String member, String searchText) {
        return member.matches("(?i).*" + searchText + ".*");
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

    Object[] toStoreRow() {
        return new Object[]{
                id,
                path,
                name,
                StringUtils.formatLongInt(length),
                DateUtils.formatDateTimeLong(lastModified),
                md5,
                sha1,
                crc32
        };
    }

    Object[] toBranchRow(long length, int count) {
        return new Object[]{
                id,
                path,
                name,
                StringUtils.formatLongInt(length),
                count
        };
    }
}
