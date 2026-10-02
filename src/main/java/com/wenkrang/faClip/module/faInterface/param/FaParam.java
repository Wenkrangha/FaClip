package com.wenkrang.faClip.module.faInterface.param;

import com.wenkrang.faClip.module.faInterface.param.bukkitParam.EffectParam;
import com.wenkrang.faClip.module.faInterface.param.bukkitParam.MaterialParam;
import com.wenkrang.faClip.module.faInterface.param.bukkitParam.PlayerParam;
import com.wenkrang.faClip.module.faInterface.param.javaParam.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FaParam {
    private final ArrayList<SimpleParam> simpleParams = new ArrayList<>();

    public FaParam() {
        simpleParams.add(new StringParam());
        simpleParams.add(new DoubleParam());
        simpleParams.add(new IntParam());
        simpleParams.add(new FloatParam());
        simpleParams.add(new LongParam());
        simpleParams.add(new BooleanParam());
        simpleParams.add(new ShortParam());
        simpleParams.add(new ByteParam());
        simpleParams.add(new CharParam());
        simpleParams.add(new EffectParam());
        simpleParams.add(new MaterialParam());
        simpleParams.add(new PlayerParam());
    }

    public List<SimpleParam> getSimpleParams() {
        return simpleParams;
    }

    /**
     * 检查给定的参数是否符合预定义的检查规则，并返回所有匹配的检查器类型。
     *
     * @param param 待检查的字符串参数
     * @return 包含所有匹配检查器类型的Set，如果没有任何匹配则返回空列表
     */
    public @NotNull Set<Type> check(@NotNull String param) {
        // 初始化一个用于存储匹配检查器类型的列表
        ArrayList<Type> types = new ArrayList<>();

        // 遍历所有预定义的参数检查器
        for (SimpleParam paramChecker : simpleParams) {
            // 如果当前检查器匹配参数，则将其类型添加到结果列表中
            if (paramChecker.check(param)) {
                types.addAll(paramChecker.getType());
            }
        }

        // 返回包含所有匹配检查器类型的列表
        return new HashSet<>(types);
    }

    public @Nullable Object parse(@NotNull String param, @NotNull Type type) {
        for (SimpleParam paramChecker : simpleParams) {
            if (paramChecker.getType().contains(type)) {
                return paramChecker.convert(param);
            }
        }
        return null;
    }

    /**
     * 根据提供的类型返回参数转换器
     * @param type 类型
     * @return 返回的参数转换器
     */
    public @Nullable SimpleParam getConvertor(@NotNull Type type) {
        return simpleParams.stream().filter(i -> i.getType().contains(type)).findFirst().orElse(null);
    }


}
