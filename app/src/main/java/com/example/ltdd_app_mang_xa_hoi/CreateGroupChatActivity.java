package com.example.ltdd_app_mang_xa_hoi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import Adapters.ListFriendAdapter;
import Entity.Lv_ListFriend;

public class CreateGroupChatActivity extends AppCompatActivity {
    ImageButton backButton;
    ListView lvAddmember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_chat);
        backButton = findViewById(R.id.back);
        lvAddmember = findViewById(R.id.lv_addmember);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}