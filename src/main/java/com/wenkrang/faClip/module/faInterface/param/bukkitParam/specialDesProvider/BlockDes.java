package com.wenkrang.faClip.module.faInterface.param.bukkitParam.specialDesProvider;

import com.wenkrang.faClip.module.faCommand.annotation.DesProvider;
import com.wenkrang.faClip.module.faCommand.interpreter.FaCmdContext;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class BlockDes implements DesProvider {
    @Override
    public @NotNull String[] getDes(FaCmdContext faCmdContext) {
        return Arrays.stream(Material.values()).filter(Material::isBlock).map(Enum::name).toArray(String[]::new);
    }
}
