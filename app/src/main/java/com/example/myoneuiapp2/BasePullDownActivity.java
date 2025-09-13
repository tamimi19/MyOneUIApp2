package com.example.myoneuiapp2;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * فئة أساسية توفر سلوك السحب لأسفل للمحتوى (قوائم ListView).
 * الأنشطة التي تريد هذا السلوك ترث من هذه الفئة.
 *
 * Requirements:
 * - في الـ layout يجب وجود TextView مع id = header_text
 * - و ListView مع id = list_view
 */
public abstract class BasePullDownActivity extends AppCompatActivity {

    protected TextView headerText;
    protected ListView listView;

    private float originalTextSizePx = 0f;
    protected float startY = 0f;
    private float maxTranslateY = 240f;
    protected float currentTranslateY = 0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initPullDown() {
        View header = findViewById(R.id.header_text);
        View lv = findViewById(R.id.list_view);

        if (header instanceof TextView && lv instanceof ListView) {
            headerText = (TextView) header;
            listView = (ListView) lv;
            originalTextSizePx = headerText.getTextSize();
            maxTranslateY = dpToPx(120);
            attachTouchListener();
        }
    }

    private void attachTouchListener() {
        listView.setOnTouchListener(new ListTouchListener());
    }

    private void applyTranslateAndScale(float translate) {
        currentTranslateY = translate;
        listView.setTranslationY(currentTranslateY);

        float factor = 1.0f;
        if (maxTranslateY > 0) {
            factor = 1.0f + (currentTranslateY / maxTranslateY) * 0.4f;
        }
        float newTextSize = originalTextSizePx * factor;
        headerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
    }

    private void animateReset() {
        final float from = currentTranslateY;
        ValueAnimator animator = ValueAnimator.ofFloat(from, 0f);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(280);

        // تم التعديل هنا: استبدال الكلاس الداخلي ResetAnimatorUpdateListener
        // بتعبير lambda لتبسيط الكود وتجنب مشاكل R8.
        animator.addUpdateListener(animation -> {
            float val = (float) animation.getAnimatedValue();
            applyTranslateAndScale(val);
        });

        animator.start();
    }

    private boolean isListAtTop() {
        if (listView == null || listView.getAdapter() == null) return true;
        if (listView.getFirstVisiblePosition() == 0) {
            View firstChild = listView.getChildAt(0);
            if (firstChild == null) return true;
            return firstChild.getTop() >= listView.getPaddingTop();
        }
        return false;
    }

    protected float dpToPx(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }

    // تم التعديل هنا: إزالة "private final" لتبسيط الكلاس لمترجم R8.
    class ListTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (!isListAtTop()) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startY = event.getY();
                }
                return false;
            }

            float y = event.getY();
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    startY = y;
                    return false;
                case MotionEvent.ACTION_MOVE:
                    float dy = y - startY;
                    if (dy > 0 || currentTranslateY > 0) {
                        float translate = (dy > 0)
                                ? Math.min(maxTranslateY, currentTranslateY + dy / 2f)
                                : Math.max(0f, currentTranslateY + dy / 2f);
                        applyTranslateAndScale(translate);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    animateReset();
                    startY = 0f;
                    break;
            }
            return false;
        }
    }
    
    // لم نعد بحاجة للكلاس ResetAnimatorUpdateListener لأنه تم استبداله بـ lambda.
}
