package gk.recipeapp.controllers;

import gk.recipeapp.commands.RecipeCommand;
import gk.recipeapp.services.ImageService;
import gk.recipeapp.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ImageControllerTest {
    private static final String ID_VALUE = "1";
    @Mock
    ImageService imageService;
    @Mock
    RecipeService recipeService;

    ImageController imageController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageController = new ImageController(recipeService, imageService);
        mockMvc = MockMvcBuilders.standaloneSetup(imageController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("open image form test")
    void openImageFormTest() throws Exception {
        final RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(ID_VALUE);
        when(recipeService.findCommandByID(anyString())).thenReturn(recipeCommand);

        mockMvc.perform(get("/recipe/" + ID_VALUE + "/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).findCommandByID(anyString());
    }


    @Test
    @DisplayName("handle image post test")
    void handleImagePostTest() throws Exception {
        final MockMultipartFile multipartFile = new MockMultipartFile("imagefile", "test.txt", "text/plain", "Some strange text".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/recipe/" + ID_VALUE + "/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipe/" + ID_VALUE + "/show"));

        verify(imageService, times(1)).saveImageFile(anyString(), any());
    }

    @Test
    @DisplayName("render image from DB test")
    void renderImageFromDbTest() throws Exception {
        final RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(ID_VALUE);

        final String str = "some text";
        final Byte[] bytes = new Byte[str.getBytes().length];

        int i = 0;
        for (final Byte aByte : str.getBytes()) {
            bytes[i++] = aByte;
        }

        recipeCommand.setImage(bytes);

        when(recipeService.findCommandByID(anyString())).thenReturn(recipeCommand);

        final MockHttpServletResponse response = mockMvc.perform(get("/recipe/" + ID_VALUE + "/recipeimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        final byte[] responseByte = response.getContentAsByteArray();

        assertEquals(str.getBytes().length, responseByte.length);
    }
}
