package com.skylynx13.transpath.log;

import com.skylynx13.transpath.Transpath;
import com.skylynx13.transpath.utils.TransConst;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import javax.swing.*;
import java.io.Serializable;

/**
 * ClassName: LogAppender
 * Description: Log appender
 * Date: 2017-9-26 18:30:43
 * See:
 * https://github.com/M-Razavi/log4j-swing-appender
 * http://www.howtobuildsoftware.com/index.php/how-do/chab/java-logging-log4j-log4j2-how-to-create-a-custom-appender-in-log4j2
 * http://logging.apache.org/log4j/2.x/manual/configuration.html#Properties
 * http://logging.apache.org/log4j/2.x/manual/extending.html#Appenders
 */
@Plugin(name="Swing", category="Core", elementType="appender", printObject=true)
public class SwingAppender extends AbstractAppender {  
    private final JTextArea logTextArea = Transpath.getLogTextArea();

    private SwingAppender(String name, Filter filter,
            Layout<? extends Serializable> layout, final boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
    }
    
    @Override
    public void append(LogEvent event) {
        logTextArea.append(new String(this.getLayout().toByteArray(event)));
        logTextArea.append(TransConst.CR);
        logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
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