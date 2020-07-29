package com.example.smartpds;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UserLogin extends AppCompatActivity {

    CardView customerCard, distributorCard;
    String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectusertype);

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
