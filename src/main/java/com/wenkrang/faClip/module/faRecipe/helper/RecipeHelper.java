package com.wenkrang.faClip.module.faRecipe.helper;

import com.wenkrang.faClip.module.faData.FaData;
import com.wenkrang.faClip.module.faItem.FaItemInstance;
import com.wenkrang.faClip.module.faMessage.exception.FaDataParseException;
import com.wenkrang.faClip.module.faRecipe.FaRecipe;
import com.wenkrang.faClip.module.faRecipe.FaRecipeInstance;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class RecipeHelper {
    /**
     * 检查Choice是否需要精确匹配
     * @param list 需要判断的定义串
     * @return 判断结果
     */
    private static boolean isNeedExact(List<String> list) {
        if (list != null && !list.isEmpty()) {
            return !list.stream().allMatch(i -> i.contains("MC."));
        }

        return true;
    }

    public static @Nullable RecipeChoice generateChoice(FaItemInstance faItemInstance, Object o) {
        // 检查是否支持多种物品
        if (o instanceof List<?>) {
            List<String> list = (List<String>) o;

            // 转换定义流为物品
            List<ItemStack> itemStackList = list.stream()
                    .map(faItemInstance::convertDefine)
                    .filter(Objects::nonNull)
                    .toList();

            if (isNeedExact(list)) {
                return new RecipeChoice.ExactChoice(itemStackList);
            }else {
                return new RecipeChoice.MaterialChoice(
                        itemStackList.stream()
                                .map(ItemStack::getType)
                                .toArray(Material[]::new));
            }
        }else if (o instanceof String s){
            // 判断是否为组通配符
            if (s.startsWith("group.")) {
                // 获取组物品
                List<ItemStack> group = faItemInstance.getGroup(s.replace("group.", ""))
                                .stream().map(i -> (ItemStack) i).toList();

                if (group.isEmpty()) return null;

                return new RecipeChoice.ExactChoice(group);
            }

            // 判断为只支持一种物品
            ItemStack itemStack = faItemInstance.convertDefine(s);

            if (itemStack != null) {
                if (isNeedExact(List.of(s))) {
                    return new RecipeChoice.ExactChoice(itemStack);
                }else {
                    return new RecipeChoice.MaterialChoice(itemStack.getType());
                }
            }
        }

        return null;
    }

    public static ItemStack getResult(FaData faData, FaRecipeInstance faRecipeInstance) {
        if (!faData.has("result")) {
            throw new FaDataParseException(faData, "result", "FaRecipe.Exception.FaReInterpreter.NotFound");
        }

        // 获取合成结果
        // 这里先获取结果是因为配方的构造需要结果
        ItemStack result = faRecipeInstance
                .getFaItemInstance()
                .convertDefine(faData.getString("result"));

        if (result == null)
            throw new FaDataParseException(faData, faData.getString("result"),
                    "FaRecipe.Exception.FaReInterpreter.InvalidDefine");

        return result;
    }

    public static NamespacedKey getNameSpacedKey(FaRecipe faRecipe) {
        return new NamespacedKey(faRecipe.faRecipeInstance.getPlugin(), faRecipe.id);
    }

}
