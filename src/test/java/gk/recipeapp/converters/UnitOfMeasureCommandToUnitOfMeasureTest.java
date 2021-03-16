package gk.recipeapp.converters;

import gk.recipeapp.commands.UnitOfMeasureCommand;
import gk.recipeapp.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnitOfMeasureCommandToUnitOfMeasureTest {
    private static final String ID_VALUE = "1";
    private static final String DESCRIPTION = "abrakadabra";

    UnitOfMeasureCommandToUnitOfMeasure converter;

    @BeforeEach
    void setUp() {
        converter = new UnitOfMeasureCommandToUnitOfMeasure();
    }

    @Test
    @DisplayName("test null")
    void testNull() {
        assertNull(converter.convert(null));
    }

    @Test
    @DisplayName("test empty object")
    void testEmptyObject() {
        assertNotNull(converter.convert(new UnitOfMeasureCommand()));
    }

    @Test
    @DisplayName("test conversion")
    void testConversion() {
        final UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(ID_VALUE);
        unitOfMeasureCommand.setDescription(DESCRIPTION);

        final UnitOfMeasure unitOfMeasure = converter.convert(unitOfMeasureCommand);

        assertNotNull(unitOfMeasure);
        assertEquals(ID_VALUE, unitOfMeasure.getId());
        assertEquals(DESCRIPTION, unitOfMeasure.getDescription());
    }
}
