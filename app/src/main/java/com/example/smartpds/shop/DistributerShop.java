package com.example.smartpds.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpds.R;
import com.example.smartpds.adapter.ProductItemAdapter;
import com.example.smartpds.model.Distributer;
import com.example.smartpds.model.Product;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class DistributerShop extends AppCompatActivity {

    private static final String TOTAL_PRICE = "total_price";
    private RecyclerView mrecyclerView;
   // private ShopItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FirebaseDatabase db;
    DatabaseReference documentReference,ratingReference;
    final static String DISTRIBUTER_MOBILE_NUMBER="distributer_mobile_number";
    String ShopId;
    TextView distributerName,shopLocation,shopPinCode,shopContact,shopName,quantity,price;
    Button proceedtocart;
    ImageView shopImage;
    String  strdistributerName;
    String strshopLocation;
    String strshopPinCode;
    String strshopContact;
    String customerMobile;
    ProductItemAdapter productItemAdapter;
    RatingBar distributorRatingBar ;
    Button submitDistributorRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributer_shop);
        Intent intent = getIntent();
        customerMobile=intent.getStringExtra("customermobile");
        ShopId = intent.getStringExtra("mobileno");
        distributorRatingBar= (RatingBar) findViewById(R.id.shoprating);
        distributerName=findViewById(R.id.Distributername);
        submitDistributorRating=findViewById(R.id.submit_rating);
        shopLocation=findViewById(R.id.Location);
        shopContact=findViewById(R.id.shopcontact);
        quantity=findViewById(R.id.cartitemQuantity);
        price=findViewById(R.id.txtproductcost);
        shopImage=findViewById(R.id.shop_image);
        shopPinCode=findViewById(R.id.Pincode);
        shopName=findViewById(R.id.shopName);
        proceedtocart=findViewById(R.id.proceedtocart);


        db = FirebaseDatabase.getInstance();

        documentReference = db.getReference("Distributors/" + ShopId);
        ratingReference = db.getReference("DistributorRatings/" + ShopId);

        documentReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    Distributer shop = dataSnapshot.getValue(Distributer.class);

//                    strdistributerName=shop.getFname()+
//                            shop.getLname();
                    strshopLocation=shop.getAddress();
                    strshopPinCode= String.valueOf(shop.getPincode());
                    strshopContact= String.valueOf(shop.getMobile());
                    distributerName.setText(strdistributerName);
                    shopLocation.setText(strshopLocation);
                    shopContact.setText( strshopContact);
                    shopPinCode.setText(strshopPinCode);
                    distributerName.setText(shop.getShopname());

                    Picasso.with(DistributerShop.this).load(shop.getShopImage()).into(shopImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ratingReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //                    Picasso.with(DistributerShop.this).load(shop.getShopImage()).into(shopImage);
                    try {
                        if (dataSnapshot.exists()) {
                            distributorRatingBar.setRating(Float.parseFloat(dataSnapshot.child("9405462511").getValue(String.class)));
                        }
                        else {
                            Toast.makeText(DistributerShop.this, "Rating not Found Please Rate", Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        submitDistributorRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rating = "" +distributorRatingBar.getRating();
                ratingReference.child("9405462511").setValue(rating);
                Toast.makeText(DistributerShop.this, "Rating Submitted Successfully", Toast.LENGTH_SHORT).show();
            }
        });
//        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
//        Query DistributerList =firebaseDatabase.getReference("DistributorsProducts").child(getInte);
//        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
//                .setQuery(DistributerList, Product.class)
//                .build();
//

        mrecyclerView=findViewById(R.id.productsListRecyclerView);
        mrecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        FirebaseApp.initializeApp(this);
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

        Query DistributerList =firebaseDatabase.getReference("DistributorsProducts").child(ShopId);

        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(DistributerList, Product.class)
                .build();
        productItemAdapter = new ProductItemAdapter(options,this,customerMobile,ShopId);
        mrecyclerView.setAdapter(productItemAdapter);


        initlistner();  //productAdapter  Listners


        proceedtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DatabaseReference dref=db.getReference("Cart").child(customerMobile).child("rava").child("quantity").setValue(quantity.getText().toString());
//                DatabaseReference dref2=db.getReference("Cart").child(customerMobile).child("rava").child("price").setValue(price.getText().toString())
//                db.getReference("Cart").child(getIntent().getStringExtra("customermobile")).child("wheat").child("quantity").setValue("10");
//                db.getReference("Cart").child(getIntent().getStringExtra("customermobile")).child("wheat").child("price").setValue("5");
                Intent intent = new Intent(DistributerShop.this, CartActivity.class);
                        intent.putExtra("mobileno" , ShopId);
                        intent.putExtra("customermobile" , getIntent().getStringExtra("customermobile"));
                startActivity(intent);
            }

        });

    }

    private void initlistner() {
        Toast.makeText(this, "Hello Shivkumar mob no "+shopContact.getText().toString().trim(), Toast.LENGTH_SHORT).show();
        documentReference = db.getReference("Cart").child(shopContact.getText().toString().trim());

        productItemAdapter.setOnItemClickListener(new ProductItemAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Toast.makeText(getApplicationContext() , FirebaseAuth.getInstance().getUid(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void addQuantityClick(Product product, String productName, TextView v) {
                int newQuantity1 = Integer.parseInt(product.getCartUserQuntity())+1;
                String newQuantity = String.valueOf(newQuantity1);
                product.setCartUserQuntity(newQuantity);
                v.setText(newQuantity);
                int price = Integer.parseInt(product.getCartPriceQuantity()) +  Integer.parseInt(product.getPrice());
                String newPrice = String.valueOf(price);
                documentReference.child(productName).child("productimage").setValue("https://meetthetaste.files.wordpress.com/2019/02/img-20190205-wa00072035697822075996542.jpg");
                 documentReference.child(productName).child("quanity").setValue(newQuantity);
                documentReference.child(productName).child("price").setValue(newPrice);

            }

            @Override
            public void removeQuantityClick(Product product , String productName  , TextView v) {

                int newQuantity1 = Integer.parseInt(product.getCartUserQuntity())-1;
                String newQuantity = String.valueOf(newQuantity1);
                product.setCartUserQuntity(newQuantity);
                v.setText(newQuantity);
                int price = Integer.parseInt(product.getCartPriceQuantity()) - Integer.parseInt(product.getPrice());
                String newPrice = String.valueOf(price);
                documentReference.child(productName).child("quanity").setValue(newQuantity);
                documentReference.child(productName).child("price").setValue(newPrice);
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
