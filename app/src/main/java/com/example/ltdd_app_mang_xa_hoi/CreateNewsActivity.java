package com.example.ltdd_app_mang_xa_hoi;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CreateNewsActivity extends AppCompatActivity {
    ImageButton backButton;
    ImageView imagePost;
    ImageView avatar;
    TextView displayname;
    ImageButton select_imagePost;
    Spinner spn_statuspost;
    Button btnPost;
    Uri resulturi;
    EditText textpost;
    private FirebaseUser user;
    DocumentReference userRef;
    ActivityResultLauncher<String> mGetContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_news);
        avatar = findViewById(R.id.avatar);
        displayname = findViewById(R.id.displayname);
        select_imagePost = findViewById(R.id.select_imagepost);
        backButton = findViewById(R.id.back);
        textpost = findViewById(R.id.textpost);
        spn_statuspost = findViewById(R.id.spn_statuspost);
        imagePost = findViewById(R.id.imagepost);
        btnPost = findViewById(R.id.btnPost);
        user = FirebaseAuth.getInstance().getCurrentUser();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ArrayList arrayList = new ArrayList();
        arrayList.add(getResources().getString(R.string.stt_public));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_statuspost.setAdapter(arrayAdapter);
        select_imagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent = new Intent(CreateNewsActivity.this, CropperActivity.class);
                intent.putExtra("DATA", result.toString());
                startActivityForResult(intent, 101);
            }
        });
        btnPost.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                            if (resulturi!=null){
                                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                                StorageReference storageReference = storage.getReference().child("Post Images/" + System.currentTimeMillis());
                                                storageReference.putFile(resulturi)
                                                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                                if (task.isSuccessful())
                                                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                        @Override
                                                                        public void onSuccess(Uri uri) {
                                                                            uploadData(uri.toString());
                                                                        }
                                                                    });
                                                                else {
                                                                    Log.d("Loi", "" + task.getException().toString());
                                                                }
                                                            }
                                                        });
                                            }
                                            else {
                                                Toast.makeText(CreateNewsActivity.this,getString(R.string.canoneimage),Toast.LENGTH_SHORT).show();
                                            }

                                       }
                                   }
        );
        loadBasicData();
    }

    private void uploadData(String imageURL) {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("User")
                .document(user.getUid()).collection("Post Images");
        String description = textpost.getText() + "";
        String id = reference.document().getId();
        List<String> listlike = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("description", description);
        map.put("timestamp", FieldValue.serverTimestamp());
        map.put("imageUrl", imageURL);
        map.put("name", user.getDisplayName() + "");
        map.put("uid", user.getUid());
        map.put("profileImage", String.valueOf(user.getPhotoUrl()));
        map.put("likes", listlike);


        reference.document(id).set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateNewsActivity.this, "", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CreateNewsActivity.this, "Error: " + task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == 101) {
            String result = data.getStringExtra("RESULT");
            resulturi = null;
            if (result != null) {
                resulturi = Uri.parse(result);
            }
            imagePost.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(resulturi)
                    .into(imagePost);
            //imagePost.setImageURI(resulturi);
        }
    }

    private void loadBasicData() {
        userRef = FirebaseFirestore.getInstance().collection("User").document(user.getUid());
        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }

                assert value != null;
                if (value.exists()) {
                    String imageavatar = value.getString("profileImage");
                    String name = value.getString("name");
                    displayname.setText(name + "");

                    // Kiểm tra trạng thái của Activity trước khi sử dụng Glide
                    if (!isDestroyed() && !isFinishing()) {
                        Glide.with(CreateNewsActivity.this)
                                .load(imageavatar)
                                .placeholder(R.drawable.avatar)
                                .timeout(6500)
                                .into(avatar);
                    }
                }
            }
        });
    }
}