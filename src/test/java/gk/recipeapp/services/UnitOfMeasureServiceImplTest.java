package gk.recipeapp.services;

import gk.recipeapp.commands.UnitOfMeasureCommand;
import gk.recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import gk.recipeapp.domain.UnitOfMeasure;
import gk.recipeapp.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class UnitOfMeasureServiceImplTest {
    public static final String UOM_ID1 = "1";
    public static final String UOM_ID2 = "2";
    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();
    UnitOfMeasureService unitOfMeasureService;

    @Mock
    UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        unitOfMeasureService = new UnitOfMeasureServiceImpl(unitOfMeasureReactiveRepository, unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    @DisplayName("test list all uom")
    void testListAllUom() {
        final UnitOfMeasure uom1 = new UnitOfMeasure();
        final UnitOfMeasure uom2 = new UnitOfMeasure();
        uom1.setId(UOM_ID1);
        uom2.setId(UOM_ID2);

        when(unitOfMeasureReactiveRepository.findAll()).thenReturn(Flux.just(uom1, uom2));

        final List<UnitOfMeasureCommand> commands = unitOfMeasureService.listAllUoms().collectList().block();

        assertNotNull(commands);
        assertEquals(2, commands.size());
        verify(unitOfMeasureReactiveRepository, times(1)).findAll();
    }
}