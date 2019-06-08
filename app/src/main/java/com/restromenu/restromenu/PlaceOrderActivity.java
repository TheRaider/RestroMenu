package com.restromenu.restromenu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlaceOrderActivity extends AppCompatActivity {

    TextView tvHotelName, tvTotalAmount, tvDiscount, tvBillAmount;
    Button btnPlaceOrder;

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
        tvHotelName.setText(intent.getStringExtra(Utils.HOTEL_NAME));
        tvTotalAmount.setText(intent.getStringExtra(Utils.TOTAL_AMOUNT));
        tvDiscount.setText(intent.getStringExtra(Utils.DISCOUNT));
        tvBillAmount.setText(intent.getStringExtra(Utils.BILL_AMOUNT));

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(PlaceOrderActivity.this, OrderPlacedActivity.class);
                PlaceOrderActivity.this.finish();
                startActivity(newIntent);
            }
        });
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
