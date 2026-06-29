package com.wenkrang.faClip.Moudle.FaCommand.FaParam.JavaParam;

import com.wenkrang.faClip.Moudle.FaCommand.FaParam.SimpleParam;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * IntParam 用于处理整数类型的参数。
 * 支持解析 int 和 Integer 类型的值。
 */
public class IntParam implements SimpleParam {

    @Override
    public @NotNull Set<Type> getType() {
        return Set.of(Integer.class, int.class); // 返回整数对应的类型
    }

    @Override
    public boolean check(@NotNull String param) {
        try {
            Integer.parseInt(param);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public @NotNull Object convert(@NotNull String param) {
        return Integer.parseInt(param);
    }

    @Override
    public @Nullable String getName(Type type) {
        return getType().contains(type) ? "Integer" : null;
    }
}
