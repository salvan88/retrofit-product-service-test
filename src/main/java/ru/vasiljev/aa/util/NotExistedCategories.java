package ru.vasiljev.aa.util;

import lombok.experimental.UtilityClass;
import ru.geekbrains.java4.lesson6.db.dao.CategoriesMapper;
import ru.geekbrains.java4.lesson6.db.model.CategoriesExample;

@UtilityClass
public class NotExistedCategories {

    CategoriesMapper categoriesMapper = DbUtils.getCategoriesMapper();

    public long getNextCategoriesNumber() {
        return categoriesMapper.countByExample(new CategoriesExample());
    }
}
