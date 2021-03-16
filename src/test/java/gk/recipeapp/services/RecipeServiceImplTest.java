package gk.recipeapp.services;

import gk.recipeapp.commands.RecipeCommand;
import gk.recipeapp.converters.RecipeCommandToRecipe;
import gk.recipeapp.converters.RecipeToRecipeCommand;
import gk.recipeapp.domain.Recipe;
import gk.recipeapp.exceptions.NotFoundException;
import gk.recipeapp.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecipeServiceImplTest {
    private static final String ID_VALUE = "1";

    RecipeService recipeService;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Mock
    RecipeToRecipeCommand recipeToRecipeCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository, recipeCommandToRecipe, recipeToRecipeCommand);
    }

    @Test
    @DisplayName("getRecipeByID test")
    void getRecipeByIdTest() {
        final Recipe recipe = new Recipe();
        recipe.setId(ID_VALUE);
        final Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

        final Recipe recipeReturned = recipeService.findById(ID_VALUE);
        assertNotNull(recipeReturned, "Null recipe returned");
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    @DisplayName("get recipe by id not found test")
    void getRecipeByIdNotFoundTest() {
        final Optional<Recipe> recipeOptional = Optional.empty();
        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

        assertThrows(NotFoundException.class, () -> recipeService.findById(ID_VALUE));
    }

    @Test
    public void getRecipeCommandByIdTest() {
        final Recipe recipe = new Recipe();
        recipe.setId(ID_VALUE);
        final Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

        final RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(ID_VALUE);

        when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

        final RecipeCommand commandById = recipeService.findCommandById(ID_VALUE);

        assertNotNull(commandById, "Null recipe returned");
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, never()).findAll();
    }

    @Test
    @DisplayName("getRecipes test")
    void getRecipesTest() {
        final Recipe recipe = new Recipe();
        final Set<Recipe> recipeData = new HashSet<>();
        recipeData.add(recipe);

        when(recipeService.getRecipes()).thenReturn(recipeData);

        final Set<Recipe> recipeSet = recipeService.getRecipes();

        assertEquals(1, recipeSet.size());
        verify(recipeRepository, times(1)).findAll();
        verify(recipeRepository, never()).findById(anyString());
    }

    @Test
    @DisplayName("test delete recipe by id")
    void testDeleteRecipeById() {
        recipeService.deleteById(ID_VALUE);

        verify(recipeRepository, times(1)).deleteById(anyString());
    }
}