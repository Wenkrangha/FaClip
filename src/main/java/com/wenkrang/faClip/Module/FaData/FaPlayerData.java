package com.wenkrang.faClip.Module.FaData;

import org.bukkit.OfflinePlayer;

public class FaPlayerData extends FaData {
    public OfflinePlayer player;
    public String node;

    public FaPlayerData(OfflinePlayer player,String n) {
        super(player.getUniqueId() + "/" + n);
    }
}
