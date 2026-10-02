package com.wenkrang.faClip.module.faRecipe.interpreter.handlers.basic;

import com.wenkrang.faClip.module.faData.FaData;
import com.wenkrang.faClip.module.faMessage.exception.FaDataParseException;
import com.wenkrang.faClip.module.faRecipe.FaRecipe;
import com.wenkrang.faClip.module.faRecipe.FaRecipeInstance;
import com.wenkrang.faClip.module.faRecipe.interpreter.handlers.FaReHandler;

public class ReIDHandler implements FaReHandler {

    @Override
    public void handle(FaData faData, FaRecipe faRecipe, FaRecipeInstance faRecipeInstance) {
        if (faData.has("id")) {
            faRecipe.id = faData.getString("id");
        }else if (!faData.has("template")){
            throw new FaDataParseException(faData, "id", "FaRecipe.Exception.FaReInterpreter.NotFound");
        }
    }
}
