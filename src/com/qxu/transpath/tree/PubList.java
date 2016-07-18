/**
 * Copyright (c) 2016,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:PubList.java
 * Date:2016-7-12 下午3:08:02
 * 
 */
package com.qxu.transpath.tree;

 /**
 * ClassName: PubList <br/>
 * Description: TODO <br/>
 * Date: 2016-7-12 下午3:08:02 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class PubList extends NodeList {

    @Override
    public Node loadNode(String pLine) {
        return new PubNode(pLine);
    }

    @Override
    public String keepLine(Node pNode) {
        return ((PubNode)pNode).keepNode();
    }

    public void reorgOrder() {
        int newOrder = 0;
        String lastPath = "";
        for (Node aNode : nodeList) {
            if (aNode.path.equals(lastPath)) {
                newOrder++;
            }
            else {
                lastPath = aNode.path;
                newOrder = 0;
            }
            ((PubNode)aNode).order = newOrder;
        }
    }
}
