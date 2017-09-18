/**
 * Copyright (c) 2014,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.worker
 * File Name:TreeBuilder.java
 * Date:Jun 3, 2014 9:15:40 PM
 * 
 */
package com.qxu.transpath.oldschool;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import com.qxu.transpath.tree.NodeTree;

 /**
 * ClassName: TreeBuilder <br/>
 * Description: TODO <br/>
 * Date: Jun 3, 2014 9:15:40 PM <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class TreeKeeper {
    public NodeTree nTree;
    public String leafName;
    public String[] branchNames;
    
    public String toString() {
        return leafName + ":::" + branchNames[0] + ":::" + branchNames[1];
    } 
    
    public TreeKeeper() {
        nTree = new NodeTree();
    }
    
    public TreeKeeper(String srcFile) {
        this.init(srcFile);
    }
    
    public void init(String srcFile) {
        nTree = new NodeTree();
        Scanner inf = null;
        try {
            inf = new Scanner(new FileReader(srcFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (inf.hasNext()) {
            String line = inf.nextLine().trim();
            String[] namePath = line.split("\\|");
            this.leafName = namePath[0];
            this.branchNames = namePath[1].split("/");
        }
        inf.close();
    }

    public static void main(String[] args) {
        System.out.println("hi");
        TreeKeeper tk = new TreeKeeper("resource/pflist1.txt");
        System.out.println(tk.toString());
        System.out.println("bye");
    }

}


