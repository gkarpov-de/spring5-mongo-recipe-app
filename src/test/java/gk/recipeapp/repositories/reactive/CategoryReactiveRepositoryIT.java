package gk.recipeapp.repositories.reactive;

import gk.recipeapp.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
class CategoryReactiveRepositoryIT {

    public static final String CATEGORY_DESCRIPTION = "category description";
    @Autowired
    CategoryReactiveRepository categoryReactiveRepository;
    Category category;

    @BeforeEach
    void setUp() {
        categoryReactiveRepository.deleteAll().block();
        category = new Category();
        category.setDescription(CATEGORY_DESCRIPTION);
        categoryReactiveRepository.save(category).block();
    }

    @Test
    @DisplayName("Category save test")
    void categorySaveTest() {
        final Long countAfterSave = categoryReactiveRepository.count().block();
        assertEquals(1L, countAfterSave);
    }

    @Test
    @DisplayName("Find by description test")
    void findByDescriptionTest() {
        final Category fetchedCategory = categoryReactiveRepository.findByDescription(CATEGORY_DESCRIPTION).block();

        assertNotNull(fetchedCategory);
        assertNotNull(fetchedCategory.getId());
    }

}