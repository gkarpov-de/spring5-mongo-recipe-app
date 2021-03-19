package gk.recipeapp.converters;

import gk.recipeapp.commands.RecipeCommand;
import gk.recipeapp.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeToRecipeCommandTest {
    private static final String ID_VALUE = "1";
    private static final Integer COOK_TIME = 1;
    private static final Integer PREP_TIME = 2;
    private static final String DESCRIPTION = "My Recipe";
    private static final String DIRECTIONS = "Directions";
    private static final Difficulty DIFFICULTY = Difficulty.Easy;
    private static final Integer SERVINGS = 3;
    private static final String SOURCE = "Source";
    private static final String URL = "Some URL";
    private static final String CAT_ID1 = "11";
    private static final String CAT_ID2 = "12";
    private static final String INGREDIENT_ID1 = "21";
    private static final String INGREDIENT_ID2 = "22";
    private static final String NOTES_ID = "5";

    RecipeToRecipeCommand converter;

    @BeforeEach
    void setUp() {
        converter = new RecipeToRecipeCommand(new CategoryToCategoryCommand(), new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand()), new NotesToNotesCommand());
    }

    @Test
    @DisplayName("test null parameter")
    void testNullParameter() {
        assertNull(converter.convert(null));
    }

    @Test
    @DisplayName("test empty object")
    void testEmptyObject() {
        assertNotNull(converter.convert(new Recipe()));
    }

    @Test
    @DisplayName("test conversion")
    void testConversion() {
        final Recipe recipe = new Recipe();
        recipe.setId(ID_VALUE);
        recipe.setCookTime(COOK_TIME);
        recipe.setPrepTime(PREP_TIME);
        recipe.setDescription(DESCRIPTION);
        recipe.setDifficulty(DIFFICULTY);
        recipe.setDirections(DIRECTIONS);
        recipe.setServings(SERVINGS);
        recipe.setSource(SOURCE);
        recipe.setUrl(URL);

        final Notes notes = new Notes();
        notes.setRecipeNotes(NOTES_ID);
        recipe.setNotes(notes);

        final Category category1 = new Category();
        category1.setId(CAT_ID1);

        final Category category2 = new Category();
        category2.setId(CAT_ID2);

        recipe.getCategories().add(category1);
        recipe.getCategories().add(category2);

        final Ingredient ingredient1 = new Ingredient();
        ingredient1.setDescription(INGREDIENT_ID1);

        final Ingredient ingredient2 = new Ingredient();
        ingredient2.setDescription(INGREDIENT_ID2);

        recipe.getIngredients().add(ingredient1);
        recipe.getIngredients().add(ingredient2);

        //when
        final RecipeCommand command = converter.convert(recipe);

        //then
        assertNotNull(command);
        assertEquals(ID_VALUE, command.getId());
        assertEquals(COOK_TIME, command.getCookTime());
        assertEquals(PREP_TIME, command.getPrepTime());
        assertEquals(DESCRIPTION, command.getDescription());
        assertEquals(DIFFICULTY, command.getDifficulty());
        assertEquals(DIRECTIONS, command.getDirections());
        assertEquals(SERVINGS, command.getServings());
        assertEquals(SOURCE, command.getSource());
        assertEquals(URL, command.getUrl());
        assertEquals(NOTES_ID, command.getNotes().getRecipeNotes());
        assertEquals(2, command.getCategories().size());
        assertEquals(2, command.getIngredients().size());
    }
}
