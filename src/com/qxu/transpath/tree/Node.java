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
    
    public String keepNode() {
        String seperator = TransConst.COLON;
        return new StringBuffer(TransConst.NODE_ID)
                .append(seperator)
                .append(this.id)
                .append(seperator)
                .append(this.name)
                .toString();
    }

    public boolean equals(Node pNode) {
        return (this.id == pNode.id) && (this.name.equals(pNode.name));
    }

}
