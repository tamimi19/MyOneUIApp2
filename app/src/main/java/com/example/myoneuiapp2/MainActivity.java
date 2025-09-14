package com.example.myoneuiapp2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		// AppBarLayout (لا حاجة لاستدعاءات غير قياسية هنا)
        AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);

        // Toolbar من AppCompat
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);

        // أيقونة التنقل — استخدم مورد AppCompat الافتراضي المضمن أو ضع موردك في res/drawable
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_menu_overflow_material);
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));

        // التعامل مع عناصر القائمة
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            drawerLayout.closeDrawer(GravityCompat.END);
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
