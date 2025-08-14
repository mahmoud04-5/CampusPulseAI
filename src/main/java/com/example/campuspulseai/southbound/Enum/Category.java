package com.example.campuspulseai.southbound.Enum;

public enum Category {
    SPORTS("Sports"),
    ENTERTAINMENT("Entertainment"),
    EDUCATION("Education"),
    TECHNOLOGY("Technology"),
    HEALTH("Health"),
    ARTS("Arts"),
    RELIFION("Religion");

    private final String category;

    Category(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
