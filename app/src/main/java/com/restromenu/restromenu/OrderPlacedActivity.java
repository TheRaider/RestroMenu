package com.restromenu.restromenu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OrderPlacedActivity extends AppCompatActivity {

    Button btnPlaceNewOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);

        btnPlaceNewOrder = findViewById(R.id.btnPlaceNewOrder);

        btnPlaceNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(OrderPlacedActivity.this, QrScannerActivity.class);
                startActivity(newIntent);
            }
        });
    }
}
