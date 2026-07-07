package com.wenkrang.faClip.Helper;

import com.wenkrang.faClip.Moudle.FaMessage.Fm;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import static com.wenkrang.faClip.Moudle.FaMessage.Helper.i18nHelper.ft;
import static com.wenkrang.faClip.Moudle.FaMessage.Fm.waring;

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

        ResourceHelper resourceHelper = new ResourceHelper(clazz);

        ClassLoader classLoader = clazz.getClassLoader();

        resourceHelper.getResourcesByExtension("class")
                .stream()
                .map(i -> i.replace("/", ".")
                        .replace(".class", ""))
                .map(i -> {
                    try {
                        return Class.forName(i, true, classLoader);
                    } catch (ClassNotFoundException e) {
                        waring(ft("FaCommand.Error.ClassHelper.CannotGetClass", i));
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .forEach(classes::add);

        return classes;
    }
}
