package com.skylynx13.transpath.store;

import com.skylynx13.transpath.utils.TransConst;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.*;

/**
 * ClassName: StoreTree
 * Description: A Tree with a Node.
 * If this is not enough for use,
 * reference to Class DefaultMutableTreeNode.
 * Date: 2014-04-05 22:47:08
 */
public class StoreTree implements MutableTreeNode {
    private static class NodeNameAscComparator implements Comparator<StoreTree> {

        @Override
        public int compare(StoreTree o1, StoreTree o2) {
            if (o1.isBranch() == o2.isBranch()) {
                return o1.getNodeName().compareTo(o2.getNodeName());
            }
            if (o1.isBranch()) {
                return -1;
            }
            return 1;
        }

    }

    private StoreNode node;
    private StoreTree parent;
    private List<StoreTree> children;

    private StoreTree() {
    }

    public boolean isNull() {
        return ((null == this.getNode()) && (null == this.getParent()) && (null == this.getChildren()));
    }

    private StoreTree(StoreNode node) {
        this.setNode(node);
    }

    public StoreNode getNode() {
        return node;
    }

    private void setNode(StoreNode node) {
        this.node = node;
    }

    public StoreTree getParent() {
        return parent;
    }

    private List<StoreTree> getChildren() {
        return children;
    }

    public String[] getChildrenTitle() {
        if (0 == getChildCount()) {
            return TransConst.TABLE_TITLE_STORE;
        }
        for (StoreTree storeTree : getChildren()) {
            StoreNode node = storeTree.getNode();
            if (!node.isBranch()) {
                return TransConst.TABLE_TITLE_STORE;
            }
        }
        return TransConst.TABLE_TITLE_BRANCH;
    }

    public Object[][] getChildrenAsRows() {
        Object[][] rows;
        if (0 == getChildCount()) {
            rows = new Object[1][];
            rows[0] = toRow(this);
            return rows;
        }
        rows = new Object[getChildCount()][];
        int iNode = 0;
        for (StoreTree aStoreTree : getChildren()) {
            rows[iNode] = toRow(aStoreTree);
            iNode++;
        }
        return rows;
    }

    private Object[] toRow(StoreTree storeTree) {
        Object[] row;
        StoreNode aNode = storeTree.getNode();
        if (aNode.isBranch()) {
            long length = storeTree.sumLength();
            row = aNode.toBranchRow(length);
        } else {
            row = aNode.toStoreRow();
        }
        return row;
    }

    private long sumLength() {
        long length = 0;
        for (StoreTree storeTree : this.getChildren()) {
            StoreNode aNode = storeTree.getNode();
            if (aNode.isBranch()){
                length += storeTree.sumLength();
            } else {
                length += aNode.getLength();
            }
        }
        return length;
    }

    private void addChild(StoreTree storeTree) {
        if (null == children) {
            children = new ArrayList<>();
        }
        children.add(storeTree);
        storeTree.parent = this;
    }

    private void addChildNode(StoreNode node) {
        this.addChild(new StoreTree(node));
    }

    @Override
    public StoreTree getChildAt(int index) {
        if (null == children) {
            return null;
        }
        return children.get(index);
    }

    @Override
    public int getChildCount() {
        if (null == children) {
            return 0;
        }
        return children.size();
    }

    private String getNodeName() {
        return node.getName();
    }

    public void recursivelySort() {
        if (null == this.children) {
            return;
        }
        this.children.sort(new NodeNameAscComparator());
        for (StoreTree child : this.children) {
            child.recursivelySort();
        }
    }

    // For JTree display
    @Override
    public String toString() {
        return this.getNodeName();
    }

    private String getNodePathName() {
        String pathName = TransConst.EMPTY;
        if (isNotRoot()) {
            pathName += this.parent.getNodePathName();
        }
        pathName += TransConst.SLASH + this.getNodeName();
        return pathName;
    }

    private StoreTree getChildByName(String aName) {
        if (null == this.children)
            return null;
        for (StoreTree aChild : this.children) {
            if (aChild.node.getName().equals(aName))
                return aChild;
        }
        return null;
    }

    private StoreTree getChildByNameOrAddIt(String aName) {
        StoreTree aStoreTree = getChildByName(aName);
        if (null == aStoreTree) {
            aStoreTree = new StoreTree(StoreNode.newBranchNode(aName, this.getNodePathName()));
            this.addChild(aStoreTree);
        }
        return aStoreTree;
    }

    @Override
    public int getIndex(TreeNode node) {
        return 0;
    }

    @Override
    public boolean getAllowsChildren() {
        return false;
    }

    @Override
    public Enumeration<StoreTree> children() {
        if (this.isLeaf()) {
            return new Enumeration<StoreTree>() {
                public boolean hasMoreElements() {
                    return false;
                }

                public StoreTree nextElement() {
                    throw new NoSuchElementException("No elements here.");
                }
            };
        } else {
            return new Enumeration<StoreTree>() {
                Iterator<StoreTree> iter = children.iterator();

                public boolean hasMoreElements() {
                    return iter.hasNext();
                }

                public StoreTree nextElement() throws NoSuchElementException {
                    return iter.next();
                }
            };
        }
    }

    public boolean isLeaf() {
        return (null == this.children);
    }

    private boolean isNotRoot() {
        return (null != this.parent);
    }

    private boolean isBranch() {
        return !this.isLeaf() && this.isNotRoot();
    }

    private StoreTree addBranch(String pPath) {
        if (this.isNull()) {
            this.setNode(StoreNode.newBranchNode(TransConst.ROOT, "/"));
        }
        String[] nodeNames = pPath.split(TransConst.SLASH);
        StoreTree bTree = this;
        for (String nodeName : nodeNames) {
            if (null != nodeName && !nodeName.isEmpty()) {
                // TransLog.getLogger().info(nodeName);
                bTree = bTree.getChildByNameOrAddIt(nodeName);
            }
        }
        return bTree;
    }

    public static StoreTree buildFromList(StoreList pList) {
        StoreTree aTree = new StoreTree();
        aTree.appendFromList(pList);
        return aTree;
    }
    
    private void appendFromList(StoreList pList) {
        for (StoreNode aNode : pList.storeList) {
            this.addBranch(aNode.getPath()).addChildNode(aNode);
        }
    }

    @Override
    public void insert(MutableTreeNode child, int index) {
    }

    @Override
    public void remove(int index) {
    }

    @Override
    public void remove(MutableTreeNode node) {
    }

    @Override
    public void setUserObject(Object object) {
    }

    @Override
    public void removeFromParent() {
    }

    @Override
    public void setParent(MutableTreeNode newParent) {
    }
}
