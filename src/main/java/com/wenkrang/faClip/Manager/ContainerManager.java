package com.wenkrang.faClip.Manager;

import com.wenkrang.faClip.Helper.FileHelper;
import com.wenkrang.faClip.Helper.ZipHelper;
import com.wenkrang.faClip.Moudle.FaContainer.FaClassLoader;
import com.wenkrang.faClip.Moudle.FaContainer.FaContainer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

/**
 * 容器管理器，用于管理和创建FaContainer容器实例
 * 负责加载JAR文件、创建类加载器、启动插件等操作
 */
public class ContainerManager {
    private final @NotNull Plugin plugin;
    private final @NotNull File temp;
    private @NotNull ArrayList<FaContainer> containers = new ArrayList<>();

    /**
     * 创建一个新的FaContainer容器实例
     *
     * @return 新创建的FaContainer实例
     * @throws IOException 文件操作异常
     */
    public @NotNull FaContainer createContainer() throws IOException {
        FaContainer faContainer = new FaContainer(null, UUID.randomUUID(), null, null);
        containers.add(faContainer);
        return faContainer;
    }

    /**
     * 加载指定的JAR文件到容器中
     *
     * @param file       要加载的JAR文件
     * @param faContainer 目标容器
     * @throws IOException 文件操作异常
     */
    public void loadJar(@NotNull File file, @NotNull FaContainer faContainer) throws IOException {
        //检查所加载的文件是否是Jar
        if (ZipHelper.isZipFile(file) && file.getName().substring(file.getName().lastIndexOf(".")).equals(".jar")) {
            //新建容器临时目录
            FileHelper.mkdirsWithLog(temp);
            File tempContainer = new File(temp, faContainer.getUuid().toString());
            FileHelper.mkdirsWithLog(tempContainer);

            //解压
            ZipHelper.unzip(file, tempContainer);

            //启用新类加载器
            FaClassLoader faClassLoader = new FaClassLoader(tempContainer.getAbsolutePath(), plugin);

            faContainer.setClassLoader(faClassLoader);

        } else {
            throw new RuntimeException("the current file isn't Jar");
        }
    }

    /**
     * 启动容器中的插件实例
     *
     * @param faContainer 要启动的容器
     * @throws IOException                    文件操作异常
     * @throws InvalidConfigurationException 配置文件格式异常
     * @throws ClassNotFoundException        类未找到异常
     * @throws InstantiationException        实例化异常
     * @throws NoSuchMethodException         方法不存在异常
     * @throws InvocationTargetException     方法调用异常
     * @throws IllegalAccessException      访问权限异常
     */
    public void bootstrap(@NotNull FaContainer faContainer) throws IOException, InvalidConfigurationException, ClassNotFoundException, InstantiationException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //获取主类
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.load(new File(faContainer.getRunFolder(), "plugin.yml"));
        String main = yamlConfiguration.getString("main");

        //加载主类
        FaClassLoader classLoader = faContainer.getClassLoader();

        Thread.currentThread().setContextClassLoader(classLoader);

        Class<?> clazz = classLoader.loadClass(main);

        //设置新插件实例
        Object newInstance;
        try {
            newInstance = clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new InstantiationException("Failed to instantiate class: " + clazz.getName());
        }

        faContainer.setInstance(newInstance);

        //调用启用函数
        Method onEnable = clazz.getMethod("onEnable");


        onEnable.invoke(newInstance);
    }


    /**
     * 构造函数，初始化容器管理器
     *
     * @param plugin 主插件实例
     */
    public ContainerManager(@NotNull Plugin plugin) {
        this.plugin = plugin;
        temp = new File("./.FaUtilities/Containers/" + plugin.getName() + "/");
    }

    /**
     * 获取所有容器列表
     *
     * @return 容器列表
     */
    public ArrayList<FaContainer> getContainers() {
        return containers;
    }
}
