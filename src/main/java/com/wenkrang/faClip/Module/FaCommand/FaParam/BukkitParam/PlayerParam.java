package com.wenkrang.faClip.Module.FaCommand.FaParam.BukkitParam;

import com.wenkrang.faClip.Module.FaCommand.Annotation.DesProvider;
import com.wenkrang.faClip.Module.FaCommand.FaCmdInterpreter.FaCmdContext;
import com.wenkrang.faClip.Module.FaCommand.FaParam.SimpleParam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;

public class PlayerParam implements SimpleParam, DesProvider {
    @Override
    public Set<Type> getType() {
        return Set.of(Player.class);
    }

    @Override
    public boolean check(String param) {
        if (param.matches("\\w+")){
            return Bukkit.getPlayerExact(param) != null;
        }
        return false;
    }

    @Override
    public Object convert(String param) {
        return Bukkit.getPlayerExact(param);
    }

    @Override
    public @Nullable String getName(Type type) {
        return getType().equals(type) ? "Player" : null;
    }

    @Override
    public @NotNull String[] getDes(FaCmdContext faCmdContext) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getDisplayName)
                .toArray(String[]::new);
    }
}
