package com.example.myoneuiapp2;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import android.widget.Toast;
import java.util.Locale;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        // إعداد الاستماع لتغييرات التفضيلات
        ListPreference languagePref = findPreference("language");
        if (languagePref != null) {
            languagePref.setOnPreferenceChangeListener(this);
        }
        ListPreference themePref = findPreference("theme");
        if (themePref != null) {
            themePref.setOnPreferenceChangeListener(this);
        }
        SwitchPreferenceCompat notificationsPref = findPreference("notifications");
        if (notificationsPref != null) {
            notificationsPref.setOnPreferenceChangeListener(this);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String key = preference.getKey();
        Activity activity = getActivity();
        if (key.equals("language")) {
            // تغيير اللغة
            String lang = newValue.toString();
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLocale(locale);
                // createConfigurationContext يمكن استخدامها إذا رغبت
            } else {
                config.locale = locale;
                if (activity != null) {
                    activity.getResources().updateConfiguration(config, activity.getResources().getDisplayMetrics());
                }
            }
            if (activity != null) activity.recreate();
            return true;
        } else if (key.equals("theme")) {
            // تغيير السمة (الوضع)
            String theme = newValue.toString();
            if (theme.equals("dark")) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            if (activity != null) activity.recreate();
            return true;
        } else if (key.equals("notifications")) {
            // تبديل الإشعارات
            boolean enabled = (boolean) newValue;
            if (enabled) {
                Toast.makeText(getContext(), getString(R.string.notifications_enabled), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), getString(R.string.notifications_disabled), Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }
}
