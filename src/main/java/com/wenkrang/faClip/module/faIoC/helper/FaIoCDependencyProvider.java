package com.wenkrang.faClip.module.faIoC.helper;

import com.wenkrang.faClip.helper.ClassHelper;
import com.wenkrang.faClip.module.faIoC.FaIoCInstance;
import com.wenkrang.faClip.module.faIoC.annotation.Qualifier;
import com.wenkrang.faClip.module.faMessage.exception.FaIoCException;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FaIoCDependencyProvider {
    private final FaIoCInstance ioCInstance;

    private final ArrayList<Class<?>> classes;

    public FaIoCDependencyProvider(FaIoCInstance ioCInstance) {
        this.ioCInstance = ioCInstance;
        classes = ClassHelper.getClasses(ioCInstance.getPlugin().getClass());
    }

    public FaIoCInstance getIoCInstance() {
        return ioCInstance;
    }

    private boolean isNormalClass(Class<?> clazz) {
        return !clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers());
    }

    public Class<?>[] getImplementations(Class<?> clazz) {
        return classes
                .stream()
                .filter(i -> Arrays.stream(i.getInterfaces())
                        .toList()
                        .contains(clazz))
                .toArray(Class<?>[]::new);
    }

    public @Nullable Object provide(Class<?> clazz) {
        if (!isNormalClass(clazz)) return null;

        if (!ioCInstance.hasContext(clazz)) {
            if (ioCInstance.getEnvInstances().containsKey(clazz)) {
                return ioCInstance.getEnvInstances().get(clazz);
            }

            ioCInstance.load(clazz);
        }

        return ioCInstance.getContext(clazz).getInstance();
    }

    public @Nullable Class<?> getByQualifier(Parameter parameter) {
        if (parameter.isAnnotationPresent(Qualifier.class)) {
            Qualifier annotation = parameter.getAnnotation(Qualifier.class);

            // 获取过滤器
            String value = annotation.value();

            // 类型
            Class<?> type = parameter.getType();

            // 符合的实现类
            List<Class<?>> implementations = Arrays.stream(getImplementations(type))
                    .filter(i -> i.getName().equals(value)).toList();

            if (implementations.isEmpty()) {
                throw new FaIoCException("FaIoC.Error.FaIoCInterpreter.ImplementationNotFound",  type.getName());
            }

            if (implementations.size() != 1) {
                throw new FaIoCException("FaIoC.Error.FaIoCInterpreter.ImplementationConflict", type.getName());
            }

            return implementations.stream().findFirst().get();
        }

        return null;
    }

    public Object provide(Parameter parameter) {
        Class<?> type = parameter.getType(); // 获取类型

        // 检查是否是接口
        if (isNormalClass(type)) {
            // 普通类
            return provide(type);
        }else {
            // 接口
            Class<?>[] implementations = getImplementations(type);

            // 检查是否有实现类
            if (implementations.length == 0) {
                throw new FaIoCException("FaIoC.Error.FaIoCInterpreter.ImplementationNotFound",  type.getName());
            }

            // 检查是否有多个实现类
            if (implementations.length > 1) {
                return getByQualifier(parameter);
            }else {
                Class<?> implementation = implementations[0];

                return provide(implementation);
            }
        }
    }

    public boolean hasProvider(Parameter parameter) {
        try {
            provide(parameter);
            return true;
        } catch (FaIoCException e) {
            return false;
        }
    }
}
