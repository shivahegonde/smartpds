package com.example.smartpds.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpds.R;
import com.example.smartpds.VerifyPhoneActivity;
import com.example.smartpds.VerifyPhoneActivityForOrder;
import com.example.smartpds.model.Order;
import com.example.smartpds.model.Product;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Random;
import java.util.UUID;


public class CartActivity extends AppCompatActivity {

    private RecyclerView mrecyclerView;
    private CartAdapter productItemAdapter;
    private TextView totalPrice;
    Button chekoutButton;
    private static final String TOTAL_PRICE = "total_price";
    private static final String USER_MOBILE_NUMBER = "user_mobile_number";
    int totalPriceOfCart = 0;
    public static String uniqueUserId = "8668283745";
    public static String userId, distributorId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        chekoutButton = findViewById(R.id.checkout);
        mrecyclerView = findViewById(R.id.cartListRecyclerView);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        totalPrice = findViewById(R.id.totalamountvalue);
        userId = getIntent().getStringExtra("customerMobile");
        distributorId = getIntent().getStringExtra("distributorMobile");


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        Query cartList = firebaseDatabase.getReference("Cart").child(userId);
        final FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(cartList, Product.class)
                .build();
        productItemAdapter = new CartAdapter(options);
        mrecyclerView.setAdapter(productItemAdapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Cart").child(userId);
        final DatabaseReference checkoutReference = FirebaseDatabase.getInstance().getReference("Orders").child(userId);
// use firebase auth userid instead of uniqueuserid
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String price = snapshot.child("price").getValue(String.class);
                    String qty = snapshot.child("quanity").getValue(String.class);
                    //    Product model = new Product(price , qty);
                    //   model.setPrice(price);
                    // model.setQuanity(qty);

                    if (price != null) {
                        int modelPricce = Integer.parseInt(price);
                        totalPriceOfCart += modelPricce;
                    }


                }
                String totalprice = String.valueOf(totalPriceOfCart);
                totalPrice.setText(totalprice);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        chekoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Order order=new Order("001",""+totalPriceOfCart,userId,distributorId,"no");
                String orderId = checkoutReference.push().getKey();
                String uniqueOrder = UUID.randomUUID().toString();
                checkoutReference.child(orderId).child("orderId").setValue(uniqueOrder);
                checkoutReference.child(orderId).child("totalAmount").setValue("" + totalPriceOfCart);
                checkoutReference.child(orderId).child("customer").setValue(userId);
                checkoutReference.child(orderId).child("distributor").setValue(distributorId);
                checkoutReference.child(orderId).child("timestamp").setValue(ServerValue.TIMESTAMP);
                checkoutReference.child(orderId).child("orderPlaced").setValue("no");
                Date date = new Date();
                Toast.makeText(CartActivity.this, "" + date.getTime(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CartActivity.this, VerifyPhoneActivityForOrder.class);
                intent.putExtra("phonenumber", userId);
                intent.putExtra("distributormobile", distributorId);
                intent.putExtra("key",orderId);
                intent.putExtra("totalamount", "" + totalPriceOfCart);
                startActivity(intent);
                Toast.makeText(CartActivity.this, "Items will be added into firebase " + distributorId, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (productItemAdapter != null)
            productItemAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        productItemAdapter.stopListening();
    }
}
