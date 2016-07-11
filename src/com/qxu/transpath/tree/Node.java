/**
 * Copyright (c) 2014,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:Node.java
 * Date:2014-4-2 下午10:34:48
 * 
 */
package com.qxu.transpath.tree;

import com.qxu.transpath.utils.TransConst;

/**
 * ClassName: Node <br/>
 * Description: Basic Unit of NodeTree. <br/>
 * Date: 2014-4-2 下午10:34:48 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 *         Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class Node {
    public int id;
    public String name;

    public Node() {
        this.id = 0;
        this.name = "";
    }

    public Node(int pId, String pName) {
        this.id = pId;
        this.name = pName;
    }

    @Override
    public Node clone() {
        return new Node(this.id, this.name);
    }
    
    @Deprecated
    public String getSimpleName() {
        int iEnd = name.length();
        if (name.lastIndexOf('.') > 0) {
            iEnd = name.lastIndexOf('.');
        }
        if (name.indexOf('(') > 0) {
            iEnd = name.indexOf('(');
        }
        return name.substring(0, iEnd);
    }

    @Deprecated
    public String keepNode() {
        StringBuffer nodeBuf = new StringBuffer();
        nodeBuf.append(TransConst.NODE_ID);
        nodeBuf.append(TransConst.COLON);
        nodeBuf.append(this.id);
        nodeBuf.append(TransConst.COLON);
        nodeBuf.append(this.name);
        return nodeBuf.toString();
    }

    public boolean equals(Node pNode) {
        return (this.id == pNode.id) && (this.name.equals(pNode.name));
    }

}
