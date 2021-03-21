package gk.recipeapp.repositories.reactive;

import gk.recipeapp.domain.UnitOfMeasure;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UnitOfMeasureReactiveRepository extends ReactiveMongoRepository<UnitOfMeasure, String> {
    Mono<UnitOfMeasure> findByDescription(String description);
}
