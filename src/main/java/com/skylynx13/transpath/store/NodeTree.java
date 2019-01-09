package com.skylynx13.transpath.store;

import com.skylynx13.transpath.utils.TransConst;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.*;

/**
 * ClassName: NodeTree
 * Description: A Tree with a Node.
 * If this is not enough for use,
 * reference to Class DefaultMutableTreeNode.
 * Date: 2014-04-05 22:47:08
 */
public class NodeTree implements MutableTreeNode {
    private static class NodeNameAscComparator implements Comparator<NodeTree> {

        @Override
        public int compare(NodeTree o1, NodeTree o2) {
            if (o1.isBranch() == o2.isBranch()) {
                return o1.getNodeName().compareTo(o2.getNodeName());
            }
            if (o1.isBranch()) {
                return -1;
            }
            return 1;
        }

    }

    @SuppressWarnings("unused")
    private static class NodeNameDescComparator implements Comparator<NodeTree> {

        @Override
        public int compare(NodeTree o1, NodeTree o2) {
            return o2.getNodeName().compareTo(o1.getNodeName());
        }

    }

    private Node node;
    private NodeTree parent;
    private List<NodeTree> children;
    private boolean allowsChildren;

    private NodeTree() {
    }

    public boolean isNull() {
        return ((null == this.getNode()) && (null == this.getParent()) && (null == this.getChildren()));
    }

    public NodeTree(Node node) {
        this.setNode(node);
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public NodeTree getParent() {
        return parent;
    }

    private void setParent(NodeTree parent) {
        this.parent = parent;
    }

    private List<NodeTree> getChildren() {
        return children;
    }

    public String[] getChildrenTitle() {
        if (0 == getChildCount()) {
            return TransConst.TABLE_TITLE_STORE;
        }
        for (NodeTree nodeTree : getChildren()) {
            Node node = nodeTree.getNode();
            if (node instanceof StoreNode) {
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
        for (NodeTree aNodeTree : getChildren()) {
            rows[iNode] = toRow(aNodeTree);
            iNode++;
        }
        return rows;
    }

    private Object[] toRow(NodeTree nodeTree) {
        Object[] row;
        Node aNode = nodeTree.getNode();
        if (aNode instanceof StoreNode) {
            row = ((StoreNode) aNode).toRow();
        } else if (aNode instanceof BranchNode) {
            long length = nodeTree.sumLength();
            row = ((BranchNode) aNode).toRow(length);
        } else {
            row = null;
        }
        return row;
    }

    private long sumLength() {
        long length = 0;
        for (NodeTree nodeTree : this.getChildren()) {
            Node aNode = nodeTree.getNode();
            if (aNode instanceof StoreNode) {
                length += ((StoreNode) aNode).length;
            } else if (aNode instanceof BranchNode) {
                length += nodeTree.sumLength();
            } else {
                return 0;
            }
        }
        return length;
    }

    public void setChildren(List<NodeTree> children) {
        this.children = children;
    }

    public void addChild(NodeTree nodeTree) {
        if (null == children) {
            children = new ArrayList<>();
        }
        children.add(nodeTree);
        nodeTree.parent = this;
    }

    private void addChildNode(Node node) {
        this.addChild(new NodeTree(node));
    }

    @Override
    public NodeTree getChildAt(int index) {
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

    public int getNodeId() {
        return node.id;
    }

    public void setNodeId(int pId) {
        node.id = pId;
    }

    private String getNodeName() {
        return node.name;
    }

    public void setNodeName(String pName) {
        node.name = pName;
    }

    public void recursivelySort() {
        if (null == this.children) {
            return;
        }
        this.children.sort(new NodeNameAscComparator());
        for (NodeTree child : this.children) {
            child.recursivelySort();
        }
    }

    // For JTree display
    @Override
    public String toString() {
        return this.getNodeName();
    }

    public String getNodePathName() {
        String pathName = TransConst.EMPTY;
        if (!isRoot()) {
            pathName += this.parent.getNodePathName();
        }
        pathName += TransConst.SLASH + this.getNodeName();
        return pathName;
    }

    private NodeTree getChildByName(String aName) {
        if (null == this.children)
            return null;
        for (NodeTree aChild : this.children) {
            if (aChild.node.name.equals(aName))
                return aChild;
        }
        return null;
    }

    private NodeTree getChildByNameOrAddIt(String aName) {
        NodeTree aNodeTree = getChildByName(aName);
        if (null == aNodeTree) {
            aNodeTree = new NodeTree(new BranchNode(aName, this.getNodePathName()));
            this.addChild(aNodeTree);
        }
        return aNodeTree;
    }

    @Override
    public int getIndex(TreeNode node) {
        if (null == node) {
            throw new IllegalArgumentException("Argument is null.");
        }
        if ((this.isLeaf()) || (node.getParent() != this)) {
            return -1;
        }
        return children.indexOf(node);
    }

    @Override
    public boolean getAllowsChildren() {
        return this.allowsChildren;
    }

    public void setAllowsChildren(boolean allowsChildren) {
        this.allowsChildren = allowsChildren;
    }

    @Override
    public Enumeration<NodeTree> children() {
        if (this.isLeaf()) {
            return new Enumeration<NodeTree>() {
                public boolean hasMoreElements() {
                    return false;
                }

                public NodeTree nextElement() {
                    throw new NoSuchElementException("No elements here.");
                }
            };
        } else {
            return new Enumeration<NodeTree>() {
                Iterator<NodeTree> iter = children.iterator();

                public boolean hasMoreElements() {
                    return iter.hasNext();
                }

                public NodeTree nextElement() throws NoSuchElementException {
                    return iter.next();
                }
            };
        }
    }

    public boolean isLeaf() {
        return (null == this.children);
    }

    private boolean isRoot() {
        return (null == this.parent);
    }

    private boolean isBranch() {
        return !this.isLeaf() && !this.isRoot();
    }

    private NodeTree addBranch(String pPath) {
        if (this.isNull()) {
            this.setNode(new BranchNode(TransConst.ROOT, "/"));
        }
        String[] nodeNames = pPath.split(TransConst.SLASH);
        NodeTree bTree = this;
        for (String nodeName : nodeNames) {
            if (null != nodeName && !nodeName.isEmpty()) {
                // TransLog.getLogger().info(nodeName);
                bTree = bTree.getChildByNameOrAddIt(nodeName);
            }
        }
        return bTree;
    }

    public static NodeTree buildFromList(StoreList pList) {
        NodeTree aTree = new NodeTree();
        aTree.appendFromList(pList);
        return aTree;
    }
    
    private void appendFromList(StoreList pList) {
        for (Node aNode : pList.storeList) {
            this.addBranch(aNode.path).addChildNode(aNode);
        }
    }

    @Override
    public void insert(MutableTreeNode child, int index) {
        // TODO Auto-generated method stub
    }

    @Override
    public void remove(int index) {
        // TODO Auto-generated method stub
    }

    @Override
    public void remove(MutableTreeNode node) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setUserObject(Object object) {
        // TODO Auto-generated method stub
    }

    @Override
    public void removeFromParent() {
        // TODO Auto-generated method stub
    }

    @Override
    public void setParent(MutableTreeNode newParent) {
        // TODO Auto-generated method stub
    }

}
