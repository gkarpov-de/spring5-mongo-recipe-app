package gk.recipeapp.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@Service
public interface ImageService {
    Mono<Void> saveImageFile(String recipeId, MultipartFile file);
}
