package ru.vasiljev.aa;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import retrofit2.Response;
import ru.geekbrains.java4.lesson6.db.dao.CategoriesMapper;
import ru.geekbrains.java4.lesson6.db.dao.ProductsMapper;
import ru.vasiljev.aa.base.enums.CategoryType;
import ru.vasiljev.aa.dto.Product;
import ru.vasiljev.aa.service.ProductService;
import ru.vasiljev.aa.steps.CommonPostProduct;
import ru.vasiljev.aa.steps.CommonRandomProduct;
import ru.vasiljev.aa.util.DbUtils;
import ru.vasiljev.aa.util.ErrorBody;
import ru.vasiljev.aa.util.RetrofitUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GET product test case")
public class GetProductsTests{

    private static ProductService productService;
    private Product product;
    private Integer productId;
    private Integer fakeId;
    static ProductsMapper productsMapper;
    static CategoriesMapper categoriesMapper;
    static Faker faker = new Faker();

    @BeforeAll
    static void beforeAll() {
        productsMapper = DbUtils.getProductsMapper();
        categoriesMapper = DbUtils.getCategoriesMapper();
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
    }

    @BeforeEach
    void setUp() {
        product = CommonPostProduct.getProduct(CategoryType.FOOD.getTitle());
        productId = product.getId();
        fakeId = (int) (Math.random() * 10000);
    }

    @SneakyThrows
    @Test
    @Step("Test")
    @Description("(+) GET list of all products(FOOD)(200)")
    void getProductsFoodPositiveTest() {

        retrofit2.Response<List<Product>> response = productService
                .getAllProducts()
                .execute();

        assertThat(response.code()).as("Wrong code type").isEqualTo(200);
    }

    @SneakyThrows
    @ParameterizedTest(name = "(+) Parameterized Test(200)")
    @EnumSource(CategoryType.class)
    void getProductPositiveTest(CategoryType categoryType) {

        product = CommonRandomProduct.getRandomProduct(categoryType.getTitle());

        Response<Product> response = productService
                .createProduct(product)
                .execute();

        productId = response.body().getId();

        response = productService
                .getProduct(productId)
                .execute();

        assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(productId)).getId())
                .as("Id is not equal")
                .isEqualTo(Long.valueOf(response.body().getId()));
        assertThat(categoriesMapper.selectByPrimaryKey(categoryType.getId()).getTitle())
                .as("CategoryTitle is not equal")
                .isEqualTo(product.getCategoryTitle());
        assertThat(response.code()).as("Wrong code type").isEqualTo(200);
    }

    @SneakyThrows
    @Test
    @Step("Test")
    @Description("(-) GET not existed product(FOOD)(404)")
    void getProductFoodNegativeTest() {
        retrofit2.Response<Product> response = productService
                .getProduct(fakeId)
                .execute();

        assertThat(ErrorBody.getErrorBody(response).getMessage())
                .as("Wrong error message")
                .isEqualTo("Unable to find product with id: %d", fakeId);
        assertThat(response.code()).as("Wrong code type").isEqualTo(404);
    }

    @SneakyThrows
    @AfterEach
    @Step("Tear down")
    @Description("Tear down")
    void tearDown() {

        if (productId != null) {
            productsMapper.deleteByPrimaryKey(Long.valueOf(productId));
            assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(productId))).isNull();
        }
    }
}