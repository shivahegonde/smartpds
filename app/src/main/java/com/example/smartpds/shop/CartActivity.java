package com.example.smartpds.shop;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpds.R;
import com.example.smartpds.adapter.CartAdapter;
import com.example.smartpds.model.Product;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {

    private RecyclerView mrecyclerView;
    private CartAdapter productItemAdapter;
  private TextView totalPrice;
  String mobileno;
    private static final String TOTAL_PRICE = "total_price";
     int totalPriceOfCart=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mrecyclerView=findViewById(R.id.cartListRecyclerView);
        mrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        totalPrice =findViewById(R.id.totalamountvalue);
        mobileno=getIntent().getStringExtra("customermobile");

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        Query cartList =firebaseDatabase.getReference("Cart").child(mobileno);
        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(cartList, Product.class)
                .build();
        productItemAdapter = new CartAdapter(options);
        mrecyclerView.setAdapter(productItemAdapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Cart").child(mobileno);
// use firebase auth userid instead of uniqueuserid
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Product model = snapshot.getValue(Product.class);
                    int modelPricce = Integer.parseInt(model.getPrice());
                    totalPriceOfCart+=modelPricce;
                }
                String totalprice = String.valueOf(totalPriceOfCart);
                totalPrice.setText(totalprice);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        }

    @Override
    protected void onStart() {
        super.onStart();
        if (productItemAdapter!=null)
            productItemAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        productItemAdapter.stopListening();
    }
}
