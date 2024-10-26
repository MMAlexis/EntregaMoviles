package com.example.evaluaciont2.service;

import com.example.evaluaciont2.entity.Products;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {

    @GET("products")
    public abstract Call<List<Products>> listaProducts();
}
