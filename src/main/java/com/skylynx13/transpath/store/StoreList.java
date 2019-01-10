package com.skylynx13.transpath.store;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.utils.DateUtils;
import com.skylynx13.transpath.utils.FileUtils;
import com.skylynx13.transpath.utils.StringUtils;
import com.skylynx13.transpath.utils.TransConst;

/**
 * ClassName: StoreList
 * Description: Store list
 * Date: 2016-06-17 12:05:40
 */
public class StoreList {
    private String version;
    private int minId;
    int maxId;
    private long storeSize; // recap, clear
    ArrayList<StoreNode> storeList;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getMinId() {
        return minId;
    }

    public void setMinId(int minId) {
        this.minId = minId;
    }

    public int getMaxId() {
        return maxId;
    }

    public void setMaxId(int maxId) {
        this.maxId = maxId;
    }

    public ArrayList<StoreNode> getStoreList() {
        return storeList;
    }

    public void setStoreList(ArrayList<StoreNode> storeList) {
        this.storeList = storeList;
    }

    long getStoreSize() {
        return storeSize;
    }

    public void setStoreSize(long storeSize) {
        this.storeSize = storeSize;
    }

    public StoreList() {
        refreshVersion();
        minId = 0;
        maxId = 0;
        storeSize = 0;
        storeList = new ArrayList<>();
    }

    public StoreList(StoreList pList) {
        refreshVersion();
        storeList = new ArrayList<>(pList.storeList);
        recap();
        calcSize();
    }

    public StoreList(String fileName) {
        storeList = new ArrayList<>();
        load(fileName);
        calcSize();
    }

    public StoreList(ArrayList<StoreNode> pList) {
        refreshVersion();
        storeList = new ArrayList<>(pList);
        recap();
    }

    public int size() {
        return (null == storeList) ? 0 : storeList.size();
    }

    void recap() {
        minId = Integer.MAX_VALUE;
        maxId = 0;
        for (StoreNode aNode : storeList) {
            if (aNode.getId() < minId) {
                minId = aNode.getId();
            }
            if (aNode.getId() > maxId) {
                maxId = aNode.getId();
            }
        }
        calcSize();
    }

    private void clear() {
        version = DateUtils.formatDateTimeLongToday();
        minId = 0;
        maxId = 0;
        storeSize = 0;
        storeList = new ArrayList<>();
    }

    public StoreNode get(int index) {
        return storeList.get(index);
    }

    public StoreNode getById(int pId) {
        for (StoreNode aNode : storeList) {
            if (aNode.getId() == pId) {
                return aNode;
            }
        }
        return null;
    }

    public ArrayList<Integer> getIdList() {
        ArrayList<Integer> idList = new ArrayList<>();
        for (StoreNode aNode : storeList) {
            idList.add(aNode.getId());
        }
        return idList;
    }

    public StoreList getListByIds(ArrayList<Integer> ids) {
        StoreList rNodeList = getNewList();
        for (StoreNode node : storeList) {
            if (ids.contains(node.getId())) {
                rNodeList.addNode(node.getClone());
            }
        }
        return rNodeList;
    }

    boolean hasNode(StoreNode pNode) {
        return storeList.contains(pNode);
    }

    void addNode(StoreNode pNode) {
        if (0 == size()) {
            minId = 1;
        }
        pNode.setId(++maxId);
        storeList.add(pNode);
    }

    private void enlist(StoreNode pNode) {
        if (0 == size()) {
            minId = pNode.getId();
            maxId = pNode.getId();
        }
        minId = Math.min(minId, pNode.getId());
        maxId = Math.max(maxId, pNode.getId());
        storeList.add(pNode);
    }

    HashMap<Integer, Integer> attachList(StoreList pList) {
        HashMap<Integer, Integer> aMap = new HashMap<>();
        for (StoreNode aNode : pList.storeList) {
            int oldId = aNode.getId();
            addNode(aNode);
            if (oldId != aNode.getId()) {
                aMap.put(oldId, aNode.getId());
            }
        }

        //Return value only used by test.
        return aMap;
    }

    public void removeById(int pId) {
        ArrayList<StoreNode> removeList = new ArrayList<>();
        for (StoreNode aNode : storeList) {
            if (aNode.getId() == pId) {
                removeList.add(aNode);
            }
        }
        storeList.removeAll(removeList);
        recap();
    }

    public void removeByIdMap(HashMap<Integer, Integer> idMap) {
        ArrayList<StoreNode> removeList = new ArrayList<>();
        for (StoreNode aNode : storeList) {
            if (idMap.containsKey(aNode.getId())) {
                removeList.add(aNode);
            }
        }
        storeList.removeAll(removeList);
        recap();
    }

