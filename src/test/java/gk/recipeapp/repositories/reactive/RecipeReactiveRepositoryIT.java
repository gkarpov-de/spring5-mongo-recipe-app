package gk.recipeapp.repositories.reactive;

import gk.recipeapp.domain.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RecipeReactiveRepositoryIT {

    public static final String RECIPE_ID = "recipe_id";
    public static final String RECIPE_DESCRIPTION = "description";
    @Autowired
    RecipeReactiveRepository reactiveRepository;
    Recipe recipe;

    @BeforeEach
    void setUp() {
        reactiveRepository.deleteAll().block();
        recipe = new Recipe();
        recipe.setDescription(RECIPE_DESCRIPTION);
        recipe.setId(RECIPE_ID);
        reactiveRepository.save(recipe).block();
    }

    @Test
    @DisplayName("Recipe save test")
    void recipeSaveTest() {
        final Long countAfterSave = reactiveRepository.count().block();
        assertEquals(1L, countAfterSave);
    }

    @Test
    @DisplayName("Find by id test")
    void findByIdTest() {
        final Recipe fetchedRecipe = reactiveRepository.findById(RECIPE_ID).block();

        assertNotNull(fetchedRecipe);
        assertEquals(RECIPE_DESCRIPTION, fetchedRecipe.getDescription());
    }
}
