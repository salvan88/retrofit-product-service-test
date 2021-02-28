package ru.vasiljev.aa.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import ru.vasiljev.aa.dto.Product;

import java.util.List;

public interface ProductService {

    @GET("products")
    Call<List<Product>> getAllProducts();

    @POST("products")
    Call<Product> createProduct(@Body Product createProductRequest);

    @PUT("products")
    Call<Product> updateProduct(@Body Product updateProductRequest);

    @GET("products/{id}")
    Call<Product> getProduct(@Path("id") Integer id);

    @DELETE("products/{id}")
    Call<ResponseBody> deleteProduct(@Path("id") Integer id);

}
