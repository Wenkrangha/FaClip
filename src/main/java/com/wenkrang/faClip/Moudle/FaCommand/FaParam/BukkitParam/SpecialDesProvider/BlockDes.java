package com.wenkrang.faClip.Moudle.FaCommand.FaParam.BukkitParam.SpecialDesProvider;

import com.wenkrang.faClip.Moudle.FaCommand.Annotation.DesProvider;
import com.wenkrang.faClip.Moudle.FaCommand.FaCmdInterpreter.FaCmdContext;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class BlockDes implements DesProvider {
    @Override
    public @NotNull String[] getDes(FaCmdContext faCmdContext) {
        return Arrays.stream(Material.values()).filter(Material::isBlock).map(Enum::name).toArray(String[]::new);
    }
}
