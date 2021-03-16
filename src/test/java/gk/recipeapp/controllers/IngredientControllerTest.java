package gk.recipeapp.controllers;

import gk.recipeapp.commands.IngredientCommand;
import gk.recipeapp.commands.RecipeCommand;
import gk.recipeapp.services.IngredientService;
import gk.recipeapp.services.RecipeService;
import gk.recipeapp.services.UnitOfMeasureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class IngredientControllerTest {
    private static final String ID_VALUE = "1";
    private static final String INGREDIENT_ID = "2";

    @Mock
    private RecipeService recipeService;

    @Mock
    private IngredientService ingredientService;

    @Mock
    private UnitOfMeasureService unitOfMeasureService;

    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new IngredientController(recipeService, ingredientService, unitOfMeasureService))
                .build();
    }

    @Test
    @DisplayName("test list of ingredients")
    void testListOfIngredients() throws Exception {
        when(recipeService.findCommandByID(anyString())).thenReturn(new RecipeCommand());

        mockMvc.perform(get("/recipe/" + ID_VALUE + "/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/list"))
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).findCommandByID(anyString());
    }

    @Test
    @DisplayName("test show ingredient")
    void testShowIngredient() throws Exception {
        final IngredientCommand ingredientCommand = new IngredientCommand();

        when(ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(ingredientCommand);

        mockMvc.perform(get("/recipe/" + ID_VALUE + "/ingredient/" + INGREDIENT_ID + "/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"));

        verify(ingredientService, times(1)).findByRecipeIdAndIngredientId(anyString(), anyString());
    }

    @Test
    @DisplayName("test new ingredient form")
    void testNewIngredientForm() throws Exception {
        final RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(ID_VALUE);

        when(recipeService.findCommandByID(anyString())).thenReturn(recipeCommand);
        when(unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());

        mockMvc.perform(get("/recipe/" + ID_VALUE + "/ingredient/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));

        verify(recipeService, times(1)).findCommandByID(anyString());
        verify(unitOfMeasureService, times(1)).listAllUoms();
    }


    @Test
    @DisplayName("test update ingredient form")
    void testUpdateIngredientForm() throws Exception {
        final IngredientCommand ingredientCommand = new IngredientCommand();

        when(ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(ingredientCommand);
        when(unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());

        mockMvc.perform(get("/recipe/1/ingredient/2/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));
    }

    @Test
    @DisplayName("test save or update ingredient")
    void testSaveOrUpdateIngredient() throws Exception {
        final IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(ID_VALUE);
        ingredientCommand.setId(INGREDIENT_ID);

        when(ingredientService.saveIngredientCommand(any())).thenReturn(ingredientCommand);

        mockMvc.perform(post("/recipe/" + ID_VALUE + "/ingredient")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("ingredientID", "")
                .param("description", "some description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/" + ID_VALUE + "/ingredient/" + INGREDIENT_ID + "/show"));
    }

    @Test
    @DisplayName("test delete ingredient")
    void testDeleteIngredient() throws Exception {
        mockMvc.perform(get("/recipe/" + ID_VALUE + "/ingredient/" + INGREDIENT_ID + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/" + ID_VALUE + "/ingredients"));

        verify(ingredientService, times(1)).deleteById(anyString(), anyString());
    }
}
