package com.example.ltdd_app_mang_xa_hoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import Adapters.ListFriendAdapter;
import Entity.Lv_ListFriend;

public class NewChatActivity extends AppCompatActivity {
LinearLayout btn_creategroup;
ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);
        backButton = findViewById(R.id.back);
        ListView lv_listfriend;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_creategroup = findViewById(R.id.btn_creategroup);
        lv_listfriend= findViewById(R.id.lv_listfriend);
        btn_creategroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewChatActivity.this, CreateGroupChatActivity.class);
                startActivity(intent);
            }
        });
        ArrayList<Lv_ListFriend> arrayList = new ArrayList<>();
        arrayList.add(new Lv_ListFriend(R.drawable.avatar, "Thành An", 1,-1));
        arrayList.add(new Lv_ListFriend(R.drawable.avatar, "Đình Yên", 0,-1));
        arrayList.add(new Lv_ListFriend(R.drawable.avatar, "Hoàng Phúc", 3,-1));
        arrayList.add(new Lv_ListFriend(R.drawable.avatar, "Tấn Hên", 0,-1));
        arrayList.add(new Lv_ListFriend(R.drawable.avatar, "Xuân Việt", 5,-1));
        ListFriendAdapter adapter = new ListFriendAdapter(this,R.layout.lv_create_group,arrayList);
        lv_listfriend.setAdapter(adapter);
        lv_listfriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(NewChatActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });
    }
}