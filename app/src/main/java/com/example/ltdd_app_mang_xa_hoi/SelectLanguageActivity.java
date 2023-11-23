        package com.example.ltdd_app_mang_xa_hoi;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.Spinner;
        import android.widget.Toast;

        import java.util.ArrayList;

        import Adapters.LanguageAdapter;
        import Adapters.ListNews_Adapter;
        import Config.MyApplication;
        import Entity.Language;

        public class SelectLanguageActivity extends AppCompatActivity {
            Spinner spn_language;
            Button btn_select;
            Language selectedLanguage; // Biến để theo dõi ngôn ngữ được chọn

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_select_language);

                btn_select = findViewById(R.id.btn_select);
                spn_language = findViewById(R.id.spn_language);
                ArrayList<Language> arrayList = new ArrayList<>();
                arrayList.add(new Language(R.drawable.vietnam, "Việt Nam"));
                arrayList.add(new Language(R.drawable.english, "English"));
                LanguageAdapter adapter = new LanguageAdapter(SelectLanguageActivity.this, R.layout.spn_language, arrayList);
                spn_language.setAdapter(adapter);
                selectedLanguage = arrayList.get(0);
                // Xử lý sự kiện khi người dùng chọn mục trong Spinner
                spn_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        // Lấy ngôn ngữ được chọn và cập nhật biến selectedLanguage
                        selectedLanguage = arrayList.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // Xử lý khi không có mục nào được chọn, nếu cần thiết
                    }
                });

                // Xử lý sự kiện khi người dùng nhấn nút
                btn_select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selectedLanguage != null) {
                            String selectedLanguageName = selectedLanguage.name;
                            // Sử dụng selectedLanguageName

                            if (selectedLanguageName.equals("Việt Nam")) {
                                MyApplication.setLocale(SelectLanguageActivity.this,"vn" );
                                startActivity(new Intent(SelectLanguageActivity.this,SplashActivity.class));

                            } else if (selectedLanguageName.equals("English")) {
                                MyApplication.setLocale(SelectLanguageActivity.this, "en");
                                startActivity(new Intent(SelectLanguageActivity.this,SplashActivity.class));
                            }
                        }
                    }
                });
            }
        }