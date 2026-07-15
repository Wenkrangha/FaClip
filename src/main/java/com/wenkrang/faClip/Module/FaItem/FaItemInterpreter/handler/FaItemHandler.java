package com.wenkrang.faClip.Module.FaItem.FaItemInterpreter.handler;

import com.wenkrang.faClip.Module.FaItem.FaItem;
import org.bukkit.configuration.file.YamlConfiguration;

public interface FaItemHandler {
    String getNode();
    void handle(FaItem faItem, YamlConfiguration yamlConfiguration);
}
