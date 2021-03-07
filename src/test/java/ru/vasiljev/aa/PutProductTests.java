package ru.vasiljev.aa;

import io.qameta.allure.Description;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import ru.geekbrains.java4.lesson6.db.dao.CategoriesMapper;
import ru.geekbrains.java4.lesson6.db.dao.ProductsMapper;
import ru.vasiljev.aa.base.enums.CategoryType;
import ru.vasiljev.aa.dto.Product;
import ru.vasiljev.aa.service.ProductService;
import ru.vasiljev.aa.steps.CommonPostProduct;
import ru.vasiljev.aa.util.DbUtils;
import ru.vasiljev.aa.util.ErrorBody;
import ru.vasiljev.aa.util.RetrofitUtils;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PUT product test case")
public class PutProductTests {

    static ProductService productService;
    private Product product;
    private Integer productId = null;
    private int randomNumber;
    static ProductsMapper productsMapper;
    static CategoriesMapper categoriesMapper;

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
        assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(productId)).getId())
                .as("Id is not equal")
                .isEqualTo(Long.valueOf(productId));
        assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(productId)).getPrice())
                .as("Price is not equal").isEqualTo(randomNumber);
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
        assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(productId)).getId())
                .as("Id is not equal").isEqualTo(Long.valueOf(productId));
        assertThat(categoriesMapper.selectByPrimaryKey(2).getTitle())
                .as("Title is not equal to Electronic").isEqualTo(product.getCategoryTitle());
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
