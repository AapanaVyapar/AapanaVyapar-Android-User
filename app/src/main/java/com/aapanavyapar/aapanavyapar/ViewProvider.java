package com.aapanavyapar.aapanavyapar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.aapanavyapar.ProductAdapter;
import com.aapanavyapar.ProductData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

public class ViewProvider extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_provider);
        Toast.makeText(this.getApplicationContext(), "View Provider...", Toast.LENGTH_LONG).show();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){
                    case R.id.av_trend:
                        selectedFragment = new TrendingFragment();
                        break;
                    case R.id.av_cart:
                        selectedFragment = new CartFragment();
                        break;
                    case R.id.av_search:
                        selectedFragment = new SearchFragment();
                        break;
                    case R.id.av_orders:
                        selectedFragment = new OrderFragment();
                        break;
                    case R.id.av_profile:
                        selectedFragment = new ProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                return true;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TrendingFragment()).commit();
    }
}