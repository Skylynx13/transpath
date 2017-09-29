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

import javax.swing.JScrollPane;
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
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import com.qxu.transpath.ui.TranspathFrame;
import com.qxu.transpath.utils.TransConst;

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
// See https://github.com/M-Razavi/log4j-swing-appender
// http://www.howtobuildsoftware.com/index.php/how-do/chab/java-logging-log4j-log4j2-how-to-create-a-custom-appender-in-log4j2
// http://logging.apache.org/log4j/2.x/manual/configuration.html#Properties
// http://logging.apache.org/log4j/2.x/manual/extending.html#Appenders
@Plugin(name="Swing", category="Core", elementType="appender", printObject=true)
public class SwingAppender extends AbstractAppender {  
    private final JTextArea logArea;

    private SwingAppender(String name, Filter filter,
            Layout<? extends Serializable> layout, final boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        logArea = TranspathFrame.getLogArea();
    }
    
    @Override
    public void append(LogEvent event) {
        logArea.append(new String(this.getLayout().toByteArray(event)));
        logArea.append(TransConst.CR);
        logArea.selectAll();
    }  
    
    @PluginFactory
    public static SwingAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filter") final Filter filter,
            @PluginAttribute("otherAttribute") String otherAttribute) {
        if (name == null) {
            LOGGER.error("No name provided for TextAreaAppender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new SwingAppender(name, filter, layout, true);
    }
}  