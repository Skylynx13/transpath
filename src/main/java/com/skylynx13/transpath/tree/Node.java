package com.skylynx13.transpath.tree;

/**
 * ClassName: Node
 * Description: Basic Unit of NodeTree.
 * Date: 2014-04-02 22:34:48
 */
public abstract class Node{
    public int id;
    public String path;
    public String name;

    public Node() {
        this.id = 0;
        this.path = "";
        this.name = "";
    }

    public abstract Node clone();

    public Node(int pId, String pPath, String pName) {
        this.id = pId;
        this.path = pPath;
        this.name = pName;
    }

    public abstract String keepNode();

    public boolean checkDupNode(Node pNode) {
        return (null != pNode)
                && (pNode.name.equals(this.name))
                && (pNode.path.equals(this.path));
    }

    boolean searchName(String searchText) {
        return search(this.name, searchText);
    }

    boolean searchPath(String searchText) {
        return search(this.path, searchText);
    }

    private boolean search(String member, String searchText) {
        return member.matches("(?i).*" + searchText + ".*");
    }


}
