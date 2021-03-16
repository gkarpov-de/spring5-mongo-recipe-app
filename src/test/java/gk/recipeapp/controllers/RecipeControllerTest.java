package gk.recipeapp.controllers;

import gk.recipeapp.commands.RecipeCommand;
import gk.recipeapp.domain.Recipe;
import gk.recipeapp.exceptions.NotFoundException;
import gk.recipeapp.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static gk.recipeapp.controllers.RecipeController.RECIPE_RECIPEFORM_URL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RecipeControllerTest {
    public static final String ID_VALUE = "1";
    @Mock
    RecipeService recipeService;

    RecipeController recipeController;
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recipeController = new RecipeController(recipeService);
        mockMvc = MockMvcBuilders.standaloneSetup(recipeController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("get recipe by id test")
    void getRecipeByIdTest() throws Exception {
        final Recipe recipe = new Recipe();
        recipe.setId(ID_VALUE);

        when(recipeService.findById(anyString())).thenReturn(recipe);

        mockMvc.perform(get("/recipe/" + ID_VALUE + "/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/show"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    @DisplayName("get recipe not found test")
    void getRecipeNotFoundTest() throws Exception {
        final Recipe recipe = new Recipe();
        recipe.setId(ID_VALUE);

        when(recipeService.findById(anyString())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    @DisplayName("get recipe number format exception test")
    void getRecipeNumberFormatExceptionTest() throws Exception {
        mockMvc.perform(get("/recipe/asdasd/show"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }

    @Test
    @DisplayName("test get new recipe form")
    void testGetNewRecipeForm() throws Exception {
        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(RECIPE_RECIPEFORM_URL))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    @DisplayName("test post recipe form")
    void testPostRecipeForm() throws Exception {
        final RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(ID_VALUE);

        when(recipeService.saveRecipeCommand(any())).thenReturn(recipeCommand);

        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "some description")
                .param("directions", "some directions"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/" + ID_VALUE + "/show"));
    }

    @Test
    @DisplayName("test post new recipe form validation fail")
    void testPostNewRecipeFormValidationFail() throws Exception {
        final RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(ID_VALUE);

        when(recipeService.saveRecipeCommand(any())).thenReturn(recipeCommand);

        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("cookTime", "3000"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name(RECIPE_RECIPEFORM_URL));
    }

    @Test
    @DisplayName("test get update view")
    void testGetUpdateView() throws Exception {
        final RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(ID_VALUE);

        when(recipeService.findCommandByID(anyString())).thenReturn(recipeCommand);

        mockMvc.perform(get("/recipe/" + ID_VALUE + "/update"))
                .andExpect(status().isOk())
                .andExpect(view().name(RECIPE_RECIPEFORM_URL))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    @DisplayName("test delete recipe")
    void testDeleteRecipe() throws Exception {
        mockMvc.perform(get("/recipe/" + ID_VALUE + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(recipeService, times(1)).deleteById(anyString());
    }
}
