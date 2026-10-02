package com.wenkrang.faClip.module.faRecipe.interpreter;

import com.wenkrang.faClip.helper.ResourceHelper;
import com.wenkrang.faClip.module.faData.FaData;
import com.wenkrang.faClip.module.faRecipe.FaRecipe;
import com.wenkrang.faClip.module.faRecipe.FaRecipeInstance;
import com.wenkrang.faClip.module.faRecipe.interpreter.handlers.*;
import com.wenkrang.faClip.module.faRecipe.interpreter.handlers.basic.ReIDHandler;
import com.wenkrang.faClip.module.faRecipe.interpreter.handlers.basic.ReInstanceHandler;
import com.wenkrang.faClip.module.faRecipe.interpreter.handlers.basic.ReTypeHandler;
import com.wenkrang.faClip.module.faRecipe.interpreter.handlers.extra.ReTemplateHandler;
import com.wenkrang.faClip.module.faRecipe.interpreter.handlers.typeHandler.*;

import java.io.InputStream;
import java.util.ArrayList;

public class FaReInterpreter {
    private final FaRecipeInstance faRecipeInstance;

    private final ResourceHelper resourceHelper;

    private final ArrayList<FaReHandler> pipe = new ArrayList<>();

    public FaReInterpreter(FaRecipeInstance faRecipeInstance) {
        this.faRecipeInstance = faRecipeInstance;
        this.resourceHelper = new ResourceHelper(faRecipeInstance.getPlugin().getClass());

        pipe.add(new ReTemplateHandler());

        // 基础 handler
        pipe.add(new ReInstanceHandler());
        pipe.add(new ReIDHandler());
        pipe.add(new ReTypeHandler());

        // 类型 handler
        pipe.add(new ShapedRecipeHandler());
        pipe.add(new ShapeLessRecipeHandler());
        pipe.add(new FurnaceRecipeHandler());
        pipe.add(new BlastingRecipeHandler());
        pipe.add(new SmokingRecipeHandler());
        pipe.add(new CampfireRecipeHandler());
        pipe.add(new StoneCutRecipeHandler());
        pipe.add(new SmithingRecipeHandler());
    }

    public FaRecipeInstance getFaRecipeInstance() {
        return faRecipeInstance;
    }

    public FaRecipe interpreter(String path) {
        InputStream resource = faRecipeInstance.getPlugin().getResource(path);

        FaData faData = new FaData(resource);

        FaRecipe faRecipe = new FaRecipe();

        // 管道处理
        pipe.forEach(i -> i.handle(faData, faRecipe, faRecipeInstance));

        return faRecipe;
    }

}
