package gk.recipeapp.services;

import gk.recipeapp.commands.IngredientCommand;
import gk.recipeapp.converters.IngredientCommandToIngredient;
import gk.recipeapp.converters.IngredientToIngredientCommand;
import gk.recipeapp.domain.Ingredient;
import gk.recipeapp.domain.Recipe;
import gk.recipeapp.repositories.RecipeRepository;
import gk.recipeapp.repositories.UnitOfMeasureRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(final IngredientToIngredientCommand ingredientToIngredientCommand,
                                 final IngredientCommandToIngredient ingredientCommandToIngredient,
                                 final RecipeRepository recipeRepository, final UnitOfMeasureRepository unitOfMeasureRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(final String recipeId, final String ingredientId) {
        final Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        final Recipe recipe = recipeOptional.orElseThrow(() -> new RuntimeException("recipe id not found. Id: " + recipeId));

        final Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredientToIngredientCommand::convert).findFirst();

        if (ingredientCommandOptional.isEmpty()) {
            //todo impl error handling
            log.error("Ingredient id not found: " + ingredientId);
        }

        final IngredientCommand ingredientCommand = ingredientCommandOptional.get();
        ingredientCommand.setRecipeId(recipeId);

        return ingredientCommandOptional.get();
    }

    @Override
    public IngredientCommand saveIngredientCommand(final IngredientCommand command) {
        final Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

        if (recipeOptional.isEmpty()) {

            //todo toss error if not found!
            log.error("Recipe not found for id: " + command.getRecipeId());
            return new IngredientCommand();
        } else {
            final Recipe recipe = recipeOptional.get();

            final Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst();

            if (ingredientOptional.isPresent()) {
                final Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(command.getDescription());
                ingredientFound.setAmount(command.getAmount());
                ingredientFound.setUom(unitOfMeasureRepository
                        .findById(command.getUom().getId())
                        .orElseThrow(() -> new RuntimeException("UOM NOT FOUND"))); //todo address this
            } else {
                //add new Ingredient
                final Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                //  ingredient.setRecipe(recipe);
                recipe.addIngredient(ingredient);
            }

            final Recipe savedRecipe = recipeRepository.save(recipe);

            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients().stream()
                    .filter(recipeIngredients -> recipeIngredients.getId().equals(command.getId()))
                    .findFirst();

            //check by description
            if (savedIngredientOptional.isEmpty()) {
                //not totally safe... But best guess
                savedIngredientOptional = savedRecipe.getIngredients().stream()
                        .filter(recipeIngredients -> recipeIngredients.getDescription().equals(command.getDescription()))
                        .filter(recipeIngredients -> recipeIngredients.getAmount().equals(command.getAmount()))
                        .filter(recipeIngredients -> recipeIngredients.getUom().getId().equals(command.getUom().getId()))
                        .findFirst();
            }

            if (savedIngredientOptional.isEmpty()) {
                throw new RuntimeException("Could not save Ingredient into Recipe id: " + savedRecipe.getId());
            }

            final IngredientCommand ingredientCommandSaved = ingredientToIngredientCommand.convert(savedIngredientOptional.get());
            assert ingredientCommandSaved != null;
            ingredientCommandSaved.setRecipeId(recipe.getId());

            return ingredientCommandSaved;
        }

    }

    @Override
    public void deleteById(final String recipeId, final String idToDelete) {

        log.debug("Deleting ingredient: " + recipeId + ":" + idToDelete);

        final Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (recipeOptional.isPresent()) {
            final Recipe recipe = recipeOptional.get();
            log.debug("found recipe");

            final Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(idToDelete))
                    .findFirst();

            if (ingredientOptional.isPresent()) {
                log.debug("found Ingredient");
                final Ingredient ingredientToDelete = ingredientOptional.get();
                // ingredientToDelete.setRecipe(null);
                recipe.getIngredients().remove(ingredientOptional.get());
                recipeRepository.save(recipe);
            }
        } else {
            log.debug("Recipe Id Not found. Id:" + recipeId);
        }
    }
}