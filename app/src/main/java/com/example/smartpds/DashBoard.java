package com.example.smartpds;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.smartpds.orderview.DisplayOrdersActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class DashBoard extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, NavigationView.OnNavigationItemSelectedListener {
    Menu menu;
    SliderLayout sliderLayout;
    HashMap<String, String> Hash_file_maps;
    CardView walletCard, shopCard, allOrders, manageBeneficiary;
    String mobile;
    String email, name, profilePic;
    private NotificationManagerCompat notificationManager;
    TextView customerName, customerEmail;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseKyc;
    private NavigationView mNavigationView;
    private ImageView customerProfilePic;
    private Long walletAmount;
    private static final String CUSTOMER_MOBILE_NUMBER = "customerMobileNumber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        drawerLayout = (DrawerLayout) findViewById(R.id.user_drawer_layout);
        navigationView = findViewById(R.id.user_nav_view);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (
                        this,
                        drawerLayout,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close
                ) {
        };
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        menu = navigationView.getMenu();
        menu.findItem(R.id.nav_logout).setVisible(false);
        menu.findItem(R.id.nav_profile).setVisible(false);

        /////////////////////////////////

        mNavigationView = (NavigationView) findViewById(R.id.user_nav_view);
        customerName = mNavigationView.getHeaderView(0).findViewById(R.id.customer_name);
        customerEmail = mNavigationView.getHeaderView(0).findViewById(R.id.customer_email);
        customerProfilePic = mNavigationView.getHeaderView(0).findViewById(R.id.customer_profile_pic);
        notificationManager = NotificationManagerCompat.from(this);
        mobile = getIntent().getStringExtra("mobile");
        final DrawerLayout drawer = findViewById(R.id.user_drawer_layout);
        mDatabase = FirebaseDatabase.getInstance().getReference("Customers").child(mobile);
        mDatabaseKyc = FirebaseDatabase.getInstance().getReference("CustomerKYC").child(mobile);
        ImageView menuIcon = (ImageView) findViewById(R.id.menu_icon);
        ImageView extraMenuIcon = (ImageView) findViewById(R.id.extra_menu_icon);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        extraMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                        sendOnChannel1(view);

            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                email = dataSnapshot.child("email").getValue(String.class);
                name = dataSnapshot.child("fname").getValue(String.class) + " " + dataSnapshot.child("lname").getValue(String.class);
                walletAmount = dataSnapshot.child("walletAmmount").getValue(long.class);

                customerEmail.setText(email);
                customerName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        mDatabaseKyc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                profilePic = dataSnapshot.child("profilepic").child("url").getValue(String.class);
                Picasso.with(DashBoard.this).load(profilePic).into(customerProfilePic);
//                Toast.makeText(DashBoard.this, "Profile Pic Loaded=== "+profilePic, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Hash_file_maps = new HashMap<String, String>();
        sliderLayout = (SliderLayout) findViewById(R.id.slider);
        Uri path1 = Uri.parse("android.resource://com.example.smartpds/" + R.drawable.aaa);
        Uri path2 = Uri.parse("android.resource://com.example.smartpds/" + R.drawable.aab);
        Uri path3 = Uri.parse("android.resource://com.example.smartpds/" + R.drawable.aac);
        Uri path4 = Uri.parse("android.resource://com.example.smartpds/" + R.drawable.aad);
        Uri path5 = Uri.parse("android.resource://com.example.smartpds/" + R.drawable.aae);
        Uri path6 = Uri.parse("android.resource://com.example.smartpds/" + R.drawable.aaf);
        String str1 = path1.toString();
        String str2 = path2.toString();
        String str3 = path3.toString();
        String str4 = path4.toString();
        String str5 = path5.toString();
        String str6 = path6.toString();
        Hash_file_maps.put("Shivkumar", str1);
        Hash_file_maps.put("Aditya", str2);
        Hash_file_maps.put("Vishal", str3);
        Hash_file_maps.put("Pooja", str4);
        Hash_file_maps.put("Vasanta", str5);
        Hash_file_maps.put("Raj", str6);


        for (String name : Hash_file_maps.keySet()) {

            TextSliderView textSliderView = new TextSliderView(DashBoard.this);
            textSliderView
                    .description(name)
                    .image(Hash_file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3000);
        sliderLayout.addOnPageChangeListener(this);


        walletCard = findViewById(R.id.bankCard);
        allOrders = findViewById(R.id.allorders);
        manageBeneficiary = findViewById(R.id.manage_beneficiary);
        shopCard = findViewById(R.id.shop_card);
        manageBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoard.this, BeneficiaryActivity.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
            }
        });

        allOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(DashBoard.this, "All Orders", Toast.LENGTH_SHORT).show();
                Intent showOrders = new Intent(getApplicationContext(), DisplayOrdersActivity.class);
                showOrders.putExtra(CUSTOMER_MOBILE_NUMBER, mobile);
                startActivity(showOrders);

            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashBoard.this, "Buy Clicked", Toast.LENGTH_LONG).show();
            }
        });
        shopCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoard.this, DistributorQRScanner.class);
                intent.putExtra("customermobile", mobile);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStop() {

        sliderLayout.stopAutoCycle();

        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        Toast.makeText(this, slider.getBundle().get("extra") + " " + "   " + Hash_file_maps.get("Shivkumar"), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.user_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.overflow_menu, menu);

        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action b ar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.logout:
                // Red item was selected
                Intent intent = new Intent(DashBoard.this, Logout.class);
                break;
            case R.id.exit:
                // Green item was selected
                return true;
            case R.id.nav_profile:
                Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_wallet:
                Toast.makeText(this, "Wallet Clicked", Toast.LENGTH_SHORT).show();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_wallet) {
            Toast.makeText(this, "Wallet Clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_govtschemes) {
            Toast.makeText(this, "Govt Schemes Clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_help) {
            Toast.makeText(this, "Help Clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_aboutus) {
            Toast.makeText(this, "AboutUs Clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_app_setting) {

        } else if (id == R.id.nav_notification_setting) {

        }

        DrawerLayout drawer = findViewById(R.id.user_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
