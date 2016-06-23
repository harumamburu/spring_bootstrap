package com.mylab.spring.logging;

import com.mylab.spring.event.Event;

import java.util.ArrayList;
import java.util.List;

public class CacheEventLogger extends FileEventLogger {

    private int cacheSize;
    private List<Event> cache;

    public CacheEventLogger(String filename, String encoding, int cacheSize) {
        super(filename, encoding);
        this.cacheSize = cacheSize;
        cache = new ArrayList<>(cacheSize);
    }

    @Override
    public void logEvent(Event event) {
        cache.add(event);
        if (cache.size() == cacheSize) {
            dumpCache();
        }
    }

    private void dumpCache() {
        cache.forEach(super::logEvent);
        cache.clear();
    }

    private void destroy() {
        if (!cache.isEmpty()) {
            dumpCache();
        }
    }
}
