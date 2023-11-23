package com.example.ltdd_app_mang_xa_hoi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.CommentAdapter;
import Entity.CommentModel;
import Entity.Lv_ListCmt;

public class NewsDetailActivity extends AppCompatActivity {
    ImageButton backButton;
    EditText commentEt;
    ImageButton sendBtn;
    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    List<CommentModel> list;
    FirebaseUser user;
    String id, uid;
    CollectionReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        init();

        reference = FirebaseFirestore.getInstance().collection("User")
                .document(uid)
                .collection("Post Images")
                .document(id)
                .collection("Comments");

        loadCommentData();

        clickListener();
    }

    private void init() {
        backButton = findViewById(R.id.back);
        commentEt = findViewById(R.id.writecmt);
        if (getIntent().getBooleanExtra("focusOnEditText", true)) {
            commentEt.requestFocus();
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        commentEt = findViewById(R.id.writecmt);
        sendBtn = findViewById(R.id.btn_send);
        recyclerView = findViewById(R.id.lv_listcmt);

        user = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView.setLayoutManager(new LinearLayoutManager(NewsDetailActivity.this));

        list = new ArrayList<>();
        commentAdapter = new CommentAdapter(NewsDetailActivity.this, list);
        recyclerView.setAdapter(commentAdapter);

        Intent intent = getIntent();
        id = intent.getStringExtra("id").toString();
        uid = intent.getStringExtra("uid").toString();

    }

    private void loadCommentData() {

        reference.orderBy("timestamp", Query.Direction.ASCENDING) // Sắp xếp theo timestamp tăng dần
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Xử lý lỗi nếu cần
                        return;
                    }

                    if (value == null) {
                        // Xử lý khi không có dữ liệu
                        return;
                    }

                    list.clear();
                    for (DocumentSnapshot snapshot : value) {
                        CommentModel model = snapshot.toObject(CommentModel.class);
                        list.add(model);
                    }
                    commentAdapter.notifyDataSetChanged();
                });

    }

    private void clickListener() {

        sendBtn.setOnClickListener(v -> {

            String comment = commentEt.getText().toString();

            if (comment.isEmpty()) {
                Toast.makeText(NewsDetailActivity.this, "Enter comment", Toast.LENGTH_SHORT).show();
                return;
            }


            String commentID = reference.document().getId();

            Map<String, Object> map = new HashMap<>();
            map.put("uid", user.getUid());
            map.put("comment", comment);
            map.put("commentID", commentID);
            map.put("postID", id);
            map.put("name", user.getDisplayName());
            if (user.getPhotoUrl() != null) {
                map.put("profileImageUrl", user.getPhotoUrl().toString());
            }
            map.put("timestamp", FieldValue.serverTimestamp());

            reference.document(commentID)
                    .set(map)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            commentEt.setText("");

                        } else {

                            assert task.getException() != null;
                            Toast.makeText(NewsDetailActivity.this, "Failed to comment:" + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }

                    });

        });
    }
}