package com.wenkrang.faClip.module.faInterface.handler;

import com.wenkrang.faClip.module.faCommand.helper.NodeHelper;
import com.wenkrang.faClip.module.faInterface.annotation.Intf;
import com.wenkrang.faClip.module.faInterface.FaIntf;
import com.wenkrang.faClip.module.faMessage.Fm;
import com.wenkrang.faClip.module.faMessage.exception.FaIntfException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static com.wenkrang.faClip.module.faMessage.helper.I18nHelper.t;

public class IntfHandler implements SimpleAnnotationHandler {
    /**
     * 检查接口注册是否合规
     * @param method
     * @param node
     * @return
     */
    public static boolean check(Method method,String node) {
        if (!Modifier.isStatic(method.getModifiers()))
            throw new FaIntfException("FaInterface.Error.Interpreter.NotStatic", method.getName());
        if (!NodeHelper.check(node)) {
            Fm.warning(t("FaInterface.Error.Interpreter.CantUnderstand"));
            return false;
        }
        return true;
    }

    @Override
    public void handle(FaIntf faIntf, Method method) {
        Intf annotation = method.getAnnotation(Intf.class);

        String node = annotation.value();

        // 检查
        if (!check(method, annotation.value())) return;

        // 检查节点是否合规
        faIntf.setMethod(method);
        faIntf.setNode(node);
    }
    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return Intf.class;
    }
}
