package com.example.ltdd_app_mang_xa_hoi;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import MainFragment.AccountFragment;

public class UpdateAccountActivity extends AppCompatActivity {
    ImageButton backButton;
    Button btn_Save;
    TextView txtname, txtstatus;
    ImageButton editprofileimage, changecoverimage;
    ImageView avatar, cover;
    String uid;
    ActivityResultLauncher<String> mGetAvatar, mGetCover;
    Uri resulturiavatar, resulturicover;
    FirebaseUser user;
    DocumentReference myRef;
    String URLAvatar = "", URLCover = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        btn_Save = findViewById(R.id.btn_Save);
        backButton = findViewById(R.id.back);
        txtname = findViewById(R.id.txt_name);
        txtstatus = findViewById(R.id.txt_status);
        avatar = findViewById(R.id.profile_photo);
        cover = findViewById(R.id.cover_photo);
        editprofileimage = findViewById(R.id.editprofileimage);
        changecoverimage = findViewById(R.id.btn_changecover);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        loadBasicData();
        editprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetAvatar.launch("image/*");
            }
        });
        changecoverimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetCover.launch("image/*");
            }
        });
        mGetAvatar = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent = new Intent(UpdateAccountActivity.this, CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, 101);
            }
        });
        mGetCover = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent = new Intent(UpdateAccountActivity.this, CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, 102);
            }
        });

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 101) {
            String result = data.getStringExtra("RESULT");
            resulturiavatar = null;
            if (result != null) {
                resulturiavatar = Uri.parse(result);
            }
            avatar.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(resulturiavatar)
                    .into(avatar);
            //imagePost.setImageURI(resulturi);
        }
        if (resultCode == -1 && requestCode == 102) {
            String result = data.getStringExtra("RESULT");
            resulturicover = null;
            if (result != null) {
                resulturicover = Uri.parse(result);
            }
            cover.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(resulturicover)
                    .into(cover);
            //imagePost.setImageURI(resulturi);
        }
    }

    public void loadBasicData() {
        myRef = FirebaseFirestore.getInstance().collection("User").document(uid);
        final Activity currentActivity = this; // Lưu lại tham chiếu đến activity hiện tại

        myRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (currentActivity.isDestroyed() || currentActivity.isFinishing()) {
                    // Activity đã bị hủy, không nên thực hiện thao tác nào đó ở đây
                    return;
                }

                if (error != null)
                    return;

                assert value != null;
                if (value.exists()) {
                    String name = value.getString("name");
                    String status = value.getString("status");
                    String imageProfile = value.getString("profileImage");
                    String imageCover = value.getString("coverImage");
                    Random random = new Random();
                    int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
                    if (name.equals(""))
                        txtname.setHint(getString(R.string.inputname) + "");
                    else {
                        txtname.setHintTextColor(color);
                        txtname.setHint(name);

                    }
                    if (status.equals(""))
                        txtstatus.setHint(getString(R.string.inputstatus) + "");
                    else {
                        txtstatus.setHintTextColor(color);
                        txtstatus.setHint(status);
                    }
                    ColorDrawable colorDrawable = new ColorDrawable(color);

                    // Glide loading
                    Glide.with(currentActivity)
                            .load(imageProfile)
                            .placeholder(colorDrawable)
                            .into(avatar);
                    Glide.with(currentActivity)
                            .load(imageCover)
                            .placeholder(colorDrawable)
                            .into(cover);
                }
            }
        });
    }

    public void uploadData() {
        final StorageReference refAvatar = FirebaseStorage.getInstance().getReference().child("Profile Images");
        final StorageReference refCover = FirebaseStorage.getInstance().getReference().child("Cover Images");

        List<Task<Uri>> tasks = new ArrayList<>();

        if (resulturiavatar != null) {
            tasks.add(uploadImageAndGetUrl(refAvatar, resulturiavatar, uri1 -> URLAvatar = uri1.toString()));
        }

        if (resulturicover != null) {
            tasks.add(uploadImageAndGetUrl(refCover, resulturicover, uri -> URLCover = uri.toString()));
        }

        if (!tasks.isEmpty()) {
            Tasks.whenAllComplete(tasks).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    updateProfileAndFirestore();
                } else {
                    Toast.makeText(UpdateAccountActivity.this, "Error getting download URLs", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            updateProfileAndFirestore();
        }
    }

    private Task<Uri> uploadImageAndGetUrl(StorageReference storageReference, Uri fileUri, OnSuccessListener<Uri> onSuccessListener) {
        TaskCompletionSource<Uri> tcs = new TaskCompletionSource<>();

        storageReference.putFile(fileUri)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return storageReference.getDownloadUrl();
                })
                .addOnSuccessListener(onSuccessListener)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        tcs.setResult(task.getResult());
                    } else {
                        tcs.setException(task.getException());
                    }
                });

        return tcs.getTask();
    }

    private void updateProfileAndFirestore() {
        myRef = FirebaseFirestore.getInstance().collection("User").document(uid);
        UserProfileChangeRequest.Builder request = new UserProfileChangeRequest.Builder();
        Map<String, Object> map = new HashMap<>();

        if (!URLAvatar.equals("")) {
            map.put("profileImage", URLAvatar);
        }

        if (!URLCover.equals("")) {
            map.put("coverImage", URLCover);
        }

        if (!txtstatus.getText().toString().equals("")) {
            map.put("status", txtstatus.getText().toString());
        }

        if (!txtname.getText().toString().equals("")) {
            map.put("name", txtname.getText().toString());
            request.setDisplayName(txtname.getText().toString());
        }

        user.updateProfile(request.build());
        myRef.update(map).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(UpdateAccountActivity.this, "Updated Successful", Toast.LENGTH_SHORT).show();
            } else {
                assert task.getException() != null;
                Toast.makeText(UpdateAccountActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}