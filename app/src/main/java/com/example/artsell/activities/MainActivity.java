package com.example.artsell.activities;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.artsell.R;

import com.example.artsell.databinding.ActivityMainBinding;
import com.example.artsell.fragments.ChatsFragment;
import com.example.artsell.fragments.HomeFragment;
import com.example.artsell.fragments.MyCartsFragment;
import com.example.artsell.fragments.OrderFragment;
import com.example.artsell.fragments.ProfileFragment;
import com.example.artsell.fragments.MarketFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    ActivityMainBinding binding;
    Animation fabOpen, fabClose;
    FloatingActionButton fabProfile, fabMyCarts, fabAsk;
    AppCompatButton btnMarket, btnOrder;

    boolean isOpen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        auth = FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        final ImageButton fabMenu = findViewById(R.id.fabMenu);

        btnMarket = findViewById(R.id.btn_market);
        btnOrder = findViewById(R.id.btn_order);

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        fabProfile = findViewById(R.id.nav_profile);
        fabMyCarts = findViewById(R.id.nav_my_carts);
        fabAsk = findViewById(R.id.nav_ask);

        btnMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new MarketFragment());

            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new OrderFragment());

            }
        });

        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.setSelected(!fabMenu.isSelected());
                fabMenu.setImageResource(fabMenu.isSelected() ? R.drawable.animated_plus : R.drawable.animated_minus);
                Drawable drawable = fabMenu.getDrawable();
                if (drawable instanceof Animatable) {
                    ((Animatable) drawable).start();
                }
                animateFab();
            }
        });

        fabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (auth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else {
                replaceFragment(new ProfileFragment());

                animateFab();
                }
            }
        });

        fabMyCarts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View View) {
                replaceFragment(new MyCartsFragment());

                animateFab();
            }
        });

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    binding.btns.setVisibility(View.GONE);

                    replaceFragment(new HomeFragment());
                    break;
                case R.id.nav_market:
                    if (auth.getCurrentUser() == null) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    } else {
                        binding.btns.setVisibility(View.VISIBLE);
                        replaceFragment(new MarketFragment());

                    }
                    break;
                case R.id.scene_chat:
                    binding.btns.setVisibility(View.GONE);
                    if (auth.getCurrentUser() == null) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    } else {
                        replaceFragment(new ChatsFragment());
                    }
                    break;
                case R.id.scene_btn:
                    binding.btns.setVisibility(View.GONE);
                    break;
                default:
                    binding.btns.setVisibility(View.GONE);
                    return true;
            }
            return true;
        });
    }

    private void animateFab(){

        if (isOpen){
            fabProfile.startAnimation(fabClose);
            fabMyCarts.startAnimation(fabClose);
            fabAsk.startAnimation(fabClose);

            fabProfile.setClickable(false);
            fabMyCarts.setClickable(false);
            fabAsk.setClickable(false);
            isOpen=false;
        } else {
            fabProfile.startAnimation(fabOpen);
            fabMyCarts.startAnimation(fabOpen);
            fabAsk.startAnimation(fabOpen);

            fabProfile.setClickable(true);
            fabMyCarts.setClickable(true);
            fabAsk.setClickable(true);
            isOpen=true;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.drawer_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showToast(String message){
        Toast.makeText( this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

}