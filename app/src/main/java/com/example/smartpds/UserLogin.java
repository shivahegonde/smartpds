package com.example.smartpds;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UserLogin extends AppCompatActivity {

    CardView customerCard, distributorCard;
    String mobile;
    SharedPreferences pref;
Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectusertype);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        if(pref.contains("username") && pref.contains("usertype")) {
            Toast.makeText(this, "Session Available", Toast.LENGTH_SHORT).show();
            String mobile = pref.getString("username", null);
            String type = pref.getString("usertype", null);
            if (type.equalsIgnoreCase("distributor")) {
                intent = new Intent(UserLogin.this, DistributorDashBoard.class);
                intent.putExtra("phonenumber", "+" +91+mobile);
                intent.putExtra("mobile", mobile);
                intent.putExtra("usertype",type);

                startActivity(intent);
            }
            else {
                intent = new Intent(UserLogin.this, DashBoard.class);
                intent.putExtra("phonenumber", "+" +91+mobile);
                intent.putExtra("mobile", mobile);
                intent.putExtra("usertype",type);

                startActivity(intent);

            }
        }
        customerCard = findViewById(R.id.customer);
        distributorCard = findViewById(R.id.distributor);
        customerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserLogin.this, "Customer", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserLogin.this,MainActivity.class);
                intent.putExtra("usertype","customer");

                startActivity(intent);


            }
        });
        distributorCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserLogin.this, "Distributor", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserLogin.this,MainActivity.class);
                intent.putExtra("usertype","distributor");


                startActivity(intent);
            }
        });

    }
}
