package com.wenkrang.faClip.Moudle.FaContainer;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义类加载器，用于从指定目录加载类文件，并破坏双亲委派机制。
 */
public class FaClassLoader extends ClassLoader{
    /**
     * 已加载类的缓存，防止重复加载。
     */
    private final Map<String, Class<?>> loadedClasses = new ConcurrentHashMap<>();

    /**
     * 类文件所在的目录路径。
     */
    private final String classDir;

    /**
     * 构造方法，初始化类加载器。
     *
     * @param classDir 类文件所在的目录路径
     */
    public FaClassLoader(String classDir, @NotNull Plugin plugin) {
        super(plugin.getClass().getClassLoader()); // 设置父类加载器
        this.classDir = classDir;
    }

    /**
     * 重写loadClass方法，实现自定义类加载逻辑。
     *
     * @param name    类名
     * @param resolve 是否需要解析类
     * @return 加载的类
     * @throws ClassNotFoundException 类未找到异常
     */
    @Override
    protected Class<?> loadClass(@NotNull String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz;
        // 1. 优先尝试当前ClassLoader加载（关键：确保依赖类也通过当前ClassLoader加载）
        try {
            clazz = findClass(name);
            if (resolve) {
                resolveClass(clazz);
            }
            return clazz;
        } catch (ClassNotFoundException e) {
            // 2. 如果当前ClassLoader找不到，再尝试父类加载器（仅用于系统类）
            if (getParent() != null) {
                return getParent().loadClass(name);
            }
            throw e;
        }
    }

    /**
     * 查找并加载指定名称的类。
     *
     * @param name 类名
     * @return 加载的类对象
     * @throws ClassNotFoundException 类未找到异常
     */
    @Override
    protected Class<?> findClass(@NotNull String name) throws ClassNotFoundException {
        // 1. 检查是否已缓存
        Class<?> cached = loadedClasses.get(name);
        if (cached != null) {
            return cached;
        }

        // 2. 从指定目录加载class文件
        String classPath = name.replace('.', '/') + ".class";
        File classFile = new File(classDir, classPath);

        // 检查文件是否存在
        if (!classFile.exists() || !classFile.isFile()) {
            throw new ClassNotFoundException("Class not found: " + name + " at " + classFile.getAbsolutePath());
        }

        try (FileInputStream fis = new FileInputStream(classFile)) {
            // 3. 读取class文件内容
            byte[] data = new byte[(int) classFile.length()];
            int bytesRead = fis.read(data);
            if (bytesRead != data.length) {
                throw new IOException("Failed to read full class file: " + classFile.getAbsolutePath());
            }

            // 4. 定义类
            Class<?> clazz = defineClass(name, data, 0, data.length);

            // 5. 缓存类
            loadedClasses.put(name, clazz);
            return clazz;
        } catch (IOException e) {
            throw new ClassNotFoundException("Failed to load class: " + name, e);
        }
    }

    /**
     * 获取资源文件的URL。
     *
     * @param name 资源名称
     * @return 资源对应的URL，如果找不到则返回null
     */
    @Override
    public URL getResource(String name) {
        File Dir = new File(classDir);
        if (Dir.exists()) {
            File[] files = Dir.listFiles();
            for (File file : files) {
                if (file.getName().equals(name)) {
                    try {
                        return file.toURI().toURL();
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return super.getResource(name);
    }
}
