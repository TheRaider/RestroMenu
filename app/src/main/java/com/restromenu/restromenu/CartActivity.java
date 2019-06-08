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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.journeyapps.barcodescanner.Util;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ArrayList<HotelItem> hotelItemsOrdered = new ArrayList<>();
    RecyclerView rvHotelItemsOrdered;
    LinearLayout llCheckout,llCartEmpty,llOrderDetails;
    String hotelName = " ";
    Button btnExploreProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Intent intent = getIntent();
        hotelName = intent.getStringExtra(Utils.HOTEL_NAME);
        hotelItemsOrdered = intent.getParcelableArrayListExtra(Utils.HOTEL_ITEMS_ORDERED);


        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Cart");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Adapter
        HotelItemsAdapter hotelItemsAdapter = new HotelItemsAdapter(this);

        // Recycler View
        rvHotelItemsOrdered = findViewById(R.id.rvHotelItemsOrdered);
        rvHotelItemsOrdered.setLayoutManager(new LinearLayoutManager(this));
        rvHotelItemsOrdered.setAdapter(hotelItemsAdapter);

        // Checkout
        llCheckout = findViewById(R.id.llCheckout);
        llCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCartEmpty()){
                    Snackbar.make(findViewById(android.R.id.content), "Cart is Empty!!. Please add items", Snackbar.LENGTH_LONG).show();
                }else{
                    Intent newIntent = new Intent(CartActivity.this, PlaceOrderActivity.class);
                    newIntent.putExtra(Utils.HOTEL_NAME, hotelName);
                    int totalAmount = getBill();
                    int discount = totalAmount/20; // discount = 5%
                    int billAmount = totalAmount - discount;
                    newIntent.putExtra(Utils.TOTAL_AMOUNT, String.valueOf(totalAmount));
                    newIntent.putExtra(Utils.DISCOUNT, String.valueOf(discount));
                    newIntent.putExtra(Utils.BILL_AMOUNT, String.valueOf(billAmount));

                    startActivity(newIntent);
                }

            }
        });

        // If cart is empty
        llCartEmpty = findViewById(R.id.llCartEmpty);
        llOrderDetails = findViewById(R.id.llOrderDetails);
        if(hotelItemsOrdered.size() == 0){
            llCartEmpty.setVisibility(View.VISIBLE);
            llOrderDetails.setVisibility(View.GONE);
            llCheckout.setVisibility(View.GONE);
        }

        // Explore Products when cart is empty
        btnExploreProducts = findViewById(R.id.btnExploreProducts);
        btnExploreProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartActivity.this.onBackPressed();
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

    public boolean isCartEmpty(){
        int quantity = 0;
        for(HotelItem hotelItem : hotelItemsOrdered){
            if(hotelItem.getQuantity() > 0)
                quantity += hotelItem.getQuantity();
        }
        if(quantity>0) return false;
        else return true;
    }


    public int getBill(){
      int amount = 0;
      for(HotelItem hotelItem : hotelItemsOrdered){
          amount += (hotelItem.getProductPrice()*hotelItem.getQuantity());
      }
      return amount;
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
            final HotelItem  hotelItem = hotelItemsOrdered.get(holder.getAdapterPosition());
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
                    hotelItemsOrdered.set(holder.getAdapterPosition(), hotelItemClone);
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
                    hotelItemsOrdered.set(holder.getAdapterPosition(),hotelItemClone);
                    holder.tvProductQuantity.setText(String.valueOf(hotelItemClone.getQuantity()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return hotelItemsOrdered.size();
        }
    }
}
