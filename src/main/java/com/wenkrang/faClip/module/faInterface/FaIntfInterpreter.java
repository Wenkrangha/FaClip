package com.wenkrang.faClip.module.faInterface;

import com.wenkrang.faClip.module.faInterface.annotation.Intf;
import com.wenkrang.faClip.module.faInterface.handler.IntfHandler;
import com.wenkrang.faClip.module.faInterface.handler.SimpleAnnotationHandler;
import com.wenkrang.faClip.module.faInterface.param.FaParam;
import com.wenkrang.faClip.module.faInterface.param.SimpleParam;
import com.wenkrang.faClip.module.faMessage.exception.FaIntfException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class FaIntfInterpreter {
    public FaInterfaceInstance interfaceInstance;

    public ArrayList<SimpleAnnotationHandler> annotationHandlers = new ArrayList<>();

    private final FaParam faParam;

    public FaIntfInterpreter(FaInterfaceInstance interfaceInstance) {
        this.interfaceInstance = interfaceInstance;
        faParam = new FaParam();

        annotationHandlers.add(new IntfHandler());
    }

    public @NotNull FaIntf interpret(@NotNull Method method,@NotNull String node) {
        // 检查是否静态
        if (Modifier.isStatic(method.getModifiers())) {
            // 初始化接口
            FaIntf faIntf = new FaIntf(interfaceInstance);

            // 检查接口是否合规
            if (!IntfHandler.check(method, node)) {
                throw new FaIntfException("FaInterface.Error.Interpreter.CantUnderstand", node);
            }

            faIntf.setMethod(method);
            faIntf.setNode(node);

            annotationHandlers.stream()
                    .filter(i -> method.isAnnotationPresent(i.getAnnotationClass()))
                    .forEach(i -> i.handle(faIntf, method));

            // 委托转换器
            Class<?>[] parameterTypes = method.getParameterTypes();

            faIntf.paramConvertors = new SimpleParam[method.getParameterCount()];

            for (int i = 0;i < parameterTypes.length;i++) {
                Class<?> parameterType = parameterTypes[i];

                SimpleParam convertor = faParam.getConvertor(parameterType);

                // 检查是否支持
                if (convertor == null) {
                    if (!(FaIntfContext.class.isAssignableFrom(parameterType))) {
                        throw new FaIntfException("FaInterface.Error.Interpreter.UnsupportedType", parameterType.getTypeName(), node);
                    }
                }

                faIntf.paramConvertors[i] = convertor;
            }

            return faIntf;
        } else {
            throw new FaIntfException("FaInterface.Error.Interpreter.NotStatic", method.getName());
        }
    }

    public FaIntf interpret(Method method) {
        Intf annotation = method.getAnnotation(Intf.class);

        if (annotation != null) {
            return interpret(method, annotation.value());
        }

        return null;
    }

    public FaParam getFaParam() {
        return faParam;
    }
}
