package gk.recipeapp.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = {"recipe"})
public class Notes {
    private String id;

    private Recipe recipe;

    private String recipeNotes;

    public Notes() {
    }

    public Notes(final Recipe recipe, final String recipeNotes) {
        this.recipe = recipe;
        this.recipeNotes = recipeNotes;
    }
}
