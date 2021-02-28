package ru.vasiljev.aa;

import io.qameta.allure.Description;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import ru.vasiljev.aa.base.enums.CategoryType;
import ru.vasiljev.aa.dto.Product;
import ru.vasiljev.aa.service.ProductService;
import ru.vasiljev.aa.steps.CommonDelProduct;
import ru.vasiljev.aa.steps.CommonPostProduct;
import ru.vasiljev.aa.util.ErrorBody;
import ru.vasiljev.aa.util.RetrofitUtils;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PUT product test case")
public class PutProductTests {

    static ProductService productService;
    private Product product;
    private Integer productId = null;
    private int randomNumber;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
    }

    @BeforeEach
    void setUp() {
        product = CommonPostProduct.getProduct(CategoryType.FOOD.getTitle());
        randomNumber = (int) (Math.random() * 100000 + 1);
    }

    @SneakyThrows
    @Test
    @Description("(+) Изменить price существующего продукта(FOOD)(200)")
    void putProductChangePrisePositiveTest() {
        Response<Product> response =
                productService.updateProduct(product.withPrice(randomNumber))
                        .execute();

        assertThat(response.code()).as("Wrong code type").isEqualTo(200);
        assertThat(response.body().getId()).as("Id is not equal").isEqualTo(product.getId());
        assertThat(response.body().getPrice()).as("Price is not equal").isEqualTo(randomNumber);
    }

    @SneakyThrows
    @Test
    @Description("(-) Изменить price несуществующего продукта(400)")
    void putProductChangeWrongIdNegativeTest() {
        Response<Product> response =
                productService.updateProduct(product.withId(randomNumber))
                        .execute();

        assertThat(response.code()).as("Wrong code type").isEqualTo(400);
        assertThat(ErrorBody.getErrorBody(response).getMessage()).as("Wrong error message")
                .isEqualTo("Product with id: %d doesn't exist", randomNumber);
    }

    @SneakyThrows
    @Test
    @Description("(+) Изменить title существующего продукта(ELECTRONIC)(200)")
    void putProductChangeTitlePositiveTest() {
        product = CommonPostProduct.getProduct(CategoryType.ELECTRONIC.getTitle());
        productId = product.getId();

        Response<Product> response =
                productService.updateProduct(product.withTitle("Computer"))
                        .execute();

        assertThat(response.code()).as("Wrong code type").isEqualTo(200);
        assertThat(response.body().getId()).as("Id is not equal").isEqualTo(product.getId());

    }

    @SneakyThrows
    @AfterEach
    @Description("Удаление материала")
    void tearDown() {
        CommonDelProduct.getTearDown(productId);
    }
}
