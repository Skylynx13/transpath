package com.skylynx13.transpath.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ClassName: TransLog
 * Description: Transpath logger
 * Date: 2016-10-19 17:17:21
 */
public class TransLog {
    
    private static Logger translog = LogManager.getLogger("com.skylynx13.transpath");

    public static Logger getLogger() {
        return translog;
    }
}
