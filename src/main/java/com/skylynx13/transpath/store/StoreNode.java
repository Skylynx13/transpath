package com.skylynx13.transpath.store;

import java.io.File;

import com.skylynx13.transpath.tree.Node;
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

public class StoreNode extends Node{
    public long length;
    private long lastModified;
    String md5;
    private String sha1;
    private String crc32;

    public StoreNode() {
        length = 0;
        lastModified = 0;
        md5 = "";
        sha1 = "";
        crc32 = "";
    }

    @Override
    public Node clone() {
        StoreNode node = new StoreNode();
        node.id = this.id;
        node.name = this.name;
        node.path = this.path;
        node.length = this.length;
        node.lastModified = this.lastModified;
        node.md5 = this.md5;
        node.sha1 = this.sha1;
        node.crc32 = this.crc32;
        return node;
    }

    StoreNode(String pRoot, File pFile) {
        id = 0;
        name = pFile.getName();
        path = pFile.getParent().replaceAll(TransConst.BACK_SLASH_4, TransConst.SLASH)
                .replaceAll(pRoot, TransConst.EMPTY) + TransConst.SLASH;
        length = pFile.length();
        lastModified = pFile.lastModified();
        md5 = FileUtils.digestMd5(pFile);
        sha1 = FileUtils.digestSha(pFile);
        crc32 = FileUtils.digestCrc32(pFile);
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

    public boolean equals(StoreNode pStoreNode) {
        return (this.id == pStoreNode.id) 
                && (this.length == pStoreNode.length)
                && (this.lastModified == pStoreNode.lastModified) 
                && (this.md5.equals(pStoreNode.md5))
                && (this.sha1.equals(pStoreNode.sha1))
                && (this.crc32.equals(pStoreNode.crc32))
                && (this.path.equals(pStoreNode.path)) 
                && (this.name.equals(pStoreNode.name));
    }

    boolean checkDupNode(StoreNode pStoreNode) {
        return (null != pStoreNode)
                && (pStoreNode.length == this.length)
                && (pStoreNode.crc32.equals(this.crc32))
                && (pStoreNode.md5.equals(this.md5))
                && (pStoreNode.sha1.equals(this.sha1));
    }

    public Object[] toRow() {
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
    
}
