package gk.recipeapp.converters;

import gk.recipeapp.commands.IngredientCommand;
import gk.recipeapp.domain.Ingredient;
import gk.recipeapp.domain.Recipe;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientCommandToIngredient implements Converter<IngredientCommand, Ingredient> {
    private final UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure;

    public IngredientCommandToIngredient(final UnitOfMeasureCommandToUnitOfMeasure unitOfMeasureCommandToUnitOfMeasure) {
        this.unitOfMeasureCommandToUnitOfMeasure = unitOfMeasureCommandToUnitOfMeasure;
    }

    @Nullable
    @Override
    public Ingredient convert(@Nullable final IngredientCommand ingredientCommand) {
        if (ingredientCommand == null) {
            return null;
        }

        final Ingredient ingredient = new Ingredient();

        final String ingredientCommandId = ingredientCommand.getId();
        if (ingredientCommandId != null && !ingredientCommandId.isEmpty()) {
            ingredient.setId(ingredientCommandId);
        }

        if (ingredientCommand.getRecipeId() != null) {
            final Recipe recipe = new Recipe();
            recipe.setId(ingredientCommand.getRecipeId());
            recipe.addIngredient(ingredient);
        }

        ingredient.setDescription(ingredientCommand.getDescription());
        ingredient.setUom(unitOfMeasureCommandToUnitOfMeasure.convert(ingredientCommand.getUom()));
        ingredient.setAmount(ingredientCommand.getAmount());
        return ingredient;
    }
}
