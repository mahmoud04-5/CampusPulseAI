package com.example.campuspulseai.southbound.Enum;

public enum Category {
    SPORTS("Sports"),
    ENTERTAINMENT("Entertainment"),
    EDUCATION("Education"),
    TECHNOLOGY("Technology"),
    HEALTH("Health"),
    ARTS("Arts"),
    RELIGION("Religion");

    private final String name;

    Category(String category) {
        this.name = category;
    }

    public String getCategory() {
        return name;
    }
}
