package com.example.ltdd_app_mang_xa_hoi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class UpdateAccountActivity extends AppCompatActivity {
ImageButton backButton;
TextView name,email;
ImageButton editprofileimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);
        backButton = findViewById(R.id.back);
        name= findViewById(R.id.txt_name);
        editprofileimage= findViewById(R.id.editprofileimage);
        email= findViewById(R.id.txt_email);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        editprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        name.setHint("Nguyen Thanh An");
        email.setHint("BelleSoriano@gmail.com");
    }
}