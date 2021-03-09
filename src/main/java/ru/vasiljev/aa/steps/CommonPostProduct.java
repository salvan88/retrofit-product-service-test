package ru.vasiljev.aa.steps;

import lombok.SneakyThrows;
import ru.vasiljev.aa.dto.Product;
import ru.vasiljev.aa.service.ProductService;
import ru.vasiljev.aa.util.RetrofitUtils;

public class CommonPostProduct {
    private static Product product;
    private static ProductService productService;

    @SneakyThrows
    public static Product getProduct(String title) {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);

        product = CommonRandomProduct.getRandomProduct(title);

        retrofit2.Response<Product> response =
                productService.createProduct(product)
                        .execute();
        return response.body();
    }
}
