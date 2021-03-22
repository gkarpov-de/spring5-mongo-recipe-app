package gk.recipeapp.services;

import gk.recipeapp.commands.RecipeCommand;
import gk.recipeapp.domain.Recipe;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public interface RecipeService {
    Flux<Recipe> getRecipes();

    Mono<Recipe> findById(final String id);

    Mono<RecipeCommand> saveRecipeCommand(final RecipeCommand command);

    Mono<Void> deleteById(final String id);

    Mono<RecipeCommand> findCommandById(final String id);
}
