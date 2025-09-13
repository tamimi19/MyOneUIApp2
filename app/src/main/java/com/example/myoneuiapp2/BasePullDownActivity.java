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

    // حفظ حجم النص الأصلي بالبكسل
    private float originalTextSizePx = 0f;

    // تتبع لموضع اللمس
    private float startY = 0f;

    // الحد الأقصى للإزاحة الرأسيّة (بالبكسل)
    private float maxTranslateY = 240f; // سيتم تحويله من dp في onCreate

    // الحالة الحالية للإزاحة
    private float currentTranslateY = 0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ملاحظة: لا تستخدم setContentView هنا، لأن الأنشطة الوريثة ستنادي setContentView ثم يجب استدعاء initPullDown() بعدها.
    }

    /**
     * يجب استدعاء هذه الدالة بعد setContentView(...) في النشاط الوريث
     */
    protected void initPullDown() {
        // الحصول على المراجع
        View header = findViewById(R.id.header_text);
        View lv = findViewById(R.id.list_view);

        if (header instanceof TextView && lv instanceof ListView) {
            headerText = (TextView) header;
            listView = (ListView) lv;

            originalTextSizePx = headerText.getTextSize();

            // تحويل maxTranslateY من dp إلى px
            maxTranslateY = dpToPx(120); // 120dp كحد أقصى (قابل للتعديل)

            attachTouchListener();
        } else {
            // إن لم يكن التخطيط مطابقًا، فلا نفعل شيئًا (يسمح لتطبيقك أن يعمل دون كسر)
        }
    }

    private void attachTouchListener() {
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // فقط إذا كانت القائمة في القمة نسمح بتأثير السحب لأسفل
                if (!isListAtTop()) {
                    // إعادة تعيين بدايات السحب عند النزول الداخلي للقائمة
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        startY = event.getY();
                    }
                    return false; // لا نعترض سلوك التمرير الطبيعي
                }

                float y = event.getY();
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = y;
                        return false; // لا نعترض ACTION_DOWN
                    case MotionEvent.ACTION_MOVE:
                        float dy = y - startY;
                        // نعمل فقط للسحب لأسفل (dy > 0) أو أثناء إرجاع (currentTranslateY > 0)
                        if (dy > 0 || currentTranslateY > 0) {
                            // نحسب قيمة الإزاحة (نقل حساس أقل من المسافة الحقيقية)
                            float translate = currentTranslateY;
                            if (dy > 0) {
                                translate = Math.min(maxTranslateY, currentTranslateY + dy / 2f);
                            } else {
                                // dy < 0 أثناء السحب لأعلى، نخفض الإزاحة
                                translate = Math.max(0f, currentTranslateY + dy / 2f);
                            }
                            applyTranslateAndScale(translate);
                            // لا نعيد startY هنا لكي يكون السحب سلسًا عبر حركات متعددة
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // عند الإفلات نعيد كل شيء إلى الصفر بحركة ناعمة
                        animateReset();
                        startY = 0f;
                        break;
                }
                // نعيد false حتى تستمر ListView بأخذ أحداث التمرير (لكننا نقوم بتأثير مرئي فوقها)
                return false;
            }
        });
    }

    private void applyTranslateAndScale(float translate) {
        currentTranslateY = translate;
        // نحرك ListView و header (نحرك الـ listView فقط؛ header يبقى في مكانه لكن يمكن تغيير حجمه)
        listView.setTranslationY(currentTranslateY);

        // نحسب عامل التكبير لحجم النص: من 1.0 إلى 1.4 (مثال)
        float factor = 1.0f;
        if (maxTranslateY > 0) {
            factor = 1.0f + (currentTranslateY / maxTranslateY) * 0.4f; // تكبير حتى +40%
        }
        float newTextSize = originalTextSizePx * factor;
        headerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
    }

    private void animateReset() {
        // انيميشن لإعادة translationY إلى 0
        final float from = currentTranslateY;
        ValueAnimator animator = ValueAnimator.ofFloat(from, 0f);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(280);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                applyTranslateAndScale(val);
            }
        });
        animator.start();
    }

    private boolean isListAtTop() {
        if (listView == null) return true;
        if (listView.getAdapter() == null) return true;
        // القائمة في القمة إذا كانت العنصر الأول مرئيًا وفوقه top >= 0
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
                          }
