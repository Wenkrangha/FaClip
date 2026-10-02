package com.wenkrang.faClip.module.faRecipe.interpreter.handlers;

import com.wenkrang.faClip.module.faData.FaData;
import com.wenkrang.faClip.module.faRecipe.FaRecipe;
import com.wenkrang.faClip.module.faRecipe.FaRecipeInstance;

public interface FaReHandler {
    void handle(FaData faData, FaRecipe faRecipe, FaRecipeInstance faRecipeInstance);
}
