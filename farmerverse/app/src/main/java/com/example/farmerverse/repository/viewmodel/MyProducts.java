package com.example.farmerverse.repository.viewmodel;

import com.example.farmerverse.model.Product;
import com.example.farmerverse.model.ProductData;
import com.example.farmerverse.repository.IProductRepository;

import java.util.ArrayList;

public class MyProducts implements IProductRepository {

    final int defNumProducts = 10;
    ArrayList<Product> myProds;

    // Default Data
    public MyProducts(){
        this.myProds = ProductData.getData(defNumProducts);
    }

    // Load Data
    public MyProducts(ArrayList<Product> prods){
        this.myProds = prods;
    }

    @Override
    public ArrayList<Product> getAll() {
        return myProds;
    }

    @Override
    public Product getById(int id) {
        return myProds.get(id);
    }

    // If user tries to add existing product, increase its quantity to not allow duplicates
    @Override
    public void add(Product product) {
        for(Product p : myProds){
            if (p.getName().equals(product.getName())){
                p.setQuantity(p.getQuantity()+product.getQuantity());
                return;
            }
        }
        myProds.add(product);
    }

    @Override
    public void delete(int id) {
        myProds.remove(id);
    }
}
