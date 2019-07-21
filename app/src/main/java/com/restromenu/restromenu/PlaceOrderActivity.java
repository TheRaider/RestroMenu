package com.restromenu.restromenu;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.restromenu.restromenu.models.HotelDesk;
import com.restromenu.restromenu.models.HotelItem;
import com.restromenu.restromenu.models.Order;

import java.util.ArrayList;
import java.util.UUID;

public class PlaceOrderActivity extends AppCompatActivity {

    TextView tvHotelName, tvTotalAmount, tvDiscount, tvBillAmount;
    Button btnPlaceOrder;

    ArrayList<HotelItem> hotelItemsOrdered = new ArrayList<>();
    String hotelName = " ";

    String billAmount = " ";

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Order");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Referencing Views
        tvHotelName = findViewById(R.id.tvHotelName);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvBillAmount = findViewById(R.id.tvBillAmount);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);


        Intent intent = getIntent();
        hotelName = intent.getStringExtra(Utils.HOTEL_NAME);
        hotelItemsOrdered = intent.getParcelableArrayListExtra(Utils.HOTEL_ITEMS_ORDERED);
        tvHotelName.setText(intent.getStringExtra(Utils.HOTEL_NAME));
        tvTotalAmount.setText(intent.getStringExtra(Utils.TOTAL_AMOUNT));
        tvDiscount.setText(intent.getStringExtra(Utils.DISCOUNT));
        tvBillAmount.setText(intent.getStringExtra(Utils.BILL_AMOUNT));

        billAmount = tvBillAmount.getText().toString();

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               placeOrder();
            }
        });

        // Getting Firebase Database Reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }




    public void placeOrder(){
        final String orderId = UUID.randomUUID().toString();
        final Order order = new Order(hotelName,hotelItemsOrdered);

        if(!isNetworkConnected()){
            Snackbar.make(findViewById(android.R.id.content), "Internet not available, Please Connect to Internet.", Snackbar.LENGTH_LONG).show();
            return;
        }

        mDatabase.child("Orders").child(orderId).setValue(order)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        HotelDesk hotelDesk = new HotelDesk(order, billAmount);
                        mDatabase.child("Hotel Desk").child(orderId).setValue(hotelDesk);
                        Intent newIntent = new Intent(PlaceOrderActivity.this, OrderPlacedActivity.class);
                        PlaceOrderActivity.this.finish();
                        startActivity(newIntent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(findViewById(android.R.id.content), "Server Error!! Unable to place order.", Snackbar.LENGTH_LONG).show();

                    }
                });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return true;
        }
    }

}
