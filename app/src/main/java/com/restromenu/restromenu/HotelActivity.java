package com.restromenu.restromenu;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HotelActivity extends AppCompatActivity {

    String hotelName = " ";
    String hotelItemsJsonString = "";
    ArrayList<HotelItem> hotelItems = new ArrayList<>();
    LinearLayout llGoToCart, llInvalidQrCode;
    RecyclerView rvHotelItems;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        Intent intent = getIntent();
        hotelItemsJsonString = intent.getStringExtra(Utils.HOTEL_ITEMS_JSON_STRING);

        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        llInvalidQrCode = findViewById(R.id.llInvalidQrCode);
        llGoToCart = findViewById(R.id.llGoToCart);
        rvHotelItems = findViewById(R.id.rvHotelItems);

        // Intialising the list of hotel items
        loadHotelItems(hotelItemsJsonString);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(hotelName);
           // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        // Go to Cart
        llGoToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotelActivity.this,CartActivity.class);
                intent.putExtra(Utils.HOTEL_NAME, hotelName);
                intent.putParcelableArrayListExtra(Utils.HOTEL_ITEMS_ORDERED, getHotelItemsOrdered(hotelItems));
                startActivity(intent);
            }
        });


        // Adapter
        HotelItemsAdapter hotelItemsAdapter = new HotelItemsAdapter(this);

        // Recycler View
        rvHotelItems.setLayoutManager(new LinearLayoutManager(this));
        rvHotelItems.setAdapter(hotelItemsAdapter);

        //Invalid QR Code
        if(hotelItems.size() == 0){
            rvHotelItems.setVisibility(View.GONE);
            llGoToCart.setVisibility(View.GONE);
            toolbar.setVisibility(View.GONE);
        }

    }

    public ArrayList<HotelItem> getHotelItemsOrdered(ArrayList<HotelItem> hotelItemsList) {
        ArrayList<HotelItem> hotelItemsOrdered = new ArrayList<>();
        for(HotelItem hotelItem : hotelItemsList){
            if(hotelItem.getQuantity() > 0)
                hotelItemsOrdered.add(hotelItem);
        }
        return hotelItemsOrdered;
    }


    public ArrayList<HotelItem> loadHotelItems(String jsonString) {
        try {
            //converting the data to json
            JSONObject obj = new JSONObject(jsonString);
            hotelName = obj.getString("HotelName");
            JSONArray hotelItemsJson = obj.getJSONArray("HotelItems");
            for(int i=0; i<hotelItemsJson.length(); i++){
                JSONObject jsonObject = hotelItemsJson.getJSONObject(i);
                HotelItem hotelItem = new HotelItem();
                hotelItem.setProductId(jsonObject.getString("productId"));
                hotelItem.setName(jsonObject.getString("name"));
                int productPrice;
                try {
                    productPrice = Integer.parseInt(jsonObject.getString("productPrice"));
                }catch (NumberFormatException e){
                    productPrice = 100;
                }
                hotelItem.setProductPrice(productPrice);
                hotelItems.add(hotelItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            //if control comes here
            //that means the encoded format not matches
            llInvalidQrCode.setVisibility(View.VISIBLE);

        }

        return hotelItems;
    }

    class HotelItemsAdapter extends RecyclerView.Adapter<HotelItemsAdapter.HotelItemViewHolder>{

        Context mContext;

        public HotelItemsAdapter(Context mContext) {
            this.mContext = mContext;
        }

        class HotelItemViewHolder extends RecyclerView.ViewHolder {

            TextView tvProductName, tvProductPrice, tvProductQuantity;
            Button btnMinus, btnPlus;

            HotelItemViewHolder(View view) {
                super(view);
                tvProductName = (TextView) view.findViewById(R.id.tvProductName);
                tvProductPrice = (TextView) view.findViewById(R.id.tvProductPrice);
                tvProductQuantity = (TextView) view.findViewById(R.id.tvProductQuantity);
                btnMinus = (Button) view.findViewById(R.id.btnMinus);
                btnPlus = (Button) view.findViewById(R.id.btnPlus);
            }
        }

        @Override
        public HotelItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_hotel_item, parent, false);

            return new HotelItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final HotelItemViewHolder holder, int position) {
            // Loading data to view holders
            final HotelItem  hotelItem = hotelItems.get(holder.getAdapterPosition());
            holder.tvProductName.setText(hotelItem.getName());
            holder.tvProductPrice.setText(String.valueOf(hotelItem.getProductPrice()));
            holder.tvProductQuantity.setText(String.valueOf(hotelItem.getQuantity()));

            holder.btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = hotelItem.getQuantity();
                    if (quantity > 0) quantity--;
                    HotelItem hotelItemClone = hotelItem;
                    hotelItemClone.setQuantity(quantity);
                    hotelItems.set(holder.getAdapterPosition(), hotelItemClone);
                    holder.tvProductQuantity.setText(String.valueOf(hotelItemClone.getQuantity()));
                }
            });

            holder.btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = hotelItem.getQuantity();
                    quantity++;
                    HotelItem hotelItemClone = hotelItem;
                    hotelItemClone.setQuantity(quantity);
                    hotelItems.set(holder.getAdapterPosition(),hotelItemClone);
                    holder.tvProductQuantity.setText(String.valueOf(hotelItemClone.getQuantity()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return hotelItems.size();
        }
    }

}

