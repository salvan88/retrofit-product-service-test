package ru.vasiljev.aa.steps;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import ru.vasiljev.aa.base.enums.CategoryType;
import ru.vasiljev.aa.dto.Product;
import ru.vasiljev.aa.service.ProductService;
import ru.vasiljev.aa.util.RetrofitUtils;

public class CommonRandomProduct {
    private static Product product;
    private static ProductService productService;
    static Faker faker = new Faker();

    @SneakyThrows
    public static Product getRandomProduct(String title) {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);

        return product = new Product()
                .withCategoryTitle(title)
                .withPrice((int) (Math.random() * 1000 + 1))
                .withTitle(faker.food().ingredient());
    }
}
