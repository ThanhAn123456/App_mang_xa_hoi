package com.example.ltdd_app_mang_xa_hoi;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Config.CustomGridLayoutManager;
import Entity.PostImageModel;
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

public class ProfileActivity extends AppCompatActivity {
    ImageButton backButton;
    Button btn_follow, btn_sendmess;
    List<String> followersList, followingList, followingList_myuser;
    int count;
    ImageView coverimage;
    CircleImageView avatar;
    TextView name, status, numberfollower, numberfollowing, numberpost;
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter<PostImageModel, ProfileActivity.PostImageHolder> adapter;
    DocumentReference userRef, myRef;
    private FirebaseUser user;
    String uid,token;
    boolean isFollowed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        uid = intent.getStringExtra("userId");
        backButton = findViewById(R.id.back);
        btn_follow = findViewById(R.id.btn_follow);
        btn_sendmess = findViewById(R.id.btn_sendmess);
        recyclerView = findViewById(R.id.lv_profileimage);
        name = findViewById(R.id.profile_name);
        avatar = findViewById(R.id.avatar);
        coverimage = findViewById(R.id.cover_photo);
        numberfollower = findViewById(R.id.numberfollower);
        numberfollowing = findViewById(R.id.numberfollowing);
        numberpost = findViewById(R.id.numberpost);
        status = findViewById(R.id.status);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userRef = FirebaseFirestore.getInstance().collection("User")
                .document(uid);
        myRef = FirebaseFirestore.getInstance().collection("User")
                .document(user.getUid());
        loadBasicData();
        loadMyData();
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new GridLayoutManager(ProfileActivity.this,3));
        recyclerView.setLayoutManager(new CustomGridLayoutManager(ProfileActivity.this,3));
        loadPostImages();
        recyclerView.setAdapter(adapter);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFollowed) {
                    followersList.remove(user.getUid()); //xoá người theo dõi bên họ
                    followingList_myuser.remove(uid); //xoá người đang theo dõi bên mình
                    Map<String, Object> map = new HashMap<>();
                    map.put("followers", followersList);
                    final Map<String, Object> map_2 = new HashMap<>();
                    map_2.put("following", followingList_myuser);
                    userRef.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                btn_follow.setText(getString(R.string.follow));
                                myRef.update(map_2).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(ProfileActivity.this, getString(R.string.unfollow), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });

                } else {
                    createNotification();
                    followersList.add(user.getUid());
                    followingList_myuser.add(uid);
                    Map<String, Object> map = new HashMap<>();
                    map.put("followers", followersList);
                    final Map<String, Object> map_2 = new HashMap<>();
                    map_2.put("following", followingList_myuser);

                    userRef.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                btn_follow.setText(getString(R.string.unfollow));
                                myRef.update(map_2).addOnCompleteListener(task12 -> {
                                    if (task12.isSuccessful()) {
                                        Toast.makeText(ProfileActivity.this, getString(R.string.follow), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                    readToken(uid);
                    sendNotification(user.getDisplayName()+ " đã theo dõi bạn");
                }
            }
        });
        btn_sendmess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryChat();
            }
        });
    }
    void queryChat() {
        MaterialDialog progressDialog = new MaterialDialog(ProfileActivity.this, MaterialDialog.getDEFAULT_BEHAVIOR());
        progressDialog.title(R.string.chatstart, "Start chat");
            progressDialog.cancelable(false);
            progressDialog.icon(R.drawable.chat,getResources().getDrawable(R.drawable.chat));

        progressDialog.show();

        CollectionReference reference = FirebaseFirestore.getInstance().collection("Messages");
        reference.whereArrayContains("uid", user.getUid())
                .whereEqualTo("isGroupChat", false)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();
                            progressDialog.dismiss();
                            for (DocumentSnapshot snapshotChat : snapshot) {
                                List<String> uidArray = (List<String>) snapshotChat.get("uid");
                                if (uidArray != null && uidArray.contains(uid)) {
                                    Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
                                    ArrayList<String> uids=new ArrayList<>();
                                    uids.add(user.getUid());
                                    uids.add(uid);

                                    intent.putStringArrayListExtra("uid",uids);
                                    intent.putExtra("id", snapshotChat.getId()); // return doc id
                                    intent.putExtra("isGroupChat",false);
                                    startActivity(intent);
                                    return;
                                }

                            }
                        progressDialog.dismiss();
                        startChat(progressDialog);
                    } else {
                        progressDialog.dismiss();
                    }
                });

    }
    void createNotification() {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Notifications");
        String id = reference.document().getId();
        Map<String, Object> map = new HashMap<>();
        map.put("time", FieldValue.serverTimestamp());
        map.put("notification", user.getDisplayName() +" "+ getString( R.string.followed));
        map.put("id", id);
        map.put("uid", uid);
        reference.document(id).set(map);
    }
    void startChat(MaterialDialog progressDialog) {

        CollectionReference reference = FirebaseFirestore.getInstance().collection("Messages");

        List<String> list = new ArrayList<>();
        list.add(0, user.getUid());
        list.add(1, uid);

        String pushID = reference.document().getId();

        Map<String, Object> map = new HashMap<>();
        map.put("id", pushID);
        map.put("lastMessage", "Hi");
        map.put("time", FieldValue.serverTimestamp());
        map.put("uid", list);
        map.put("isGroupChat",false);
        map.put("name",name.getText().toString().toLowerCase());

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
            Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
            intent.putStringArrayListExtra("uid", (ArrayList<String>) list);
            intent.putExtra("id", pushID);
            intent.putExtra("isGroupChat",false);
            startActivity(intent);

        }, 3000);
    }
    private void loadMyData() {
        myRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Tag_b", error.getMessage());
                    return;
                }

                if (value == null || !value.exists()) {
                    return;
                }
                followingList_myuser = (List<String>) value.get("following");
            }
        });
    }

    private void loadBasicData() {

        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                assert value != null;
                if (value.exists()) {
                    String fullname = value.getString("name") + "";
                    String textstatus = value.getString("status") + "";
                    followersList = (List<String>) value.get("followers");
                    followingList = (List<String>) value.get("following");
                    String profileURL = value.getString("profileImage");
                    String coverURL = value.getString("coverImage");
                    name.setText(fullname);
                    status.setText(textstatus);
                    numberfollower.setText(String.valueOf(followersList.size()));
                    numberfollowing.setText(String.valueOf(followingList.size()));
                    try {
                        Glide.with(ProfileActivity.this)
                                .load(profileURL)

                                .timeout(6500)
                                .into(avatar);
                        Glide.with(ProfileActivity.this)
                                .load(coverURL)

                                .timeout(6500)
                                .into(coverimage);
                    } catch (Exception e) {

                    }
                    if (followersList.contains(user.getUid())) {
                        btn_follow.setText(getString(R.string.unfollow));
                        isFollowed = true;

                    } else {
                        isFollowed = false;
                        btn_follow.setText(getString(R.string.follow));
                    }
                }
            }
        });
    }

    private void loadPostImages() {
        DocumentReference reference = FirebaseFirestore.getInstance().collection("User").document(uid);

        Query query = reference.collection("Post Images");

        FirestoreRecyclerOptions<PostImageModel> options = new FirestoreRecyclerOptions.Builder<PostImageModel>()
                .setQuery(query, PostImageModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<PostImageModel, ProfileActivity.PostImageHolder>(options) {
            @NonNull
            @Override
            public ProfileActivity.PostImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_imageinprofile, parent, false);
                return new ProfileActivity.PostImageHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProfileActivity.PostImageHolder holder, int position, @NonNull PostImageModel model) {

                Glide.with(holder.itemView.getContext().getApplicationContext())
                        .load(model.getImageUrl())
                        .timeout(6500)

                        .into(holder.imageView);
                count = getItemCount();
                numberpost.setText("" + count);
                final String documentId = getSnapshots().getSnapshot(position).getId();
                final String userId = uid;
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(holder.itemView.getContext(), NewsDetailActivity.class);

                        // Pass the document ID and UID as extras to the Intent
                        intent.putExtra("id", documentId);
                        intent.putExtra("uid", userId);

                        // Start the new activity
                        holder.itemView.getContext().startActivity(intent);
                    }
                });
            }

            @Override
            public int getItemCount() {

                return super.getItemCount();
            }
        };

    }

    private static class PostImageHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public PostImageHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
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
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
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
                        dataObj.put("userIdFollow", currentUser.getUid());

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

}