package ru.vasiljev.aa.base.enums;

import lombok.Getter;

public enum CategoryType {
    FOOD(1, "Food"),
    ELECTRONIC(2, "Electronic");
    @Getter
    private final int id;
    @Getter
    private final String title;

    CategoryType(int id, String title) {
        this.id = id;
        this.title = title;
    }
}
