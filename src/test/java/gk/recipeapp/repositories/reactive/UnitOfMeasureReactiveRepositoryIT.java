package gk.recipeapp.repositories.reactive;

import gk.recipeapp.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UnitOfMeasureReactiveRepositoryIT {

    public static final String UOM_DESCRIPTION = "uom description";
    @Autowired
    UnitOfMeasureReactiveRepository uomRepository;

    UnitOfMeasure uom;

    @BeforeEach
    void setUp() {
        uomRepository.deleteAll().block();
        uom = new UnitOfMeasure();
        uom.setDescription(UOM_DESCRIPTION);
        uomRepository.save(uom).block();
    }

    @Test
    @DisplayName("uom save test")
    void uomSaveTest() {
        final Long count = uomRepository.count().block();
        assertEquals(1L, count);
    }

    @Test
    @DisplayName("Find by description test")
    void findByDescriptionTest() {
        final UnitOfMeasure fetchedUom = uomRepository.findByDescription(UOM_DESCRIPTION).block();
        assertNotNull(fetchedUom);
        assertNotNull(fetchedUom.getId());
    }

}