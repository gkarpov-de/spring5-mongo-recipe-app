package gk.recipeapp.controllers;

import gk.recipeapp.commands.IngredientCommand;
import gk.recipeapp.commands.RecipeCommand;
import gk.recipeapp.commands.UnitOfMeasureCommand;
import gk.recipeapp.services.IngredientService;
import gk.recipeapp.services.RecipeService;
import gk.recipeapp.services.UnitOfMeasureService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Log4j2
@Controller
public class IngredientController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(final RecipeService recipeService, final IngredientService ingredientService, final UnitOfMeasureService unitOfMeasureService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping("/recipe/{recipeId}/ingredients")
    public String showIngredients(@PathVariable final String recipeId, final Model model) {
        log.debug("Getting ingredient list for recipe id: {}", recipeId);
        model.addAttribute("recipe", recipeService.findCommandByID(recipeId));
        return "recipe/ingredient/list";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/show")
    public String showIngredient(@PathVariable final String recipeId, @PathVariable final String ingredientId, final Model model) {
        log.debug("Getting ingredient id: {} from recipe id: {}", ingredientId, recipeId);
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));
        return "recipe/ingredient/show";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/new")
    public String newIngredient(@PathVariable final String recipeId, final Model model) {
        log.debug("Creating new ingredient for recipe id: {}", recipeId);
        final RecipeCommand recipeCommand = recipeService.findCommandByID(recipeId);
        if (recipeCommand == null) {
            throw new RuntimeException("Recipe id: " + recipeId + " not found.");
        }

        final IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);

        model.addAttribute("ingredient", ingredientCommand);

        ingredientCommand.setUom(new UnitOfMeasureCommand());
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());

        return "recipe/ingredient/ingredientform";
    }

    @GetMapping("/recipe/{recipeId}/ingredient/{ingredientId}/update")
    public String updateRecipeIngredient(@PathVariable final String recipeId, @PathVariable final String ingredientId, final Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, ingredientId));
        model.addAttribute("uomList", unitOfMeasureService.listAllUoms());

        return "recipe/ingredient/ingredientform";
    }

    @PostMapping("recipe/{recipeId}/ingredient")
    public String saveOrUpdateIngredient(@ModelAttribute final IngredientCommand ingredientCommand) {
        final IngredientCommand savedIngredientCommand = ingredientService.saveIngredientCommand(ingredientCommand);

        final String ingredientCommandId = savedIngredientCommand.getId();
        final String recipeId = savedIngredientCommand.getRecipeId();
        log.debug("saved ingredient id: {}, recipe id: {}", ingredientCommandId, recipeId);

        return "redirect:/recipe/" + recipeId + "/ingredient/" + ingredientCommandId + "/show";
    }

    @GetMapping("recipe/{recipeId}/ingredient/{ingredientId}/delete")
    public String deleteIngredient(@PathVariable final String recipeId, @PathVariable final String ingredientId) {
        ingredientService.deleteById(recipeId, ingredientId);
        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
}
