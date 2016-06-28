package com.mylab.spring.logging;

import com.mylab.spring.event.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PreDestroy;
import java.util.List;

public class CacheEventLogger extends FileEventLogger {

    private int cacheSize;
    private List<Event> cache;

    public int getCacheSize() {
        return cacheSize;
    }

    @Autowired
    public void setCacheSize(@Value("${logging.cache.size}")int cacheSize) {
        this.cacheSize = cacheSize;
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
