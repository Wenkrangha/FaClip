package com.wenkrang.faClip.Moudle.FaCommand.FaParam.BukkitParam;

import com.wenkrang.faClip.Moudle.FaCommand.Annotation.DesProvider;
import com.wenkrang.faClip.Moudle.FaCommand.FaCmdInterpreter.FaCmdContext;
import com.wenkrang.faClip.Moudle.FaCommand.FaParam.SimpleParam;
import org.bukkit.Effect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;

public class EffectParam implements SimpleParam, DesProvider {
    @Override
    public @NotNull String[] getDes(FaCmdContext faCmdContext) {
        return Arrays.stream(Effect.values()).map(Enum::name).toArray(String[]::new);
    }

    @Override
    public Set<Type> getType() {
        return Set.of(Effect.class);
    }

    @Override
    public boolean check(String param) {
       try {
           Effect.valueOf(param);
           return true;
       } catch (IllegalArgumentException e) {
           return false;
       }
    }

    @Override
    public Object convert(String param) {
        try {
            return Effect.valueOf(param);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public @Nullable String getName(Type type) {
        return "Effect";
    }
}
