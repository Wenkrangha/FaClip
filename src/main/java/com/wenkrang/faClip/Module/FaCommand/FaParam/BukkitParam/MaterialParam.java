package com.wenkrang.faClip.Module.FaCommand.FaParam.BukkitParam;

import com.wenkrang.faClip.Module.FaCommand.Annotation.DesProvider;
import com.wenkrang.faClip.Module.FaCommand.FaCmdInterpreter.FaCmdContext;
import com.wenkrang.faClip.Module.FaCommand.FaParam.SimpleParam;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;

/**
 * 约定：该类转换返回的是 Material，因为Block是实例
 */
public class MaterialParam implements SimpleParam, DesProvider {
    @Override
    public @NotNull String[] getDes(FaCmdContext faCmdContext) {
        return Arrays.stream(Material.values()).map(Enum::name).toArray(String[]::new);
    }

    @Override
    public Set<Type> getType() {
        return Set.of(Material.class);
    }

    @Override
    public boolean check(String param) {
        if (param != null) {
            Material material = Material.getMaterial(param);
            return material != null;
        }
        return false;
    }

    @Override
    public Object convert(String param) {
        if (param != null) {
            return Material.getMaterial(param);
        }
        return null;
    }

    @Override
    public @Nullable String getName(Type type) {
        return getType().equals(type) ? "Material" : null;
    }
}
