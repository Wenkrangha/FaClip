package com.wenkrang.faClip.module.faRecipe.interpreter.handlers.typeHandler;

import com.wenkrang.faClip.module.faData.FaData;
import com.wenkrang.faClip.module.faMessage.exception.FaDataParseException;
import com.wenkrang.faClip.module.faRecipe.FaRecipe;
import com.wenkrang.faClip.module.faRecipe.FaRecipeInstance;
import com.wenkrang.faClip.module.faRecipe.helper.RecipeHelper;
import com.wenkrang.faClip.module.faRecipe.interpreter.handlers.FaReHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingTransformRecipe;

/**
 * 锻造台配方
 */
public class SmithingRecipeHandler implements FaReHandler {
    @Override
    public void handle(FaData faData, FaRecipe faRecipe, FaRecipeInstance faRecipeInstance) {
        String type = faRecipe.type;
        if (type != null && type.equalsIgnoreCase("smith")) {
            // 检查必要字段
            if (!faData.has("template"))
                throw new FaDataParseException(faData, "template", "FaRecipe.Exception.FaReInterpreter.NotFound");
            if (!faData.has("base"))
                throw new FaDataParseException(faData, "base", "FaRecipe.Exception.FaReInterpreter.NotFound");
            if (!faData.has("addition"))
                throw new FaDataParseException(faData, "addition", "FaRecipe.Exception.FaReInterpreter.NotFound");

            ItemStack result = RecipeHelper.getResult(faData, faRecipeInstance);

            RecipeChoice template = RecipeHelper.generateChoice(
                    faRecipeInstance.getFaItemInstance(), faData.get("template"));
            RecipeChoice base = RecipeHelper.generateChoice(
                    faRecipeInstance.getFaItemInstance(), faData.get("base"));
            RecipeChoice addition = RecipeHelper.generateChoice(
                    faRecipeInstance.getFaItemInstance(), faData.get("addition"));

            if (template == null)
                throw new FaDataParseException(faData, "template", "FaRecipe.Exception.FaReInterpreter.InvalidDefine");
            if (base == null)
                throw new FaDataParseException(faData, "base", "FaRecipe.Exception.FaReInterpreter.InvalidDefine");
            if (addition == null)
                throw new FaDataParseException(faData, "addition", "FaRecipe.Exception.FaReInterpreter.InvalidDefine");

            faRecipe.recipe = new SmithingTransformRecipe(
                    RecipeHelper.getNameSpacedKey(faRecipe),
                    result,
                    template,
                    base,
                    addition
            );
        }
    }
}