    void removeByPath(String pPath) {
        ArrayList<StoreNode> removeList = new ArrayList<>();
        for (StoreNode aNode : storeList) {
            if (aNode.getPath().equals(pPath)) {
                removeList.add(aNode);
            }
        }
        storeList.removeAll(removeList);
        recap();
    }

    private void refreshVersion() {
        version = DateUtils.formatDateTimeLongToday();
    }

    HashMap<Integer, Integer> reorgId() {
        HashMap<Integer, Integer> aMap = new HashMap<>();
        int newId = 0;
        for (StoreNode aNode : storeList) {
            newId++;
            if (aNode.getId() != newId) {
                aMap.put(aNode.getId(), newId);
                aNode.setId(newId);
            }
        }
        recap();
        return aMap;
    }

    void load(String pFileName) {
        load(new File(pFileName));
    }

    private void load(File pFile) {
        clear();
        Scanner aScan;
        try {
            aScan = new Scanner(new FileReader(pFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        if (aScan.hasNext()) {
            loadVersion(aScan.nextLine());
        }
        while (aScan.hasNext()) {
            storeList.add(loadNode(aScan.nextLine()));
        }
        aScan.close();
        recap();
    }

    private void loadVersion(String pLine) {
        version = pLine.split(TransConst.COLON)[0];
    }

    public void orderById() {
        storeList.sort(Comparator.comparingInt(StoreNode::getId));
    }

    void orderByPathAndName() {
        storeList.sort((sn1, sn2) -> {
            int cmp = sn1.getPath().compareTo(sn2.getPath());
            if (cmp != 0) {
                return cmp;
            }
            return sn1.getName().compareTo(sn2.getName());
        });
    }

    private String keepHeader() {
        return version + TransConst.COLON +
                String.format(TransConst.FORMAT_INT_08, minId) + TransConst.COLON +
                String.format(TransConst.FORMAT_INT_08, maxId) + TransConst.COLON +
                String.format(TransConst.FORMAT_INT_20, storeSize) + TransConst.COLON +
                String.format(TransConst.FORMAT_INT_08, size());

    }

    void keepFile(String pFileName) {
        File aFile = new File(pFileName);
        keepFile(aFile);
    }

    private void keepFile(File pFile) {
        if (0 == size()) {
            return;
        }
        try {
            PrintWriter out = new PrintWriter(pFile);
            out.println(keepHeader());
            for (StoreNode aNode : storeList) {
                out.println(keepLine(aNode));
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        StringBuilder strBuff = new StringBuilder(keepHeader());
        strBuff.append(TransConst.CRLN);
        for (StoreNode aNode : storeList) {
            strBuff.append(keepLine(aNode)).append(TransConst.CRLN);
        }
        return strBuff.toString();
    }

    public static ArrayList<Integer> FindIdOnlyInAList(ArrayList<Integer> aList, ArrayList<Integer> bList) {
        ArrayList<Integer> idList = new ArrayList<>();
        for (int element : aList) {
            if (!bList.contains(element)) {
                idList.add(element);
            }
        }
        return idList;
    }

    public Object[][] toRows() {
        if (0 == size()) {
            return null;
        }
        Object[][] rows = new Object[size()][];
        int iNode = 0;
        for (StoreNode aNode : storeList) {
            rows[iNode] = toRow(aNode);
            iNode++;
        }
        return rows;
    }

    public StoreList searchName(String searchText) {
        StoreList foundList = getNewList();
        if (0 == size() || StringUtils.isEmpty(searchText)) {
            return foundList;
        }
        for (StoreNode aNode : storeList) {
            if (aNode.searchName(searchText)) {
                foundList.enlist(aNode);
            }
        }
        foundList.calcSize();
        return foundList;
    }

    public StoreList searchPath(String searchText) {
        StoreList foundList = getNewList();
        if (0 == size() || StringUtils.isEmpty(searchText)) {
            return foundList;
        }
        for (StoreNode aNode : storeList) {
            if (aNode.searchPath(searchText)) {
                foundList.enlist(aNode);
            }
        }
        return foundList;
    }

    private void calcSize() {
        storeSize = 0;
        for (StoreNode aNode : storeList) {
            storeSize += aNode.getLength();
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
                processedBytes += aNode.getLength();
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
        storeList.sort(Comparator.comparing(StoreNode::getMd5));
    }

    private StoreNode loadNode(String pLine) {
        return new StoreNode(pLine);
    }

    private String keepLine(StoreNode pNode) {
        return pNode.keepNode();
    }

    private Object[] toRow(StoreNode pNode) {
        return pNode.toStoreRow();
    }

    private StoreList getNewList() {
        return new StoreList();
    }

}
