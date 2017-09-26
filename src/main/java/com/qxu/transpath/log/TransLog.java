/**
 * Copyright (c) 2016,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.utils
 * File Name:TransLog.java
 * Date:Oct 19, 2016 5:17:21 PM
 * 
 */
package com.qxu.transpath.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

 /**
 * ClassName: TransLog <br/>
 * Description: TODO <br/>
 * Date: Oct 19, 2016 5:17:21 PM <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class TransLog {
    
    static Logger translog = LogManager.getLogger("com.qxu.transpath"); 

    public static Logger getLogger() {
        return translog;
    }
}
