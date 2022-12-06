package com.example.farmerverse.model;

import java.util.ArrayList;
import java.util.Random;

public class ProductData {

    static final Double baseMultiplier = 9.7;
    static final Random rand = new Random();

    public static ArrayList<Product> getData(Integer rows){
        ArrayList<Product> prods = new ArrayList<>();


        Items[] items = Items.items;
        Double randPrice = rand.nextDouble() * baseMultiplier;

        for(int i=0; i < rows; i++){
            Items item = items[rand.nextInt(items.length)];
            if(isUnique(item.name, prods))
            prods.add(new Product(i, item.name, rand.nextInt(200), item.price));
        }

        return prods;
    }

    // Not allow duplicates
    public static boolean isUnique(String name, ArrayList<Product> prods){
        for (Product p : prods){
            if (name.equals(p.name))
                return false;
        }
        return true;
    }

}
