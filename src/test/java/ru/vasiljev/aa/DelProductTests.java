package ru.vasiljev.aa;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.*;
import ru.vasiljev.aa.base.enums.CategoryType;
import ru.vasiljev.aa.dto.Product;
import ru.vasiljev.aa.service.ProductService;
import ru.vasiljev.aa.steps.CommonDelProduct;
import ru.vasiljev.aa.steps.CommonPostProduct;
import ru.vasiljev.aa.util.RetrofitUtils;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DELETE product test case")
public class DelProductTests {
    static ProductService productService;
    private Integer productId = null;
    private Product product;
    private Integer fakeId;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
    }

    @BeforeEach
    void setUp() {
        product = CommonPostProduct.getProduct(CategoryType.FOOD.getTitle());
        fakeId = (int) (Math.random() * 10000);
    }

    @SneakyThrows
    @Test
    @Step("Тест")
    @Description("(+) Удаление существующего продукта(FOOD)(200)")
    void delProductFoodPositiveTest() {
        retrofit2.Response<ResponseBody> response = productService
                .deleteProduct(product.getId())
                .execute();

        assertThat(response.code()).as("Wrong code type").isEqualTo(200);
    }

    @SneakyThrows
    @Test
    @Step("Тест")
    @Description("(+) Удаление несуществующего продукта(FOOD)(204)")
    void delProductFoodNegativeTest() {
        retrofit2.Response<ResponseBody> response = productService
                .deleteProduct(fakeId)
                .execute();

        assertThat(response.code()).as("Wrong code type").isEqualTo(204);
    }

    @SneakyThrows
    @AfterEach
    @Step("Удаление мусора")
    @Description("Удаление материала")
    void tearDown() {
        CommonDelProduct.getTearDown(productId);
    }
}