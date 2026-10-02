package com.wenkrang.faClip.module;

public interface FaModule {
    void auto();
    void close();
    Status status();
    String getName();

    enum Status {
        READY,
        STARTING,
        STOPPED
    }
}
