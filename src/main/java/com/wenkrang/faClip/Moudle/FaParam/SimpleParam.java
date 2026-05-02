package com.wenkrang.faClip.Moudle.FaParam;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * SimpleParam 接口用于定义简单的参数类型处理器。
 * 实现此接口的类可以处理特定类型的参数验证、转换和类型信息。
 */
public interface SimpleParam {
    /**
     * 获取此参数处理器支持的类型集合。
     *
     * @return 支持的类型集合
     */
    Set<Type> getType();
    
    /**
     * 检查给定的字符串参数是否符合此参数类型的要求。
     *
     * @param param 要检查的字符串参数
     * @return 如果参数有效则返回 true，否则返回 false
     */
    boolean check(String param);
    
    /**
     * 将字符串参数转换为此参数类型的实际对象。
     *
     * @param param 要转换的字符串参数
     * @return 转换后的对象
     */
    Object convert(String param);
    
    /**
     * 获取指定类型的名称。
     *
     * @param type 要获取名称的类型
     * @return 类型名称，如果不支持此类型则返回 null
     */
    @Nullable String getName(Type type);
}
