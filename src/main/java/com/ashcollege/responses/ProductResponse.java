package com.ashcollege.responses;

import com.ashcollege.entities.Product;

import java.util.List;

public class ProductResponse extends BasicResponse{
    private List<Product> products;

    public ProductResponse(boolean success, Integer errorCode, List<Product> products) {
        super(success, errorCode);
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
