package gk.recipeapp.services;

import gk.recipeapp.commands.UnitOfMeasureCommand;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public interface UnitOfMeasureService {

    Flux<UnitOfMeasureCommand> listAllUoms();
}
