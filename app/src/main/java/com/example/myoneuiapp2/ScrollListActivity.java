package com.example.myoneuiapp2;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ScrollListActivity extends AppCompatActivity {

    private ListView listView;
    private TextView headerText;
    private float originalTextSizePx = 0;
    private float startY = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_list);

        // ربط عناصر الواجهة
        headerText = findViewById(R.id.header_text);
        listView = findViewById(R.id.list_view);

        // حفظ حجم الخط الأصلي بالبيكسل
        originalTextSizePx = headerText.getTextSize();

        // مثال: إعداد بيانات تجريبية للقائمة
        String[] items = {"عنصر 1", "عنصر 2", "عنصر 3", "عنصر 4", "عنصر 5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        // تعيين مستمع لمس أحداث اللمس على ListView
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // التأكد من أن العنصر الأول ظاهر (للسماح بالتمرير العلوي فقط عند وصوله للقمة)
                if (listView.getFirstVisiblePosition() == 0) {
                    View firstChild = listView.getChildAt(0);
                    if (firstChild != null && firstChild.getTop() >= 0) {
                        float currentY = event.getY();
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                // بدء التتبع من نقطة البداية
                                startY = currentY;
                                break;
                            case MotionEvent.ACTION_MOVE:
                                float dy = currentY - startY;
                                if (dy > 0) {
                                    // السحب لأسفل => تكبير الخط
                                    float scaleFactor = 1 + (dy / 1000f); // قيمة تجريبية للتكبير
                                    scaleFactor = Math.min(scaleFactor, 2.0f); // تحديد حد أقصى للتكبير
                                    headerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, originalTextSizePx * scaleFactor);
                                } else if (dy < 0) {
                                    // السحب لأعلى أو رفع => تصغير الخط
                                    float scaleFactor = 1 + (dy / 1500f); // قيمة تجريبية للتصغير
                                    scaleFactor = Math.max(scaleFactor, 0.8f); // تحديد حد أدنى للتصغير
                                    headerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, originalTextSizePx * scaleFactor);
                                }
                                break;
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // عند الإفلات: إعادة الخط إلى حجمه الأصلي
                                headerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, originalTextSizePx);
                                startY = 0f;
                                break;
                        }
                    }
                }
                // إرجاع false للسماح باستمرار التمرير بعد التلاعب بالتحجيم
                return false;
            }
        });
    }
}
