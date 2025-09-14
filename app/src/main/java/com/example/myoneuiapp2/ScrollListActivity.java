package com.example.myoneuiapp2;

import android.os.Bundle;
import io.github.oneuiproject.sesl.support.v7.app.AppCompatActivity;
import io.github.oneuiproject.sesl.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.MenuItem;

public class ScrollListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_list);

        Toolbar toolbar = findViewById(R.id.toolbar_scroll);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // ملء القائمة بـ70 عنصر
        String[] items = new String[70];
        for (int i = 0; i < 70; i++) {
            items[i] = "Item " + (i + 1);
        }
        ListView listView = findViewById(R.id.list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // معالجة زر العودة في شريط الأدوات
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
