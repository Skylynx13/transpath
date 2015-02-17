/**
 * Copyright (c) 2015,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:AllTests.java
 * Date:2015-2-12 下午9:38:20
 * 
 */
package com.qxu.transpath.tree;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

 /**
 * ClassName: AllTests <br/>
 * Description: TODO <br/>
 * Date: 2015-2-12 下午9:38:20 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

@RunWith(Suite.class)
@SuiteClasses({ 
    NodeTest.class, 
    NodeListTest.class, 
    NodeTreeTest.class })
public class AllTreeTests {

}
