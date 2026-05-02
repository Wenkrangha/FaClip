package com.wenkrang.faClip.Moudle.FaParam.JavaParam;

import com.wenkrang.faClip.Moudle.FaParam.SimpleParam;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * ShortParam 用于处理短整型参数。
 * 支持解析范围在 -32768 到 32767 之间的整数值。
 */
public class ShortParam implements SimpleParam {
    @Override
    public @NotNull Set<Type> getType() {
        return Set.of(Short.class, short.class);
    }

    @Override
    public boolean check(@NotNull String param) {
        try {
            Short.parseShort(param);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public @NotNull Object convert(@NotNull String param) {
        return Short.parseShort(param);
    }

    @Override
    public @Nullable String getName(Type type) {
        return getType().contains(type) ? "Short" : null;
    }
}
