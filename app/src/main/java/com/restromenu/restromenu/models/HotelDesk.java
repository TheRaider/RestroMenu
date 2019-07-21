package com.restromenu.restromenu.models;

public class HotelDesk {
    Order order;
    String amount;

    public HotelDesk(Order order, String amount) {
        this.order = order;
        this.amount = amount;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
