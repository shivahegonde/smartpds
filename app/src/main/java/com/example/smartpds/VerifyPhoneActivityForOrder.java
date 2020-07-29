package com.example.smartpds;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpds.shop.DistributerShop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivityForOrder extends AppCompatActivity {


    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText editText;
    private Button signIn;
    TextView otpText;
    String phonenumber, userType, mobileNo, key,isDistributor;
    int totalAmount;
    int newPrice = 0;
    RatingBar shopRating;
    EditText review;
    DatabaseReference ratingReference,reviewReference;
    FirebaseDatabase db;

    FirebaseAuthSettings firebaseAuthSettings;
    private String distributorMobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        userType = getIntent().getStringExtra("usertype");
        key = getIntent().getStringExtra("key");
        mAuth = FirebaseAuth.getInstance();
        signIn = findViewById(R.id.buttonsignin);

        progressBar = findViewById(R.id.progressbar);
        db = FirebaseDatabase.getInstance();

        firebaseAuthSettings = mAuth.getFirebaseAuthSettings();
        editText = findViewById(R.id.edittextcode);
        otpText = findViewById(R.id.textview);
        otpText.setText("Enter OTP for your Order");
        userType = getIntent().getStringExtra("usertype");
        mobileNo = getIntent().getStringExtra("phonenumber");
        isDistributor = getIntent().getStringExtra("isDistributor");
        distributorMobileNo = getIntent().getStringExtra("distributormobile");
        totalAmount = Integer.parseInt(getIntent().getStringExtra("totalamount"));
        phonenumber = getIntent().getStringExtra("phonenumber");
//        sendVerificationCode("+91"+phonenumber);
        ratingReference = db.getReference("DistributorRatings/" + distributorMobileNo);
        reviewReference = db.getReference("DistributorReviews/" + distributorMobileNo);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String code = editText.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    editText.setError("Enter code...");
                    editText.requestFocus();
                    return;
                }
//                verifyCode(code);
                placeOrder(code);
//                Intent intent = new Intent(VerifyPhoneActivityForOrder.this, DashBoard.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.putExtra("mobile", phonenumber);
//                startActivity(intent);

            }
        });

    }

    private void placeOrder(String code) {

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Orders/" + mobileNo);
        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart/" + mobileNo);
        final DatabaseReference customerWallet = FirebaseDatabase.getInstance().getReference("Customers/" + mobileNo).child("walletAmmount");
        final DatabaseReference distributorWallet = FirebaseDatabase.getInstance().getReference("Distributors/" + distributorMobileNo).child("walletAmmount");

        // Exist! Do whatever.
//        rootRef.child(key).child("orderPlaced").setValue("yes");
//        Toast.makeText(VerifyPhoneActivityForOrder.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();


        customerWallet.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Exist! Do whatever.
                    int amount = snapshot.getValue(Integer.class);

                    newPrice = amount - totalAmount;

                    if(amount<totalAmount){
                        try {
                            showErrorDialog(VerifyPhoneActivityForOrder.this, "Transaction Error");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(VerifyPhoneActivityForOrder.this, "You Have insufficient Wallet Balance", Toast.LENGTH_SHORT).show();
//                        Intent intent=new Intent(getApplicationContext(),DashBoard.class);
//                        intent.putExtra("mobile",mobileNo);
//                        startActivity(intent);
                    }
                    else {
                        distributorWallet.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    // Exist! Do whatever.
                                    int amount = snapshot.getValue(int.class);
                                    newPrice = amount + totalAmount;
                                    Toast.makeText(VerifyPhoneActivityForOrder.this, "Distributor Wallet Updated Successfully", Toast.LENGTH_SHORT).show();
                                }
                                distributorWallet.setValue(newPrice);
                                rootRef.child(key).child("orderPlaced").setValue("yes");
                                cartRef.removeValue();

                                try {
                                    if (!isDistributor.equalsIgnoreCase("yes")) {
                                        showRatingDialog(VerifyPhoneActivityForOrder.this, "Rate This Distributor");
                                    }
                                    else
                                    {
                                        Intent intent = new Intent(VerifyPhoneActivityForOrder.this, DistributorDashBoard.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("mobile", distributorMobileNo);
                                        startActivity(intent);
                                    }
                                }catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed, how to handle?

                            }
                        });
                        Toast.makeText(VerifyPhoneActivityForOrder.this, "Transaction done", Toast.LENGTH_SHORT).show();
                        customerWallet.setValue(newPrice);

                    }
                }

            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed, how to handle?

            }
        });



    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {//for otp enable this

                            final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Orders/" + mobileNo);
                            final DatabaseReference customerWallet = FirebaseDatabase.getInstance().getReference("Customers/" + mobileNo).child("walletAmmount");
                            final DatabaseReference distributorWallet = FirebaseDatabase.getInstance().getReference("Orders/" + mobileNo).child("walletAmmount");
                            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        // Exist! Do whatever.
                                       Intent intent = new Intent(VerifyPhoneActivityForOrder.this, DashBoard.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("mobile", mobileNo);
                                        startActivity(intent);

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed, how to handle?

                                }
                            });

                            customerWallet.addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        // Exist! Do whatever.
                                        int amount = snapshot.getValue(int.class);


                                        if(amount<totalAmount){
                                            //DialogBox

                                            rootRef.child(key).child("orderPlaced").setValue("no");
                                            Toast.makeText(VerifyPhoneActivityForOrder.this, "Order cannot be placed", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(VerifyPhoneActivityForOrder.this, "You Have insufficient Wallet Balance", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(getApplicationContext(),DashBoard.class);
                                            intent.putExtra("mobile",mobileNo);
                                            startActivity(intent);
                                        }
                                        else{
                                            rootRef.child(key).child("orderPlaced").setValue("yes");
                                            int newPrice = amount - totalAmount;
                                            customerWallet.setValue(newPrice);
                                        Toast.makeText(VerifyPhoneActivityForOrder.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(VerifyPhoneActivityForOrder.this, DashBoard.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("mobile", mobileNo);
                                        startActivity(intent);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed, how to handle?

                                }
                            });

                            distributorWallet.addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        // Exist! Do whatever.
                                        int amount = snapshot.getValue(int.class);
                                        int newPrice = amount + totalAmount;
                                        distributorWallet.setValue(newPrice);
                                        Toast.makeText(VerifyPhoneActivityForOrder.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(VerifyPhoneActivityForOrder.this, DashBoard.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("mobile", mobileNo);
                                        startActivity(intent);

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed, how to handle?

                                }
                            });

                        }
                    }
                });


    }


    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                editText.setText(code);
