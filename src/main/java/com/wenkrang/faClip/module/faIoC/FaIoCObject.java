package com.wenkrang.faClip.module.faIoC;

import com.wenkrang.faClip.module.faIoC.annotation.Service;
import com.wenkrang.faClip.module.faIoC.handlers.IoCInstanceHandler;
import com.wenkrang.faClip.module.faMessage.exception.FaIoCException;

import java.lang.reflect.Constructor;

public class FaIoCObject {
    public Class<?> getClazz() {
        return clazz;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public Object getInstance() {
        if (getClazz().isAnnotationPresent(Service.class)) {
            Service annotation = getClazz().getAnnotation(Service.class);

            Service.Scope scope = annotation.scope();

            if (scope == Service.Scope.PROTOTYPE) {
                return IoCInstanceHandler.create(this);
            }
        }

        return instance;
    }

    public Object[] getParams() {
        return params;
    }

    private final Class<?> clazz; // 类型
    private final Constructor<?> constructor; // 构造器
    private final Object instance; // 实例
    private final Object[] params; // 构造参数

    private FaIoCObject(Class<?> clazz, Constructor<?> constructor, Object o, Object[] params) {
        this.clazz = clazz;
        this.constructor = constructor;
        instance = o;
        this.params = params;
    }

    public static class Builder {
        private Class<?> clazz;
        private Constructor<?> constructor;
        private Object instance;
        private Object[] params;

        public void setParams(Object[] params) {
            this.params = params;
        }

        public Object[] getParams() {
            return params;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public Constructor<?> getConstructor() {
            return constructor;
        }

        public Object getInstance() {
            return instance;
        }

        public Builder setInstance(Object instance) {
            this.instance = instance;
            return this;
        }

        public Builder setClazz(Class<?> clazz) {
            this.clazz = clazz;
            return this;
        }
        public Builder setConstructor(Constructor<?> constructor) {
            this.constructor = constructor;
            return this;
        }

        public FaIoCObject build() {
            if (clazz == null) {
                throw new FaIoCException("FaIoC.Error.FaIoCObject.Builder.FieldsNotInit", "clazz");
            }
            if (constructor == null) {
                throw new FaIoCException("FaIoC.Error.FaIoCObject.Builder.FieldsNotInit", "constructor");
            }
            if (instance == null) {
                throw new FaIoCException("FaIoC.Error.FaIoCObject.Builder.FieldsNotInit", "instance");
            }
            if (params == null) {
                throw new FaIoCException("FaIoC.Error.FaIoCObject.Builder.FieldsNotInit", "params");
            }
            return new FaIoCObject(clazz, constructor, instance, params);
        }
    }
}
