package gk.recipeapp.services;

import gk.recipeapp.commands.IngredientCommand;

public interface IngredientService {
    IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String id);

    IngredientCommand saveIngredientCommand(IngredientCommand ingredientCommand);

    void deleteById(String recipeId, String ingredientId);
}
