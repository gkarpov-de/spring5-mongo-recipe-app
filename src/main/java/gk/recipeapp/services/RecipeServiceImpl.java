package gk.recipeapp.services;

import gk.recipeapp.commands.RecipeCommand;
import gk.recipeapp.converters.RecipeCommandToRecipe;
import gk.recipeapp.converters.RecipeToRecipeCommand;
import gk.recipeapp.domain.Recipe;
import gk.recipeapp.exceptions.NotFoundException;
import gk.recipeapp.repositories.RecipeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Log4j2
@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(final RecipeRepository recipeRepository, final RecipeCommandToRecipe recipeCommandToRecipe, final RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeRepository = recipeRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Set<Recipe> getRecipes() {
        log.debug("Executing getRecipe");
        final Set<Recipe> recipeSet = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
        return recipeSet;
    }

    @Override
    public Recipe findById(final String id) {
        log.debug("Executing findByID({})", id);
        final Optional<Recipe> recipeOptional = recipeRepository.findById(id);
        if (recipeOptional.isEmpty()) {
            throw new NotFoundException("Recipe <id:" + id + "> not found.");
        }
        return recipeOptional.get();
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(final RecipeCommand command) {
        final Recipe detachedRecipe = recipeCommandToRecipe.convert(command);
        if (detachedRecipe == null) {
            throw new RuntimeException("RecipeCommand to Recipe conversion failed. Null returned"
            );
        }
        final Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        log.debug("Saved Recipe id: {}", savedRecipe.getId());
        return recipeToRecipeCommand.convert(savedRecipe);
    }

    @Override
    @Transactional
    public RecipeCommand findCommandByID(final String id) {
        return recipeToRecipeCommand.convert(findById(id));
    }

    @Override
    public void deleteById(final String id) {
        recipeRepository.deleteById(id);
    }

    @Transactional
    @Override
    public RecipeCommand findCommandById(final String id) {
        return recipeToRecipeCommand.convert(findById(id));
    }
}
