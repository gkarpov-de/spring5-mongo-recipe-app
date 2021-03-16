package gk.recipeapp.converters;

import gk.recipeapp.commands.RecipeCommand;
import gk.recipeapp.domain.Category;
import gk.recipeapp.domain.Ingredient;
import gk.recipeapp.domain.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RecipeToRecipeCommand implements Converter<Recipe, RecipeCommand> {

    private final CategoryToCategoryCommand categoryConverter;
    private final IngredientToIngredientCommand ingredientConverter;
    private final NotesToNotesCommand notesConverter;

    public RecipeToRecipeCommand(final CategoryToCategoryCommand categoryConverter, final IngredientToIngredientCommand ingredientConverter, final NotesToNotesCommand notesConverter) {
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
        this.notesConverter = notesConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public RecipeCommand convert(@Nullable final Recipe recipe) {
        if (recipe == null) {
            return null;
        }

        final RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(recipe.getId());
        recipeCommand.setDescription(recipe.getDescription());
        recipeCommand.setPrepTime(recipe.getPrepTime());
        recipeCommand.setCookTime(recipe.getCookTime());
        recipeCommand.setDifficulty(recipe.getDifficulty());
        recipeCommand.setDirections(recipe.getDirections());
        recipeCommand.setServings(recipe.getServings());
        recipeCommand.setSource(recipe.getSource());
        recipeCommand.setUrl(recipe.getUrl());
        recipeCommand.setNotes(notesConverter.convert(recipe.getNotes()));
        recipeCommand.setImage(recipe.getImage());

        final Set<Category> categorySet = recipe.getCategories();
        if (categorySet != null && categorySet.size() > 0) {
            categorySet.forEach(category -> recipeCommand.getCategories().add(categoryConverter.convert(category)));
        }

        final Set<Ingredient> ingredientSet = recipe.getIngredients();
        if (ingredientSet != null && ingredientSet.size() > 0) {
            ingredientSet.forEach(ingredient -> recipeCommand.getIngredients().add(ingredientConverter.convert(ingredient)));
        }

        return recipeCommand;
    }
}
