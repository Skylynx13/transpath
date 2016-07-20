/**
 * Copyright (c) 2016,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.test
 * File Name:SuperClass.java
 * Date:2016-7-18 上午11:28:28
 * 
 */
package com.qxu.transpath.test;

 /**
 * ClassName: SuperClass <br/>
 * Description: TODO <br/>
 * Date: 2016-7-18 上午11:28:28 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class SuperClass {
    public void frame1() {
        System.out.println(func1());
    }
    public String func1() {
        return "SuperClass Func1";
    }
    public static void main(String[] args) {
        System.out.println("SuperClass starts...");
        System.out.println("SuperClass ends.");
    }
}
