package com.restromenu.restromenu;

import java.util.ArrayList;

public class Order {
    private String hotelName;
    private ArrayList<HotelItem> hotelItemsOrdered;

    public Order() {
    }

    public Order(String hotelName, ArrayList<HotelItem> hotelItemsOrdered) {
        this.hotelName = hotelName;
        this.hotelItemsOrdered = hotelItemsOrdered;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public ArrayList<HotelItem> getHotelItemsOrdered() {
        return hotelItemsOrdered;
    }

    public void setHotelItemsOrdered(ArrayList<HotelItem> hotelItemsOrdered) {
        this.hotelItemsOrdered = hotelItemsOrdered;
    }
}
