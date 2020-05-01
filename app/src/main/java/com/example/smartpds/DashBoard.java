package com.example.smartpds;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

import static com.example.smartpds.App.CHANNEL_1_ID;

public class DashBoard extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, NavigationView.OnNavigationItemSelectedListener {

    SliderLayout sliderLayout;
    HashMap<String,String> Hash_file_maps ;
    CardView walletCard,shopCard;
    private NotificationManagerCompat notificationManager;
    private EditText editTextTitle;
    private EditText editTextMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        notificationManager = NotificationManagerCompat.from(this);
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextMessage = findViewById(R.id.edit_text_message);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ImageView menuIcon = (ImageView) findViewById(R.id.menu_icon);
        ImageView extraMenuIcon = (ImageView) findViewById(R.id.extra_menu_icon);
        menuIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        extraMenuIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                        sendOnChannel1(view);

            }
        });
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Hash_file_maps = new HashMap<String, String>();
        sliderLayout = (SliderLayout)findViewById(R.id.slider);
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


        for(String name : Hash_file_maps.keySet()){

            TextSliderView textSliderView = new TextSliderView(DashBoard.this);
            textSliderView
                    .description(name)
                    .image(Hash_file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3000);
        sliderLayout.addOnPageChangeListener(this);


        walletCard=findViewById(R.id.bankCard);
        shopCard =findViewById(R.id.shop_card);
walletCard.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        NotificationChannel channel1 = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel1.setDescription("This is Channel 1");
        }
        NotificationManager manager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager = getSystemService(NotificationManager.class);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(channel1);
        }



    }
});

        shopCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DashBoard.this,DistributorQRScanner.class);
                startActivity(intent);
            }
        });
    }

    public void sendOnChannel1(View v) {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.digiration)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }


    @Override
    protected void onStop() {

        sliderLayout.stopAutoCycle();

        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        Toast.makeText(this,slider.getBundle().get("extra") + " "+"   "+Hash_file_maps.get("Shivkumar"),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.logout:
                // Red item was selected
                Intent intent=new Intent(DashBoard.this,Logout.class);
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

        }
        else if (id == R.id.nav_notification_setting) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
