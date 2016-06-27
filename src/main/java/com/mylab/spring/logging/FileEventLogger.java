package com.mylab.spring.logging;

import com.mylab.spring.event.Event;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Component("fileLogger")
public class FileEventLogger implements EventLogger {

    protected String filename;
    protected File logFile;
    protected String encoding;

    final String newLine = System.getProperty("line.separator");

    public FileEventLogger(String filename, String encoding) {
        this.filename = filename;
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
