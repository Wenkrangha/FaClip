package com.wenkrang.faClip.Module.FaCommand.FaParam.BukkitParam;

import com.wenkrang.faClip.Module.FaCommand.Annotation.DesProvider;
import com.wenkrang.faClip.Module.FaCommand.FaCmdInterpreter.FaCmdContext;
import com.wenkrang.faClip.Module.FaCommand.FaParam.SimpleParam;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Set;

public class WorldParam implements SimpleParam, DesProvider {
    @Override
    public @NotNull String[] getDes(FaCmdContext faCmdContext) {
        return Bukkit.getWorlds().stream().map(World::getName).toArray(String[]::new);
    }

    @Override
    public Set<Type> getType() {
        return Set.of(World.class);
    }

    @Override
    public boolean check(String param) {
        return Bukkit.getWorlds().stream().anyMatch(world -> world.getName().equals(param));
    }

    @Override
    public Object convert(String param) {
        return Bukkit.getWorlds().stream().filter(i -> i.getName().equals(param)).findFirst();
    }

    @Override
    public @Nullable String getName(Type type) {
        return getType().contains(type) ? "World" : null;
    }
}
