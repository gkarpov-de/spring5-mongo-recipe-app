package gk.recipeapp.converters;

import gk.recipeapp.commands.UnitOfMeasureCommand;
import gk.recipeapp.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnitOfMeasureToUnitOfMeasureCommandTest {
    private static final String ID_VALUE = "1";
    private static final String DESCRIPTION = "abrakadabra";

    UnitOfMeasureToUnitOfMeasureCommand converter;

    @BeforeEach
    void setUp() {
        converter = new UnitOfMeasureToUnitOfMeasureCommand();
    }

    @Test
    @DisplayName("test null")
    void testNull() {
        assertNull(converter.convert(null));
    }

    @Test
    @DisplayName("test empty object")
    void testEmptyObject() {
        assertNotNull(converter.convert(new UnitOfMeasure()));
    }

    @Test
    @DisplayName("test conversion")
    void testConversion() {
        final UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(ID_VALUE);
        unitOfMeasure.setDescription(DESCRIPTION);

        final UnitOfMeasureCommand unitOfMeasureCommand = converter.convert(unitOfMeasure);

        assertNotNull(unitOfMeasureCommand);
        assertEquals(ID_VALUE, unitOfMeasureCommand.getId());
        assertEquals(DESCRIPTION, unitOfMeasureCommand.getDescription());
    }
}
