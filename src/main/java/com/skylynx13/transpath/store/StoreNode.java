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
 */

public class StoreNode implements Cloneable {
    private int id;
    private String path;
    private String name;

    private long length;

    private long lastModified;
    private String md5;
    private String sha1;
    private String crc32;

    private boolean branch = false;

    public boolean isBranch() {
        return branch;
    }

    private void setBranch() {
        this.branch = true;
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

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    String getMd5() {
        return md5;
    }

    public StoreNode() {
        id = 0;
        path = "";
        name = "";
        length = 0;
        lastModified = 0;
        md5 = "";
        sha1 = "";
        crc32 = "";
        branch = false;
    }

    StoreNode(String sEntry) {
        String[] sItems = sEntry.split(TransConst.COLON);
        id = Integer.parseInt(sItems[0]);
        length = Long.parseLong(sItems[1]);
        lastModified = Long.parseLong(sItems[2]);
        md5 = sItems[3];
        sha1 = sItems[4];
        crc32 = sItems[5];
        path = sItems[6];
        name = sItems[7];
        branch = false;
    }

    StoreNode(String pRoot, File pFile) {
        id = 0;
        name = pFile.getName();
        path = FileUtils.regulatePath(pFile.getParent().replaceAll(pRoot, TransConst.SLASH) + TransConst.SLASH);
        length = pFile.length();
        lastModified = pFile.lastModified();
        md5 = FileUtils.digestMd5(pFile);
        sha1 = FileUtils.digestSha(pFile);
        crc32 = FileUtils.digestCrc32(pFile);
    }

    static StoreNode newBranchNode(String name, String path) {
        StoreNode branchNode = new StoreNode();
        branchNode.setName(name);
        branchNode.setPath(path);
        branchNode.setBranch();
        return branchNode;
    }

    @Override
    public StoreNode clone() {
        StoreNode storeNode = null;
        try {
            storeNode = (StoreNode) super.clone();
        } catch (CloneNotSupportedException e) {
            TransLog.getLogger().error("StoreNode clone error: " + e.getMessage());
            e.printStackTrace();
        }
        return storeNode;
    }

    boolean checkDupStoreNode(StoreNode pStoreNode) {
        return (null != pStoreNode)
                && (pStoreNode.length == this.length)
                && (pStoreNode.crc32.equals(this.crc32))
                && (pStoreNode.md5.equals(this.md5))
                && (pStoreNode.sha1.equals(this.sha1));
    }

    boolean searchName(String searchText) {
        return search(this.name, searchText);
    }

    private boolean search(String member, String searchText) {
        return member.matches("(?i).*" + searchText + ".*");
    }

    public String keepNode() {
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
                name;
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

    Object[] toBranchRow(long length) {
        return new Object[]{
                id,
                path,
                name,
                StringUtils.formatLongInt(length)
        };
    }

}
