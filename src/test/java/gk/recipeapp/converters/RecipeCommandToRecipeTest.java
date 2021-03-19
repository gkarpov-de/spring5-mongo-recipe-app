package gk.recipeapp.converters;

import gk.recipeapp.commands.CategoryCommand;
import gk.recipeapp.commands.IngredientCommand;
import gk.recipeapp.commands.NotesCommand;
import gk.recipeapp.commands.RecipeCommand;
import gk.recipeapp.domain.Difficulty;
import gk.recipeapp.domain.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeCommandToRecipeTest {
    private static final String ID_VALUE = "1";
    private static final Integer COOK_TIME = 1;
    private static final Integer PREP_TIME = 2;
    private static final String DESCRIPTION = "My Recipe";
    private static final String DIRECTIONS = "Directions";
    private static final Difficulty DIFFICULTY = Difficulty.Easy;
    private static final Integer SERVINGS = 3;
    private static final String SOURCE = "Source";
    private static final String URL = "Some URL";
    private static final String CAT_ID1 = "22";
    private static final String CAT_ID2 = "23";
    private static final String INGREDIENT_ID1 = "11";
    private static final String INGREDIENT_ID2 = "12";
    private static final String NOTES_ID = "5";

    RecipeCommandToRecipe converter;


    @BeforeEach
    public void setUp() {
        converter = new RecipeCommandToRecipe(new CategoryCommandToCategory(),
                new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure()),
                new NotesCommandToNotes());
    }

    @Test
    public void testNullObject() {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() {
        assertNotNull(converter.convert(new RecipeCommand()));
    }

    @Test
    public void testConversion() {
        //given
        final RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(ID_VALUE);
        recipeCommand.setCookTime(COOK_TIME);
        recipeCommand.setPrepTime(PREP_TIME);
        recipeCommand.setDescription(DESCRIPTION);
        recipeCommand.setDifficulty(DIFFICULTY);
        recipeCommand.setDirections(DIRECTIONS);
        recipeCommand.setServings(SERVINGS);
        recipeCommand.setSource(SOURCE);
        recipeCommand.setUrl(URL);

        final NotesCommand notes = new NotesCommand();
        notes.setId(NOTES_ID);

        recipeCommand.setNotes(notes);

        final CategoryCommand category = new CategoryCommand();
        category.setId(CAT_ID1);

        final CategoryCommand category2 = new CategoryCommand();
        category2.setId(CAT_ID2);

        recipeCommand.getCategories().add(category);
        recipeCommand.getCategories().add(category2);

        final IngredientCommand ingredient = new IngredientCommand();
        ingredient.setId(INGREDIENT_ID1);

        final IngredientCommand ingredient2 = new IngredientCommand();
        ingredient2.setId(INGREDIENT_ID2);

        recipeCommand.getIngredients().add(ingredient);
        recipeCommand.getIngredients().add(ingredient2);

        //when
        final Recipe recipe = converter.convert(recipeCommand);

        assertNotNull(recipe);
        assertEquals(ID_VALUE, recipe.getId());
        assertEquals(COOK_TIME, recipe.getCookTime());
        assertEquals(PREP_TIME, recipe.getPrepTime());
        assertEquals(DESCRIPTION, recipe.getDescription());
        assertEquals(DIFFICULTY, recipe.getDifficulty());
        assertEquals(DIRECTIONS, recipe.getDirections());
        assertEquals(SERVINGS, recipe.getServings());
        assertEquals(SOURCE, recipe.getSource());
        assertEquals(URL, recipe.getUrl());
        assertEquals(NOTES_ID, recipe.getNotes().getId());
        assertEquals(2, recipe.getCategories().size());
        assertEquals(2, recipe.getIngredients().size());
    }

}