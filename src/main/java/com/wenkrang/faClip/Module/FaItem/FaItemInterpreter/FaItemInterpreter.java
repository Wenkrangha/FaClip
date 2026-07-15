package com.wenkrang.faClip.Module.FaItem.FaItemInterpreter;

import com.wenkrang.faClip.Module.FaItem.FaItem;
import com.wenkrang.faClip.Module.FaItem.FaItemInterpreter.handler.*;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class FaItemInterpreter {
    private ArrayList<FaItemHandler> handlers = new ArrayList<>();

    public Plugin plugin;

    public FaItemInterpreter(Plugin p) {
        handlers.add(new TypeHandler());
        handlers.add(new NameHandler());
        handlers.add(new IdHandler());
        handlers.add(new LoreHandler());
        handlers.add(new TagHandler());

        plugin = p;
    }

    public FaItem interpreter(YamlConfiguration yamlConfiguration) {
        FaItem faItem = new FaItem(plugin, Material.AIR);

        for (FaItemHandler faItemHandler : handlers) {
            faItemHandler.handle(faItem, yamlConfiguration, this);
        }

        return faItem;
    }
}
