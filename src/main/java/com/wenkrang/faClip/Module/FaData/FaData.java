package com.wenkrang.faClip.Module.FaData;

import com.wenkrang.faClip.Module.FaMessage.Helper.i18nHelper;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * YAML 数据工具类
 * <p>封装 {@link YamlConfiguration}，提供更便捷的数据读写、保存、重载等操作</p>
 */
public class FaData {

    // 目标文件
    private File file;

    // 插件实例
    public static Plugin plugin = null;

    private YamlConfiguration config = null;

    // ==================== 构造函数 ====================

    /**
     * 用指定文件初始化 FaYamlData
     * @param f YAML 文件
     */
    public FaData(File f) {
        file = f;
        init();
    }

    /**
     * 用指定文件夹和文件名初始化 FaYamlData
     * @param workspace 工作目录
     * @param child 文件名
     */
    public FaData(File workspace, String child) {
        file = new File(workspace, child);
        init();
    }

    /**
     * 用指定节点名初始化 FaYamlData
     * <p>节点必须以 "/" 分割</p>
     * <p>eg. "playerData/stuff/xxxxxxxxxxxxxx-xxxxxxxxxxxxx-xxxxxxxxxxxxx-xxxxxxxxxxxxxxx"</p>
     * <p>结尾不要写文件扩展名 ( .dat 之类的 )</p>
     * @param Node 节点
     */
    public FaData(String Node) {
        if (plugin != null) {
            File dataFolder = plugin.getDataFolder();

            // 初始化文件夹
            if (!(dataFolder.isDirectory() && dataFolder.exists())) {
                if (!dataFolder.mkdirs()) {
                    throw new RuntimeException(i18nHelper
                            .ft("FaData.Exception.FaYamlData.FolderCannotInit"
                                    , dataFolder.getName())
                    );
                }
            }

            file = new File(dataFolder, Node + ".yaml");
            init();
        } else {
            throw new NullPointerException(i18nHelper.t("FaData.Exception.FaYamlData.PluginIsNotinitialized"));
        }
    }

