package com.wenkrang.faClip.Moudle.FaCommand.AnnotationHandler;

import com.wenkrang.faClip.Moudle.FaCommand.FaCmd;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 注解处理器接口
 * 用于处理命令方法上的各种注解，提取配置信息并设置到 FaCmd 对象中
 */
public interface FaAnnotationHandler {
    /**
     * 处理注解，将注解信息提取并设置到命令对象中
     * 
     * @param command 命令对象
     * @param method 带有注解的方法
     */
    void handle(FaCmd command, Method method);
    
    /**
     * 获取此处理器处理的注解类型
     * 
     * @return 注解类对象
     */
    Class<? extends Annotation> getAnnotationClass();
}
