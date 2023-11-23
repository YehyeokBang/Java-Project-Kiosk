package org.example.model;

import java.util.HashMap;
import java.util.Map;

public class OrderManager {
    private Map<Menu, Integer> orders;

    public OrderManager() {
        orders = new HashMap<>();
    }

    public Map<Menu, Integer> getOrders() {
        return orders;
    }

    public void addOrder(Menu menu, int quantity) {
        if (orders.containsKey(menu)) {
            orders.put(menu, orders.get(menu) + quantity);
        } else {
            orders.put(menu, quantity);
        }
    }

    public void removeOrder(Menu menu) {
        orders.remove(menu);
    }

    public int calculateTotalPrice() {
        int totalPrice = 0;
        for (Menu menu : orders.keySet()) {
            int quantity = orders.get(menu);
            totalPrice += menu.getPrice() * quantity;
        }
        return totalPrice;
    }
}
