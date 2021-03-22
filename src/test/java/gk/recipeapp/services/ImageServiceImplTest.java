package gk.recipeapp.services;

import gk.recipeapp.domain.Recipe;
import gk.recipeapp.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ImageServiceImplTest {

    public static final String ID_VALUE = "1";
    @Mock
    private RecipeReactiveRepository recipeReactiveRepository;

    private ImageService imageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageService = new ImageServiceImpl(recipeReactiveRepository);
    }

    @Test
    @DisplayName("save Image file test")
    void saveImageFile() throws IOException {
        //given
        final MultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                "some text".getBytes());

        final Recipe recipe = new Recipe();
        recipe.setId(ID_VALUE);

        when(recipeReactiveRepository.findById(anyString())).thenReturn(Mono.just(recipe));
        when(recipeReactiveRepository.save(any(Recipe.class))).thenReturn(Mono.just(recipe));

        final ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        //when
        imageService.saveImageFile(ID_VALUE, multipartFile);

        //then
        verify(recipeReactiveRepository, times(1)).save(argumentCaptor.capture());
        final Recipe savedRecipe = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
    }
}