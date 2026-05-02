package com.wenkrang.faClip.Moudle.FaContainer;

import java.io.File;
import java.util.UUID;

/**
 * 这是一个用于在一个插件安全修改ClassLoader的类
 */
public class FaContainer {

    /**
     * 插件实例对象
     */
    private Object Instance;
    /**
     * 插件对应的JAR文件
     */
    private File JarFile;

    /**
     * 容器的唯一标识符，用于区分不同的容器实例
     */
    private UUID Uuid;

    /**
     * 插件运行时的工作目录
     */
    private File RunFolder;

    /**
     * 自定义的类加载器，用于安全地加载和管理插件类
     */
    private FaClassLoader ClassLoader;

    public FaContainer(File jarFile, UUID uuid, File runFolder, FaClassLoader classLoader) {
        JarFile = jarFile;
        Uuid = uuid;
        RunFolder = runFolder;
        ClassLoader = classLoader;
    }

    public FaClassLoader getClassLoader() {
        return ClassLoader;
    }

    public File getRunFolder() {
        return RunFolder;
    }

    public UUID getUuid() {
        return Uuid;
    }

    public File getJarFile() {
        return JarFile;
    }


    public Object getInstance() {
        return Instance;
    }

    public void setInstance(Object instance) {
        Instance = instance;
    }

    public void setClassLoader(FaClassLoader classLoader) {
        ClassLoader = classLoader;
    }
}
