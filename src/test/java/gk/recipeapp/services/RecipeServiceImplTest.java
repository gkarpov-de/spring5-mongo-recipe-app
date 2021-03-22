package gk.recipeapp.services;

import gk.recipeapp.commands.RecipeCommand;
import gk.recipeapp.converters.RecipeCommandToRecipe;
import gk.recipeapp.converters.RecipeToRecipeCommand;
import gk.recipeapp.domain.Recipe;
import gk.recipeapp.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class RecipeServiceImplTest {
    private static final String ID_VALUE = "1";

    RecipeService recipeService;

    @Mock
    RecipeReactiveRepository recipeReactiveRepository;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recipeService = new RecipeServiceImpl(recipeReactiveRepository, recipeCommandToRecipe, recipeToRecipeCommand);
    }

    @Test
    @DisplayName("getRecipeByID test")
    void getRecipeByIdTest() {
        final Recipe recipe = new Recipe();
        recipe.setId(ID_VALUE);

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        final Recipe recipeReturned = recipeService.findById(ID_VALUE).block();
        assertNotNull(recipeReturned, "Null recipe returned");
        verify(recipeReactiveRepository, times(1)).findById(anyString());
        verify(recipeReactiveRepository, never()).findAll();
    }

    @Test
    public void getRecipeCommandByIdTest() {
        final Recipe recipe = new Recipe();
        recipe.setId(ID_VALUE);

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        final RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(ID_VALUE);

        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        final RecipeCommand commandById = recipeService.findCommandById(ID_VALUE).block();

        assertNotNull(commandById, "Null recipe returned");
        verify(recipeReactiveRepository, times(1)).findById(anyString());
        verify(recipeReactiveRepository, never()).findAll();
    }

    @Test
    @DisplayName("getRecipes test")
    void getRecipesTest() {
        final Recipe recipe = new Recipe();
        final Set<Recipe> recipeData = new HashSet<>();
        recipeData.add(recipe);

        when(recipeService.getRecipes()).thenReturn(Flux.just(recipe));

        final List<Recipe> recipeSet = recipeService.getRecipes().collectList().block();

        assertEquals(1, recipeSet.size());
        verify(recipeReactiveRepository, times(1)).findAll();
        verify(recipeReactiveRepository, never()).findById(anyString());
    }

    @Test
    @DisplayName("test delete recipe by id")
    void testDeleteRecipeById() {
        when(recipeReactiveRepository.deleteById(anyString())).thenReturn(Mono.empty());

        //when
        recipeService.deleteById(ID_VALUE);

        //then
        verify(recipeReactiveRepository, times(1)).deleteById(anyString());
    }
}