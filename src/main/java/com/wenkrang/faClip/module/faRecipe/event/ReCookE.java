package com.wenkrang.faClip.module.faRecipe.event;

import com.wenkrang.faClip.module.faRecipe.FaRecipeInstance;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.inventory.Recipe;

import java.util.List;

public class ReCookE implements Listener {
    private final FaRecipeInstance faRecipeInstance;

    public ReCookE(FaRecipeInstance faRecipeInstance) {
        this.faRecipeInstance = faRecipeInstance;
    }


    @EventHandler
    public void onCook(BlockCookEvent blockCookEvent) {
        List<Recipe> recipesFor = Bukkit.getRecipesFor(blockCookEvent.getResult());
        for (Recipe recipe : recipesFor) {
            ReCraftE.before(faRecipeInstance, recipe, blockCookEvent);
        }
    }
}
