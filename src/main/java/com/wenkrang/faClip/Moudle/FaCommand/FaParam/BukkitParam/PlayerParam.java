package com.wenkrang.faClip.Moudle.FaCommand.FaParam.BukkitParam;

import com.wenkrang.faClip.Moudle.FaCommand.Annotation.DesProvider;
import com.wenkrang.faClip.Moudle.FaCommand.FaCmdInterpreter.FaCmdContext;
import com.wenkrang.faClip.Moudle.FaCommand.FaParam.SimpleParam;
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
        return param.matches("\\w+");
    }

    @Override
    public Object convert(String param) {
        return Bukkit.getPlayer(param) == null ? Bukkit.getOfflinePlayer(param) : Bukkit.getPlayer(param);
    }

    @Override
    public @Nullable String getName(Type type) {
        return "Player";
    }

    @Override
    public @NotNull String[] getDes(FaCmdContext faCmdContext) {
        return Arrays.stream(Bukkit.getOfflinePlayers())
                .map(i -> i.getName() == null ? "" : i.getName())
                .toArray(String[]::new);
    }
}
