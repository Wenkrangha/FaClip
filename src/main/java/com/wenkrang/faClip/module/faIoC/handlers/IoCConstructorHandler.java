package com.wenkrang.faClip.module.faIoC.handlers;

import com.wenkrang.faClip.module.faIoC.FaIoCInstance;
import com.wenkrang.faClip.module.faIoC.FaIoCObject;
import com.wenkrang.faClip.module.faIoC.annotation.Autowired;
import com.wenkrang.faClip.module.faMessage.exception.FaIoCException;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class IoCConstructorHandler implements IoCHandler{
    @Override
    public void handle(FaIoCObject.Builder builder, FaIoCInstance faIoCInstance) {
        Class<?> clazz = builder.getClazz();

        // 获取构造器
        Constructor<?>[] constructors;

        if (clazz.getConstructors().length != 0) {
            constructors = clazz.getConstructors();
        }else {
            constructors = new Constructor[1];
            try {
                Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
                // 解除访问检查
                declaredConstructor.setAccessible(true);
                constructors[0] = declaredConstructor;
            } catch (Exception e) {
                throw new FaIoCException("FaIoC.Error.FaIoCInterpreter.ConstructorNotFound",e);
            }
        }

        // 筛选可用构造器
        if (constructors.length > 1) {
            // 检查冲突
            List<Constructor<?>> list = Arrays.stream(constructors)
                    .filter(i -> i.isAnnotationPresent(Autowired.class))
                    .toList();

            // 检查数量
            if (list.size() > 1) {
                throw new FaIoCException("FaIoC.Error.FaIoCInterpreter.ConstructorConflict");
            }
            if (list.isEmpty()) {
                throw new FaIoCException("FaIoC.Error.FaIoCInterpreter.ConstructorNotFound");
            }

            // 设置构造器
            Optional<Constructor<?>> first = Arrays.stream(constructors)
                    .filter(i -> i.isAnnotationPresent(Autowired.class))
                    .findFirst();

            first.ifPresent(builder::setConstructor);
        }else {
            builder.setConstructor(constructors[0]);
        }
    }
}
