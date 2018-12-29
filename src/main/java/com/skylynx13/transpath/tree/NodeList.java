package com.skylynx13.transpath.tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

import com.skylynx13.transpath.store.StoreList;
import com.skylynx13.transpath.utils.DateUtils;
import com.skylynx13.transpath.utils.StringUtils;
import com.skylynx13.transpath.utils.TransConst;

/**
 * ClassName: NodeList
 * Description: Node list
 * Date: 2016-07-12 15:10:19
 */
public abstract class NodeList {
    public String version;
    public int minId;
    public int maxId;
    public ArrayList<Node> nodeList;

    public NodeList() {
        refreshVersion();
        minId = 0;
        maxId = 0;
        nodeList = new ArrayList<Node>();
    }

    public NodeList(NodeList pNodeList) {
        refreshVersion();
        nodeList = new ArrayList<Node>(pNodeList.nodeList);
        recap();
    }

    public NodeList(String fileName) {
        nodeList = new ArrayList<Node>();
        load(fileName);
    }

    public NodeList(ArrayList<Node> pList) {
        refreshVersion();
        nodeList = new ArrayList<Node>(pList);
        recap();
    }

    public int size() {
        return (null == nodeList) ? 0 : nodeList.size();
    }

    public void recap() {
        minId = Integer.MAX_VALUE;
        maxId = 0;
        for (Node aNode : nodeList) {
            if (aNode.id < minId) {
                minId = aNode.id;
            }
            if (aNode.id > maxId) {
                maxId = aNode.id;
            }
        }
    }

    public void clear() {
        version = DateUtils.formatDateTimeLongToday();
        minId = 0;
        maxId = 0;
        nodeList = new ArrayList<Node>();
    }

    public Node get(int index) {
        return nodeList.get(index);
    }

    public Node getById(int pId) {
        for (Node aNode : nodeList) {
            if (aNode.id == pId) {
                return aNode;
            }
        }
        return null;
    }

    public ArrayList<Integer> getIdList() {
        ArrayList<Integer> idList = new ArrayList<Integer>();
        for (Node aNode : nodeList) {
            idList.add(aNode.id);
        }
        return idList;
    }

    public NodeList getListByIds(ArrayList<Integer> ids) {
        NodeList rNodeList = getNewList();
        for (Node node : nodeList) {
            if (ids.contains(node.id)) {
                rNodeList.addNode(node.clone());
            }
        }
        return rNodeList;
    }

    public boolean hasNode(Node pNode) {
        return nodeList.contains(pNode);
    }

    public int addNode(Node pNode) {
        if (0 == size()) {
            minId = 1;
        }
        pNode.id = ++maxId;
        nodeList.add(pNode);
        return pNode.id;
    }

    public void enlist(Node pNode) {
        if (0 == size()) {
            minId = pNode.id;
            maxId = pNode.id;
        }
        minId = Math.min(minId, pNode.id);
        maxId = Math.max(maxId, pNode.id);
        nodeList.add(pNode);
    }

    public HashMap<Integer, Integer> attachList(NodeList pList) {
        HashMap<Integer, Integer> aMap = new HashMap<Integer, Integer>();
        for (Node aNode : pList.nodeList) {
            int oldId = aNode.id;
            addNode(aNode);
            if (oldId != aNode.id) {
                aMap.put(oldId, aNode.id);
            }
        }

        //Return value only used by test.
        return aMap;
    }

    public void removeById(int pId) {
        ArrayList<Node> removeList = new ArrayList<Node>();
        for (Node aNode : nodeList) {
            if (aNode.id == pId) {
                removeList.add(aNode);
            }
        }
        nodeList.removeAll(removeList);
        recap();
    }

    public void removeByIdMap(HashMap<Integer, Integer> idMap) {
        ArrayList<Node> removeList = new ArrayList<Node>();
        for (Node aNode : nodeList) {
            if (idMap.containsKey(aNode.id)) {
                removeList.add(aNode);
            }
        }
        nodeList.removeAll(removeList);
        recap();
    }

