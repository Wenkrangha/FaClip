package com.wenkrang.faClip.module.faDebugger;

import com.wenkrang.faClip.module.FaClip;
import com.wenkrang.faClip.module.faCommand.FaCmdInstance;

/**
 * 这里是插件的调试类，用于FaClip自身开发调试
 */
public class Debugger {

    private final FaCmdInstance faCmdInstance;

    public Debugger() {
        this.faCmdInstance = new FaCmdInstance(FaClip.getPlugin(FaClip.class));
        faCmdInstance.auto();
    }

    public FaCmdInstance getFaCmdInstance() {
        return faCmdInstance;
    }
}
