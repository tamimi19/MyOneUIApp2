package com.example.myoneuiapp2;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.Gravity;
import com.google.android.material.navigation.NavigationView;
import android.content.Intent;

// استيراد مكونات سامسونج SESL
import androidx.appcompat.widget.SeslToolbar;
import androidx.appcompat.widget.SeslAppBarLayout;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ربط الـ AppBarLayout وتفعيل ميزة السحب لأسفل للوصول السهل
        SeslAppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
        appBarLayout.seslSetExpandedAccessibilityEnabled(true);

        // استخدام SeslToolbar بدل Toolbar العادي
        SeslToolbar toolbar = findViewById(R.id.toolbar);
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
