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
            String userid = getIntent().getExtras().getString("userId");
            FirebaseFirestore.getInstance().collection("Messages")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    List<String> uidList = (List<String>) document.get("uid");
                                    boolean isGroupChat = document.getBoolean("isGroupChat");

                                    // Check if both conditions are satisfied
                                    if (uidList.contains(FireBaseUtil.currentUserId()) && uidList.contains(userid) && !isGroupChat) {
                                        // Found a document that satisfies all conditions
                                        String id = document.getId();
                                        Intent intent = new Intent(SplashActivity.this, ChatActivity.class);
                                        intent.putExtra("uid", userid);
                                        intent.putExtra("id", id);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            } else {
                                Log.e("arydjydjyiutiufi:  ", "Truy vấn Firestore Thất bại", task.getException());
                            }
                        }
                    });

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