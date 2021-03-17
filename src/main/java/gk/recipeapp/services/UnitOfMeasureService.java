package gk.recipeapp.services;

import gk.recipeapp.commands.UnitOfMeasureCommand;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface UnitOfMeasureService {

    Set<UnitOfMeasureCommand> listAllUoms();
}
