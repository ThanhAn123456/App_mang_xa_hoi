package com.example.ltdd_app_mang_xa_hoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Adapters.AddMemberAdapter;
import Adapters.UserAdapter;
import Entity.AddMemberModel;
import Entity.Users;

public class CreateGroupChatActivity extends AppCompatActivity {
    ImageButton backButton;
    RecyclerView recyclerView;
    AddMemberAdapter adapter;
    List<AddMemberModel> list;
    List<String> listfriend;
    CollectionReference reference;
    FirebaseUser user;
    Set<String> listGroup;
    TextView txtcreate;
    EditText txtnamegroup;
    List<String> listIdGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_chat);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseFirestore.getInstance().collection("User");
        init();
        clicklistener();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        listfriend = new ArrayList<>();
        list = new ArrayList<>();
        listGroup = new HashSet<>();
        listIdGroup = new ArrayList<>();
        loadFriend();
        adapter = new AddMemberAdapter(this, list);
        adapter.OnPressed(new AddMemberAdapter.OnPressed() {
            @Override
            public void onCheck(int position, String id, boolean isChecked) {
                if (isChecked) {
                    listGroup.add(id);
                } else {
                    listGroup.remove(id);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void init() {
        backButton = findViewById(R.id.back);
        recyclerView = findViewById(R.id.addmember);
        txtcreate = findViewById(R.id.btn_creategroup);
        txtnamegroup = findViewById(R.id.txt_name_group);
    }

    public void clicklistener() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        txtcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listGroup.size() >= 2) {
                    String groupName = txtnamegroup.getText().toString().trim();
                    if (!TextUtils.isEmpty(groupName))
                        queryChat();
                    else
                        Toast.makeText(CreateGroupChatActivity.this, "Phải có tên nhóm", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateGroupChatActivity.this, "Phải chọn ít nhất 2 người", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void queryChat() {
        listIdGroup.clear();
        GetListChecked();
        MaterialDialog progressDialog = new MaterialDialog(CreateGroupChatActivity.this, MaterialDialog.getDEFAULT_BEHAVIOR());
        progressDialog.title(R.string.chatstart, "Start chat");
        progressDialog.cancelable(false);
        progressDialog.icon(R.drawable.chat, getResources().getDrawable(R.drawable.chat));
        progressDialog.show();
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Messages");
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        boolean foundMatch = false;

                        for (QueryDocumentSnapshot document : querySnapshot) {
                            List<String> firestoreUidList = (List<String>) document.get("uid");
                            firestoreUidList.sort(String::compareTo);
                            listIdGroup.sort(String::compareTo);
                            if (firestoreUidList.equals(listIdGroup)) {
                                foundMatch = true;
                                Intent intent = new Intent(CreateGroupChatActivity.this, ChatActivity.class);
                                intent.putExtra("uid", "1");
                                intent.putExtra("id", document.getId()); // return doc id
                                startActivity(intent);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        Toast.makeText(CreateGroupChatActivity.this, "Nhóm này đã có rôì, dừng tạo nữa", Toast.LENGTH_SHORT).show();
                                    }
                                }, 3000);
                                break;
                            }
                        }

                        progressDialog.dismiss();

                        if (!foundMatch) {
                            // No match found, perform other action or start chat
                            startChat(progressDialog);
                        }
                    } else {
                        progressDialog.dismiss();
                    }
                } else {
                    progressDialog.dismiss();
                    System.out.println("Failed with: " + task.getException());
                }
            }
        });
    }

    void GetListChecked() {
        listIdGroup.add(0, user.getUid());
        for (String element : listGroup) {
            listIdGroup.add(element);
        }
    }

    void startChat(MaterialDialog progressDialog) {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Messages");

        String pushID = reference.document().getId();
        Map<String, Object> map = new HashMap<>();
        map.put("id", pushID);
        map.put("lastMessage", "Hi");
        map.put("time", FieldValue.serverTimestamp());
        map.put("uid", listIdGroup);
        map.put("isGroupChat",true);
        map.put("name", txtnamegroup.getText().toString());

        reference.document(pushID).update(map).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                reference.document(pushID).set(map);
            }
        });


        CollectionReference messageRef = FirebaseFirestore.getInstance()
                .collection("Messages")
                .document(pushID)
                .collection("Messages");

        String messageID = messageRef.document().getId();

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("id", messageID);
        messageMap.put("message", "Hi");
        messageMap.put("senderID", user.getUid());
        messageMap.put("time", FieldValue.serverTimestamp());

        messageRef.document(messageID).set(messageMap);

        new Handler().postDelayed(() -> {
            progressDialog.dismiss();
            Intent intent = new Intent(CreateGroupChatActivity.this, ChatActivity.class);
            intent.putExtra("uid", "1");
            intent.putExtra("id", pushID);

            startActivity(intent);

        }, 3000);
    }

    public void loadFriend() {
        reference.document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        listfriend = (List<String>) documentSnapshot.get("following");
                        if (listfriend != null && !listfriend.isEmpty()) {
                            queryFriends();
                        } else {
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
                                    AddMemberModel model = snapshot.toObject(AddMemberModel.class);
                                    list.add(model);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }


}