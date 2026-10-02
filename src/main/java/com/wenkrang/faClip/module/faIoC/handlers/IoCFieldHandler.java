package com.wenkrang.faClip.module.faIoC.handlers;

import com.wenkrang.faClip.module.faIoC.FaIoCInstance;
import com.wenkrang.faClip.module.faIoC.FaIoCObject;
import com.wenkrang.faClip.module.faIoC.annotation.Autowired;
import com.wenkrang.faClip.module.faMessage.exception.FaIoCException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class IoCFieldHandler implements IoCHandler{
    @Override
    public void handle(FaIoCObject.Builder builder, FaIoCInstance faIoCInstance) {
        Class<?> clazz = builder.getClazz();

        // 获取字段
        Field[] fields = clazz.getFields();

        // 需要自动装配的字段
        List<Field> autoWired = Arrays
                .stream(fields)
                .filter(i -> i.isAnnotationPresent(Autowired.class))
                .toList();

        for (Field field : autoWired) {
            // 获取字段类型
            Class<?> fieldType = field.getType();

            // 从容器中获取字段类型对应的对象
            FaIoCObject faIoCObject = faIoCInstance.getIocContainer().get(fieldType);

            // 设置字段值
            field.setAccessible(true);
            try {
                field.set(builder.getInstance(), faIoCObject.getInstance());
            } catch (IllegalAccessException e) {
                throw new FaIoCException("FaIoC.Error.FaIoCInterpreter.FieldSetFailed", e, field.getName());
            }
        }
    }
}
