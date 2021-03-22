package gk.recipeapp.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class Ingredient {
    @Id
    private String id = UUID.randomUUID().toString();

    private String description;
    private BigDecimal amount;
    private UnitOfMeasure uom;

    public Ingredient() {
    }

    public Ingredient(final String description, final BigDecimal amount, final UnitOfMeasure uom) {
        this.description = description;
        this.amount = amount;
        this.uom = uom;
    }
}