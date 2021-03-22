package gk.recipeapp.controllers;

import gk.recipeapp.commands.IngredientCommand;
import gk.recipeapp.commands.RecipeCommand;
import gk.recipeapp.commands.UnitOfMeasureCommand;
import gk.recipeapp.exceptions.NotFoundException;
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

    private final IngredientService ingredientService;
    private final RecipeService recipeService;
    private final UnitOfMeasureService unitOfMeasureService;

    public IngredientController(final IngredientService ingredientService, final RecipeService recipeService, final UnitOfMeasureService unitOfMeasureService) {
        this.ingredientService = ingredientService;
        this.recipeService = recipeService;
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @GetMapping("/recipe/{recipeId}/ingredients")
    public String listIngredients(@PathVariable final String recipeId, final Model model) {
        log.debug("Getting ingredient list for recipe id: " + recipeId);

        // use command object to avoid lazy load errors in Thymeleaf.
        model.addAttribute("recipe", recipeService.findCommandById(recipeId).block());

        return "recipe/ingredient/list";
    }

    @GetMapping("recipe/{recipeId}/ingredient/{id}/show")
    public String showRecipeIngredient(@PathVariable final String recipeId,
                                       @PathVariable final String id, final Model model) {
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, id).block());
        return "recipe/ingredient/show";
    }

    @GetMapping("recipe/{recipeId}/ingredient/new")
    public String newRecipe(@PathVariable final String recipeId, final Model model) {

        //make sure we have a good id value
        final RecipeCommand recipeCommand = recipeService.findCommandById(recipeId).block();
        if (recipeCommand == null) {
            throw new NotFoundException("DB error: recipe id: " + recipeId + " not found");
        }

        //need to return back parent id for hidden form property
        final IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setRecipeId(recipeId);
        model.addAttribute("ingredient", ingredientCommand);

        //init uom
        ingredientCommand.setUom(new UnitOfMeasureCommand());

        model.addAttribute("uomList", unitOfMeasureService.listAllUoms().collectList().block());

        return "recipe/ingredient/ingredientform";
    }

    @GetMapping("recipe/{recipeId}/ingredient/{id}/update")
    public String updateRecipeIngredient(@PathVariable final String recipeId,
                                         @PathVariable final String id, final Model model) {
        log.warn("Recipe id: {}, ingredient id: {}", recipeId, id);
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, id).block());

        model.addAttribute("uomList", unitOfMeasureService.listAllUoms().collectList().block());
        return "recipe/ingredient/ingredientform";
    }

    @PostMapping("recipe/{recipeId}/ingredient")
    public String saveOrUpdate(@ModelAttribute final IngredientCommand command) {
        final IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command).block();

        log.debug("saved ingredient id:" + savedCommand.getId());

        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId() + "/show";
    }

    @GetMapping("recipe/{recipeId}/ingredient/{id}/delete")
    public String deleteIngredient(@PathVariable final String recipeId,
                                   @PathVariable final String id) {

        log.debug("deleting ingredient id:" + id);
        ingredientService.deleteById(recipeId, id).block();

        return "redirect:/recipe/" + recipeId + "/ingredients";
    }
}