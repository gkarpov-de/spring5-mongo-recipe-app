package gk.recipeapp.converters;

import gk.recipeapp.commands.IngredientCommand;
import gk.recipeapp.domain.Ingredient;
import gk.recipeapp.domain.Recipe;
import gk.recipeapp.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class IngredientToIngredientCommandTest {
    private static final String ID_VALUE = "1";
    private static final String DESCRIPTION = "abrakadabra";
    private static final String UOM_ID = "2";
    private static final BigDecimal AMOUNT = new BigDecimal(3);
    private static final Recipe recipe = new Recipe();

    IngredientToIngredientCommand converter;

    @BeforeEach
    void setUp() {
        converter = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    }

    @Test
    @DisplayName("test null")
    void testNull() {
        assertNull(converter.convert(null));
    }

    @Test
    @DisplayName("test empty object")
    void testEmptyObject() {
        assertNotNull(converter.convert(new Ingredient()));
    }

    @Test
    @DisplayName("test conversion")
    void testConversion() {
        final Ingredient ingredient = new Ingredient();
        ingredient.setDescription(DESCRIPTION);
        ingredient.setAmount(AMOUNT);
        final UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(UOM_ID);
        ingredient.setUom(unitOfMeasure);

        final IngredientCommand ingredientCommand = converter.convert(ingredient);

        assertNotNull(ingredientCommand);
        assertNotNull(ingredientCommand.getUom());
        assertEquals(DESCRIPTION, ingredientCommand.getDescription());
        assertEquals(AMOUNT, ingredientCommand.getAmount());
        assertEquals(UOM_ID, ingredientCommand.getUom().getId());
    }

    @Test
    @DisplayName("test with null UOM")
    void testWithNullUom() {
        //given
        final Ingredient ingredient = new Ingredient();
        ingredient.setAmount(AMOUNT);
        ingredient.setDescription(DESCRIPTION);

        //when
        final IngredientCommand ingredientCommand = converter.convert(ingredient);

        //then
        assertNotNull(ingredientCommand);
        assertNull(ingredientCommand.getUom());
        assertEquals(AMOUNT, ingredientCommand.getAmount());
        assertEquals(DESCRIPTION, ingredientCommand.getDescription());
    }
}
