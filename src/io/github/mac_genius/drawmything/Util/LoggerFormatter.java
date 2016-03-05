package io.github.mac_genius.drawmything.Util;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Created by Mac on 3/2/2016.
 */
public class LoggerFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        /*return  "[" + record.getLoggerName() + " "
                + record.getLevel().getName() + "]: ";*/
        return "";
    }
}
