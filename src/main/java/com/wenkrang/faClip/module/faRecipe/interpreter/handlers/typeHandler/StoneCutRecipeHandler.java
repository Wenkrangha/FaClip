package com.wenkrang.faClip.module.faRecipe.interpreter.handlers.typeHandler;

import com.wenkrang.faClip.module.faData.FaData;
import com.wenkrang.faClip.module.faMessage.exception.FaDataParseException;
import com.wenkrang.faClip.module.faRecipe.FaRecipe;
import com.wenkrang.faClip.module.faRecipe.FaRecipeInstance;
import com.wenkrang.faClip.module.faRecipe.helper.RecipeHelper;
import com.wenkrang.faClip.module.faRecipe.interpreter.handlers.FaReHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.StonecuttingRecipe;

/**
 * 切石机配方
 */
public class StoneCutRecipeHandler implements FaReHandler {
    @Override
    public void handle(FaData faData, FaRecipe faRecipe, FaRecipeInstance faRecipeInstance) {
        String type = faRecipe.type;
        if (type != null && type.equalsIgnoreCase("stonecut")) {
            if (!faData.has("input"))
                throw new FaDataParseException(faData, "input", "FaRecipe.Exception.FaReInterpreter.NotFound");

            ItemStack result = RecipeHelper.getResult(faData, faRecipeInstance);

            RecipeChoice choice = RecipeHelper.generateChoice(
                    faRecipeInstance.getFaItemInstance(), faData.get("input"));

            if (choice == null)
                throw new FaDataParseException(faData, "input", "FaRecipe.Exception.FaReInterpreter.InvalidDefine");

            faRecipe.recipe = new StonecuttingRecipe(
                    RecipeHelper.getNameSpacedKey(faRecipe),
                    result,
                    choice
            );
        }
    }
}
