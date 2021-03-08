package ru.vasiljev.aa;

import io.qameta.allure.Description;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.geekbrains.java4.lesson6.db.dao.CategoriesMapper;
import ru.geekbrains.java4.lesson6.db.dao.ProductsMapper;
import ru.vasiljev.aa.base.enums.CategoryType;
import ru.vasiljev.aa.dto.Product;
import ru.vasiljev.aa.service.ProductService;
import ru.vasiljev.aa.steps.CommonPostProduct;
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
    @Description("(+) Получить список всех продуктов(FOOD)(200)")
    void getProductsFoodPositiveTest() {

        retrofit2.Response<List<Product>> response = productService
                .getAllProducts()
                .execute();

        assertThat(response.code()).as("Wrong code type").isEqualTo(200);
    }

    @SneakyThrows
    @Test
    @Description("(+) Получить конкретный продукт(Electronic)(200)")
    void getProductElectPositiveTest() {
        product = CommonPostProduct.getProduct(CategoryType.ELECTRONIC.getTitle());
        productId = product.getId();

        retrofit2.Response<Product> response = productService
                .getProduct(productId)
                .execute();

        assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(productId)).getId())
                .as("Id is not equal")
                .isEqualTo(Long.valueOf(response.body().getId()));
        assertThat(categoriesMapper.selectByPrimaryKey(2).getTitle())
                .as("CategoryTitle is not equal")
                .isEqualTo(product.getCategoryTitle());
        assertThat(response.code()).as("Wrong code type").isEqualTo(200);
    }


    @SneakyThrows
    @Test
    @Description("(+) Получить конкретный продукт(FOOD)(200)")
    void getProductFoodPositiveTest() {

        retrofit2.Response<Product> response = productService
                .getProduct(product.getId())
                .execute();

        assertThat(productsMapper.selectByPrimaryKey(Long.valueOf(product.getId())).getId())
                .as("Id is not equal")
                .isEqualTo(Long.valueOf(product.getId()));
        assertThat(categoriesMapper.selectByPrimaryKey(1).getTitle())
                .as("CategoryTitle is not equal")
                .isEqualTo(response.body().getCategoryTitle());
        assertThat(response.code()).as("Wrong code type").isEqualTo(200);
    }

    @SneakyThrows
    @Test
    @Description("(-) Получить несуществующий продукт(FOOD)(404)")
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
    @Description("Удаление материала")
    void tearDown() {
        if (productId != null) {
            productsMapper.deleteByPrimaryKey(Long.valueOf(productId));
        }
    }
}