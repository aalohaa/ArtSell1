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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.artsell.R;
import com.example.artsell.databinding.ActivityMainBinding;
import com.example.artsell.fragments.CategoryFragment;
import com.example.artsell.fragments.ChatsFragment;
import com.example.artsell.fragments.HomeFragment;
import com.example.artsell.fragments.MyCartsFragment;
import com.example.artsell.fragments.OffersFragment;
import com.example.artsell.fragments.ProfileFragment;
import com.example.artsell.fragments.MyOrdersFragment;
import com.example.artsell.fragments.NewProductsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;



public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    ImageButton fabMenu;
    ActivityMainBinding binding;
    Animation fabOpen, fabClose;
    FloatingActionButton fabProfile, fabCategory, fabOffers, fabNewProducts,fabMyCarts;

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

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        fabProfile = findViewById(R.id.nav_profile);
        fabCategory = findViewById(R.id.nav_category);
        fabOffers = findViewById(R.id.nav_offers);
        fabNewProducts = findViewById(R.id.nav_new_products);
        fabMyCarts = findViewById(R.id.nav_my_carts);

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
        fabCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View View) {

                replaceFragment(new CategoryFragment());
                animateFab();
            }
        });
        fabOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View View) {
                replaceFragment(new OffersFragment());
                animateFab();
            }
        });
        fabNewProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View View) {
                replaceFragment(new NewProductsFragment());
                animateFab();
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
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.nav_my_orders:
                    if (auth.getCurrentUser() == null) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    } else {
                    replaceFragment(new MyOrdersFragment());
                    }
                    break;
                case R.id.scene_chat:
                    if (auth.getCurrentUser() == null) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    } else {
                    replaceFragment(new ChatsFragment());
                    }
                    break;
                case R.id.scene_btn:
                    break;
                default:
                    return true;
            }
            return true;
        });
    }

    private void animateFab(){

        if (isOpen){
            fabProfile.startAnimation(fabClose);
            fabCategory.startAnimation(fabClose);
            fabOffers.startAnimation(fabClose);
            fabNewProducts.startAnimation(fabClose);
            fabMyCarts.startAnimation(fabClose);

            fabProfile.setClickable(false);
            fabCategory.setClickable(false);
            fabOffers.setClickable(false);
            fabNewProducts.setClickable(false);
            fabMyCarts.setClickable(false);
            isOpen=false;
        } else {
            fabProfile.startAnimation(fabOpen);
            fabCategory.startAnimation(fabOpen);
            fabOffers.startAnimation(fabOpen);
            fabNewProducts.startAnimation(fabOpen);
            fabMyCarts.startAnimation(fabOpen);

            fabProfile.setClickable(true);
            fabCategory.setClickable(true);
            fabOffers.setClickable(true);
            fabNewProducts.setClickable(true);
            fabMyCarts.setClickable(true);
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