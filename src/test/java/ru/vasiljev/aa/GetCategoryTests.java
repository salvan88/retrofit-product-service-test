package ru.vasiljev.aa;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.geekbrains.java4.lesson6.db.dao.CategoriesMapper;
import ru.vasiljev.aa.base.enums.CategoryType;
import ru.vasiljev.aa.dto.Category;
import ru.vasiljev.aa.service.CategoryService;
import ru.vasiljev.aa.util.NotExistedCategories;
import ru.vasiljev.aa.util.DbUtils;
import ru.vasiljev.aa.util.ErrorBody;
import ru.vasiljev.aa.util.RetrofitUtils;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GET category test case")
public class GetCategoryTests {
    static CategoryService categoryService;
    static CategoriesMapper categoriesMapper;

    @BeforeAll
    static void beforeAll() {
        categoriesMapper = DbUtils.getCategoriesMapper();
        categoryService = RetrofitUtils.getRetrofit()
                .create(CategoryService.class);
    }

    @Test
    @Step("Test")
    @Description("(+) GET existed category product(FOOD)(200)")
    void getFoodCategoryPositiveTest() throws IOException {
        Response<Category> response = categoryService
                .getCategory(CategoryType.FOOD.getId())
                .execute();

        assertThat(response.isSuccessful()).isTrue();
        assertThat(categoriesMapper.selectByPrimaryKey(1).getId())
                .as("Id is not equal to 1").isEqualTo(1);
        assertThat(categoriesMapper.selectByPrimaryKey(1).getTitle())
                .as("Title is not equal to Food")
                .isEqualTo(response.body().getTitle());
    }

    @Test
    @Step("Test")
    @Description("(-) GET not existed category product(404)")
    void getCategoryNegativeTest() throws IOException {
        int i = (int) NotExistedCategories.getNextCategoriesNumber() + 2;

        Response<Category> response = categoryService
                .getCategory(i)
                .execute();

        assertThat(response.code()).as("Wrong code type").isEqualTo(404);
        assertThat(ErrorBody.getErrorBody(response)
                .getMessage()).isEqualTo("Unable to find category with id: %d", i);
        assertThat(categoriesMapper.selectByPrimaryKey(i)).isNull();
    }

}
