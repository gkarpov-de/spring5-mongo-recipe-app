package gk.recipeapp.services;

import gk.recipeapp.commands.IngredientCommand;
import gk.recipeapp.converters.IngredientCommandToIngredient;
import gk.recipeapp.converters.IngredientToIngredientCommand;
import gk.recipeapp.domain.Ingredient;
import gk.recipeapp.domain.Recipe;
import gk.recipeapp.exceptions.NotFoundException;
import gk.recipeapp.repositories.RecipeRepository;
import gk.recipeapp.repositories.UnitOfMeasureRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
public class IngredientServiceImpl implements IngredientService {
    private final RecipeRepository recipeRepository;
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(final IngredientToIngredientCommand ingredientToIngredientCommand,
                                 final IngredientCommandToIngredient ingredientCommandToIngredient,
                                 final RecipeRepository recipeRepository,
                                 final UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(final String recipeId, final String id) {
        final Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isEmpty()) {
            throw new RuntimeException("Recipe with id: " + recipeId + "not found");
        }

        final Optional<IngredientCommand> ingredientCommandOptional = recipeOptional.get()
                .getIngredients().stream().filter(ingredient -> ingredient.getId().equals(id))
                .map(ingredientToIngredientCommand::convert)
                .findFirst();

        if (ingredientCommandOptional.isEmpty()) {
            throw new RuntimeException("Ingredient with id: " + id + "not found");
        }

        return ingredientCommandOptional.get();
    }

    @Override
    @Transactional
    public IngredientCommand saveIngredientCommand(final IngredientCommand ingredientCommand) {
        final String recipeId = ingredientCommand.getRecipeId();
        // FIXME
        // could be null if new Ingredient
        String ingredientCommandId = ingredientCommand.getId();
        final Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (recipeOptional.isEmpty()) {
            log.debug("Recipe with id: {} not found.", recipeId);
            return new IngredientCommand();
        }
        final Recipe recipe = recipeOptional.get();
        final Set<String> setOfIngredientsIDsBeforeSaveRecipe = getSetOfIngredientsIDs(recipe);

        final String finalIngredientCommandId1 = ingredientCommandId;
        final Optional<Ingredient> optionalIngredient = recipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(finalIngredientCommandId1))
                .findFirst();

        if (optionalIngredient.isPresent()) {
            final Ingredient ingredientFound = optionalIngredient.get();
            ingredientFound.setDescription(ingredientCommand.getDescription());
            ingredientFound.setAmount(ingredientCommand.getAmount());
            ingredientFound.setUom(unitOfMeasureRepository.findById(ingredientCommand.getUom().getId())
                    .orElseThrow(() -> new RuntimeException("<DB Error> Uom not found."))); // TODO who/how will catch it?!
        } else {
            final Ingredient ingredient = ingredientCommandToIngredient.convert(ingredientCommand);
            if (ingredient == null) {
                throw new RuntimeException("IngredientCommand to Ingredient conversion failed. Null returned");
            }
            ingredient.setRecipe(recipe);
            recipe.addIngredient(ingredient);
        }


        final Recipe savedRecipe = recipeRepository.save(recipe);
        if (ingredientCommandId == null) {
            final var setOfIngredientsIDsAfterSaveRecipe = getSetOfIngredientsIDs(recipe);
            setOfIngredientsIDsAfterSaveRecipe.removeAll(setOfIngredientsIDsBeforeSaveRecipe);
            ingredientCommandId = setOfIngredientsIDsAfterSaveRecipe.stream().findFirst().orElseThrow(NotFoundException::new);
        }

        final String finalIngredientCommandId = ingredientCommandId;
        final Ingredient savedIngredient = savedRecipe.getIngredients()
                .stream()
                .filter(recipeIngredients -> recipeIngredients.getId().equals(finalIngredientCommandId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("<Server error> Could not save ingredient."));

        return ingredientToIngredientCommand.convert(savedIngredient);
    }

    private Set<String> getSetOfIngredientsIDs(final Recipe recipe) {
        return recipe.getIngredients()
                .stream()
                .map(Ingredient::getId)
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteById(final String recipeId, final String ingredientId) {
        log.debug("Deleting ingredient id: {} from recipe id: {}", ingredientId, recipeId);
        final Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (recipeOptional.isEmpty()) {
            log.error("<deleteById> Recipe id: {} not found.", recipeId);
            return;
        }

        log.debug("recipe id: {} found", recipeId);
        final Recipe recipe = recipeOptional.get();

        final Optional<Ingredient> optionalIngredient = recipe.getIngredients()
                .stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .findFirst();

        if (optionalIngredient.isEmpty()) {
            log.error("<deleteById> Ingredient id: {} not found", ingredientId);
            return;
        }

        log.debug("Ingredient id: {} found", ingredientId);
        final Ingredient ingredientToDelete = optionalIngredient.get();

        ingredientToDelete.setRecipe(null);
        recipe.getIngredients().remove(ingredientToDelete);
        recipeRepository.save(recipe);
    }


}
