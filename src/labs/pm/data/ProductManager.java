package labs.pm.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProductManager {

    private static final Logger logger = Logger.getLogger(ProductManager.class.getName());

    private Map<Product, List<Review>> products = new HashMap<>();
    private ResourceFormatter formatter;

    private ResourceBundle config = ResourceBundle.getBundle("labs.pm.data.config");
    private MessageFormat productFormat = new MessageFormat(config.getString("product.data.format"));
    private MessageFormat reviewFormat = new MessageFormat(config.getString("review.data.format"));

    private static Map<String, ResourceFormatter> formatters =
            Map.of("en-GB", new ResourceFormatter(Locale.UK),
                    "pt-BR", new ResourceFormatter(Locale.forLanguageTag("pt-BR")));

    public ProductManager(Locale locale) {
        this(locale.toLanguageTag());
    }

    public ProductManager(String languageTag) {
        changeLocale(languageTag);
    }

    public void changeLocale(String languageTag) {
        formatter = formatters.getOrDefault(languageTag, formatters.get("pt-BR"));
    }

    public static Set<String> getSupportedLocales() {
        return formatters.keySet();
    }

    public void parseReview(String txt) {
        try {
            Object[] values = reviewFormat.parse(txt);
            reviewProduct(Integer.parseInt((String)values[0]), Rateable.convert(Integer.parseInt((String)values[1])), (String)values[2]);
        } catch (ParseException | NullPointerException e) {
            logger.log(Level.WARNING, "Error parsing review "+txt);
        }
    }
    public void parseProduct(String txt) {
        try {
            Object[] values = productFormat.parse(txt);
            int id = Integer.parseInt((String)values[1]);
            String name = (String) values[2];
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble((String)values[3]));
            Rating rating = Rateable.convert(Integer.parseInt((String)values[4]));
            switch ((String) values[0]) {
                case "D":
                    createProduct(id, name, price, rating);
                    break;
                case "F":
                    LocalDate bestBefore = LocalDate.parse((String) values[5]);
                    createProduct(id, name, price, rating, bestBefore);
            }
        } catch (ParseException | NullPointerException | DateTimeParseException e) {
            logger.log(Level.WARNING, "Error parsing product "+txt);
        }
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        Product product = new Food(id, name, price, rating, bestBefore);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
        Product product = new Drink(id, name, price, rating);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product reviewProduct(int id, Rating rating, String comments) {
        try {
            return reviewProduct(findProduct(id), rating, comments);
        } catch (ProductManagerException e) {
            logger.log(Level.INFO, e.getMessage());
        }
        return null;
    }

    public Product reviewProduct(Product product, Rating rating, String comments) {
        //v1
//        review = new Review(rating, comments);
//        this.product = product.applyRating(rating);

        //v2
//        if (reviews[reviews.length - 1] != null) {
//            reviews = Arrays.copyOf(reviews, reviews.length + 5);
//        }
//        int sum = 0, i = 0;
//        boolean reviewed = false;
//        while (i < reviews.length && !reviewed) {
//            if (reviews[i] == null) {
//                reviews[i] = new Review(rating, comments);
//                reviewed = true;
//            }
//            sum += reviews[i].getRating().ordinal();
//            i++;
//        }
//        this.product = product.applyRating(Rateable.convert(Math.round((float) sum/i)));

        List<Review> reviews = products.get(product);
        products.remove(product, reviews);

        reviews.add(new Review(rating, comments));
//        int sum = 0;
//        for (Review review: reviews)
//            sum += review.getRating().ordinal();
//
//        product = product.applyRating(Rateable.convert(Math.round((float)sum/reviews.size())));
        product = product.applyRating(
                Rateable.convert(
                        (int) Math.round(
                                reviews.stream()
                                        .mapToInt(r -> r.getRating().ordinal())
                                        .average()
                                        .orElse(0)
                        )
                )
        );
        products.put(product, reviews);
        return product;
    }

    public void printProducts(Predicate<Product> filter, Comparator<Product> sorter) {
//        List<Product> productList = new ArrayList<>(products.keySet());
//        productList.sort(sorter);

        StringBuilder sb = new StringBuilder();
//        for (Product product: productList) {
//            sb.append(formatter.formatProduct(product));
//            sb.append("\n");
//        }
        products.keySet()
                .stream()
                .sorted(sorter)
                .filter(filter)
                .forEach(p -> sb.append(formatter.formatProduct(p) + '\n'));
        System.out.println(sb);
    }

    public Product findProduct(int id) throws ProductManagerException {
        Product result = null;
//        for (Product product: products.keySet()) {
//            if (product.getId() == id) {
//                result = product;
//                break;
//            }
//        }
        result = products.keySet()
                .stream()
                .filter(p -> p.getId() == id)
                .findFirst().orElseThrow(() -> new ProductManagerException("Product with id "+id+" not found"));
                //.orElseGet(() -> null);
        return result;
    }

    public void printProductReport(int id) {
        try {
            printProductReport(findProduct(id));
        } catch (ProductManagerException e) {
            logger.log(Level.INFO, e.getMessage());
        }
    }

    public void printProductReport(Product product) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatter.formatProduct(product));
        sb.append("\n");

        List<Review> reviews = products.get(product);
        Collections.sort(reviews);

//        for (Review review: reviews) {
//            if (review == null) {
//                break;
//            }
//            sb.append(formatter.formatReview(review));
//            sb.append("\n");
//        }
        if (reviews.isEmpty()) {
            sb.append(formatter.getText("no.reviews") + '\n');
        } else {
         sb.append(reviews.stream()
                 .map(r -> formatter.formatReview(r) + "\n")
                 .collect(Collectors.joining())
         );
        }
        System.out.println(sb);
    }

    private static class ResourceFormatter {

        private ResourceBundle resources;
        private DateTimeFormatter dateFormat;
        private NumberFormat moneyFormat;

        private ResourceFormatter(Locale locale) {
            resources = ResourceBundle.getBundle("labs.pm.data.resources", locale);
            dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
            moneyFormat = NumberFormat.getCurrencyInstance(locale);
        }

        private String formatProduct(Product product) {
            return MessageFormat.format(
                    getText("product"),
                    product.getName(),
                    moneyFormat.format(product.getPrice()),
                    product.getRating().getStars(),
                    dateFormat.format(product.getBestBefore()));
        }

        private String formatReview(Review review) {
            return MessageFormat.format(getText("review"),
                    review.getRating().getStars(), review.getComments());
        }

        private String getText(String key) {
            return resources.getString(key);
        }
    }

    public Map<String, String> getDiscounts() {
        return products.keySet().stream()
                .collect(
                        Collectors.groupingBy(
                                product -> product.getRating().getStars(),
                                Collectors.collectingAndThen(
                                        Collectors.summingDouble(
                                                product -> product.getDiscount().doubleValue()),
                                        discount -> formatter.moneyFormat.format(discount)
                                )
                        )
                );
    }
}
