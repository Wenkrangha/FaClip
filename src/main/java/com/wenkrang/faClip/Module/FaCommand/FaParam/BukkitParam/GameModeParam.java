package com.wenkrang.faClip.Module.FaCommand.FaParam.BukkitParam;

import com.wenkrang.faClip.Module.FaCommand.FaParam.SimpleParam;
import org.bukkit.GameMode;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Set;

public class GameModeParam implements SimpleParam {
    @Override
    public Set<Type> getType() {
        return Set.of(GameMode.class);
    }

    @Override
    public boolean check(String param) {
        try {
            GameMode.valueOf(param);
            return true;
        }catch (IllegalArgumentException e){
            return false;
        }
    }

    @Override
    public Object convert(String param) {
        return GameMode.valueOf(param);
    }

    @Override
    public @Nullable String getName(Type type) {
        return getType().contains(type) ? "GameMode" : null;
    }
}
