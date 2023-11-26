package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Category {
    public final List<String> category;

    public Category(List<String> categoryNames) {
        this.category = new ArrayList<>(categoryNames);
    }

    public List<String> getCategory() {
        return category;
    }
}
