package com.wenkrang.faClip.Helper;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static com.wenkrang.faClip.Module.FaMessage.Fm.waring;
import static com.wenkrang.faClip.Module.FaMessage.Helper.i18nHelper.ft;

public class ResourceHelper {
    // 存储资源路径而不是 URL
    private final List<String> resourcePaths = new ArrayList<>();
    
    // 参考类
    private final Class<?> aClass;

    private final boolean isJar;

    public ResourceHelper(Class<?> clazz) {
        aClass = clazz;
        URL url = clazz.getClassLoader().getResource("");

        if (url != null) {
            isJar = false;
            // 开发环境：从文件夹加载
            File file = new File(url.getFile());
            scanDirectory(file, "");
        } else {
            isJar = true;
            // 生产环境：从 jar 加载
            try {
                String jarPath = clazz.getProtectionDomain()
                    .getCodeSource().getLocation().getFile();
                scanJarFile(new File(jarPath));
            } catch (Exception e) {
                waring(ft("FaCommand.Error.ClassHelper.CannotOpenJarFile", e.getMessage()));
            }
        }
    }

    /**
     * 扫描 jar 文件获取所有资源路径
     */
    private void scanJarFile(File jarFile) throws IOException {
        try (JarFile jar = new JarFile(jarFile)) {
            jar.stream()
                .filter(entry -> !entry.isDirectory())
                .forEach(entry -> resourcePaths.add(entry.getName()));
        }
    }

    /**
     * 递归扫描目录获取所有资源路径
     * @param file 目录
     * @param workSpace 工作目录
     */
    private void scanDirectory(@NotNull File file, String workSpace) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File currentFile : files) {
                    String filePath = workSpace + file.getName() + "/";

                    // 递归导入文件
                    scanDirectory(currentFile, filePath);
                }
            }
        }else {
            String rootPath = aClass.getClassLoader()
                    .getResource("")
                    .getFile()
                    .substring(1)
                    .replace("\\", "/");

            String relative = file.getPath().replace("\\", "/")
                    .replace(rootPath, "");

            resourcePaths.add(relative);
        }
    }

    /**
     * 获取所有资源路径列表
     * @return 不可修改的资源路径列表
     */
    public List<String> getResourcePaths() {
        return Collections.unmodifiableList(resourcePaths);
    }

    /**
     * 获取指定资源的 URL
     * @param path 资源路径
     * @return 资源的 URL，如果不存在则返回 null
     */
    public URL getResourceUrl(String path) {
        return aClass.getClassLoader().getResource(path);
    }

    /**
     * 获取指定资源的 InputStream
     * @param path 资源路径
     * @return 资源的 InputStream，如果不存在则返回 null
     */
    public InputStream getResourceAsStream(String path) {
        return aClass.getClassLoader().getResourceAsStream(path);
    }

    /**
     * 按扩展名过滤资源
     * @param extension 扩展名（不包含点）
     * @return 匹配的资源路径列表
     */
    public List<String> getResourcesByExtension(String extension) {
        return resourcePaths.stream()
            .filter(path -> path.endsWith("." + extension))
            .collect(Collectors.toList());
    }

    /**
     * 按前缀过滤资源
     * @param prefix 路径前缀
     * @return 匹配的资源路径列表
     */
    public List<String> getResourcesByPrefix(String prefix) {
        return resourcePaths.stream()
            .filter(path -> path.startsWith(prefix))
            .collect(Collectors.toList());
    }

    public Class<?> getaClass() {
        return aClass;
    }

    public boolean isJar() {
        return isJar;
    }
}
