package gk.recipeapp.services;

import gk.recipeapp.commands.IngredientCommand;
import gk.recipeapp.converters.IngredientCommandToIngredient;
import gk.recipeapp.converters.IngredientToIngredientCommand;
import gk.recipeapp.domain.Ingredient;
import gk.recipeapp.domain.Recipe;
import gk.recipeapp.repositories.RecipeRepository;
import gk.recipeapp.repositories.reactive.RecipeReactiveRepository;
import gk.recipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Log4j2
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeReactiveRepository recipeReactiveRepository;
    private final UnitOfMeasureReactiveRepository unitOfMeasureRepository;
    private final RecipeRepository recipeRepository;

    public IngredientServiceImpl(final IngredientToIngredientCommand ingredientToIngredientCommand,
                                 final IngredientCommandToIngredient ingredientCommandToIngredient,
                                 final RecipeReactiveRepository recipeReactiveRepository, final UnitOfMeasureReactiveRepository unitOfMeasureRepository, RecipeRepository recipeRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(final String recipeId, final String ingredientId) {
        return recipeReactiveRepository.findById(recipeId)
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
                .single()
                .map(ingredient -> {
                    IngredientCommand command = ingredientToIngredientCommand.convert(ingredient);
                    command.setRecipeId(recipeId);
                    return command;
                });
    }

    @Override
    public Mono<IngredientCommand> saveIngredientCommand(final IngredientCommand command) {
        final Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

        if (recipeOptional.isEmpty()) {
            // todo toss error if not found!
            log.error("Recipe not found for id: " + command.getRecipeId());
            return Mono.just(new IngredientCommand());
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
                ingredientFound.setUom(unitOfMeasureRepository.findById(command.getUom().getId()).block());
            } else {
                //add new Ingredient
                final Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                //  ingredient.setRecipe(recipe);
                recipe.addIngredient(ingredient);
            }

            final Recipe savedRecipe = recipeReactiveRepository.save(recipe).block();

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

            return Mono.just(ingredientCommandSaved);
        }

    }

    @Override
    public Mono<Void> deleteById(final String recipeId, final String idToDelete) {

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
                recipe.getIngredients().remove(ingredientOptional.get());
                recipeRepository.save(recipe);
            }
        } else {
            log.debug("Recipe Id Not found. Id:" + recipeId);
        }

        return Mono.empty();
    }
}