package com.wenkrang.faClip.module.faRecipe.event;

import com.wenkrang.faClip.module.faRecipe.FaRecipeInstance;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class RePlayerJoinE implements Listener {
    private final FaRecipeInstance faRecipeInstance;

    public RePlayerJoinE(FaRecipeInstance faRecipeInstance) {
        this.faRecipeInstance = faRecipeInstance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().discoverRecipes(faRecipeInstance.getRecipeManager().getNamespacedKeys().values());
    }
}
