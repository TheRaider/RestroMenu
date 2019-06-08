package com.restromenu.restromenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

//implementing onclicklistener
public class QrScannerActivity extends AppCompatActivity implements View.OnClickListener {

    //View Objects
    private Button btnScan;
    //qr code scanner object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        //View objects
        btnScan = (Button) findViewById(R.id.btnScan);

        //intializing scan object
        qrScan = new IntentIntegrator(this);

        //attaching onclick listener
        btnScan.setOnClickListener(this);
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                String hotelItemsJsonString = result.getContents();
                Intent intent = new Intent(this,HotelActivity.class);
                intent.putExtra(Utils.HOTEL_ITEMS_JSON_STRING, hotelItemsJsonString);
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onClick(View view) {
        //initiating the qr code scan
        qrScan.setOrientationLocked(false);
        qrScan.initiateScan();
    }
}
