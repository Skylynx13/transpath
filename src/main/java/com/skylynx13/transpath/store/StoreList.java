package com.skylynx13.transpath.store;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.tree.Node;
import com.skylynx13.transpath.tree.NodeList;
import com.skylynx13.transpath.utils.FileUtils;
import com.skylynx13.transpath.utils.TransConst;

/**
 * ClassName: StoreList
 * Description: Store list
 * Date: 2016-06-17 12:05:40
 */
public class StoreList extends NodeList {
    private long fileSize; // recap, clear

    long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    StoreList() {
        fileSize = 0;
    }

    public StoreList(StoreList pList) {
        super(pList);
        calcFileSize();
    }

    public StoreList(String fileName) {
        super(fileName);
        calcFileSize();
    }

    @Override
    public void clear() {
        super.clear();
        fileSize = 0;
    }

    @Override
    public void recap() {
        super.recap();
        calcFileSize();
    }

    private void calcFileSize() {
        fileSize = 0;
        for (Node aNode : nodeList) {
            fileSize += ((StoreNode) aNode).length;
        }
    }

    void build(String pRoot, String pPathName) {
        String aRoot = FileUtils.regulateSlash(pRoot);
        String aPathName = aRoot + FileUtils.regulateSlash(pPathName);

        if (new File(aRoot).isFile() || new File(aPathName).isFile()) {
            return;
        }

        buildByPath(aRoot, aPathName);
        recap();
    }

    private void buildByPath(String pRoot, String pPathName) {
        buildByPath(pRoot, new File(pPathName));
    }

    private void buildByPath(String pRoot, File pPath) {
        clear();

        if (!pPath.isDirectory() || pPath.listFiles() == null) {
            return;
        }
        TransLog.getLogger().info(pPath);
        long processedBytes = 0;
        for (File aFile : Objects.requireNonNull(pPath.listFiles())) {
            if (aFile.isFile()) {
                StoreNode aNode = new StoreNode(pRoot, aFile);
                addNode(aNode);
                TransLog.getLogger().info(aNode.keepNode());
                processedBytes += aNode.length;
                TransLog.getLogger().info("Processed Bytes: " + processedBytes);
            }
            if (aFile.isDirectory()) {
                StoreList aList = new StoreList();
                aList.buildByPath(pRoot, aFile);
                attachList(aList);
            }
        }
    }

    void orderByMd5() {
        nodeList.sort(Comparator.comparing(sn -> ((StoreNode) sn).md5));
    }

    @Override
    public String keepHeader() {
        return version + TransConst.COLON +
                String.format(TransConst.FORMAT_INT_08, minId) + TransConst.COLON +
                String.format(TransConst.FORMAT_INT_08, maxId) + TransConst.COLON +
                String.format(TransConst.FORMAT_INT_20, fileSize) + TransConst.COLON +
                String.format(TransConst.FORMAT_INT_08, size());

    }

    @Override
    public Node loadNode(String pLine) {
        return new StoreNode(pLine);
    }

    @Override
    public String keepLine(Node pNode) {
        return pNode.keepNode();
    }

    @Override
    public Object[] toRow(Node pNode) {
        return ((StoreNode) pNode).toRow();
    }

    @Override
    protected NodeList getNewList() {
        return new StoreList();
    }

    @Override
    public StoreList searchName(String searchText) {
        StoreList storeList = (StoreList) super.searchName(searchText);
        storeList.calcFileSize();
        return storeList;
    }
}
