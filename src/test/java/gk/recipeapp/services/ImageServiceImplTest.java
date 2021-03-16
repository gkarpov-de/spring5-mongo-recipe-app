package gk.recipeapp.services;

import gk.recipeapp.domain.Recipe;
import gk.recipeapp.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ImageServiceImplTest {

    public static final String ID_VALUE = "1";
    @Mock
    private RecipeRepository recipeRepository;

    private ImageService imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageService = new ImageServiceImpl(recipeRepository);
    }

    @Test
    @DisplayName("save Image file test")
    void saveImageFile() throws IOException {
        final Recipe recipe = new Recipe();
        recipe.setId(ID_VALUE);
        final Optional<Recipe> optionalRecipe = Optional.of(recipe);

        final MockMultipartFile multipartFile = new MockMultipartFile("imagefile",
                "testing.txt", "text/plain", "some text".getBytes(StandardCharsets.UTF_8));

        when(recipeRepository.findById(anyString())).thenReturn(optionalRecipe);

        final ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        imageService.saveImageFile(ID_VALUE, multipartFile);

        verify(recipeRepository, times(1)).save(argumentCaptor.capture());
        final Recipe savedRecipe = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);

    }
}