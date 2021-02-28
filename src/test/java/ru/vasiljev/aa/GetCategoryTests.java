package ru.vasiljev.aa;

import io.qameta.allure.Description;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.vasiljev.aa.base.enums.CategoryType;
import ru.vasiljev.aa.dto.Category;
import ru.vasiljev.aa.service.CategoryService;
import ru.vasiljev.aa.util.ErrorBody;
import ru.vasiljev.aa.util.RetrofitUtils;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("GET category test case")
public class GetCategoryTests {
    static CategoryService categoryService;

    @BeforeAll
    static void beforeAll() {
        categoryService = RetrofitUtils.getRetrofit()
                .create(CategoryService.class);
    }

    @Test
    @Description("(+) Получить категорию продуктов(FOOD)(200)")
    void getFoodCategoryPositiveTest() throws IOException {
        Response<Category> response = categoryService
                .getCategory(CategoryType.FOOD.getId())
                .execute();
        assertThat(response.isSuccessful()).isTrue();
        assertThat(response.body().getId()).as("Id is not equal to 1").isEqualTo(1);
        assertThat(response.body().getTitle()).as("Title is not equal to Food").isEqualTo(CategoryType.FOOD.getTitle());
    }

    @Test
    @Description("(-) Получить несуществующую категорию(404)")
    void getCategoryNegativeTest() throws IOException {
        Response<Category> response = categoryService
                .getCategory(3)
                .execute();

        assertThat(response.code()).as("Wrong code type").isEqualTo(404);
        assertThat(ErrorBody.getErrorBody(response)
                .getMessage()).isEqualTo("Unable to find category with id: 3");
    }

}
