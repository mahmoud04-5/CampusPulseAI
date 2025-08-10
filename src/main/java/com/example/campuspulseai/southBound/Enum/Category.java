package com.example.campuspulseai.southBound.Enum;

public enum Category {
    Sports("Sports"),
    Entertainment("Entertainment"),
    Education("Education"),
    Technology("Technology"),
    Health("Health"),
    Arts("Arts"),
    Religion("Religion");

    private final String category;

    Category(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
