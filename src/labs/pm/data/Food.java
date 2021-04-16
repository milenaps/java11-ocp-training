package labs.pm.data;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Food extends Product {

    private LocalDate bestBefore;

    Food(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        super(id, name, price, rating);
        this.bestBefore = bestBefore;
    }

    public LocalDate getBestBefore() {
        return bestBefore;
    }

    @Override
    public Food applyRating(Rating rating) {
        return new Food(getId(), getName(), getPrice(), rating, getBestBefore());
    }
}
