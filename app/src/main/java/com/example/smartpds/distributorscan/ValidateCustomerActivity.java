package com.example.smartpds.distributorscan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpds.R;


public class ValidateCustomerActivity extends AppCompatActivity {

    Button verification_code_btn_verify;
    final static String DISTRIBUTER_MOBILE_NUMBER="distributer_mobile_number";
    private static final String USER_MOBILE_NUMBER = "user_mobile_number";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_customer);

        final String distributer = getIntent().getStringExtra(DISTRIBUTER_MOBILE_NUMBER);
        final String customer = getIntent().getStringExtra(USER_MOBILE_NUMBER);

        verification_code_btn_verify = findViewById(R.id.verification_code_btn_verify);
        verification_code_btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent customerDetailActivity=new Intent(getApplicationContext(), CustomerDetailActivity.class);
                customerDetailActivity.putExtra(DISTRIBUTER_MOBILE_NUMBER ,distributer);
                customerDetailActivity.putExtra(USER_MOBILE_NUMBER ,customer );
                startActivity(customerDetailActivity);
            }
        });
    }
}
