package com.skylynx13.transpath.tree;

import com.skylynx13.transpath.utils.StringUtils;

/**
 * ClassName: BranchNode
 * Description: Branch node
 * Date: 2016-07-12 16:47:49
 */
public class BranchNode extends Node {

    public BranchNode(String pName) {
        name = pName;
    }

    public BranchNode(String pName, String pPath) {
        name = pName;
        path = pPath;
    }

    @Override
    public Node clone() {
        BranchNode node = new BranchNode(this.name, this.path);
        node.id = this.id;
        return node;
    }

    @Override
    public String keepNode() {
        return name;
    }

    public Object[] toRow(long length) {
        Object[] row = {id, path, name, StringUtils.formatLongInt(length)};
        return row;
    }
 }
