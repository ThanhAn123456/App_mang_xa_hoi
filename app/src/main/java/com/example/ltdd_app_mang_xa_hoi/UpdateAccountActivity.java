package com.example.ltdd_app_mang_xa_hoi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UpdateAccountActivity extends AppCompatActivity {
ImageView backButton;
TextView name,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);
        backButton = findViewById(R.id.back);
        name= findViewById(R.id.txt_name);
        email= findViewById(R.id.txt_email);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        name.setHint("Nguyen Thanh An");
        email.setHint("BelleSoriano@gmail.com");
    }
}