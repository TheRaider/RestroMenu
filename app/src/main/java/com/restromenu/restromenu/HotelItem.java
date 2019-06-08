package com.restromenu.restromenu;

import android.os.Parcel;
import android.os.Parcelable;

public class HotelItem implements Parcelable{
    private String productId;
    private String name;
    private int quantity;
    private int productPrice;

    HotelItem() {
    }

    public HotelItem(String productId, String name, int quantity, int productPrice) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.productPrice = productPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    HotelItem(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<HotelItem> CREATOR = new Parcelable.Creator<HotelItem>() {
        public HotelItem createFromParcel(Parcel in) {
            return new HotelItem(in);
        }

        public HotelItem[] newArray(int size) {

            return new HotelItem[size];
        }

    };

    public void readFromParcel(Parcel in) {
        productId = in.readString();
        name = in.readString();
        quantity = in.readInt();
        productPrice = in.readInt();

    }
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(name);
        dest.writeInt(quantity);
        dest.writeInt(productPrice);
    }
}

