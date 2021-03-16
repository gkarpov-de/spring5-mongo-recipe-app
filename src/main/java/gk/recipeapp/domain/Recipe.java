package gk.recipeapp.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Document
public class Recipe {
    @Id
    private String id;
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    private String directions;
    private Byte[] image;
    private Difficulty difficulty;
    private Notes notes;
    private final Set<Ingredient> ingredients = new HashSet<>();
    @DBRef
    private final Set<Category> categories = new HashSet<>();

    public void setNotes(final Notes notes) {
        if (notes != null) {
            this.notes = notes;
            notes.setRecipe(this);
        }
    }

    public void addIngredient(final Ingredient ingredient) {
        ingredient.setRecipe(this);
        this.ingredients.add(ingredient);
    }
}
