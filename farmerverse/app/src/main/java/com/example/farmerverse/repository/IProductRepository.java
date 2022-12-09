package com.example.farmerverse.repository;

import com.example.farmerverse.model.Product;

import java.util.ArrayList;

public interface IProductRepository {
    ArrayList<Product> getAll();
    Product getById(int id);
    void add(Product product);
    void delete(int id);
    Product getLast();
}
