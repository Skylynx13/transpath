/**
 * Copyright (c) 2015,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.tree
 * File Name:NodeTest.java
 * Date:2015-2-15 下午3:47:33
 * 
 */
package com.qxu.transpath.oldschool;

import static org.junit.Assert.*;

import org.junit.Test;

import com.qxu.transpath.oldschool.BranchesNode;

 /**
 * ClassName: NodeTest <br/>
 * Description: TODO <br/>
 * Date: 2015-2-15 下午3:47:33 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class BranchesNodeTest {

    @Test
    public void mergeTest() {
        BranchesNode n1 = new BranchesNode(1, "abc");
        n1.putBranch("1ST", "the_1st_branch");
        n1.putBranch("3RD", "the_3rd_branch");
        BranchesNode n2 = new BranchesNode(1, "abcd");
        n2.putBranch("1ST", "the_1xx_branch");
        n2.putBranch("2ND", "the_2nd_branch");
        n2.putBranch("3RD", "the_3xx_branch");
        n2.putBranch("4TH", "the_4th_branch");
        BranchesNode n3 = n1.merge(n2);
        assertEquals(1, n3.getId());
        assertEquals("abc", n3.getName());
        assertEquals("the_1st_branch", n3.getBranch("1ST"));
        assertEquals("the_2nd_branch", n3.getBranch("2ND"));
        assertEquals("the_3rd_branch", n3.getBranch("3RD"));
        assertEquals("the_4th_branch", n3.getBranch("4TH"));
    }

}