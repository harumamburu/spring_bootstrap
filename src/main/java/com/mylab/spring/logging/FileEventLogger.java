package com.mylab.spring.logging;

import com.mylab.spring.event.Event;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

public class FileEventLogger implements EventLogger {

    protected String filename;
    protected String encoding;
    protected File logFile;

    private final String newLine = System.getProperty("line.separator");

    public String getFilename() {
        return filename;
    }

    @Autowired
    public void setFilename(@Value("${logging.logfile}")String filename) {
        this.filename = filename;
    }

    public String getEncoding() {
        return encoding;
    }

    @Autowired
    public void setEncoding(@Value("${logging.encoding}")String encoding) {
        this.encoding = encoding;
    }

    @Override
    public void logEvent(Event event) {
        try {
            FileUtils.writeStringToFile(logFile, event.toString() + newLine, encoding, true);
        } catch (IOException exc) {
            throw new RuntimeException("Failed to write an event to the log", exc);
        }
    }

    @PostConstruct
    protected void init() throws IOException {
        logFile = new File(filename);
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
        if (!logFile.isFile() || !logFile.canWrite()){
            throw new IOException("Can't write to file");
        }
    }
}
