package gk.recipeapp.services;

import gk.recipeapp.commands.RecipeCommand;
import gk.recipeapp.converters.RecipeCommandToRecipe;
import gk.recipeapp.converters.RecipeToRecipeCommand;
import gk.recipeapp.domain.Recipe;
import gk.recipeapp.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeReactiveRepository recipeReactiveRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(final RecipeReactiveRepository recipeReactiveRepository, final RecipeCommandToRecipe recipeCommandToRecipe, final RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Flux<Recipe> getRecipes() {
        log.debug("Executing getRecipe");
        return recipeReactiveRepository.findAll();
    }

    @Override
    public Mono<Recipe> findById(final String id) {
        log.debug("Executing findByID({})", id);
        return recipeReactiveRepository.findById(id);
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(final RecipeCommand command) {
        return recipeReactiveRepository.save(recipeCommandToRecipe.convert(command))
                .map(recipeToRecipeCommand::convert);
    }

    @Override
    public Mono<RecipeCommand> findCommandById(final String id) {
        return recipeReactiveRepository.findById(id)
                .map(recipe -> {
                    final RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);
                    recipeCommand.getIngredients()
                            .forEach(ingredientCommand -> ingredientCommand.setRecipeId(id));
                    return recipeCommand;
                });
    }

    @Override
    public Mono<Void> deleteById(final String id) {
        recipeReactiveRepository.deleteById(id).block();
        return Mono.empty();
    }
}
