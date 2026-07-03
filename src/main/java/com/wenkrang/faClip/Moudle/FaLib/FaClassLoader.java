package com.wenkrang.faClip.Moudle.FaLib;

import org.bukkit.plugin.Plugin;
public class FaClassLoader extends ClassLoader {
    private Plugin plugin;

    private ClassLoader FaLoader;

    public FaClassLoader(Plugin plugin) {
        super(plugin.getClass().getClassLoader());
        this.plugin = plugin;
        FaLoader = this.getClass().getClassLoader();
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
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
}
