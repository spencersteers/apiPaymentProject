package com.example.apipaymentapp.Content;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class Products {

    /**
     * An array of sample (dummy) items.
     */
    public static List<Product> PRODUCTS = new ArrayList<Product>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, Product> PRODUCTS_MAP = new HashMap<String, Product>();

    static {
        // Add 3 sample items.
        addProduct(new Product("1", "Chocolate", 18.64));
        addProduct(new Product("2", "Oil", 27.97));
        addProduct(new Product("3", "Eighth", 55.94));
        addProduct(new Product("4", "Quarter", 93.24));
    }

    private static void addProduct(Product item) {
        PRODUCTS.add(item);
        PRODUCTS_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Product {
        public String id;
        public String name;
        public double price;

        public Product(String id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        @Override
        public String toString() {
            DecimalFormat df = new DecimalFormat("#.00");
            return name + " (" + df.format(price) + ")";
        }
    }
}
