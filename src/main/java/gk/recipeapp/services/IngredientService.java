package gk.recipeapp.services;

import gk.recipeapp.commands.IngredientCommand;
import org.springframework.stereotype.Service;

@Service
public interface IngredientService {
    IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId);

    IngredientCommand saveIngredientCommand(IngredientCommand command);

    void deleteById(String recipeId, String idToDelete);
}
