package com.wenkrang.faClip.module.faCommand;

import com.wenkrang.faClip.helper.ClassHelper;
import com.wenkrang.faClip.manager.CommandManager;
import com.wenkrang.faClip.module.FaModule;
import com.wenkrang.faClip.module.faCommand.annotationHandler.*;
import com.wenkrang.faClip.module.faCommand.interpreter.FaCmdInterpreter;
import com.wenkrang.faClip.module.faInterface.FaInterfaceInstance;
import com.wenkrang.faClip.module.faInterface.FaIntf;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FaCmdInstance implements FaModule {
    private final FaInterfaceInstance faInterfaceInstance;
    private final @NotNull CommandManager commandManager;
    private final Plugin plugin;
    private ArrayList<String> nodes = new ArrayList<>();
    private final ArrayList<FaCmd> faCmds = new ArrayList<>();
    private int refreshTaskId = -1;
    private FaCmdInterpreter faCmdInterpreter;
    private boolean DebugMode = false;
    private FaModule.Status status = Status.STOPPED;

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

    /**
     * 根据接口获取命令
     * @param faIntf 接口
     * @return 查到的接口
     */
    public FaCmd getFaCmd(FaIntf faIntf) {
        return faCmds.stream()
                .filter(i -> i.getFaIntf().getId().equals(faIntf.getId()))
                .findFirst()
                .orElse(null);
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



    public FaCmdInstance(Plugin plugin) {
        this.plugin = plugin;
        commandManager = new CommandManager();

        faInterfaceInstance = new FaInterfaceInstance(plugin);

        faCmdInterpreter = new FaCmdInterpreter(this, plugin);
        faCmdInterpreter.addAnnotationHandlers(new CmdNodeHandler());
        faCmdInterpreter.addAnnotationHandlers(new CmdPermissionHandler());
        faCmdInterpreter.addAnnotationHandlers(new RequireOPHandler());
        faCmdInterpreter.addAnnotationHandlers(new CmdHelpHandler());
        faCmdInterpreter.addAnnotationHandlers(new CmdOnlyForHelpHandler());
        faCmdInterpreter.addAnnotationHandlers(new CmdPlayerHandler());
    }

    @Override
    public void auto() {
        status = Status.STARTING;

        enableForAll();

        status = Status.READY;
    }

    public void close() {
        for (FaCmd faCmd : faCmds) {
            if (faCmd.getCommand() != null && faCmd.getCommand().isRegistered())
                commandManager.unregister(faCmd.getCommand().getLabel());
        }

        status = Status.STOPPED;
    }

    @Override
    public Status status() {
        return status;
    }

    @Override
    public String getName() {
        return "FaCommand";
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
     */
    public void enableForAll() {
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

    public boolean isDebugMode() {
        return DebugMode;
    }

    public void setDebugMode(boolean debugMode) {
        DebugMode = debugMode;
    }

    public FaInterfaceInstance getFaInterfaceInstance() {
        return faInterfaceInstance;
    }
}
