package org.learn.kafka;

/**
 * Created by qianqian on 10/01/2018.
 */
public class FileMonitor {
    String name;
    private long lastTimeFileSize = 0;

    public FileMonitor(String name, long lastTimeFileSize) {
        this.name = name;
        this.lastTimeFileSize = lastTimeFileSize;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastTimeFileSize(long lastTimeFileSize) {
        this.lastTimeFileSize = lastTimeFileSize;
    }

    public String getName() {
        return name;
    }

    public long getLastTimeFileSize() {
        return lastTimeFileSize;
    }
}
