package gk.recipeapp.controllers;

import gk.recipeapp.commands.RecipeCommand;
import gk.recipeapp.exceptions.NotFoundException;
import gk.recipeapp.services.RecipeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Log4j2
@Controller
public class RecipeController {
    public static final String RECIPE_RECIPEFORM_URL = "recipe/recipeform";
    RecipeService recipeService;

    public RecipeController(final RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{id}/show")
    public String showByID(@PathVariable final String id, final Model model) {
        model.addAttribute("recipe", recipeService.findById(id));
        return "recipe/show";
    }

    @GetMapping("recipe/new")
    public String newRecipe(final Model model) {
        model.addAttribute("recipe", new RecipeCommand());

        return RECIPE_RECIPEFORM_URL;
    }

    @GetMapping("recipe/{id}/update")
    public String updateRecipe(@PathVariable final String id, final Model model) {
        model.addAttribute("recipe", recipeService.findCommandByID(id));

        return RECIPE_RECIPEFORM_URL;
    }

    @PostMapping("recipe")
    public String saveOrUpdate(@ModelAttribute("recipe") final RecipeCommand command, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(log::debug);

            return RECIPE_RECIPEFORM_URL;
        }

        final RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);
        return "redirect:/recipe/" + savedCommand.getId() + "/show";
    }

    @GetMapping("recipe/{id}/delete")
    public String deleteRecipeById(@PathVariable final String id) {
        log.debug("Deleting Recipe with id: {}", id);
        recipeService.deleteById(id);
        return "redirect:/";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(final Exception exception) {
        log.error("Handling Not Found Exception");
        log.error(exception.getMessage());

        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("404error");
        modelAndView.addObject("exception", exception);
        return modelAndView;
    }
}
