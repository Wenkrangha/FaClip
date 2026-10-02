package com.wenkrang.faClip.module.faRecipe.event;

import com.wenkrang.faClip.module.faInterface.FaIntf;
import com.wenkrang.faClip.module.faInterface.FaIntfContext;
import com.wenkrang.faClip.module.faMessage.Fm;
import com.wenkrang.faClip.module.faMessage.exception.FaException;
import com.wenkrang.faClip.module.faMessage.exception.FaRecipeException;
import com.wenkrang.faClip.module.faRecipe.FaRecipe;
import com.wenkrang.faClip.module.faRecipe.FaRecipeInstance;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Recipe;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ReCraftE implements Listener {
    private final FaRecipeInstance faRecipeInstance;

    public ReCraftE(FaRecipeInstance faRecipeInstance) {
        this.faRecipeInstance = faRecipeInstance;
    }

    public static void before(FaRecipeInstance faRecipeInstance, Recipe recipe, Event event) {
        // 匹配配方
        if (faRecipeInstance.getRecipeManager().getRecipes().containsValue(recipe)) {
            FaRecipe faRecipe = faRecipeInstance
                    .getRecipes()
                    .values()
                    .stream()
                    .filter(i -> i.recipe.equals(recipe))
                    .findFirst()
                    .orElse(null);

            if (faRecipe != null && faRecipe.events.containsKey("before")) {
                String node = faRecipe.events.get("before");

                List<FaIntf> intf = faRecipeInstance.getFaInterfaceInstance().getIntf(node);

                if (intf.size() != 1) {
                    Fm.reportWarning("FaRecipe.Warning.Event.IntfNotFound", node);
                    return;
                }

                // 构造参数传递
                FaIntfContext faIntfContext = new FaIntfContext();

                faIntfContext.set("event", event);
                faIntfContext.set("recipeInstance", faRecipeInstance);
                faIntfContext.set("recipe", faRecipe);

                try {
                    intf.getFirst().invoke(null, faIntfContext, new String[0]);
                } catch (InvocationTargetException e) {
                    // 解包业务方法抛出的异常：FaException 原样上抛，其余包装为 FaRecipeException
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    if (cause instanceof FaException faException) throw faException;
                    throw new FaRecipeException("FaRecipe.Error.Event.BeforeInvokeFailed", cause, node);
                } catch (IllegalAccessException e) {
                    throw new FaRecipeException("FaRecipe.Error.Event.BeforeInvokeFailed", e, node);
                }
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent craftItemEvent) {
        before(faRecipeInstance, craftItemEvent.getRecipe(), craftItemEvent);
    }
}
