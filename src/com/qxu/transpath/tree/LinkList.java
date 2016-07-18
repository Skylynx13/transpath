/**
 * Copyright (c) 2016,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:IdLinkList.java
 * Date:2016-7-18 下午5:07:33
 * 
 */
package com.qxu.transpath.tree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.qxu.transpath.utils.TransConst;

 /**
 * ClassName: IdLinkList <br/>
 * Description: TODO <br/>
 * Date: 2016-7-18 下午5:07:33 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class LinkList extends NodeList{

    @Override
    public Node loadNode(String pLine) {
        return new LinkNode(pLine);
    }

    @Override
    public String keepLine(Node pNode) {
        return ((LinkNode)pNode).keepNode();
    }
   
}
