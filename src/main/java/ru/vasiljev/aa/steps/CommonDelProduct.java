package ru.vasiljev.aa.steps;

import lombok.SneakyThrows;
import ru.vasiljev.aa.service.ProductService;
import ru.vasiljev.aa.util.RetrofitUtils;

public class CommonDelProduct {

    static ProductService productService;

    @SneakyThrows
    public static void getTearDown(Integer productId) {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);

        if (productId != null) {
            productService
                    .deleteProduct(productId)
                    .execute();
        }
    }
}
