package com.wenkrang.faClip.Module.FaDebugger;

import com.wenkrang.faClip.FaClip;
import com.wenkrang.faClip.Module.FaCommand.FaCmdInstance;

/**
 * 这里是插件的调试类
 */
public class Debugger {

    private final FaCmdInstance faCmdInstance;

    public Debugger() {
        this.faCmdInstance = FaCmdInstance.create(FaClip.plugin);

        faCmdInstance.enableForAll(FaClip.plugin);
    }

    public FaCmdInstance getFaCmdInstance() {
        return faCmdInstance;
    }
}
