package com.wenkrang.faClip.Moudle.FaCommand.FaParam.JavaParam;

import com.wenkrang.faClip.Moudle.FaCommand.FaParam.SimpleParam;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * DoubleParam 用于处理双精度浮点数类型的参数。
 * 支持解析 double 和 Double 类型的值，仅接受有小数部分的数值。
 */
public class DoubleParam implements SimpleParam {
    @Override
    public @NotNull Set<Type> getType() {
        return Set.of(Double.class, double.class); // 返回小数对应的类型
    }

    @Override
    public boolean check(@NotNull String param) {
        try {
            // 尝试解析为 double
            double value = Double.parseDouble(param);
            // 排除整数情况（即没有小数部分）
            return value != (int) value;
        } catch (NumberFormatException e) {
            return false; // 无法解析为数字时返回 false
        }
    }

    @Override
    public @NotNull Object convert(@NotNull String param) {
        return Double.parseDouble(param);
    }

    @Override
    public @Nullable String getName(Type type) {
        return getType().contains(type) ? "Double" : null;
    }
}
