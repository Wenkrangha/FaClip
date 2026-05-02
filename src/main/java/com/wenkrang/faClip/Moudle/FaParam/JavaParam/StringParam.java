package com.wenkrang.faClip.Moudle.FaParam.JavaParam;

import com.wenkrang.faClip.Moudle.FaParam.SimpleParam;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * StringParam 用于处理字符串类型的参数。
 * 接受任何字符串输入，始终返回成功。
 */
public class StringParam implements SimpleParam {
    @Override
    public @NotNull Set<Type> getType() {
        return Set.of(String.class); // 返回字符串对应的类型
    }

    @Override
    public boolean check(String param) {
        return true;
    }

    @Override
    public Object convert(String param) {
        return param;
    }

    @Override
    public @Nullable String getName(Type type) {
        return getType().contains(type) ? "String" : null;
    }
}
