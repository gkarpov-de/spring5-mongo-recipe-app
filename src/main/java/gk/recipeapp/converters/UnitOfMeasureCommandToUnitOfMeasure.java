package gk.recipeapp.converters;

import gk.recipeapp.commands.UnitOfMeasureCommand;
import gk.recipeapp.domain.UnitOfMeasure;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class UnitOfMeasureCommandToUnitOfMeasure implements Converter<UnitOfMeasureCommand, UnitOfMeasure> {

    @Synchronized
    @Override
    @Nullable
    public UnitOfMeasure convert(@Nullable final UnitOfMeasureCommand unitOfMeasureCommand) {
        if (unitOfMeasureCommand == null) {
            return null;
        }

        final UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(unitOfMeasureCommand.getId());
        unitOfMeasure.setDescription(unitOfMeasureCommand.getDescription());
        return unitOfMeasure;
    }
}
