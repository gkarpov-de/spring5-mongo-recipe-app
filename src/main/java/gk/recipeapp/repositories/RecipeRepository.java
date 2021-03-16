package gk.recipeapp.repositories;

import gk.recipeapp.domain.Recipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRepository extends CrudRepository<Recipe, String> {
}
