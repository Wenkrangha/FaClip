package com.wenkrang.faClip.Moudle.FaCommand.FaCmdInterpreter;

import com.wenkrang.faClip.Moudle.FaCommand.FaCmd;
import com.wenkrang.faClip.Moudle.FaCommand.FaCmdInstance;
import com.wenkrang.faClip.Moudle.FaCommand.Helper.CmdNodeHelper;
import com.wenkrang.faClip.Moudle.FaParam.FaParam;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * FaGuesser 类用于根据给定的参数猜测匹配的命令节点。
 * 它通过过滤和验证参数来确定哪些节点是有效的候选节点。
 */
public record FaGuesser(FaCmdInstance faCmdInstance) {
    /**
     * 构造函数，初始化 FaGuesser 实例。
     *
     * @param faCmdInstance 命令实例对象，用于获取节点和方法信息
     */
    public FaGuesser {
    }

    /**
     * 获取当前实例中的命令实例对象。
     *
     * @return 当前实例中的命令实例对象
     */
    @Override
    public FaCmdInstance faCmdInstance() {
        return faCmdInstance;
    }


    public Parameter @NotNull [] skipContext(Parameter @NotNull [] parameters){
        return Arrays.stream(parameters)
                .filter(i -> !i.getType().equals(FaCmdContext.class))
                .toArray(Parameter[]::new);
    }

    /**
     * 根据参数筛选出最终的候选命令列表。
     *
     * @param args 实际参数列表
     * @return 符合条件的命令列表
     */
    public @NotNull List<FaCmd> guessFaCmd(@NotNull List<String> args, @NotNull guessMode mode) {
        // 猜测节点
        // 首先分出参数中的命令节点和参数
        // 命令节点是主命令和子命令，参数是方法参数

        // 考虑三种可空性
        // 主命令空，子命令空，参数空

        // 1.主命令空
        // 由于主命令是空的，所以全部匹配，没必要浪费算力

        if (args.isEmpty())
            return mode.equals(guessMode.fuzzy) ?
                new ArrayList<>(faCmdInstance.getFaCmds())
                :
                faCmdInstance.getFaCmd(args.getFirst());

        // 2.子命令空（不完整）
        // 所有参数拼在一起
        String convertedArgs = CmdNodeHelper.formNode(args);

        // 匹配分为两个阶段，命令阶段和参数阶段，每个阶段都可空，所以需要边界检查


        int counter = 0;
        while (counter < convertedArgs.length()) {
            // 逐个进行检测，直到没有匹配的命令，即进入参数阶段
            int finalCounter = counter;
            String substring = convertedArgs.substring(0, finalCounter);

            if (faCmdInstance.getNodes().stream()
                    .noneMatch(node -> node.startsWith(substring) && node.length() >= substring.length()))
                break;

            counter++;
        }

        // 这里获取结果
        String prefix = convertedArgs.substring(0, counter);
        if (prefix.charAt(prefix.length() - 1) == '.') prefix = prefix.substring(0, prefix.length() - 1);

        // 真正的参数
        List<String> realArgs = CmdNodeHelper.removeEmpty(CmdNodeHelper.removeNode(prefix, args));
        // 到这里，我们就获得到了节点和参数
        // 3.如果没有参数
        final String finalPrefix = prefix;
        if (realArgs.isEmpty()) {
            // 模糊模式这里就可以返回了
            // 但是我们需要考虑一个问题
            // 当玩家确认了上一个子命令时，下一个子命令必须是上一个子命令的子命令
            // 如应只返回test.test，而不是test.test和test.test2
            // test2 startsWith test
            if (mode.equals(guessMode.fuzzy)) {
                if (args.getLast().isEmpty()) {
                    // 最后一个参数为空，说明用户输入了空格但未输入具体内容
                    // 应该返回当前前缀的直接子命令
                    return Stream.concat(faCmdInstance.getFaCmd(finalPrefix).stream(),
                            faCmdInstance.getFaCmds().stream()
                                    .filter(i -> i.getNode().startsWith(finalPrefix + "."))
                    ).toList();
                } else {
                    // 最后一个参数不为空，进行前缀匹配
                    return faCmdInstance.getFaCmds().stream()
                            .filter(i -> i.getNode().startsWith(finalPrefix))
                            .toList();
                }
            }
        }

        // 参数匹配
        // 我认为如果都进参数匹配阶段了，那么参数之前的都应该是匹配的
        List<FaCmd> faCmds = faCmdInstance.getFaCmds().stream().filter(i -> i.getNode().equals(finalPrefix)).toList();

        ArrayList<FaCmd> result = new ArrayList<>();
        for (FaCmd cmd : faCmds) {
            Method method = cmd.getMethod();

            // 如果精确匹配的话，后面可以省了
            if (skipContext(method.getParameters()).length != realArgs.size()
                    && mode.equals(guessMode.full))
                continue;

            Parameter[] parameters = skipContext(method.getParameters());
            FaParam faParam = new FaParam();
            // 这里显式转换，因为没法静态猜测
            // 方法的参数类型
            List<Type> methodParamTypes = Arrays.stream(parameters).map(i -> (Type) i.getType()).toList();
            // 输入的参数类型
            List<Set<Type>> argsTypes = realArgs.stream().map(faParam::check).toList();

            if (IntStream.range(0,Math.min(skipContext(parameters).length,realArgs.size()))
                    .allMatch(i -> argsTypes.get(i).contains(methodParamTypes.get(i)))) {
                result.add(cmd);
            }
        }


        return result;
    }

    public enum guessMode {
        /**
         * 完全匹配
         */
        full,
        /**
         * 模糊匹配
         */
        fuzzy
    }
}
