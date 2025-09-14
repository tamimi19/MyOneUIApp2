package com.example.myoneuiapp2;

import android.os.Bundle;
import sesl.support.drawerlayout.widget.DrawerLayout;
import android.view.Gravity;
import android.content.Intent;

// استيراد مكونات سامسونج SESL من المسار الصحيح
import sesl.support.v7.app.AppCompatActivity;
import sesl.support.v7.widget.Toolbar;
import sesl.support.v7.widget.AppBarLayout;
import sesl.support.v7.view.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ربط الـ AppBarLayout وتفعيل ميزة السحب لأسفل للوصول السهل
        // تم تغيير اسم الكلاس والدالة لتتوافق مع المكتبة
        AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
        appBarLayout.setExpandedAccessibilityEnabled(true);

        // استخدام Toolbar الخاص بـ One UI
        // تم تغيير اسم الكلاس
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        // استخدام NavigationView الخاص بـ One UI
        NavigationView navigationView = findViewById(R.id.nav_view);

        // إعداد أيقونة القائمة لفتح درج التنقل على اليمين
        // ملاحظة: قد تحتاج إلى أيقونة مخصصة هنا إذا كانت الايقونة الافتراضية لا تعمل
        toolbar.setNavigationIcon(sesl.support.appcompat.R.drawable.abc_ic_menu_overflow_material);
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
