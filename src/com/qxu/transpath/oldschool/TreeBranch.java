/**
 * Copyright (c) 2014,TravelSky. 
 * All Rights Reserved.
 * TravelSky CONFIDENTIAL
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath
 * File Name:TBranch.java
 * Date:Mar 12, 2014 11:34:51 PM
 * 
 */
package com.qxu.transpath.oldschool;

import com.qxu.transpath.utils.TransConst;

 /**
 * ClassName: TBranch <br/>
 * Description: TODO <br/>
 * Date: Mar 12, 2014 11:44:51 PM <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */
public class TreeBranch {
    private String treeName;
    private String itemPath;
    
    @Override
    public String toString() {
        return treeName + TransConst.TREENAME_BRANCH + itemPath;
    }

    public String getTreeName() {
        return treeName;
    }
    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }
    public String getItemPath() {
        return itemPath;
    }
    public void setItemPath(String itemPath) {
        this.itemPath = itemPath;
    }
}
