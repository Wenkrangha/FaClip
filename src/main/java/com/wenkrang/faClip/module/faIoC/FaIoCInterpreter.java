package com.wenkrang.faClip.module.faIoC;

import com.wenkrang.faClip.helper.AnnotationHelper;
import com.wenkrang.faClip.module.faIoC.annotation.Service;
import com.wenkrang.faClip.module.faIoC.handlers.IoCConstructorHandler;
import com.wenkrang.faClip.module.faIoC.handlers.IoCFieldHandler;
import com.wenkrang.faClip.module.faIoC.handlers.IoCHandler;
import com.wenkrang.faClip.module.faIoC.handlers.IoCInstanceHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class FaIoCInterpreter {
    private final FaIoCInstance faIoCInstance;
    private final ArrayList<IoCHandler> handlers = new ArrayList<>();

    public FaIoCInterpreter(FaIoCInstance faIoCInstance) {
        this.faIoCInstance = faIoCInstance;

        handlers.add(new IoCConstructorHandler());
        handlers.add(new IoCInstanceHandler());
        handlers.add(new IoCFieldHandler());
    }

    public FaIoCInstance getFaIoCInstance() {
        return faIoCInstance;
    }

    public @Nullable FaIoCObject interpreter(Class<?> clazz) {
        // 检查注解
        if (clazz != null) {
            FaIoCObject.Builder builder = new FaIoCObject.Builder();

            builder.setClazz(clazz);

            handlers.forEach(handler -> handler.handle(builder, faIoCInstance));

            return builder.build();
        }

        return null;
    }
}
