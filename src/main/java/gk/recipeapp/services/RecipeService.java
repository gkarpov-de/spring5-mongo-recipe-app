package gk.recipeapp.services;

import gk.recipeapp.commands.RecipeCommand;
import gk.recipeapp.domain.Recipe;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public interface RecipeService {
    Set<Recipe> getRecipes();

    Recipe findById(final String id);

    RecipeCommand saveRecipeCommand(final RecipeCommand command);

    RecipeCommand findCommandByID(final String id);

    void deleteById(final String id);

    RecipeCommand findCommandById(final String id);
}
