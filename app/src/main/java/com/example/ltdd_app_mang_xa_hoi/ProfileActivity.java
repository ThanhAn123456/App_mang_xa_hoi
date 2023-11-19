package com.example.ltdd_app_mang_xa_hoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity { ImageView backButton;
Button btn_follow;
Button btn_sendmess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        backButton = findViewById(R.id.back);
        btn_follow = findViewById(R.id.btn_follow);
        btn_sendmess = findViewById(R.id.btn_sendmess);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        String uid=intent.getStringExtra("userId");
        Toast.makeText(ProfileActivity.this,uid+"",Toast.LENGTH_SHORT).show();
        btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_follow.getText().toString().equals(getString(R.string.follow)))
                    btn_follow.setText(getString(R.string.unfollow));
                else if (btn_follow.getText().toString().equals(getString(R.string.unfollow)))
                    btn_follow.setText(getString(R.string.follow));
            }
        });
        btn_sendmess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ProfileActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });
    }
}