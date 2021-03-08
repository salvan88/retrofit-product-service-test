package ru.vasiljev.aa;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.geekbrains.java4.lesson6.db.dao.CategoriesMapper;
import ru.geekbrains.java4.lesson6.db.dao.ProductsMapper;
import ru.vasiljev.aa.base.enums.CategoryType;
import ru.vasiljev.aa.dto.Product;
import ru.vasiljev.aa.service.ProductService;
import ru.vasiljev.aa.util.DbUtils;
import ru.vasiljev.aa.util.ErrorBody;
import ru.vasiljev.aa.util.RetrofitUtils;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("POST product test case")
public class PostProductTests {
    private Integer productId = null;
    static ProductService productService;
    private Product product;
    static ProductsMapper productsMapper;
    static CategoriesMapper categoriesMapper;
    Faker faker = new Faker();

    @BeforeAll
    static void beforeAll() {
        productsMapper = DbUtils.getProductsMapper();
        categoriesMapper = DbUtils.getCategoriesMapper();
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withCategoryTitle(CategoryType.FOOD.getTitle())
                .withPrice((int) (Math.random() * 1000 + 1))
                .withTitle(faker.food().ingredient());
    }

    @SneakyThrows
    @Test
    @Description("(+) Добавление нового продукта с Id = null(FOOD)(201)")
    void createProductFoodPositiveTest() {

        retrofit2.Response<Product> response = productService
                .createProduct(product)
                .execute();

        assertThat(categoriesMapper.selectByPrimaryKey(1).getTitle())
                .as("Title is not equal to Food").isEqualTo(product.getCategoryTitle());
        assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(response.body().getId())).getId())
                .as("Id is not equal").isEqualTo(Long.valueOf(response.body().getId()));
        assertThat(response.code()).as("Wrong code type").isEqualTo(201);
    }

    @SneakyThrows
    @Test
    @Description("(-) Добавление нового продукта с указанием Id(400)")
    void createProductIdNullNegativeTest() {
        retrofit2.Response<Product> response = productService
                .createProduct(product.withId(1))
                .execute();

        assertThat(ErrorBody.getErrorBody(response).getMessage())
                .as("Wrong error message").isEqualTo("Id must be null for new entity");
        assertThat(response.code()).as("Wrong code type").isEqualTo(400);
    }

    @SneakyThrows
    @Test
    @Description("(-) Добавление нового продукта с указанием несуществующей categoryTitle(400)")
    void createProductCategoryTitleIntNegativeTest() {
        retrofit2.Response<Product> response =
                productService.createProduct(product.withCategoryTitle(faker.pokemon().name()))
                        .execute();

        assertThat(response.code()).as("Wrong code type").isEqualTo(400);
    }

    @SneakyThrows
    @AfterEach
    @Description("Удаление материала")
    void tearDown() {
        if (productId != null) {
            productsMapper.deleteByPrimaryKey(Long.valueOf(productId));
        }
    }
}
