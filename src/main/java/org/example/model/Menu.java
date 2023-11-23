package org.example.model;

import java.awt.*;

public class Menu {
    private String name;
    private int price;
    private String description;
    private String category;
    private Image image;

    public Menu(String name, int price, String description, String category, Image image) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }
}
