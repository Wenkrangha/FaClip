package com.wenkrang.faClip.Moudle.FaCommand;

import com.wenkrang.faClip.Helper.ClassHelper;
import com.wenkrang.faClip.Manager.CommandManager;
import com.wenkrang.faClip.Moudle.FaCommand.AnnotationHandler.CmdHelpHandler;
import com.wenkrang.faClip.Moudle.FaCommand.AnnotationHandler.CmdNodeHandler;
import com.wenkrang.faClip.Moudle.FaCommand.AnnotationHandler.CmdPermissionHandler;
import com.wenkrang.faClip.Moudle.FaCommand.AnnotationHandler.RequireOPHandler;
import com.wenkrang.faClip.Moudle.FaCommand.FaCmdInterpreter.FaCmdInterpreter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FaCmdInstance {
    private final @NotNull CommandManager commandManager;
    private final Plugin plugin;
    private ArrayList<String> nodes = new ArrayList<>();
    private final ArrayList<FaCmd> faCmds = new ArrayList<>();
    private int refreshTaskId = -1;
    private FaCmdInterpreter faCmdInterpreter;

    public ArrayList<String> getNodes() {
        return nodes;
    }

    private void setNodes(ArrayList<String> nodes) {
        this.nodes = nodes;
    }

    private void removeNode(String node) {
        if (faCmds.stream().filter(i -> i.getNode().equals(node)).count() == 1) nodes.remove(node);
    }

    private void addNode(String node) {
        if (!nodes.contains(node)) nodes.add(node);
    }

    /**
     * 获取指定节点的FaCmd实例
     * @param node 节点
     * @return FaCmd实例
     */
    public @NotNull List<FaCmd> getFaCmd(String node) {
        return faCmds.stream().filter(i -> i.getNode().equals(node)).toList();
    }

    public @NotNull ArrayList<FaCmd> getFaCmds() {
        return faCmds;
    }

    public void addFaCmd(@NotNull FaCmd faCmd) {
        faCmds.add(faCmd);
        addNode(faCmd.getNode());
    }

    public void removeFaCmd(@NotNull FaCmd faCmd) {
        faCmds.remove(faCmd);
        removeNode(faCmd.getNode());
    }



    private FaCmdInstance(Plugin plugin) {
        commandManager = new CommandManager();
        this.plugin = plugin;
    }

    public static @NotNull FaCmdInstance create(Plugin plugin) {
        FaCmdInstance faCmdInstance = new FaCmdInstance(plugin);
        faCmdInstance.faCmdInterpreter = new FaCmdInterpreter(faCmdInstance, plugin);

        faCmdInstance.faCmdInterpreter.addAnnotationHandlers(new CmdNodeHandler());
        faCmdInstance.faCmdInterpreter.addAnnotationHandlers(new CmdPermissionHandler());
        faCmdInstance.faCmdInterpreter.addAnnotationHandlers(new RequireOPHandler());
        faCmdInstance.faCmdInterpreter.addAnnotationHandlers(new CmdHelpHandler());

        return faCmdInstance;
    }

    public void close() {
        for (FaCmd faCmd : faCmds) {
            try {
                if (faCmd.getCommand().isRegistered()) commandManager.unregister(faCmd.getCommand().getLabel());
            }catch (Exception ignored){}
        }
    }

    /**
     * 启用命令类（单次或少量调用）
     * <p>
     * 支持多次调用，会自动合并刷新请求
     * 但是不建议这样用！！！
     * 
     * @param commandClasses 命令类数组
     */
    public void enableFor(Class<?> @NotNull [] commandClasses) {
        registerCommands(commandClasses);
    }

    /**
     * 启用命令类（全量调用）
     * @param plugin 插件
     */
    public void enableForAll(@NotNull Plugin plugin) {
        enableFor(ClassHelper.getClasses(plugin.getClass()).toArray(Class[]::new));
    }
    
    private void registerCommands(Class<?> @NotNull [] commandClasses) {
        for (Class<?> commandClass : commandClasses) {
            final Method[] methods = commandClass.getMethods();
            for (Method method : methods) {
                faCmdInterpreter.initialize(method);
            }
        }
        
        scheduleRefresh();
    }
    
    /**
     * 调度命令刷新任务（防抖机制）
     * 多次调用会合并为一次刷新
     */
    private void scheduleRefresh() {
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            return;
        }
        
        if (refreshTaskId != -1) {
            Bukkit.getScheduler().cancelTask(refreshTaskId);
        }
        
        refreshTaskId = Bukkit.getScheduler().scheduleSyncDelayedTask(
            plugin, 
            () -> {
                Bukkit.getOnlinePlayers().forEach(Player::updateCommands);
                refreshTaskId = -1;
            }, 
            2L
        );
    }

    public @NotNull CommandManager getCommandManager() {
        return commandManager;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