//                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivityForOrder.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };


    public void showErrorDialog(Activity activity, String msg) throws IOException {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.insufficientwallet);
//        myImage.setImageBitmap(mIcon_val);
//            myImage.setImageBitmap(myBitmap);


            TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
            text.setTextSize(25);
            text.setText("Insufficient Wallet Amount");

        Button dialogButton1 = (Button) dialog.findViewById(R.id.btn1);
        Button dialogButton2 = (Button) dialog.findViewById(R.id.btn2);
        dialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent intent = new Intent(VerifyPhoneActivityForOrder.this, DashBoard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("mobile", mobileNo);
                startActivity(intent);
            }
        });
        dialog.show();

    }

    public void showRatingDialog(Activity activity, String msg) throws IOException {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.ratingdialog);
//        myImage.setImageBitmap(mIcon_val);
//            myImage.setImageBitmap(myBitmap);
        RatingBar shopRating= (RatingBar) dialog.findViewById(R.id.rating);
        EditText review=(EditText) dialog.findViewById(R.id.reviewtext);

        shopRating.setVisibility(View.VISIBLE);
        Button dialogButton1 = (Button) dialog.findViewById(R.id.btn1);
        Button dialogButton2 = (Button) dialog.findViewById(R.id.btn2);
        dialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rating = "" +shopRating.getRating();
                String reviewText=review.getText().toString();

                if (reviewText!="" && reviewText.length()!=2){
                    reviewReference.child(mobileNo).setValue(reviewText);
                    Toast.makeText(VerifyPhoneActivityForOrder.this, "Review Submitted Successfully", Toast.LENGTH_SHORT).show();
                }
                if (rating.length()!=0) {
                    ratingReference.child(mobileNo).setValue(rating);
                    Toast.makeText(VerifyPhoneActivityForOrder.this, "Rating Submitted Successfully", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
                Intent intent = new Intent(VerifyPhoneActivityForOrder.this, DashBoard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("mobile", mobileNo);
                startActivity(intent);
            }
        });
        dialog.show();

    }

}
