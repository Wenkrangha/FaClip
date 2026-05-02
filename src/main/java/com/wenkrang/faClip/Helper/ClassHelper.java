package com.wenkrang.faClip.Helper;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import static com.wenkrang.faClip.Moudle.FaMessage.Helper.i18nHelper.ft;

/**
 * 类操作帮助类
 * 用于扫描和获取项目中的所有类，支持松散目录和 JAR 包两种形式
 */
public class ClassHelper {
    /**
     * 获取指定类所在包中的所有类
     * 自动检测是松散目录还是 JAR 包格式
     *
     * @param clazz 参考类对象
     * @return 类列表
     */
    public static @NotNull ArrayList<Class<?>> getClasses(@NotNull Class<?> clazz) {
        ArrayList<Class<?>> classes = new ArrayList<>();

        // 获取当前类加载器根目录
        // 哎呀，也就是你包里的类
        URL url = clazz.getClassLoader().getResource("");
        // 要么是松散文件夹，此时不为空
        if (url != null) {
            File file = new File(url.getFile());
            classes.addAll(getClasses(file, ""));
        } else {
            // 要么是打包成jar了，需要用JarFile访问

            // 这里获取到该jar的文件夹位置
            File file = new File(clazz.getProtectionDomain().getCodeSource().getLocation().getFile());

            // 导入Jar内所有的类
            classes.addAll(handleJarFile(file));
        }

        return classes;
    }

    /**
     * 处理jar文件，返回jar内所有类
     *
     * @param file jar文件
     * @return 类列表
     */
    public static @NotNull ArrayList<Class<?>> handleJarFile(@NotNull File file) {
        ArrayList<Class<?>> classes = new ArrayList<>();

        // 用try来打开JarFile
        try (JarFile jarFile = new JarFile(file)) {

            // 获取到所有的JarEntry

            jarFile.stream()
                    .filter(i -> !i.isDirectory())
                    .filter(i -> i.getName().endsWith(".class"))
                    .map(i -> i.getName().substring(0, i.getName().length() - 6)
                            .replace(".class", "")
                            .replace("/", "."))
                    .map(i -> {
                        try {
                            return Class.forName(i);
                        } catch (ClassNotFoundException e) {
                            Logger.getGlobal().warning(ft("FaCommand.Error.ClassHelper.CannotGetClass", i));
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .forEach(classes::add);



        }catch (IOException e) {
            Logger.getGlobal().warning(ft("FaCommand.Error.ClassHelper.CannotOpenJarFile", file.getPath()));
        }

        return classes;
    }

    /**
     * 获取目录下的所有类
     *
     * @param file 目录
     * @param workSpace 工作目录
     * @return 类列表
     */
    public static @NotNull ArrayList<Class<?>> getClasses(@NotNull File file, String workSpace) {
        ArrayList<Class<?>> classes = new ArrayList<>();

        // 检查是否是目录
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File currentFile : files) {
                    String clazzPath = workSpace + file.getName() + "/";

                    // 递归导入类
                    classes.addAll(getClasses(currentFile, clazzPath));
                }
            }
        }else if (file.getPath().endsWith(".class")) {
            // 获取类
            // 转换为Binary Name
            String preClassName = (workSpace + file.getName())
                    .replace(".class","")
                    .replace("/", ".");
            // 这里去掉第一次循环加上的运行目录名
            String className =
                    preClassName
                    .substring((preClassName.split("\\.")[0] + "/").length());

            try {
                // 导入类
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException e) {
                Logger.getGlobal().warning(ft("FaCommand.Error.ClassHelper.CannotGetClass", file.getPath()));
            }

        }

        return classes;
    }
}
