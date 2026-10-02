package com.wenkrang.faClip.module.faInterface;

import com.wenkrang.faClip.module.faCommand.helper.NodeHelper;
import com.wenkrang.faClip.module.faInterface.param.FaParam;
import com.wenkrang.faClip.module.faInterface.param.SimpleParam;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * FaClip的接口
 */
public class FaIntf {
    private final String id = UUID.randomUUID().toString();
    // 接口指向的方法
    private Method method;

    public void setNode(String node) {
        this.node = node;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    private String node;

    public SimpleParam[] paramConvertors;

    private final FaInterfaceInstance faInterfaceInstance;

    public FaIntf(FaInterfaceInstance faInterfaceInstance) {

        this.faInterfaceInstance = faInterfaceInstance;
    }

    public Method getMethod() {
        return method;
    }

    public String getNode() {
        return node;
    }

    public String getId() {
        return id;
    }

    public FaIntfCheckResult checkParam(String[] args) {
        // 获取方法参数类型
        Class<?>[] parameterTypes = method.getParameterTypes();

        // 获取真参数（删除节点部分）
        List<String> TrueArgs = NodeHelper.removeNode(node, List.of(args));

        // 准备参数转换器
        FaParam faParam = faInterfaceInstance.getFaIntfInterpreter().getFaParam();

        // 匹配计数器
        int match = 0;
        int contextCounter = 0;

        // 获取排除FaIntfContext的参数顺序
        ArrayList<Integer> trueOrder = new ArrayList<>();
        for (int i = 0;i < parameterTypes.length;i++) {
            if (!(FaIntfContext.class.isAssignableFrom(parameterTypes[i]))) {
                trueOrder.add(i);
            }else {
                match++;
                contextCounter++;
            }
        }

        // 按照真正的顺序获取方法参数类型
        for (int i = 0;i < trueOrder.size();i++) {
            Type parameterType = parameterTypes[trueOrder.get(i)];
            // 如果参数数量足够
            if (!(i + 1 > TrueArgs.size())) {
                // 检查参数类型是否符合
                SimpleParam convertor = faParam.getConvertor(parameterType);
                if (convertor != null && convertor.check(TrueArgs.get(i))) match++;
            }
        }

        if (TrueArgs.size() > parameterTypes.length - contextCounter)
            return FaIntfCheckResult.NO_MATCH;

        if (match == parameterTypes.length) {
            return FaIntfCheckResult.FULL_MATCH;
        }
        if (match == parameterTypes.length - 1) {
            return FaIntfCheckResult.PARAM_LAST_NO_MATCH;
        }else if (match > 0) {
            return FaIntfCheckResult.PARAM_PARTIAL_NO_MATCH;
        }
        return FaIntfCheckResult.PARAM_NO_MATCH;
    }

    public FaIntfCheckResult checkNode(String[] args) {
        ArrayList<String> strings = NodeHelper.separateNode(node);

        if (args.length == 0) return FaIntfCheckResult.NODE_NO_MATCH;
        if (!args[0].equalsIgnoreCase(strings.getFirst())) return FaIntfCheckResult.NODE_NO_MATCH;

        // 检查节点
        int match = 0;

        // 检查匹配数
        for (int i = 0; i < strings.size(); i++) {
            // 防止越界
            if (i + 1 > args.length) break;
            if (args[i].equalsIgnoreCase(strings.get(i))) match++; else break;
        }

        if (match == strings.size()) {
            return FaIntfCheckResult.NODE_FULL_MATCH;
        }
        if (match == strings.size() - 1) {
            return FaIntfCheckResult.NODE_LAST_NO_MATCH;
        }
        if (match > 0) {
            return FaIntfCheckResult.NODE_PARTIAL_NO_MATCH;
        }
        return FaIntfCheckResult.NODE_NO_MATCH;
    }

    /**
     * 进行参数检查，检查参数是否符合接口要求
     * @param args 参数
     * @return 检查结果
     */
    public FaIntfCheckResult check(String[] args) {
        FaIntfCheckResult faIntfCheckResult = checkNode(args);

        if (faIntfCheckResult == FaIntfCheckResult.NODE_FULL_MATCH) {
            return checkParam(args);
        }else return faIntfCheckResult;
    }

    public boolean fuzzyCheck(String[] args) {
        if (args.length == 0) return false;

        // 模糊检查最后一项不检查
        String[] arrayNeedCheck
                = Arrays.stream(args).limit(args.length - 1).toArray(String[]::new);

        ArrayList<String> strings = NodeHelper.separateNode(node);

        // 检查节点
        for (int i = 0;i < arrayNeedCheck.length;i++) {
            if (i >= strings.size()) break;
            if (!arrayNeedCheck[i].equalsIgnoreCase(strings.get(i))) return false;
        }

        // 检查参数
        Class<?>[] parameterTypes = method.getParameterTypes();
        List<Class<?>> skip = Arrays.stream(parameterTypes).filter(i -> !(FaIntfContext.class.isAssignableFrom(i))).toList();
        // 获取真参数（删除节点部分）
        List<String> TrueArgs = NodeHelper.removeNode(node, List.of(arrayNeedCheck));

        // 检查参数转换器
        FaParam faParam = faInterfaceInstance.getFaIntfInterpreter().getFaParam();

        for (int i = 0;i < TrueArgs.size();i++) {
            if (i >= skip.size()) return false;
            if (!faParam.check(TrueArgs.get(i)).contains(skip.get(i))) return false;
        }

        return true;
    }

    public ArrayList<Integer> getTrueOrder(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        ArrayList<Integer> result = new ArrayList<>();

        for (int i = 0;i < parameterTypes.length;i++) {
            if (!(FaIntfContext.class.isAssignableFrom(parameterTypes[i]))) {
                result.add(i);
            }
        }

        return result;
    }

    public Object invoke(Object object, FaIntfContext faIntfContext,@NotNull String[] args) throws InvocationTargetException, IllegalAccessException {
        // 获取除了FaIntfContext的真正的参数位
        ArrayList<Integer> trueOrder = getTrueOrder(method);
        Object[] convertedArgs = new Object[method.getParameterCount()];
        Class<?>[] parameterTypes = method.getParameterTypes();

        List<String> methodArgs = NodeHelper.removeNode(node, Arrays.stream(args).toList());

        // 填充FaIntfContext
        for (int i = 0;i < method.getParameterCount();i++) {
            if (FaIntfContext.class.isAssignableFrom(parameterTypes[i])) {
                convertedArgs[i] = faIntfContext;
            }
        }

        // 填充参数
        for (int i = 0;i < trueOrder.size();i++) {
            int order = trueOrder.get(i);
            String arg = methodArgs.get(i);

            SimpleParam paramConvertor = paramConvertors[order];
            if (paramConvertor == null) continue;

            Object convert = paramConvertor.convert(arg);

            convertedArgs[order] = convert;
        }
        return method.invoke(object, convertedArgs);
    }
}
