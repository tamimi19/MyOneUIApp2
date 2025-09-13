package com.example.myoneuiapp2;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // إعداد أيقونة القائمة لفتح درج التنقل على اليمين
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(Gravity.END));

        // التعامل مع اختيار عناصر القائمة الجانبية
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            drawerLayout.closeDrawer(Gravity.END);
            int id = menuItem.getItemId();
            if (id == R.id.nav_settings) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            } else if (id == R.id.nav_scroll_list) {
                startActivity(new Intent(MainActivity.this, ScrollListActivity.class));
            }
            return true;
        });
    }

}
