package gk.recipeapp.bootstrap;

import gk.recipeapp.domain.*;
import gk.recipeapp.exceptions.NotFoundException;
import gk.recipeapp.repositories.CategoryRepository;
import gk.recipeapp.repositories.RecipeRepository;
import gk.recipeapp.repositories.UnitOfMeasureRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Log4j2
@Component
@Profile("default")
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    RecipeRepository recipeRepository;
    CategoryRepository categoryRepository;
    UnitOfMeasureRepository unitOfMeasureRepository;

    public RecipeBootstrap(final RecipeRepository recipeRepository, final CategoryRepository categoryRepository, final UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    private List<Recipe> getRecipes() {
        final List<Recipe> recipes = new ArrayList<>();
        final Map<String, UnitOfMeasure> uoms = getUoms();
        final Recipe recipe1 = new Recipe();
        final Category catItalian = categoryRepository.findByDescription("Italian").orElseThrow(NotFoundException::new);
        final Category catMexican = categoryRepository.findByDescription("Mexican").orElseThrow(NotFoundException::new);

        recipe1.getCategories().add(catItalian);
        recipe1.getCategories().add(catMexican);

        recipe1.setCookTime(1);
        recipe1.setPrepTime(2);
        recipe1.setDescription("Perfect Guacamole");
        recipe1.setDifficulty(Difficulty.Moderate);
        recipe1.setDirections("Cut the avocado, remove flesh\nCut the avocado, remove flesh\nCut the avocados in half. Remove the pit. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel an Avocado.) Place in a bowl.\n2. Mash with a fork\nUsing a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)\n3. Add salt, lime juice, and the rest\nSprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown. Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness. Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste. Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.\n4. Serve\nServe immediately, or if making a few hours ahead, place plastic wrap on the surface of the guacamole and press down to cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.");
        recipe1.addIngredient(new Ingredient("ripe avocados", new BigDecimal(2), uoms.get("Each")));
        recipe1.addIngredient(new Ingredient("salt", new BigDecimal("0.25"), uoms.get("Teaspoon")));
        recipe1.addIngredient(new Ingredient("fresh lime juice or lemon juice", new BigDecimal(1), uoms.get("Tablespoon")));
        recipe1.addIngredient(new Ingredient("minced red onion or thinly sliced green onion", new BigDecimal(2), uoms.get("Tablespoon")));
        recipe1.addIngredient(new Ingredient("serrano chilies, stems and seeds removed, minced", new BigDecimal(1), uoms.get("Each")));
        recipe1.addIngredient(new Ingredient("cilantro (leaves and tender stems), finely chopped", new BigDecimal(2), uoms.get("Tablespoon")));
        recipe1.addIngredient(new Ingredient("freshly grated black pepper", new BigDecimal(1), uoms.get("Dash")));
        recipe1.addIngredient(new Ingredient("ripe tomato, seeds and pulp removed, chopped", new BigDecimal("0.5"), uoms.get("Each")));
        recipe1.setServings(3);
        recipe1.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        recipe1.setSource("Simply recipes");
        recipe1.setNotes(new Notes(recipe1, "peace of cake"));
        recipes.add(recipe1);
        final Recipe recipe2 = new Recipe();
        recipe2.getCategories().add(catMexican);
        recipe2.setCookTime(1);
        recipe2.setPrepTime(2);
        recipe2.setDescription("Spicy Grilled Chicken Tacos");
        recipe2.setDifficulty(Difficulty.Hard);
        recipe2.setDirections("X3 4To");
        recipe2.setNotes(new Notes(recipe2, "peace of cake2"));
        recipes.add(recipe2);

        return recipes;
    }

    private Map<String, UnitOfMeasure> getUoms() {
        final List<String> uomDescriptions = List.of("Teaspoon", "Tablespoon", "Cup", "Pinch", "Pint", "Ounce", "Dash", "Each");
        final Map<String, UnitOfMeasure> uoms = new HashMap<>();

        for (final String uomDescription : uomDescriptions) {
            uoms.put(uomDescription, getUomByDescription(uomDescription));
        }

        return uoms;
    }

    private UnitOfMeasure getUomByDescription(final String uomDescription) {
        final Optional<UnitOfMeasure> unitOfMeasureOptional = unitOfMeasureRepository.findByDescription(uomDescription);
        if (unitOfMeasureOptional.isEmpty()) {
            throw new RuntimeException("Expected UOM not found");
        }
        return unitOfMeasureOptional.get();
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent contextRefreshedEvent) {
        recipeRepository.saveAll(getRecipes());
        log.debug("Loading Bootstrap Data");
    }
}
