package com.example.myoneuiapp2;

import android.os.Bundle;

// استيراد مكونات التفضيلات المخصصة من مكتبة One UI (SESL)
import dev.oneuiproject.oneui.preference.ListPreference;
import dev.oneuiproject.oneui.preference.Preference;
import dev.oneuiproject.oneui.preference.PreferenceFragmentCompat;
import dev.oneuiproject.oneui.preference.SwitchPreferenceCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // هنا يمكنك إضافة منطق خاص بالتفضيلات إذا أردت
        // على سبيل المثال، ربط ملخص القائمة بالقيمة الحالية
        ListPreference themePref = findPreference("theme");
        if (themePref != null) {
            themePref.setSummary(themePref.getEntry());
            themePref.setOnPreferenceChangeListener((preference, newValue) -> {
                // يمكنك هنا وضع كود تغيير الثيم
                // ...
                preference.setSummary(themePref.getEntries()[themePref.findIndexOfValue(newValue.toString())]);
                return true;
            });
        }
    }
}
