package com.mylab.spring.logging;

import com.mylab.spring.event.Event;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

public class CacheEventLogger extends FileEventLogger {

    private final int cacheSize;
    private final List<Event> cache;

    public CacheEventLogger(int cacheSize) {
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

    @PreDestroy
    private void destroy() {
        if (!cache.isEmpty()) {
            dumpCache();
        }
    }
}
