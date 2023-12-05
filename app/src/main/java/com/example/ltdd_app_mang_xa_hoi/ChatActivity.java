package com.example.ltdd_app_mang_xa_hoi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.ChatAdapter;
import Entity.ChatModel;
import Entity.Users;
import Util.FireBaseUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {
    ImageButton backButton;
    FirebaseUser user;
    CircleImageView imageView;
    TextView name, status;
    EditText chatET;
    ImageView sendBtn;
    RecyclerView recyclerView;

    ChatAdapter adapter;
    List<ChatModel> list;
    String chatID;
    String oppositeUID, token;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        if (view != null && (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int[] sourceCoordinates = new int[2];
            view.getLocationOnScreen(sourceCoordinates);
            float x = event.getRawX() + view.getLeft() - sourceCoordinates[0];
            float y = event.getRawY() + view.getTop() - sourceCoordinates[1];

            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) {
                closeKeyboard();
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        chatID = getIntent().getStringExtra("id");
        oppositeUID = getIntent().getStringExtra("uid");
        if (!oppositeUID.equals("1"))
            readToken(oppositeUID);
        clicklistener();
        loadUserData();
        loadMessages();
    }

    public void readToken(String uid) {
        DocumentReference reference = FirebaseFirestore.getInstance().collection("User").document(uid);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Lấy trường token từ tài liệu người dùng
                        token = document.getString("fcmToken");
                        Log.d("Firestore", "Token: " + token);
                    } else {
                        Log.d("Firestore", "Không tìm thấy tài liệu");
                    }
                } else {
                    Log.e("Firestore", "Lỗi khi lấy dữ liệu", task.getException());
                }
            }
        });
    }

    public void clicklistener() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = chatET.getText().toString().trim();
                if (message.isEmpty()) {
                    return;
                }
                CollectionReference reference = FirebaseFirestore.getInstance().collection("Messages");
                Map<String, Object> map = new HashMap<>();
                map.put("lastMessage", message);
                map.put("time", FieldValue.serverTimestamp());
                reference.document(chatID).update(map);
                String messageID = reference
                        .document(chatID)
                        .collection("Messages")
                        .document()
                        .getId();
                Map<String, Object> messageMap = new HashMap<>();
                messageMap.put("id", messageID);
                messageMap.put("message", message);
                messageMap.put("senderID", user.getUid());
                messageMap.put("time", FieldValue.serverTimestamp());
                reference.document(chatID).collection("Messages").document(messageID).set(messageMap)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                chatET.setText("");
                                if (!oppositeUID.equals("1"))
                                    sendNotification(message);
                            } else {
                                Toast.makeText(ChatActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });
    }

    public void sendNotification(String message) {
        FireBaseUtil.currentUserDetail().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Users currentUser = task.getResult().toObject(Users.class);
                    try {
                        JSONObject jsonObject = new JSONObject();
                        JSONObject notificationObj = new JSONObject();
                        notificationObj.put("title", currentUser.getName());
                        notificationObj.put("body", message);

                        JSONObject dataObj = new JSONObject();
                        dataObj.put("userId", currentUser.getUid());

                        jsonObject.put("notification", notificationObj);
                        jsonObject.put("data", dataObj);
                        jsonObject.put("to", token);

                        callApi(jsonObject);
                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    void callApi(JSONObject jsonObject) {
        MediaType JSON = MediaType.get("application/json");

        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Bearer AAAAro3DwRA:APA91bGkU5CTR11JSryE0ZwIdoSCgufYoSNhVajESiy5cMCxS9P8xYm40KJFMzsCSqm8IGZyn_1gx0cpUxY006o-zOywYEYH9EbKxQb2tq71qkZvTw62bLFKK90csF8ExGcCxhnPPdox")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }

    void init() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        backButton = findViewById(R.id.back);
        imageView = findViewById(R.id.profileImage);
        name = findViewById(R.id.nameTV);
        status = findViewById(R.id.statusTV);
        chatET = findViewById(R.id.chatET);
        sendBtn = findViewById(R.id.sendBtn);
        recyclerView = findViewById(R.id.chatRecyclerView);
        list = new ArrayList<>();
        adapter = new ChatAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    void loadUserData() {
        if (!oppositeUID.equals("1")) {
            FirebaseFirestore.getInstance().collection("User").document(oppositeUID)
                    .addSnapshotListener((value, error) -> {

                        if (error != null)
                            return;

                        if (value == null)
                            return;
                        if (!value.exists())
                            return;
                        boolean isOnline = Boolean.TRUE.equals(value.getBoolean("online"));
                        if (isOnline) {
                            status.setText("Online");
                            imageView.setBorderColor(ContextCompat.getColor(this, R.color.green));
                            status.setTextColor(ContextCompat.getColor(this, R.color.green));
                        } else {
                            status.setText("Offline");
                            status.setTextColor(ContextCompat.getColor(this, R.color.red));
                            imageView.setBorderColor(ContextCompat.getColor(this, R.color.red));
                        }

                        Glide.with(getApplicationContext()).load(value.getString("profileImage")).into(imageView);
                        name.setText(value.getString("name"));
                    });
        } else {
            DocumentReference documentRef = FirebaseFirestore.getInstance().collection("Messages").document(chatID);
            documentRef.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Lấy giá trị của trường "name"
                            String namegroup = documentSnapshot.getString("name");
                            this.name.setText(namegroup.toString());
                            status.setVisibility(View.GONE);
                            imageView.setImageResource(R.drawable.groups);
                            // Sử dụng giá trị "name" theo nhu cầu của bạn
                        }

                    });
        }
    }

    void loadMessages() {

        CollectionReference reference = FirebaseFirestore.getInstance()
                .collection("Messages")
                .document(chatID)
                .collection("Messages");
        reference
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;

                    if (value == null || value.isEmpty()) return;
                    list.clear();
                    for (QueryDocumentSnapshot snapshot : value) {
                        ChatModel model = snapshot.toObject(ChatModel.class);
                        list.add(model);

                    }
                    adapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(list.size());
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus(true);
    }

    @Override
    protected void onPause() {
        updateStatus(false);
        super.onPause();
    }


    void updateStatus(boolean status) {

        Map<String, Object> map = new HashMap<>();
        map.put("online", status);
        FirebaseFirestore.getInstance()
                .collection("User")
                .document(user.getUid())
                .update(map);
    }
}