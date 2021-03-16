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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class IngredientServiceImplTest {
    private static final String ID_VALUE = "1";
    private static final String INGREDIENT_ID1 = "11";
    private static final String INGREDIENT_ID2 = "12";
    private static final String INGREDIENT_ID3 = "13";
    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    @Mock
    private RecipeRepository recipeRepository;
    private IngredientService ingredientService;
    @Mock
    private UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImplTest() {
        this.ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
        this.ingredientCommandToIngredient = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ingredientService = new IngredientServiceImpl(ingredientToIngredientCommand, ingredientCommandToIngredient,
                recipeRepository, unitOfMeasureRepository);
    }

    @Test
    public void findByRecipeIdAndId() {
    }

    @Test
    @DisplayName("test find by recipe id and id")
    void testFindByRecipeIdAndIdHappyPath() {
        final Recipe recipe = new Recipe();
        recipe.setId(ID_VALUE);
        final Ingredient ingredient1 = new Ingredient();
        final Ingredient ingredient2 = new Ingredient();
        final Ingredient ingredient3 = new Ingredient();
        ingredient1.setId(INGREDIENT_ID1);
        ingredient2.setId(INGREDIENT_ID2);
        ingredient3.setId(INGREDIENT_ID3);
        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);
        recipe.addIngredient(ingredient3);
        final Optional<Recipe> optionalRecipe = Optional.of(recipe);

        when(recipeRepository.findById(anyString())).thenReturn(optionalRecipe);

        final IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(ID_VALUE, INGREDIENT_ID1);

        assertEquals(ID_VALUE, ingredientCommand.getRecipeId());
        assertEquals(INGREDIENT_ID1, ingredientCommand.getId());
        verify(recipeRepository, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("test save RecipeCommand")
    void testSaveRecipeCommand() {
        // given
        final IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(ID_VALUE);
        ingredientCommand.setId(INGREDIENT_ID1);

        final Optional<Recipe> recipeOptional = Optional.of(new Recipe());

        final Recipe savedRecipe = new Recipe();
        savedRecipe.setId(ID_VALUE);
        savedRecipe.addIngredient(new Ingredient());
        savedRecipe.getIngredients().iterator().next().setId(INGREDIENT_ID1);

        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);
        when(recipeRepository.save(any())).thenReturn(savedRecipe);

        //when
        final IngredientCommand savedCommand = ingredientService.saveIngredientCommand(ingredientCommand);

        //then
        assertEquals(ID_VALUE, savedCommand.getRecipeId());
        assertEquals(INGREDIENT_ID1, savedCommand.getId());
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }

    @Test
    @DisplayName("test delete Ingredient")
    void testDeleteIngredient() {
        // given
        final Recipe recipe = new Recipe();
        recipe.setId(ID_VALUE);
        final Ingredient ingredient = new Ingredient();
        ingredient.setId(INGREDIENT_ID1);
        recipe.addIngredient(ingredient);
        final Optional<Recipe> optionalRecipe = Optional.of(recipe);

        when(recipeRepository.findById(anyString())).thenReturn(optionalRecipe);

        // when
        ingredientService.deleteById(ID_VALUE, INGREDIENT_ID1);

        // then
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }
}