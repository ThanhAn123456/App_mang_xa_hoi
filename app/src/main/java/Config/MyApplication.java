package Config;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.LocaleList;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

public class MyApplication extends Application {
    private static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int savedTheme = sharedPreferences.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        String savedLanguage = sharedPreferences.getString("language", "");
        if (savedLanguage.isEmpty()) {
            setLocale(this, "vi"); // or setLocale(getApplicationContext(), "vi");
        } else {
            setLocale(this, savedLanguage); // or setLocale(getApplicationContext(), savedLanguage);
        }
        AppCompatDelegate.setDefaultNightMode(savedTheme);
    }

    public static void toggleTheme() {
        int currentTheme = AppCompatDelegate.getDefaultNightMode();
        int newTheme = (currentTheme == AppCompatDelegate.MODE_NIGHT_YES) ?
                AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES;
        // Lưu trạng thái chủ đề vào SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("theme", newTheme);
        editor.apply();

        // Cập nhật chủ đề
        AppCompatDelegate.setDefaultNightMode(newTheme);
    }
    public static void setLocale(Context context, String languageCode) {
        Log.d("MyApplication", "Setting Locale: " + languageCode);
        Locale newLocale = new Locale(languageCode);
        Locale.setDefault(newLocale);

        Configuration configuration = new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocales(new LocaleList(newLocale));
        } else {
            configuration.locale = newLocale;
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("language", languageCode).apply();

        // Cập nhật ngôn ngữ cho toàn bộ ứng dụng
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }
}
