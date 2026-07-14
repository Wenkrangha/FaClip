package com.wenkrang.faClip.Module.FaData;

import org.bukkit.OfflinePlayer;

public class FaPlayerData extends FaData {
    private OfflinePlayer player;
    private String node;

    public FaPlayerData(OfflinePlayer player,String n) {
        super("data/player/" + player.getUniqueId() + "/" + n);
    }
}
