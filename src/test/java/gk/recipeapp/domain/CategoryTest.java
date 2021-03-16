package gk.recipeapp.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryTest {
    public static final String ID_VALUE = "1";
    Category category;

    @BeforeEach
    public void setUp() {
        category = new Category();
    }

    @Test
    void getId() {
        final String id = ID_VALUE;
        category.setId(id);
        assertEquals(id, category.getId());
    }

    @Test
    void getDescription() {
        final String description = "some description";
        category.setDescription(description);
        assertEquals(description, category.getDescription());
    }

    @Test
    void getRecipes() {
    }
}