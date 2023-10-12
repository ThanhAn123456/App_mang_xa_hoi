package com.example.ltdd_app_mang_xa_hoi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import Adapters.ListFriendAdapter;
import Dto.Lv_ListFriend;

public class CreateGroupChatActivity extends AppCompatActivity {
    ImageView backButton;
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
        ArrayList<Lv_ListFriend> arrayList = new ArrayList<>();
        arrayList.add(new Lv_ListFriend(R.drawable.avatar, "Thành An", 1,0));
        arrayList.add(new Lv_ListFriend(R.drawable.avatar, "Đình Yên", 0,0));
        arrayList.add(new Lv_ListFriend(R.drawable.avatar, "Hoàng Phúc", 3,0));
        arrayList.add(new Lv_ListFriend(R.drawable.avatar, "Tấn Hên", 0,0));
        arrayList.add(new Lv_ListFriend(R.drawable.avatar, "Xuân Việt", 5,0));
        ListFriendAdapter adapter = new ListFriendAdapter(this,R.layout.lv_create_group,arrayList);
        lvAddmember.setAdapter(adapter);
    }
}