    public void removeByPath(String pPath) {
        ArrayList<Node> removeList = new ArrayList<Node>();
        for (Node aNode : nodeList) {
            if (aNode.path.equals(pPath)) {
                removeList.add(aNode);
            }
        }
        nodeList.removeAll(removeList);
        recap();
    }

    public void refreshVersion() {
        version = DateUtils.formatDateTimeLongToday();
    }

    public HashMap<Integer, Integer> reorgId() {
        HashMap<Integer, Integer> aMap = new HashMap<Integer, Integer>();
        int newId = 0;
        for (Node aNode : nodeList) {
            newId++;
            if (aNode.id != newId) {
                aMap.put(aNode.id, newId);
                aNode.id = newId;
            }
        }
        recap();
        return aMap;
    }

    public void load(String pFileName) {
        load(new File(pFileName));
    }

    public void load(File pFile) {
        clear();
        Scanner aScan = null;
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
            nodeList.add(loadNode(aScan.nextLine()));
        }
        aScan.close();
        recap();
    }

    public abstract Node loadNode(String pLine);

    public void loadVersion(String pLine) {
        version = pLine.split(TransConst.COLON)[0];
    }

    public void orderById() {
        Collections.sort(nodeList, new Comparator<Node>() {
            @Override
            public int compare(Node sn1, Node sn2) {
                return ((Integer) sn1.id).compareTo(sn2.id);
            }
        });
    }

    public void orderByPathAndName() {
        Collections.sort(nodeList, new Comparator<Node>() {
            @Override
            public int compare(Node sn1, Node sn2) {
                int cmp = sn1.path.compareTo(sn2.path);
                if (cmp != 0) {
                    return cmp;
                }
                return sn1.name.compareTo(sn2.name);
            }
        });
    }

    public String keepHeader() {
        return new StringBuffer(version).append(TransConst.COLON)
                .append(String.format(TransConst.FORMAT_INT_08, minId)).append(TransConst.COLON)
                .append(String.format(TransConst.FORMAT_INT_08, maxId)).append(TransConst.COLON)
                .append(String.format(TransConst.FORMAT_INT_08, size())).toString();

    }

    public void keepFile(String pFileName) {
        File aFile = new File(pFileName);
        keepFile(aFile);
    }

    public void keepFile(File pFile) {
        if (0 == size()) {
            return;
        }
        try {
            PrintWriter out = new PrintWriter(pFile);
            out.println(keepHeader());
            for (Node aNode : nodeList) {
                out.println(keepLine(aNode));
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected abstract String keepLine(Node pNode);

    public String toString() {
        StringBuffer strBuff = new StringBuffer(keepHeader());
        strBuff.append(TransConst.CRLN);
        for (Node aNode : nodeList) {
            strBuff.append(keepLine(aNode)).append(TransConst.CRLN);
        }
        return strBuff.toString();
    }

    public static ArrayList<Integer> FindIdOnlyInAList(ArrayList<Integer> aList, ArrayList<Integer> bList) {
        ArrayList<Integer> idList = new ArrayList<Integer>();
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
        for (Node aNode : nodeList) {
            rows[iNode] = toRow(aNode);
            iNode++;
        }
        return rows;
    }

    protected abstract Object[] toRow(Node aNode);

    public NodeList searchName(String searchText) {
        NodeList foundList = getNewList();
        if (0 == size() || StringUtils.isEmpty(searchText)) {
            return foundList;
        }
        for (Node aNode : nodeList) {
            if (aNode.searchName(searchText)) {
                foundList.enlist(aNode);
            }
        }
        return foundList;
    }

    public NodeList searchPath(String searchText) {
        NodeList foundList = getNewList();
        if (0 == size() || StringUtils.isEmpty(searchText)) {
            return foundList;
        }
        for (Node aNode : nodeList) {
            if (aNode.searchPath(searchText)) {
                foundList.enlist(aNode);
            }
        }
        return foundList;
    }

    protected abstract NodeList getNewList();

}
