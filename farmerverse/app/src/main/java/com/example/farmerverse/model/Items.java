package com.example.farmerverse.model;

import java.util.ArrayList;

public class Items {

    String name;
    Double price;

    public Items(String _name, Double _price){
        name = _name;
        price = _price;
    }
    public static Items[] items = {
            new Items("Apple Pie", 6.99) ,
            new Items("Oranges", 1.10),
            new Items("Potato", 1.50),
            new Items("Watermelon", 5.99),
            new Items("Spaghetti Squash", 1.20),
            new Items("Green Beans", 0.2),
            new Items("Romaine Lettuce", 1.1),
            new Items("Corn", 3.4),
            new Items("Pepper", 1.56),
            new Items("Cucumber", 1.67),
            new Items("Pumpkin", 3.39),
            new Items("Tomatoes", 0.78),
    };

}
