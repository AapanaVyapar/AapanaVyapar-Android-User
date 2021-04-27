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

//    ChipGroup chipGroup;
//    Chip chip;
//    RecyclerView recycler_View;


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
                  switch (item.getItemId()) {
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
                  getSupportFragmentManager().beginTransaction().replace(R.id.linear_layout1, selectedFragment).commit();
                  return true;
              }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.linear_layout1,new TrendingFragment()).commit();



//        String arr[] = {"Food", "Clothes", "Electronics", "Devotional", "Sports", "Cosmetics"};
//
//
//        chipGroup = findViewById(R.id.chipgroup);
//        for (int i = 0; i < arr.length; i++) {
//            chip = new Chip(this);
//            chip.setText(arr[i]);
//            ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(this, null, 0, R.style.CustomChipStyle);
//            chip.setChipDrawable(chipDrawable);
//            chip.setId(i);
//            chip.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                  Chip chipClick = (Chip) v;
//                    Chip chipClick = v.findViewById(v.getId());
//                    Toast.makeText(getApplicationContext(), chipClick.getText(), Toast.LENGTH_LONG).show();
//                }
//            });
//
//            chipGroup.addView(chip);
//
//            recycler_View = findViewById(R.id.recycler_view);
//            recycler_View.setHasFixedSize(true);
//            recycler_View.setLayoutManager(new LinearLayoutManager(this));
//
//            ProductData[] product_data = new ProductData[]{
//                    // new product_data(R.drawable.watch,"Rolex Women's Watch","Fashion Wrist Watches","Shop Description"),
////                    new product_data(R.drawable.laptop,"Acer Laptop","Sonam Electronics","Shop Description"),
////                    new product_data(R.drawable.burger,"Classic Cheeseburger","We Desi","Shop Description"),
////                    new product_data(R.drawable.third_image,"Yonex Badminton Kit","Jalgaon Sports","Shop Description"),
//            };
//
//            ProductAdapter product_adapter = new ProductAdapter(product_data,ViewProvider.this);
//            recycler_View.setAdapter(product_adapter);
//        }

    }


}