package gk.recipeapp.converters;

import gk.recipeapp.commands.IngredientCommand;
import gk.recipeapp.commands.UnitOfMeasureCommand;
import gk.recipeapp.domain.Ingredient;
import gk.recipeapp.domain.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class IngredientCommandToIngredientTest {
    private static final Recipe RECIPE = new Recipe();
    private static final BigDecimal AMOUNT = new BigDecimal("1");
    private static final String DESCRIPTION = "Cheeseburger";
    private static final String ID_VALUE = "1";
    private static final String UOM_ID = "2";

    IngredientCommandToIngredient converter;

    @BeforeEach
    public void setUp() throws Exception {
        converter = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
    }

    @Test
    public void testNullObject() throws Exception {
        assertNull(converter.convert(null));
    }

    @Test
    public void testEmptyObject() throws Exception {
        assertNotNull(converter.convert(new IngredientCommand()));
    }

    @Test
    public void convert() throws Exception {
        //given
        final IngredientCommand command = new IngredientCommand();
        command.setAmount(AMOUNT);
        command.setDescription(DESCRIPTION);
        final UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(UOM_ID);
        command.setUom(unitOfMeasureCommand);

        //when
        final Ingredient ingredient = converter.convert(command);

        //then
        assertNotNull(ingredient);
        assertNotNull(ingredient.getUom());
        assertEquals(AMOUNT, ingredient.getAmount());
        assertEquals(DESCRIPTION, ingredient.getDescription());
        assertEquals(UOM_ID, ingredient.getUom().getId());
    }

    @Test
    public void convertWithNullUOM() throws Exception {
        //given
        final IngredientCommand command = new IngredientCommand();
        command.setAmount(AMOUNT);
        command.setDescription(DESCRIPTION);
        final UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();


        //when
        final Ingredient ingredient = converter.convert(command);

        //then
        assertNotNull(ingredient);
        assertNull(ingredient.getUom());
        assertEquals(AMOUNT, ingredient.getAmount());
        assertEquals(DESCRIPTION, ingredient.getDescription());

    }
}
