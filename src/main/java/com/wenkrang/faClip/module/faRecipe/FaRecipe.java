package com.wenkrang.faClip.module.faRecipe;

import org.bukkit.inventory.Recipe;

import java.util.HashMap;
import java.util.Map;

public class FaRecipe {
    public String id = null;
    public Recipe recipe = null;
    public String type = null;
    public FaRecipeInstance faRecipeInstance;
    public String template = null;

    public Map<String,String> events = new HashMap<>();
}
