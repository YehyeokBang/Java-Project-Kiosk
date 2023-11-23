package org.example.model;

public enum Category {
    CATEGORY_1("커피"),
    CATEGORY_2("티");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
