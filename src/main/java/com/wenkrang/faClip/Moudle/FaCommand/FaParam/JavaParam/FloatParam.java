package com.wenkrang.faClip.Moudle.FaCommand.FaParam.JavaParam;

import com.wenkrang.faClip.Moudle.FaCommand.FaParam.SimpleParam;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * FloatParam 用于处理单精度浮点数类型的参数。
 * 支持解析 float 和 Float 类型的值，排除 NaN 和无穷大。
 */
public class FloatParam implements SimpleParam {
    @Override
    public @NotNull Set<Type> getType() {
        return Set.of(Float.class, float.class); // 返回浮点数对应的类型
    }

    @Override
    public boolean check(@NotNull String param) {
        try {
            // 尝试解析为 float
            float value = Float.parseFloat(param);
            // 检查是否为有效的浮点数
            return !Float.isNaN(value) && !Float.isInfinite(value);
        } catch (NumberFormatException e) {
            return false; // 无法解析为数字时返回 false
        }
    }

    @Override
    public @NotNull Object convert(@NotNull String param) {
        return Float.parseFloat(param);
    }

    @Override
    public @Nullable String getName(Type type) {
        return getType().contains(type) ? "Float" : null;
    }
}
