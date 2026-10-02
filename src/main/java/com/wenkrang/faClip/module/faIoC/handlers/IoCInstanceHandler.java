package com.wenkrang.faClip.module.faIoC.handlers;

import com.wenkrang.faClip.module.faIoC.FaIoCInstance;
import com.wenkrang.faClip.module.faIoC.FaIoCObject;
import com.wenkrang.faClip.module.faMessage.exception.FaIoCException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class IoCInstanceHandler implements IoCHandler{

    public static Object create(FaIoCObject faIoCObject) {
        try {
            return faIoCObject.getConstructor().newInstance(faIoCObject.getParams());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new FaIoCException("FaIoC.Error.FaIoCInterpreter.InstanceCreateFailed", e);
        }
    }


    public static Object create(FaIoCObject.Builder builder) {
        try {
            return builder.getConstructor().newInstance(builder.getParams());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new FaIoCException("FaIoC.Error.FaIoCInterpreter.InstanceCreateFailed", e);
        }
    }

    @Override
    public void handle(FaIoCObject.Builder builder, FaIoCInstance faIoCInstance) {
        // 获取构造器
        Constructor<?> constructor = builder.getConstructor();

        if (constructor != null) {
            if (constructor.getParameterCount() == 0) {
                // 无参构造
                builder.setParams(new Object[0]);
                builder.setInstance(create(builder));
            }else {
                // 有参构造
                Parameter[] parameters = constructor.getParameters();

                // 满足依赖关系
                if (Arrays.stream(parameters)
                        .allMatch(i -> faIoCInstance.getDependencyProvider().hasProvider(i))) {
                    // 填充构造参数
                    Object[] parametersValue = new Object[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        Class<?> type = parameters[i].getType();
                        // 如果是接口，则从容器中获取实现类
                        parametersValue[i] = faIoCInstance.getDependencyProvider().provide(parameters[i]);
                    }

                    // 构造
                    builder.setParams(parametersValue);
                    builder.setInstance(create(builder));
                }
            }
        }
    }
}
