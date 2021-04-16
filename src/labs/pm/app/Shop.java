package labs.pm.app;

import labs.pm.data.Product;
import labs.pm.data.ProductManager;
import labs.pm.data.Rating;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Locale;

public class Shop {

    public static void main(String[] args) {
        ProductManager pm = new ProductManager(Locale.forLanguageTag("pt-BR"));

        pm.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
        pm.printProductReport(101);

        pm.parseReview("101,X,Nice hot cup of tea");

//        pm.reviewProduct(101, Rating.FOUR_STAR, "Nice hot cup of tea");
//        pm.reviewProduct(101, Rating.TWO_STAR, "Rather weak tea");
//        pm.reviewProduct(101, Rating.FOUR_STAR, "Fine tea");
//        pm.reviewProduct(101, Rating.FOUR_STAR, "Good tea");
//        pm.reviewProduct(101, Rating.FIVE_STAR, "Perfect tea");
//        pm.reviewProduct(101, Rating.THREE_STAR, "Just add some lemon");
        pm.printProductReport(101);

//        pm.changeLocale("en-GB");
//
//        pm.createProduct(102, "Coffee", BigDecimal.valueOf(1.2), Rating.NOT_RATED);
//        pm.reviewProduct(102, Rating.TWO_STAR, "Where is the milk?");
//        pm.printProductReport(102);
//
//        pm.createProduct(103, "Cake", BigDecimal.valueOf(10.55), Rating.NOT_RATED, LocalDate.now().plusDays(2));
//        pm.reviewProduct(103, Rating.THREE_STAR, "Cake was ok");
//        pm.printProductReport(103);
//
//        Comparator<Product> ratingSorter = Comparator.comparingInt(p -> p.getRating().ordinal());
//        Comparator<Product> priceSorter = (p1, p2) -> p2.getPrice().compareTo(p1.getPrice());
//        pm.printProducts(p -> p.getPrice().floatValue() > 1.9, ratingSorter.thenComparing(priceSorter));
////        pm.printProducts(ratingSorter.thenComparing(priceSorter).reversed());
//
//        pm.getDiscounts().forEach((rating, discount) -> System.out.println(rating + "\t" + discount));
    }
}
