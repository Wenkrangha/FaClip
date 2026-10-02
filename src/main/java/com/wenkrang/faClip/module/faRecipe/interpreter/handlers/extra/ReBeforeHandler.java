package com.wenkrang.faClip.module.faRecipe.interpreter.handlers.extra;

import com.wenkrang.faClip.module.faData.FaData;
import com.wenkrang.faClip.module.faRecipe.FaRecipe;
import com.wenkrang.faClip.module.faRecipe.FaRecipeInstance;
import com.wenkrang.faClip.module.faRecipe.interpreter.handlers.FaReHandler;

public class ReBeforeHandler implements FaReHandler {
    @Override
    public void handle(FaData faData, FaRecipe faRecipe, FaRecipeInstance faRecipeInstance) {
        if (faData.has("before")) {
            faRecipe.events.put("before",faData.getString("before"));
        }
    }
}
