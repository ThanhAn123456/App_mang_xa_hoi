package com.example.ltdd_app_mang_xa_hoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Util.FireBaseUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        if (getIntent().getExtras() != null) {
            String ChatID = getIntent().getExtras().getString("ChatID");
            String UserID = getIntent().getExtras().getString("userIdFollow");
            Intent mainIntent=new Intent(this,MainContainerActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(mainIntent);
            if (ChatID != null) {
                FirebaseFirestore.getInstance().collection("Messages").document(ChatID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        ArrayList<String> uid = (ArrayList<String>)  document.get("uid");
                                        boolean isGroupChat = document.getBoolean("isGroupChat");
                                        Intent intent = new Intent(SplashActivity.this, ChatActivity.class);
                                        intent.putStringArrayListExtra("uid",uid);
                                        intent.putExtra("id", ChatID);
                                        intent.putExtra("isGroupChat", isGroupChat);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }
                        });
            } else if (UserID != null) {
                Intent intent = new Intent(SplashActivity.this, ProfileActivity.class);
                intent.putExtra("userId",UserID);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }


        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (user == null) {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));

                    } else {
                        startActivity(new Intent(SplashActivity.this, MainContainerActivity.class));
                    }
                    finish();
                }
            }, 1000);
        }

    }
}