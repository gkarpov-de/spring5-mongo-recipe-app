package gk.recipeapp.converters;

import gk.recipeapp.commands.CategoryCommand;
import gk.recipeapp.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryToCategoryCommandTest {
    private static final String ID_VALUE = "1";
    private static final String DESCRIPTION = "abrakadabra";

    CategoryToCategoryCommand converter;

    @BeforeEach
    void setUp() {
        converter = new CategoryToCategoryCommand();
    }

    @Test
    @DisplayName("test null parameter")
    void testNullParameter() {
        assertNull(converter.convert(null));
    }

    @Test
    @DisplayName("test empty object")
    void testEmptyObject() {
        assertNotNull(converter.convert(new Category()));
    }

    @Test
    @DisplayName("test conversion")
    void testConversion() {
        final Category category = new Category();
        category.setId(ID_VALUE);
        category.setDescription(DESCRIPTION);

        final CategoryCommand categoryCommand = converter.convert(category);

        assertNotNull(categoryCommand);
        assertEquals(ID_VALUE, categoryCommand.getId());
        assertEquals(DESCRIPTION, categoryCommand.getDescription());
    }


}
