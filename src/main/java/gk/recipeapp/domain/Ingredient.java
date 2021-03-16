package gk.recipeapp.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;

@Getter
@Setter
public class Ingredient {
    private String id;
    private String description;
    private BigDecimal amount;

    @DBRef
    private UnitOfMeasure uom;
    private Recipe recipe;

    public Ingredient() {
    }

    public Ingredient(final String description, final BigDecimal amount, final UnitOfMeasure uom) {
        this.description = description;
        this.amount = amount;
        this.uom = uom;
    }


}
