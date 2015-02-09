/**
 * Copyright (c) 2015,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.worker
 * File Name:KeeperTest.java
 * Date:2015-2-9 下午6:28:23
 * 
 */
package com.qxu.transpath.worker;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import com.qxu.transpath.tree.Node;

 /**
 * ClassName: KeeperTest <br/>
 * Description: TODO <br/>
 * Date: 2015-2-9 下午6:28:23 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class KeeperTest {

    @Test
    public void buildListFromFileTest() {
        Keeper keeper = Keeper.buildListFromFile("resource/tst/KeeperTest_buildListFromFileTest001.txt");
        ArrayList<Node> nodes = keeper.getNodes();
        assertEquals("ABCDEFG.cbr", nodes.get(0).getName());
        assertEquals("1st:/abc/def/ghijklmn", nodes.get(0).get1stBranch());
        assertEquals("HIJKLMN.cbz", nodes.get(1).getName());
        assertEquals("1st:/abc/opq/rstuvwxyz", nodes.get(1).get1stBranch());
    }

}
