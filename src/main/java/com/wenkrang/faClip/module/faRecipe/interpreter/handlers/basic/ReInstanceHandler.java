package com.wenkrang.faClip.module.faRecipe.interpreter.handlers.basic;

import com.wenkrang.faClip.module.faData.FaData;
import com.wenkrang.faClip.module.faRecipe.FaRecipe;
import com.wenkrang.faClip.module.faRecipe.FaRecipeInstance;
import com.wenkrang.faClip.module.faRecipe.interpreter.handlers.FaReHandler;

public class ReInstanceHandler implements FaReHandler {
    @Override
    public void handle(FaData faData, FaRecipe faRecipe, FaRecipeInstance faRecipeInstance) {
        faRecipe.faRecipeInstance = faRecipeInstance;
    }
}
