/**
 * Copyright (c) 2014, qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.worker
 * File Name:Keeper.java
 * Date:2014-4-12 下午9:10:46
 * 
 */
/**
 * Copyright (c) 2014,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.worker
 * File Name:Keeper.java
 * Date:2014-4-12 下午9:10:46
 * 
 */
package com.qxu.transpath.worker;

import javax.swing.tree.TreeNode;

/**
 * ClassName: Keeper <br/>
 * Description: Interface for persistence. <br/>
 * Date: 2014-4-12 下午9:10:46 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */
public interface Keeper {
    public TreeNode buildTree();
    public void keepTree();
}
