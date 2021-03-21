package gk.recipeapp.services;

import gk.recipeapp.commands.UnitOfMeasureCommand;
import gk.recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import gk.recipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {
    private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;
    private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

    public UnitOfMeasureServiceImpl(final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository, final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
        this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
    }

    @Override
    public Flux<UnitOfMeasureCommand> listAllUoms() {
        return unitOfMeasureReactiveRepository
                .findAll()
                .map(unitOfMeasureToUnitOfMeasureCommand::convert);
//        return StreamSupport.stream(unitOfMeasureRepository.findAll()
//                .spliterator(), false)
//                .map(unitOfMeasureToUnitOfMeasureCommand::convert)
//                .collect(Collectors.toSet());
    }
}
