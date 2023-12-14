package com.example.ltdd_app_mang_xa_hoi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Adapters.UserAdapter;
import Entity.Users;

public class DetailGroupChatActivity extends AppCompatActivity {
    String idChat;
    ImageView imagegroup;
    TextView groupname, countmember;
    RecyclerView listmember;
    ImageButton backbutton;
    String userid;
    List<Users> list;
    UserAdapter adapter;
    ImageButton btnrename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_group_chat);
        idChat= getIntent().getStringExtra("id");
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        init();
        list = new ArrayList<>();
        adapter = new UserAdapter(list);
        listmember.setAdapter(adapter);
        listmember.setLayoutManager(new LinearLayoutManager(this));
        listmember.setHasFixedSize(true);
        loadBasicData();
        clicklistener();
    }
    public void init(){
        imagegroup =findViewById(R.id.profileImage);
        groupname =findViewById(R.id.nameTV);
        countmember= findViewById(R.id.statusTV);
        listmember=findViewById(R.id.listmember);
        backbutton=findViewById(R.id.back);
        btnrename=findViewById(R.id.rename);
    }
    public void loadBasicData(){
        DocumentReference groupRef = FirebaseFirestore.getInstance().collection("Messages")
                .document(idChat);
        CollectionReference userRef = FirebaseFirestore.getInstance().collection("User");
        groupRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                if (value == null || !value.exists()) {
                    return;
                }

                String name = value.getString("name");
                ArrayList<String> listId =(ArrayList<String>) value.get("uid");
                groupname.setText(name);
                countmember.setText(listId.size()+" thành viên");
                if (listId.contains(userid)) {
                    listId.remove(userid);
                }
                userRef.whereIn("uid", listId)
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
                                }
                            }
                        });
            }
        });
    }
    public void clicklistener(){
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnrename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewdialog = LayoutInflater.from(DetailGroupChatActivity.this).inflate(R.layout.dialog_edit,null,false);
                AlertDialog.Builder alertDialog =new AlertDialog.Builder(DetailGroupChatActivity.this);
                alertDialog.setView(viewdialog);
                final EditText edt_hoten = viewdialog.findViewById(R.id.namesua);
                edt_hoten.setText(groupname.getText());
                alertDialog.setPositiveButton("Đổi tên", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DocumentReference groupRef = FirebaseFirestore.getInstance().collection("Messages")
                                .document(idChat);
                        Map<String, Object> map = new HashMap<>();
                        map.put("name",edt_hoten.getText().toString());
                        groupRef.update(map);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });
    }
}