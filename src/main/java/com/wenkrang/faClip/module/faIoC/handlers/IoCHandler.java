package com.wenkrang.faClip.module.faIoC.handlers;

import com.wenkrang.faClip.module.faIoC.FaIoCInstance;
import com.wenkrang.faClip.module.faIoC.FaIoCObject;

public interface IoCHandler {
    void handle(FaIoCObject.Builder builder, FaIoCInstance faIoCInstance);
}
