package gk.recipeapp.services;

import gk.recipeapp.domain.Recipe;
import gk.recipeapp.repositories.RecipeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Log4j2
@Service
public class ImageServiceImpl implements ImageService {
    private final RecipeRepository recipeRepository;

    public ImageServiceImpl(final RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImageFile(final String recipeId, final MultipartFile file) {
        log.debug("File: {} received for recipe id: {}", file, recipeId);
        final Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isEmpty()) {
            throw new RuntimeException("<saveImageFile> Recipe id: " + recipeId + " not found.");
        }
        final Recipe recipe = recipeOptional.get();

        try {
            final int length = file.getBytes().length;
            final Byte[] bytes = new Byte[length];
            int i = 0;
            for (final byte aByte : file.getBytes()) {
                bytes[i++] = aByte;
            }
            recipe.setImage(bytes);
        } catch (final IOException e) {
            // TODO exception handling implementation
            e.printStackTrace();
        }
        recipeRepository.save(recipe);
    }
}
