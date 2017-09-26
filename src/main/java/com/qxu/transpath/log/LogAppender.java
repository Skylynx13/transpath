/**
 * Copyright (c) 2017,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.log
 * File Name:LogAppender.java
 * Date:2017-9-26 下午6:30:43
 * 
 */
package com.qxu.transpath.log;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.Serializable;
import java.io.Writer;

import javax.swing.JTextArea;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.WriterAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;

import com.qxu.transpath.ui.TranspathFrame;

 /**
 * ClassName: LogAppender <br/>
 * Description: TODO <br/>
 * Date: 2017-9-26 下午6:30:43 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

@Plugin(name="LogAppender", category="Core", elementType="appender", printObject=true)
public class LogAppender extends AbstractAppender {  
    private final JTextArea logArea;

    public LogAppender(String name, Filter filter,
            Layout<? extends Serializable> layout, final boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        logArea = TranspathFrame.getLogArea();
    }
    
    @Override
    public void append(LogEvent event) {
        logArea.append(event.getMarker().toString());
    }  
    
    
}  