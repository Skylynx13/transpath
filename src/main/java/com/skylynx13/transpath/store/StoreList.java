package com.skylynx13.transpath.store;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

import com.skylynx13.transpath.log.TransLog;
import com.skylynx13.transpath.utils.*;

/**
 * ClassName: StoreList
 * Description: Store list
 * Date: 2016-06-17 12:05:40
 * @author skylynx
 */
public class StoreList {
    private String version;
    private int minId;
    int maxId;
    /** recap, clear */
    private long storeSize;
    ArrayList<StoreNode> storeList;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ArrayList<StoreNode> getStoreList() {
        return storeList;
    }

    long getStoreSize() {
        return storeSize;
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
    }

    public StoreList(String fileName) {
        storeList = new ArrayList<>();
        load(new File(fileName));
    }

    public StoreList(ArrayList<StoreNode> pList) {
        refreshVersion();
        storeList = new ArrayList<>(pList);
        recap();
    }

    public int size() {
        return (null == storeList) ? 0 : storeList.size();
    }

    public void recap() {
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

    public StoreNode queryMd5(String md5) {
        for (StoreNode storeNode : this.storeList) {
            if (storeNode.getMd5().equals(md5)) {
                return storeNode;
            }
        }
        return null;
    }

    public StoreNode queryNode(StoreNode pNode) {
        for (StoreNode aNode : this.storeList) {
            if (aNode.checkDupStoreNode(pNode)) {
                return aNode;
            }
        }
        return null;
    }

    public boolean checkIntegrity() {
        //TODO
        return true;
    }

    public StoreList getListByIds(ArrayList<Integer> ids) {
        StoreList rNodeList = getNewList();
        for (StoreNode node : storeList) {
            if (ids.contains(node.getId())) {
                rNodeList.addNodeWithId(node.clone());
            }
        }
        return rNodeList;
    }

    boolean hasNode(StoreNode pNode) {
        return storeList.contains(pNode);
    }

    boolean hasMd5(String md5) {
        return (queryMd5(md5) != null);
    }

    void addNode(StoreNode pNode) {
        storeList.add(pNode);
    }

    void removeNode(StoreNode pNode) {
        storeList.remove(pNode);
    }

    void addNodeWithId(StoreNode pNode) {
        if (0 == size()) {
            minId = 1;
        }
        pNode.setId(++maxId);
        storeList.add(pNode);
    }

    public void enlist(StoreNode pNode) {
        if (0 == size()) {
            minId = pNode.getId();
            maxId = pNode.getId();
        }
        minId = Math.min(minId, pNode.getId());
        maxId = Math.max(maxId, pNode.getId());
        storeList.add(pNode);
    }

    HashMap<Integer, Integer> attachListWithIdMap(StoreList pList) {
        HashMap<Integer, Integer> aMap = new HashMap<>(16);
        for (StoreNode aNode : pList.storeList) {
            int oldId = aNode.getId();
            addNodeWithId(aNode);
            if (oldId != aNode.getId()) {
                aMap.put(oldId, aNode.getId());
            }
        }
        recap();
        //Return value only used by test.
        return aMap;
    }

    void attachListWithId(StoreList pList) {
        for (StoreNode aNode : pList.storeList) {
            addNodeWithId(aNode);
        }
        recap();
    }

    void attachList(StoreList pList) {
        for (StoreNode aNode : pList.storeList) {
            addNode(aNode);
        }
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

    public void refreshVersion() {
        version = DateUtils.formatDateTimeLongToday();
    }

    HashMap<Integer, Integer> reorgId() {
        HashMap<Integer, Integer> aMap = new HashMap<>(16);
        int newId = 0;
        for (StoreNode aNode : storeList) {
            newId++;
            if (aNode.getId() != newId) {
                aMap.put(aNode.getId(), newId);
                aNode.setId(newId);
            }
        }
        return aMap;
    }

    private String buildDefaultStorelistName() {
        return TransProp.get(TransConst.LOC_LIST) + "StoreList.txt";
    }

    StoreList loadCurrent() {
        load(new File(buildDefaultStorelistName()));
        return this;
    }

    private void load(File pFile) {
        clear();
        Scanner aScan;
        try {
            aScan = new Scanner(new FileReader(pFile));
        } catch (FileNotFoundException e) {
            TransLog.getLogger().error("", e);
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

    void orderByPathAndName() {
        storeList.sort(Comparator.comparing(StoreNode::getPath).thenComparing(StoreNode::getName));
    }

    private String keepHeader() {
        return version + TransConst.COLON +
                String.format(TransConst.FORMAT_INT_08, minId) + TransConst.COLON +
                String.format(TransConst.FORMAT_INT_08, maxId) + TransConst.COLON +
                String.format(TransConst.FORMAT_INT_20, storeSize) + TransConst.COLON +
                String.format(TransConst.FORMAT_INT_08, size());
    }

    void backup() {
        int rev = 0;
        while (getBackupRev(rev).exists()) {
            rev = rev == 999 ? 0 : rev + 1;
        }
        keepFile(getBackupRev(rev));
    }

    private File getBackupRev(int rev) {
        return new File(FileUtils.regulateSysPath(TransProp.get(TransConst.LOC_LIST)
                + String.format("Backup/StoreList_%s_%03d.txt", version, rev)));
    }

    void keepFile() {
        keepFile(new File(buildDefaultStorelistName()));
    }

    public void keepFile(File pFile) {
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
            TransLog.getLogger().error("", e);
        }
    }

    public void extractKeyword(File pFile) {
        if (0 == size()) {
            return;
        }
        Set<String> keySet = new HashSet<>();
        try {
            PrintWriter out = new PrintWriter(pFile);
            for (StoreNode aNode : storeList) {
                String[] keyList = aNode.getName().split(" ");
                keySet.addAll(Arrays.asList(keyList));
            }
            for (String key : keySet) {
                out.println(key);
            }
            out.close();
        } catch (FileNotFoundException e) {
            TransLog.getLogger().error("", e);
        }
    }

    @Override
    public String toString() {
        StringBuilder strBuff = new StringBuilder(keepHeader());
        strBuff.append(TransConst.CRLN);
        for (StoreNode aNode : storeList) {
            strBuff.append(keepLine(aNode)).append(TransConst.CRLN);
        }
        return strBuff.toString();
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

    private void calcSize() {
        storeSize = 0;
        for (StoreNode aNode : storeList) {
            storeSize += aNode.getLength();
        }
    }

    void orderByMd5() {
        storeList.sort(Comparator.comparing(StoreNode::getMd5));
    }

    private StoreNode loadNode(String pLine) {
        return new StoreNode(pLine);
    }

    private String keepLine(StoreNode pNode) {
        return pNode.toNodeString();
    }

    private Object[] toRow(StoreNode pNode) {
        return pNode.toStoreRow();
    }

    private StoreList getNewList() {
        return new StoreList();
    }
}
