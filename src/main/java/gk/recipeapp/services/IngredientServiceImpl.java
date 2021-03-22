package gk.recipeapp.services;

import gk.recipeapp.commands.IngredientCommand;
import gk.recipeapp.converters.IngredientCommandToIngredient;
import gk.recipeapp.converters.IngredientToIngredientCommand;
import gk.recipeapp.domain.Ingredient;
import gk.recipeapp.domain.Recipe;
import gk.recipeapp.exceptions.NotFoundException;
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

    public IngredientServiceImpl(final IngredientToIngredientCommand ingredientToIngredientCommand,
                                 final IngredientCommandToIngredient ingredientCommandToIngredient,
                                 final RecipeReactiveRepository recipeReactiveRepository, final UnitOfMeasureReactiveRepository unitOfMeasureRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(final String recipeId, final String ingredientId) {
        return recipeReactiveRepository.findById(recipeId)
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
                .single()
                .map(ingredient -> {
                    final IngredientCommand command = ingredientToIngredientCommand.convert(ingredient);
                    command.setRecipeId(recipeId);
                    return command;
                });
    }

    @Override
    public Mono<IngredientCommand> saveIngredientCommand(final IngredientCommand command) {
        final String ingredientCommandId = command.getId();
        final String recipeId = command.getRecipeId();
        final Recipe recipe = recipeReactiveRepository.findById(recipeId).block();

        if (recipe == null) {
            // new ingredient/invalid recipeId
            if (recipeId.isEmpty()) {
                return Mono.just(new IngredientCommand());
            } else {
                throw new NotFoundException(String.format("Recipe id: %s not found", recipeId));
            }
        } else {
            final Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(ingredientCommandId))
                    .findFirst();

            if (ingredientOptional.isPresent()) {
                final Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(command.getDescription());
                ingredientFound.setAmount(command.getAmount());
                ingredientFound.setUom(unitOfMeasureRepository.findById(command.getUom().getId()).block());

                if (ingredientFound.getUom() == null) {
                    throw new NotFoundException(String.format("UOM in ingredient id: %s, recipe id: %s not found", recipeId, ingredientCommandId));
                }
            } else {
                //add new Ingredient
                final Ingredient ingredient = ingredientCommandToIngredient.convert(command);
                recipe.addIngredient(ingredient);
            }

            final Recipe savedRecipe = recipeReactiveRepository.save(recipe).block();

            Optional<Ingredient> savedIngredientOptional = savedRecipe.getIngredients()
                    .stream()
                    .filter(recipeIngredients -> recipeIngredients.getId().equals(ingredientCommandId))
                    .findFirst();

            //check by description
            if (savedIngredientOptional.isEmpty()) {
                // get saved ingredient by all field equality
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
            ingredientCommandSaved.setRecipeId(recipeId);

            return Mono.just(ingredientCommandSaved);
        }

    }

    @Override
    public Mono<Void> deleteById(final String recipeId, final String idToDelete) {

        log.debug("Deleting ingredient: {} : {}", recipeId, idToDelete);

        final Recipe recipe = recipeReactiveRepository.findById(recipeId).block();

        if (recipe != null) {
            log.debug("found recipe");

            final Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(idToDelete))
                    .findFirst();

            if (ingredientOptional.isPresent()) {
                log.debug("found Ingredient");
                recipe.getIngredients().remove(ingredientOptional.get());
                recipeReactiveRepository.save(recipe);
            }
        } else {
            log.debug(String.format("Recipe id: %s not found", recipeId));
        }

        return Mono.empty();
    }
}