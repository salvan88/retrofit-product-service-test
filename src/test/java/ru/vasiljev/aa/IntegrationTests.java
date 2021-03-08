package ru.vasiljev.aa;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.java4.lesson6.db.dao.CategoriesMapper;
import ru.geekbrains.java4.lesson6.db.dao.ProductsMapper;
import ru.vasiljev.aa.base.enums.CategoryType;
import ru.vasiljev.aa.dto.Product;
import ru.vasiljev.aa.service.ProductService;
import ru.vasiljev.aa.steps.CommonPostProduct;
import ru.vasiljev.aa.util.DbUtils;
import ru.vasiljev.aa.util.RetrofitUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTests {
    static ProductsMapper productsMapper;
    static ProductService productService;
    private Product product;
    private Integer productId;
    Faker faker = new Faker();
    
    @BeforeAll
    static void beforeAll() {
        productsMapper = DbUtils.getProductsMapper();
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
    }

    @Test
    @SneakyThrows
    void integrationEndToEndTest() {

        product = CommonPostProduct.getProduct(CategoryType.FOOD.getTitle());
        productId = product.getId();

        Response<Product> response = productService
                .getProduct(productId)
                .execute();

        assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(productId)).getId())
                .as("Id in GET step is not equal").isEqualTo(Long.valueOf(response.body().getId()));

        Response<Product> putResponse = productService.updateProduct(product
                .withTitle(faker.food().ingredient())
                .withPrice((int) faker.number().randomNumber()))
                .execute();


        productId = putResponse.body().getId();

        assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(productId)).getTitle())
                .as("Title after PUT step is not equal").isEqualTo(response.body().getTitle());
        assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(productId)).getPrice())
                .as("Price after PUT step is not equal").isEqualTo(response.body().getPrice());

        productsMapper.deleteByPrimaryKey(Long.valueOf(productId));

        Response<Product> delResponse = productService
                .getProduct(productId)
                .execute();
        assertThat(delResponse.code()).isEqualTo(404);

    }
}
