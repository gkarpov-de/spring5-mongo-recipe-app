package gk.recipeapp.controllers;

import gk.recipeapp.domain.Recipe;
import gk.recipeapp.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class IndexControllerTest {
    public static final String ID_VALUE = "1";
    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    IndexController indexController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        indexController = new IndexController(recipeService);
    }

    @Test
    @DisplayName("test Mock MVC")
    void testMockMvc() throws Exception {
        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));

    }

    @Test
    @DisplayName("test getIndexPage")
    void testGetIndexPage() {
        // given
        final Set<Recipe> recipeSet = new HashSet<>();
        final Recipe recipe = new Recipe();
        recipe.setId(ID_VALUE);

        recipeSet.add(recipe);
        recipeSet.add(new Recipe());

        when(recipeService.getRecipes()).thenReturn(recipeSet);

        final ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);
        // when
        final String viewName = indexController.getIndexPage(model);

        // then
        assertEquals("index", viewName);
        verify(recipeService, times(1)).getRecipes();
        verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
        final Set<Recipe> setController = (Set<Recipe>) argumentCaptor.getValue();
        assertEquals(2, setController.size());
    }
}