package com.example.ltdd_app_mang_xa_hoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Adapters.UserAdapter;
import Entity.Users;
import Util.FireBaseUtil;

public class NewChatActivity extends AppCompatActivity {
    LinearLayout btn_creategroup;
    ImageButton backButton;
    CollectionReference reference;
    String uid;
    List<String> listfriend;
    List<Users> list;
    UserAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);
        init();
        clicklistener();
        reference = FireBaseUtil.allUserCollectionReference();
        uid = FireBaseUtil.currentUserId();
        listfriend = new ArrayList<>();
        list = new ArrayList<>();
        loadFriend();
        adapter = new UserAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }

    void init() {
        backButton = findViewById(R.id.back);
        btn_creategroup = findViewById(R.id.btn_creategroup);
        recyclerView = findViewById(R.id.listfriend);
    }

    void clicklistener() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_creategroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewChatActivity.this, CreateGroupChatActivity.class);
                startActivity(intent);
            }
        });


    }

    public void loadFriend() {
        reference.document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        listfriend = (List<String>) documentSnapshot.get("following");
                        if (listfriend != null && !listfriend.isEmpty()) {
                            queryFriends(); // Gọi phương thức để truy vấn danh sách bạn bè
                        } else {
                            // Xử lý trường hợp listfriend là null hoặc rỗng
                        }
                    }
                });
    }

    private void queryFriends() {
        reference.whereIn("uid", listfriend)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            list.clear();
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                if (snapshot.exists()) {
                                    Users model = snapshot.toObject(Users.class);
                                    list.add(model);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            Log.d("adadadadsdad",String.valueOf(listfriend.size()+"qsasd"+ list.size()));
                        }
                    }
                });
    }
}