package com.wenkrang.faClip.Moudle.FaCommand.FaParam.JavaParam;

import com.wenkrang.faClip.Moudle.FaCommand.FaParam.SimpleParam;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * CharParam 用于处理字符类型的参数。
 * 只接受单个字符的输入。
 */
public class CharParam implements SimpleParam {
    @Override
    public @NotNull Set<Type> getType() {
        return Set.of(Character.class, char.class);
    }

    @Override
    public boolean check(@NotNull String param) {
        // 只接受单个字符
        return param.length() == 1;
    }

    @Override
    public @NotNull Object convert(@NotNull String param) {
        return param.charAt(0);
    }

    @Override
    public @Nullable String getName(Type type) {
        return getType().contains(type) ? "Character" : null;
    }
}
