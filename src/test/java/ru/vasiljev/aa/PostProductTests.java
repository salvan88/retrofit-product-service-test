package ru.vasiljev.aa;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.vasiljev.aa.base.enums.CategoryType;
import ru.vasiljev.aa.dto.Product;
import ru.vasiljev.aa.service.ProductService;
import ru.vasiljev.aa.steps.CommonDelProduct;
import ru.vasiljev.aa.util.ErrorBody;
import ru.vasiljev.aa.util.RetrofitUtils;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("POST product test case")
public class PostProductTests {
    private Integer productId;
    static ProductService productService;
    private Product product;
    Faker faker = new Faker();

    @BeforeAll
    static void beforeAll() {
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
    @Description("(+) Добавление нового продукта с Id = null(201)")
    void createProductFoodPositiveTest() {

        retrofit2.Response<Product> response = productService
                .createProduct(product)
                .execute();
        productId = response.body().getId();

        assertThat(response.body().getCategoryTitle())
                .as("Title is not equal to Food").isEqualTo(CategoryType.FOOD.getTitle());
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
                productService.createProduct(product.withCategoryTitle("word"))
                        .execute();

        assertThat(response.code()).as("Wrong code type").isEqualTo(400);
    }

    @SneakyThrows
    @AfterEach
    @Description("Удаление материала")
    void tearDown() {
        CommonDelProduct.getTearDown(productId);
    }
}
