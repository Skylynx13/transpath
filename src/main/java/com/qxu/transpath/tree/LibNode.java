/**
 * Copyright (c) 2016,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:CatalogNode.java
 * Date:2016-5-16 下午2:07:42
 * 
 */
package com.qxu.transpath.tree;

import java.util.ArrayList;

/**
 * ClassName: CatalogNode <br/>
 * Description: TODO <br/>
 * Date: 2016-5-16 下午2:07:42 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 *         Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 *          <cid>:<sid>:<cpath>:<cname>
 */

public class LibNode extends Node {
    public ArrayList<String> tagList = new ArrayList<String>();

    public LibNode(String cEntry) {
        // TODO: format tobe decided.
    }

    @Override
    public String keepNode() {
        // TODO: format tobe decided.
        return null;
    }

}
