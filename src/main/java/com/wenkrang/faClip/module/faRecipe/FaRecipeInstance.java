package com.wenkrang.faClip.module.faRecipe;

import com.wenkrang.faClip.helper.ResourceHelper;
import com.wenkrang.faClip.helper.VersionHelper;
import com.wenkrang.faClip.manager.RecipeManager;
import com.wenkrang.faClip.module.FaModule;
import com.wenkrang.faClip.module.faInterface.FaInterfaceInstance;
import com.wenkrang.faClip.module.faInterface.FaIntfInterpreter;
import com.wenkrang.faClip.module.faItem.FaItemInstance;
import com.wenkrang.faClip.module.faRecipe.event.ReCookE;
import com.wenkrang.faClip.module.faRecipe.event.ReCraftE;
import com.wenkrang.faClip.module.faRecipe.event.ReCrafterCraftE;
import com.wenkrang.faClip.module.faRecipe.event.RePlayerJoinE;
import com.wenkrang.faClip.module.faRecipe.helper.RecipeHelper;
import com.wenkrang.faClip.module.faRecipe.interpreter.FaReInterpreter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FaRecipeInstance implements FaModule {
    private final Map<String,FaRecipe> recipes = new HashMap<>();

    private final Plugin plugin;

    private final FaItemInstance faItemInstance;

    private final FaReInterpreter faReInterpreter;

    private final RecipeManager recipeManager;

    private final ResourceHelper resourceHelper;

    private final FaInterfaceInstance faInterfaceInstance;

    private Status status = Status.STOPPED;

    public void addRecipe(NamespacedKey key, Recipe recipe) {
        recipeManager.register(recipe, key);
    }

    public void addRecipe(NamespacedKey key, FaRecipe recipe) {
        recipeManager.register(recipe.recipe, key);

        recipes.put(recipe.id, recipe);
    }

    public FaRecipe getRecipe(String key) {
        return recipes.get(key);
    }

    public Map<String, FaRecipe> getRecipes() {
        return recipes;
    }

    public FaRecipeInstance(Plugin plugin, FaItemInstance faItemInstance) {
        this.plugin = plugin;
        this.faItemInstance = faItemInstance;
        this.faInterfaceInstance = new FaInterfaceInstance(plugin);
        this.faReInterpreter = new FaReInterpreter(this);
        this.recipeManager = new RecipeManager(plugin);
        this.resourceHelper = new ResourceHelper(plugin.getClass());

        registerEvents();
    }

    public void registerEvents() {
        if (!VersionHelper.isBelow("1.21.4")) {
            Bukkit.getPluginManager().registerEvents(new ReCrafterCraftE(this), plugin);
        }
        Bukkit.getPluginManager().registerEvents(new ReCraftE(this), plugin);
        Bukkit.getPluginManager().registerEvents(new ReCookE(this), plugin);
        Bukkit.getPluginManager().registerEvents(new RePlayerJoinE(this), plugin);
    }

    public FaItemInstance getFaItemInstance() {
        return faItemInstance;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public void load(String path) {
        FaRecipe recipe = faReInterpreter.interpreter(path);

        recipeManager.register(recipe.recipe, RecipeHelper.getNameSpacedKey(recipe));

        recipes.put(recipe.id, recipe);

        recipeManager.refresh();
    }

    public void loadAll() {
        List<String> resourcesByExtension = resourceHelper.getResourcesByExtension(".re");

        for (String path : resourcesByExtension) {
            FaRecipe recipe = faReInterpreter.interpreter(path);

            recipeManager.register(recipe.recipe, RecipeHelper.getNameSpacedKey(recipe));

            recipes.put(recipe.id, recipe);
        }

        recipeManager.refresh();
    }

    public void autoRegister() {
        faInterfaceInstance.enableForAll(plugin);
    }

    public void register(Class<?>... clazz) {
        faInterfaceInstance.enableFor(clazz);
    }

    public FaReInterpreter getFaReInterpreter() {
        return faReInterpreter;
    }

    public RecipeManager getRecipeManager() {
        return recipeManager;
    }

    public ResourceHelper getResourceHelper() {
        return resourceHelper;
    }

    public FaInterfaceInstance getFaInterfaceInstance() {
        return faInterfaceInstance;
    }

    @Override
    public void auto() {
        status = Status.STARTING;

        loadAll();
        autoRegister();

        status = Status.READY;
    }

    @Override
    public void close() {
        recipeManager.unregisterAll();
        recipes.clear();
        status = Status.STOPPED;
    }

    @Override
    public Status status() {
        return status;
    }

    @Override
    public String getName() {
        return "FaRecipe";
    }
}