    /**
     * 从 InputStream 初始化 FaYamlData（只读，无关联文件）
     * @param inputStream 输入流
     */
    public FaData(InputStream inputStream) {
        config = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));
    }

    // ==================== 初始化 ====================

    /**
     * 初始化静态插件实例
     * @param p 插件实例
     */
    public static void init(Plugin p) {
        plugin = p;
    }

    /**
     * 加载/重载配置文件
     */
    public void init() {
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
        } else {
            config = new YamlConfiguration();
        }
    }

    // ==================== 核心读写 ====================

    /**
     * 获取底层的 YamlConfiguration 实例
     * @return YamlConfiguration
     */
    public YamlConfiguration getYaml() {
        return config;
    }

    /**
     * 设置指定路径的值
     * @param key 路径
     * @param value 值
     */
    public void set(@NotNull String key, @Nullable Object value) {
        config.set(key, value);
    }

    /**
     * 获取指定路径的原始值
     * @param key 路径
     * @return 值，不存在时返回 null
     */
    @Nullable
    public Object get(@NotNull String key) {
        return config.get(key);
    }

    /**
     * 获取指定路径的值，不存在时返回默认值
     * @param key 路径
     * @param defaultValue 默认值
     * @return 值或默认值
     */
    @Nullable
    public Object getOrDefault(@NotNull String key, @Nullable Object defaultValue) {
        Object val = config.get(key);
        return val != null ? val : defaultValue;
    }

    /**
     * 检查配置中是否包含指定路径
     * @param key 路径
     * @return 是否包含
     */
    public boolean has(@NotNull String key) {
        return config.contains(key);
    }

    /**
     * 移除指定路径及其所有子节点
     * @param key 路径
     */
    public void remove(@NotNull String key) {
        config.set(key, null);
    }

    // ==================== 类型安全的 Getter ====================

    public String getString(@NotNull String key) {
        return config.getString(key);
    }

    public String getString(@NotNull String key, String def) {
        return config.getString(key, def);
    }

    public int getInt(@NotNull String key) {
        return config.getInt(key);
    }

    public int getInt(@NotNull String key, int def) {
        return config.getInt(key, def);
    }

    public boolean getBoolean(@NotNull String key) {
        return config.getBoolean(key);
    }

    public boolean getBoolean(@NotNull String key, boolean def) {
        return config.getBoolean(key, def);
    }

    public double getDouble(@NotNull String key) {
        return config.getDouble(key);
    }

    public double getDouble(@NotNull String key, double def) {
        return config.getDouble(key, def);
    }

    public long getLong(@NotNull String key) {
        return config.getLong(key);
    }

    public long getLong(@NotNull String key, long def) {
        return config.getLong(key, def);
    }

    public List<String> getStringList(@NotNull String key) {
        return config.getStringList(key);
    }

    public List<Integer> getIntegerList(@NotNull String key) {
        return config.getIntegerList(key);
    }

    // ==================== 节点操作 ====================

    /**
     * 获取指定路径的配置节点
     * @param key 路径
     * @return ConfigurationSection，不存在时返回 null
     */
    @Nullable
    public ConfigurationSection getSection(@NotNull String key) {
        return config.getConfigurationSection(key);
    }

    /**
     * 创建或获取指定路径的配置节点
     * @param key 路径
     * @return ConfigurationSection
     */
    @NotNull
    public ConfigurationSection createSection(@NotNull String key) {
        return config.createSection(key);
    }

    /**
     * 获取根节点下的所有键
     * @param deep 是否递归获取子节点
     * @return 键的集合
     */
    public Set<String> getKeys(boolean deep) {
        return config.getKeys(deep);
    }

    /**
     * 获取指定节点下的所有键
     * @param path 节点路径
     * @param deep 是否递归获取子节点
     * @return 键的集合，节点不存在时返回空集合
     */
    @NotNull
    public Set<String> getKeys(@NotNull String path, boolean deep) {
        ConfigurationSection section = config.getConfigurationSection(path);
        return section != null ? section.getKeys(deep) : Set.of();
    }

    // ==================== 文件操作 ====================

    /**
     * 保存配置到文件
     * <p>会自动创建不存在的父目录</p>
     */
    public void save() {
        try {
            // 确保父目录存在
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            config.save(file);
        } catch (IOException e) {
            i18nHelper.fw("FaData.Exception.FaYamlData.DataCannotBeSaved", file.getName());
        }
    }

    /**
     * 重载配置（从文件重新加载）
     */
    public void reload() {
        init();
    }

    /**
     * 从资源文件加载默认值（不会覆盖已有数据）
     * @param resourcePath 资源路径（如 "config.yml"）
     */
    public void setDefaultsFromResource(@NotNull String resourcePath) {
        if (plugin == null) return;
        InputStream resource = plugin.getResource(resourcePath);
        if (resource == null) return;
        YamlConfiguration defaults = YamlConfiguration.loadConfiguration(new InputStreamReader(resource));
        config.setDefaults(defaults);
        config.options().copyDefaults(true);
    }

    /**
     * 删除关联的文件
     * @return 是否删除成功
     */
    public boolean deleteFile() {
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 检查关联的文件是否存在
     * @return 文件是否存在
     */
    public boolean fileExists() {
        return file.exists();
    }

    /**
     * 获取关联的文件
     * @return 文件对象
     */
    public File getFile() {
        return file;
    }

    // ==================== 批量操作 ====================

    /**
     * 批量设置值
     * @param entries 键值对映射
     */
    public void setAll(@NotNull Map<String, Object> entries) {
        entries.forEach(config::set);
    }

    /**
     * 将另一个 YamlConfiguration 的数据合并到当前配置中
     * <p>源数据中已存在的键不会被覆盖</p>
     * @param source 源配置
     * @param overwrite 是否覆盖已有值
     */
    public void merge(@NotNull YamlConfiguration source, boolean overwrite) {
        for (String key : source.getKeys(true)) {
            if (overwrite || !config.contains(key)) {
                config.set(key, source.get(key));
            }
        }
    }

    /**
     * 清空所有配置
     */
    public void clear() {
        for (String key : config.getKeys(false)) {
            config.set(key, null);
        }
    }

    /**
     * 检查配置是否为空（没有任何键）
     * @return 是否为空
     */
    public boolean isEmpty() {
        return config.getKeys(false).isEmpty();
    }
}
