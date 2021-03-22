package gk.recipeapp.services;

import gk.recipeapp.domain.Recipe;
import gk.recipeapp.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Log4j2
@Service
public class ImageServiceImpl implements ImageService {
    private final RecipeReactiveRepository recipeReactiveRepository;

    public ImageServiceImpl(final RecipeReactiveRepository recipeReactiveRepository) {
        this.recipeReactiveRepository = recipeReactiveRepository;
    }

    @Override
    public Mono<Void> saveImageFile(final String recipeId, final MultipartFile file) {
        log.debug("File: {} received for recipe id: {}", file, recipeId);
        final Mono<Recipe> recipeMono = recipeReactiveRepository.findById(recipeId)
                .map(recipe -> {
                    try {
                        final int length = file.getBytes().length;
                        final Byte[] bytes = new Byte[length];
                        int i = 0;
                        for (final byte aByte : file.getBytes()) {
                            bytes[i++] = aByte;
                        }
                        recipe.setImage(bytes);
                        return recipe;
                    } catch (final IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                });
        recipeReactiveRepository.save(recipeMono.block()).block();

        return Mono.empty();
    }
}
