package com.wenkrang.faClip.module.faRecipe.interpreter.handlers.typeHandler;

import com.wenkrang.faClip.module.faData.FaData;
import com.wenkrang.faClip.module.faMessage.exception.FaDataParseException;
import com.wenkrang.faClip.module.faRecipe.FaRecipe;
import com.wenkrang.faClip.module.faRecipe.FaRecipeInstance;
import com.wenkrang.faClip.module.faRecipe.helper.RecipeHelper;
import com.wenkrang.faClip.module.faRecipe.interpreter.handlers.FaReHandler;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;

public class ShapedRecipeHandler implements FaReHandler {
    @Override
    public void handle(FaData faData, FaRecipe faRecipe, FaRecipeInstance faRecipeInstance) {
        String type = faRecipe.type;
        if (type != null) {
            if (type.equalsIgnoreCase("shaped")) {
                // 检查元素
                if (!faData.has("design"))
                    throw new FaDataParseException(faData, "design",
                            "FaRecipe.Exception.FaReInterpreter.ShapedRecipe.NoDesign");

                ItemStack result = RecipeHelper.getResult(faData, faRecipeInstance);

                // 初始化配方
                ShapedRecipe shapedRecipe = new ShapedRecipe(RecipeHelper.getNameSpacedKey(faRecipe), result);

                // 获取设计
                List<String> design = faData.getStringList("design");

                if (design.size() != 3) {
                    throw new FaDataParseException(faData, String.valueOf(design.size()),
                            "FaRecipe.Exception.FaReInterpreter.ShapedRecipe.DesignListSizeMustBeThree");
                }

                // 设置形状
                shapedRecipe.shape(design.toArray(new String[0]));

                // 获取定义
                if (faData.has("define")) {
                    ConfigurationSection defineConfig = faData.getSection("define");

                    if (defineConfig != null) {
                        for (String key : defineConfig.getKeys(false)) {
                            RecipeChoice choice = RecipeHelper
                                    .generateChoice(faRecipeInstance.getFaItemInstance(), key);

                            if (choice == null)
                                throw new FaDataParseException(faData, key,
                                        "FaRecipe.Exception.FaReInterpreter.InvalidDefine");

                            // 构造RecipeChoice
                            shapedRecipe.setIngredient(
                                    key.charAt(0),
                                    choice
                            );
                        }
                    }
                }

                faRecipe.recipe = shapedRecipe;
            }
        }
    }
}
