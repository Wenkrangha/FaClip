package com.wenkrang.faClip.Moudle.FaCommand.FaParam;

import com.wenkrang.faClip.Moudle.FaCommand.Annotation.CustomDes;
import com.wenkrang.faClip.Moudle.FaCommand.Annotation.DesProvider;
import com.wenkrang.faClip.Moudle.FaCommand.Annotation.ParamArrayDes;
import com.wenkrang.faClip.Moudle.FaCommand.Annotation.ParamDes;
import com.wenkrang.faClip.Moudle.FaCommand.FaCmd;
import com.wenkrang.faClip.Moudle.FaCommand.FaCmdInterpreter.FaCmdContext;
import com.wenkrang.faClip.Moudle.FaCommand.FaParam.BukkitParam.EffectParam;
import com.wenkrang.faClip.Moudle.FaCommand.FaParam.BukkitParam.MaterialParam;
import com.wenkrang.faClip.Moudle.FaCommand.FaParam.BukkitParam.PlayerParam;
import com.wenkrang.faClip.Moudle.FaCommand.Helper.CmdNodeHelper;
import com.wenkrang.faClip.Moudle.FaCommand.FaParam.JavaParam.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Stream;

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

    /**
     * 检查给定的参数是否符合预定义的检查规则，并返回所有匹配的检查器类型。
     *
     * @param param 待检查的字符串参数
     * @return 包含所有匹配检查器类型的ArrayList，如果没有任何匹配则返回空列表
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
     * 获取命令的用法
     *
     * @param cmd 命令
     * @param faCmdContext 命令上下文
     * @param detail 是否显示详细用法
     * @return 命令的用法
     */
    public Object[] getUsage(@NotNull FaCmd cmd, @NotNull FaCmdContext faCmdContext,boolean detail) {
        // 切割命令节点
        List<String> node = CmdNodeHelper.separateNode(cmd.getNode());

        // 获取命令的参数名称
        List<Object> paramNames = Arrays.stream(cmd.getMethod().getParameters()) // 获取命令的方法参数
                .filter(i -> !i.getType().equals(FaCmdContext.class)) // 过滤掉 FaCmdContext 参数
                .map(i ->
                        {
                            if (i.getAnnotation(CustomDes.class) != null) {
                                try {
                                    DesProvider value = i.getAnnotation(CustomDes.class).value().getDeclaredConstructor().newInstance();
                                    return (Object) value.getDes(faCmdContext);
                                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                         NoSuchMethodException e) {
                                    throw new RuntimeException(e);
                                }
                            } else if (i.getAnnotation(ParamDes.class) != null) {
                                return i.getAnnotation(ParamDes.class).value();
                            } else if (i.getAnnotation(ParamArrayDes.class) != null && detail) {
                                return i.getAnnotation(ParamArrayDes.class).value();
                            } else {
                                // 准备结果列表
                                ArrayList<String> result = new ArrayList<>();

                                // 获取符合的处理器
                                List<SimpleParam> handlers =
                                        simpleParams.stream()
                                                .filter(j -> j.getType().contains(i.getType()))
                                                .toList();

                                // 历遍处理器
                                for (SimpleParam j : handlers) {
                                    // 检查处理器是否是提供描述的
                                    if (j instanceof DesProvider value && detail) {
                                        @NotNull String[] des = value.getDes(faCmdContext);
                                        result.addAll(Arrays.asList(des));
                                    } else {
                                        // 没有描述就显示参数名称
                                        result.add("<" + j.getName(i.getType()) + ">");
                                    }
                                }

                                // 如果连参数名称为空，则返回参数类型名称（完全限定名）
                                if (result.isEmpty()) {
                                    return "<" + i.getType().getName() + ">";
                                } else {
                                    return result;
                                }
                            }
                        }
                ).toList();

        // 把节点和参数名称拼接起来
        return Stream.concat(node.stream(), paramNames.stream()).toArray(Object[]::new);
    }


    public List<String> convert(Object[] objects) {
        ArrayList<String> result = new ArrayList<>();

        for (Object object : objects) {
            if (object instanceof String str) {
                result.add(str);
            } else if (object instanceof String[] strs) {
                result.addAll(List.of(strs));
            } else if (object instanceof ArrayList<?> strs) {
                result.addAll(strs.stream().map(String::valueOf).toList());
            }
        }

        return result;
    }

}
