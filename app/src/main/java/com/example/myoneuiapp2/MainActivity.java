package com.example.myoneuiapp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        // أيقونة بسيطة لفتح الدرج
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(Gravity.END));

        // أزرار الدرج اليدوية
        Button btnScroll = findViewById(R.id.btn_nav_scroll_list);
        Button btnSettings = findViewById(R.id.btn_nav_settings);

        btnScroll.setOnClickListener(v -> {
            drawerLayout.closeDrawer(Gravity.END);
            startActivity(new Intent(MainActivity.this, ScrollListActivity.class));
        });

        btnSettings.setOnClickListener(v -> {
            drawerLayout.closeDrawer(Gravity.END);
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // في حال طبقت أي عناصر أخرى
        return super.onOptionsItemSelected(item);
    }
}
