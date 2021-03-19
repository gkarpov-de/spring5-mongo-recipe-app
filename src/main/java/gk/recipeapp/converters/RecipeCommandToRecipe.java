package gk.recipeapp.converters;

import gk.recipeapp.commands.RecipeCommand;
import gk.recipeapp.domain.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RecipeCommandToRecipe implements Converter<RecipeCommand, Recipe> {

    private final CategoryCommandToCategory categoryConverter;
    private final IngredientCommandToIngredient ingredientConverter;
    private final NotesCommandToNotes notesConverter;

    public RecipeCommandToRecipe(final CategoryCommandToCategory categoryConverter, final IngredientCommandToIngredient ingredientConverter,
                                 final NotesCommandToNotes notesConverter) {
        this.categoryConverter = categoryConverter;
        this.ingredientConverter = ingredientConverter;
        this.notesConverter = notesConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public Recipe convert(@Nullable final RecipeCommand source) {
        if (source == null) {
            return null;
        }

        final Recipe recipe = new Recipe();
        recipe.setId(source.getId());
        recipe.setCookTime(source.getCookTime());
        recipe.setPrepTime(source.getPrepTime());
        recipe.setDescription(source.getDescription());
        recipe.setDifficulty(source.getDifficulty());
        recipe.setDirections(source.getDirections());
        recipe.setServings(source.getServings());
        recipe.setSource(source.getSource());
        recipe.setUrl(source.getUrl());
        recipe.setNotes(notesConverter.convert(source.getNotes()));

        final var categoryCommandSet = source.getCategories();
        if (categoryCommandSet != null && categoryCommandSet.size() > 0) {
            categoryCommandSet
                    .forEach(category -> recipe.getCategories().add(categoryConverter.convert(category)));
        }

        final var ingredientsCommandSet = source.getIngredients();
        if (ingredientsCommandSet != null && ingredientsCommandSet.size() > 0) {
            ingredientsCommandSet
                    .forEach(ingredient -> recipe.getIngredients().add(ingredientConverter.convert(ingredient)));
        }

        return recipe;
    }
}
