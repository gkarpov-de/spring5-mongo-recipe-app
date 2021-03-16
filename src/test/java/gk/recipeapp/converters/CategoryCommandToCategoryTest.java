package gk.recipeapp.converters;

import gk.recipeapp.commands.CategoryCommand;
import gk.recipeapp.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryCommandToCategoryTest {
    private static final String ID_VALUE = "1";
    private static final String DESCRIPTION = "abrakadabra";

    CategoryCommandToCategory converter;

    @BeforeEach
    void setUp() {
        converter = new CategoryCommandToCategory();
    }

    @Test
    @DisplayName("test null parameter")
    void testNullParameter() {
        assertNull(converter.convert(null));
    }

    @Test
    @DisplayName("test empty object")
    void testEmptyObject() {
        assertNotNull(converter.convert(new CategoryCommand()));
    }

    @Test
    @DisplayName("test conversion")
    void testConversion() {
        final CategoryCommand categoryCommand = new CategoryCommand();
        categoryCommand.setId(ID_VALUE);
        categoryCommand.setDescription(DESCRIPTION);

        final Category category = converter.convert(categoryCommand);

        assertNotNull(category);
        assertEquals(ID_VALUE, category.getId());
        assertEquals(DESCRIPTION, category.getDescription());
    }
}
