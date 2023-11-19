package com.example.ltdd_app_mang_xa_hoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
         FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user =  auth.getCurrentUser();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user ==null){
                    startActivity(new Intent(SplashActivity.this,SignUpActivity.class));
                }
                else {
                    startActivity(new Intent(SplashActivity.this,MainContainerActivity.class));
                }
                finish();
            }
        }, 2500);
    }
}