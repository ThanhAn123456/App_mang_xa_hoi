package com.example.ltdd_app_mang_xa_hoi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
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
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.CommentAdapter;
import Entity.CommentModel;
import Entity.Users;
import Util.FireBaseUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    String  description,name,profileImage,imagecontent;
    Date timestamp;
    ImageButton imagecmt,imageshare;
    CheckBox imagelike;
    ImageView avatar,imageContent;
    TextView contentnews,fullname,timepost,numlike;
    String token;
    DocumentReference documentReference;
    List<String> likeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        init();
        loadPostContent();
        reference = FirebaseFirestore.getInstance().collection("User")
                .document(uid)
                .collection("Post Images")
                .document(id)
                .collection("Comments");

        loadCommentData();

        clickListener();

    }

    private void init() {
        numlike=findViewById(R.id.numberlike);
        imageContent=findViewById(R.id.imagecontent);
        avatar=findViewById(R.id.profileImage);
        contentnews= findViewById(R.id.contentnews);
        fullname=findViewById(R.id.name);
        timepost=findViewById(R.id.time);
        imagelike= findViewById(R.id.like);
        imagecmt=findViewById(R.id.cmt);
        imageshare=findViewById(R.id.share);
        backButton = findViewById(R.id.back);
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
        /*likeList = (ArrayList<String>) intent.getSerializableExtra("listLike");*/


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
    @RequiresApi(api = Build.VERSION_CODES.O)
    String calculateTime(Date date) {
        if (date != null) {
            long millis = date.toInstant().toEpochMilli();
            return DateUtils.getRelativeTimeSpanString(millis, System.currentTimeMillis(), 60000, DateUtils.FORMAT_ABBREV_TIME).toString();
        } else {
            return ""; // hoặc giá trị mặc định khác tùy vào yêu cầu của bạn
        }
    }
    public void loadPostContent(){

        documentReference= FirebaseFirestore.getInstance().collection("User").document(uid).collection("Post Images").document(id);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot value = task.getResult();

                    if (value.exists()) {
                    profileImage = value.getString("profileImage");
                    description =value.getString("description");
                            likeList = (ArrayList<String>) value.get("likes");
                    name=value.getString("name");
                    Timestamp time = (Timestamp) value.get("timestamp");
                    timestamp = time != null ? time.toDate() : null;
                    imagecontent=value.getString("imageUrl");
                        numlike.setText(likeList.size()+"");
                        if (likeList.contains(user.getUid()))
                            imagelike.setChecked(true);
                        else
                            imagelike.setChecked(false);
                        imagelike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (likeList.contains(user.getUid())) {
                                    likeList.remove(user.getUid()); // Bỏ like
                                    readToken(uid);
                                    sendNotification(user.getDisplayName()+" đã bỏ like bài viết của bạn");
                                    numlike.setText(likeList.size()+"");
                                } else {
                                    likeList.add(user.getUid()); // Like
                                    readToken(uid);
                                    sendNotification(user.getDisplayName()+" đã like bài viết của bạn");
                                    numlike.setText(likeList.size()+"");
                                }


                                // Gửi cập nhật đến Firestore
                                DocumentReference reference = FirebaseFirestore.getInstance().collection("User")
                                        .document(uid)
                                        .collection("Post Images")
                                        .document(id);

                                Map<String, Object> map = new HashMap<>();
                                map.put("likes", likeList);
                                reference.update(map);
                            }

                        });
                }

                Glide.with(avatar.getContext())
                        .load(profileImage)
                        .into(avatar);
                     contentnews.setText(description);
                     fullname.setText(name);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    timepost.setText(calculateTime(timestamp));
                }
                Glide.with(imageContent.getContext())
                        .load(imagecontent)
                        .into(imageContent);


                }
            }
        });
    }
    private void clickListener() {


        imageshare.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, imagecontent);
            intent.setType("text/*");
            startActivity(Intent.createChooser(intent, "Share link using..."));

        });

        imagecmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                commentEt.requestFocus();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
                        dataObj.put("userIdLike", currentUser.getUid());

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


}