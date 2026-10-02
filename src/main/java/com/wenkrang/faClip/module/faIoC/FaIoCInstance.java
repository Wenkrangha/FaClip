package com.wenkrang.faClip.module.faIoC;

import com.wenkrang.faClip.helper.ClassHelper;
import com.wenkrang.faClip.module.faIoC.annotation.Service;
import com.wenkrang.faClip.module.faIoC.handlers.IoCInstanceHandler;
import com.wenkrang.faClip.module.faIoC.helper.FaIoCDependencyProvider;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FaIoCInstance {
    private final FaIoCInterpreter faIoCInterpreter;

    private final FaIoCDependencyProvider dependencyProvider;

    private final Map<Class<?>, FaIoCObject> iocContainer = new HashMap<>();

    private final Map<Class<?>, Object> envInstances = new HashMap<>();

    private final Plugin plugin;

    public FaIoCInstance(Plugin plugin) {
        this.plugin = plugin;
        this.faIoCInterpreter = new FaIoCInterpreter(this);
        this.dependencyProvider = new FaIoCDependencyProvider(this);


    }

    public Map<Class<?>, Object> getEnvInstances() {
        return envInstances;
    }

    public FaIoCDependencyProvider getDependencyProvider() {
        return dependencyProvider;
    }

    public FaIoCInterpreter getFaIoCInterpreter() {
        return faIoCInterpreter;
    }

    public Map<Class<?>, FaIoCObject> getIocContainer() {
        return iocContainer;
    }

    public FaIoCObject getContext(Class<?> clazz) {
        return iocContainer.get(clazz);
    }

    public void enableForAllService() {
        List<Class<?>> list = ClassHelper
                .getClasses(plugin.getClass())
                .stream().filter(i -> i.isAnnotationPresent(Service.class))
                .toList();

        for (Class<?> clazz : list) {
            load(clazz);
        }
    }

    public void addEnvInstance(Object o) {
        envInstances.put(o.getClass(), o);
    }

    public void load(Class<?> clazz) {
        FaIoCObject interpreter = faIoCInterpreter.interpreter(clazz);

        if (interpreter != null) {
            iocContainer.put(clazz, interpreter);
        }
    }

    public boolean hasContext(Class<?> clazz) {
        return iocContainer.containsKey(clazz);
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
