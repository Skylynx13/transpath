/**
 * Copyright (c) 2014, qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath
 * File Name:ItemNode.java
 * Date:Mar 13, 2014 12:07:10 AM
 * 
 */
/**
 * Copyright (c) 2014,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath
 * File Name:ItemNode.java
 * Date:Mar 13, 2014 12:07:10 AM
 * 
 */
package com.qxu.transpath.temp;

import java.util.Iterator;
import java.util.List;

import com.qxu.transpath.utils.TransConst;

 /**
 * ClassName: ItemNode <br/>
 * Description: TODO <br/>
 * Date: Mar 13, 2014 12:07:10 AM <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */
public class ItemNode {
    private String itemName;
    private List<TreeBranch> itemTreeBranches;

    @Override
    public String toString() {
        String str = itemName;
        Iterator<TreeBranch> iTreeBranch = itemTreeBranches.iterator();
        while (iTreeBranch.hasNext()) {
            str += TransConst.BRANCH_LINKER + iTreeBranch.next().toString();
        }
        return str;
    }
    
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public List<TreeBranch> getItemTreeBranches() {
        return itemTreeBranches;
    }
    public void setItemTreeBranches(List<TreeBranch> itemTreeBranches) {
        this.itemTreeBranches = itemTreeBranches;
    }
    
}
