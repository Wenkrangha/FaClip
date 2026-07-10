package com.wenkrang.faClip.Module.FaCommand.FaParam.JavaParam;

import com.wenkrang.faClip.Module.FaCommand.FaParam.SimpleParam;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * ByteParam 用于处理字节类型的参数。
 * 支持解析范围在 -128 到 127 之间的整数值。
 */
public class ByteParam implements SimpleParam {
    @Override
    public @NotNull Set<Type> getType() {
        return Set.of(Byte.class, byte.class);
    }

    @Override
    public boolean check(@NotNull String param) {
        try {
            Byte.parseByte(param);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public @NotNull Object convert(@NotNull String param) {
        return Byte.parseByte(param);
    }

    @Override
    public @Nullable String getName(Type type) {
        return getType().contains(type) ? "Byte" : null;
    }
}
