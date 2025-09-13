package com.example.myoneuiapp2;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class ScrollListActivity extends BasePullDownActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_list);

        // شريط الأدوات
        Toolbar toolbar = findViewById(R.id.toolbar_scroll);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // إعداد القائمة (70 عنصر كما طلبت سابقًا)
        String[] items = new String[70];
        for (int i = 0; i < 70; i++) {
            items[i] = "عنصر " + (i + 1);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, items);
        // تأكد من أن listView موجود
        if (findViewById(R.id.list_view) != null) {
            // setContentView تم من قبل، الآن نهيئ الـ BasePullDownActivity
            initPullDown();
            if (listView != null) listView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // زر العودة في toolbar
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
