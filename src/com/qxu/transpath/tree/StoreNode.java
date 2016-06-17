/**
 * Copyright (c) 2016,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:StoreNode.java
 * Date:2016-5-16 下午2:04:15
 * 
 */
package com.qxu.transpath.tree;

import java.io.File;

import com.qxu.transpath.utils.FileUtils;
import com.qxu.transpath.utils.TransConst;

/**
 * ClassName: StoreNode <br/>
 * Description: TODO <br/>
 * Date: 2016-5-16 下午2:04:15 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 *         Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 *          <sid>:<size>:<time>:<md5>:<sha>:<crc32>:<spath>:<sname>
 */

public class StoreNode extends Node {
    public long length = 0;
    public long lastModified = 0;
    public String md5 = "";
    public String sha = "";
    public String crc32 = "";
    public String storePath = "";

    public StoreNode() {
        id = 0;
        name = "";
        length = 0;
        lastModified = 0;
        md5 = "";
        sha = "";
        crc32 = "";
        storePath = "";
    }
    
    public StoreNode(Node pNode) {
        id = pNode.id;
        name = pNode.name;
        storePath = pNode.getBranch("1ST");
        String[] meta = pNode.getBranch("0MD").split("/");
        length = Long.parseLong(meta[1]);
        lastModified = Long.parseLong(meta[2]);
    }
    
    public StoreNode(String pRoot, File pFile) {
        id = 0;
        name = pFile.getName();
        storePath = pFile.getParent().replaceAll(TransConst.BACK_SLASH_4, TransConst.SLASH)
                .replaceAll(pRoot, TransConst.EMPTY) + TransConst.SLASH;
        length = pFile.length();
        lastModified = pFile.lastModified();
        md5 = FileUtils.digestMd5(pFile);
        sha = FileUtils.digestSha(pFile);
        crc32 = FileUtils.digestCrc32(pFile);
    }

    public StoreNode(String sEntry) {
        String[] sItems = sEntry.split(TransConst.COLON);
        id = Integer.parseInt(sItems[0]);
        length = Long.parseLong(sItems[1]);
        lastModified = Long.parseLong(sItems[2]);
        md5 = sItems[3];
        sha = sItems[4];
        crc32 = sItems[5];
        storePath = sItems[6];
        name = sItems[7];
    }

    @Override
    public String keepNode() {
        return new StringBuffer(String.format(TransConst.FORMAT_INT_08, id))
                        .append(TransConst.COLON)
                        .append(String.format(TransConst.FORMAT_INT_13, length))
                        .append(TransConst.COLON)
                        .append(lastModified)
                        .append(TransConst.COLON)
                        .append(md5)
                        .append(TransConst.COLON)
                        .append(sha)
                        .append(TransConst.COLON)
                        .append(crc32)
                        .append(TransConst.COLON)
                        .append(storePath)
                        .append(TransConst.COLON)
                        .append(name)
                        .toString();
    }
    
    public boolean equals(StoreNode pStoreNode) {
        return super.equals(pStoreNode) 
            && (this.length == pStoreNode.length)
            && (this.lastModified == pStoreNode.lastModified)
            && (this.md5.equals(pStoreNode.md5))
            && (this.sha.equals(pStoreNode.sha))
            && (this.crc32.equals(pStoreNode.crc32))
            && (this.storePath.equals(pStoreNode.storePath));
    }
    
    public boolean equalsSimple(StoreNode pStoreNode) {
        return super.equals(pStoreNode);
    }
}
