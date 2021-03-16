package gk.recipeapp.services;

import gk.recipeapp.commands.UnitOfMeasureCommand;
import gk.recipeapp.converters.UnitOfMeasureToUnitOfMeasureCommand;
import gk.recipeapp.domain.UnitOfMeasure;
import gk.recipeapp.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UnitOfMeasureServiceImplTest {
    public static final String UOM_ID1 = "1";
    public static final String UOM_ID2 = "2";
    UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand = new UnitOfMeasureToUnitOfMeasureCommand();
    UnitOfMeasureService unitOfMeasureService;

    @Mock
    UnitOfMeasureRepository unitOfMeasureRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        unitOfMeasureService = new UnitOfMeasureServiceImpl(unitOfMeasureRepository, unitOfMeasureToUnitOfMeasureCommand);
    }

    @Test
    @DisplayName("test list all uom")
    void testListAllUom() {
        final Set<UnitOfMeasure> unitOfMeasures = new HashSet<>();
        final UnitOfMeasure unitOfMeasure1 = new UnitOfMeasure();
        final UnitOfMeasure unitOfMeasure2 = new UnitOfMeasure();
        unitOfMeasure1.setId(UOM_ID1);
        unitOfMeasure2.setId(UOM_ID2);
        unitOfMeasures.add(unitOfMeasure1);
        unitOfMeasures.add(unitOfMeasure2);

        when(unitOfMeasureRepository.findAll()).thenReturn(unitOfMeasures);

        final Set<UnitOfMeasureCommand> commands = unitOfMeasureService.listAllUoms();

        assertEquals(2, commands.size());
        verify(unitOfMeasureRepository, times(1)).findAll();
    }
}