package com.example.ltdd_app_mang_xa_hoi;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Config.CustomGridLayoutManager;
import Entity.PostImageModel;
import de.hdodenhof.circleimageview.CircleImageView;

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
    String uid;
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
                                        Toast.makeText(ProfileActivity.this, "UnFollowed", Toast.LENGTH_SHORT).show();
                                    } else {
                                        assert task1.getException() != null;
                                        Log.e("Tag_3", task1.getException().getMessage());
                                    }
                                });
                            } else {
                                Log.d("Error", "" + task.getException().getMessage());
                            }
                        }
                    });
                } else {
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
                                        Toast.makeText(ProfileActivity.this, "Followed", Toast.LENGTH_SHORT).show();
                                    } else {
                                        assert task12.getException() != null;
                                        Log.e("tag_3_1", task12.getException().getMessage());
                                    }
                                });
                            } else {
                                Log.d("Error", "" + task.getException().getMessage());
                            }
                        }
                    });
                }
            }
        });
        btn_sendmess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });

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
                    if (followersList.contains(uid)) {
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

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}