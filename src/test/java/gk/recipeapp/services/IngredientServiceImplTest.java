package gk.recipeapp.services;

import gk.recipeapp.commands.IngredientCommand;
import gk.recipeapp.converters.IngredientCommandToIngredient;
import gk.recipeapp.converters.IngredientToIngredientCommand;
import gk.recipeapp.converters.UnitOfMeasureCommandToUnitOfMeasure;
import gk.recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import gk.recipeapp.domain.Ingredient;
import gk.recipeapp.domain.Recipe;
import gk.recipeapp.repositories.RecipeRepository;
import gk.recipeapp.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class IngredientServiceImplTest {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;

    @Mock
    RecipeRepository recipeRepository;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    IngredientService ingredientService;

    //init converters
    public IngredientServiceImplTest() {
        this.ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        this.ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        ingredientService = new IngredientServiceImpl(ingredientToIngredientCommand, ingredientCommandToIngredient,
                recipeRepository, unitOfMeasureRepository);
    }

    @Test
    public void findByRecipeIdAndId() throws Exception {
    }

    @Test
    public void findByRecipeIdAndRecipeIdHappyPath() throws Exception {
        //given
        final Recipe recipe = new Recipe();
        recipe.setId("1");

        final Ingredient ingredient1 = new Ingredient();
        ingredient1.setId("1");

        final Ingredient ingredient2 = new Ingredient();
        ingredient2.setId("1");

        final Ingredient ingredient3 = new Ingredient();
        ingredient3.setId("3");

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);
        final Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

        //then
        final IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId("1", "3");

        //when
        assertEquals("3", ingredientCommand.getId());
        verify(recipeRepository, times(1)).findById(anyString());
    }


    @Test
    public void testSaveRecipeCommand() throws Exception {
        //given
        final IngredientCommand command = new IngredientCommand();
        command.setId("3");
        command.setRecipeId("2");

        final Optional<Recipe> recipeOptional = Optional.of(new Recipe());

        final Recipe savedRecipe = new Recipe();
        savedRecipe.addIngredient(new Ingredient());
        savedRecipe.getIngredients().iterator().next().setId("3");

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);

        //when
        final IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

        //then
        assertEquals("3", savedCommand.getId());
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(1)).save(any(Recipe.class));

    }

    @Test
    public void testDeleteById() throws Exception {
        //given
        final Recipe recipe = new Recipe();
        final Ingredient ingredient = new Ingredient();
        ingredient.setId("3");
        recipe.addIngredient(ingredient);
        final Optional<Recipe> recipeOptional = Optional.of(recipe);

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

        //when
        ingredientService.deleteById("1", "3");

        //then
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }
}