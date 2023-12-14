package com.example.ltdd_app_mang_xa_hoi;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import MainFragment.HomeFragment;

public class CreateNewsActivity extends AppCompatActivity {
    ImageButton backButton;
    ImageView imagePost;
    ImageView avatar;
    String imageavatar;
    TextView displayname;
    ImageButton select_imagePost, select_Camera;
    Spinner spn_statuspost;
    Button btnPost;
    Uri resulturi;
    EditText textpost;
    FirebaseUser user;
    DocumentReference userRef;
    ProgressBar progressBar;
    ActivityResultLauncher<String> mGetContent;
    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_SELECT_IMAGE = 101;
    private Uri capturedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_news);
        user = FirebaseAuth.getInstance().getCurrentUser();
        init();
        clicklistener();
        ArrayList arrayList = new ArrayList();
        arrayList.add(getResources().getString(R.string.stt_public));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_statuspost.setAdapter(arrayAdapter);
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    Intent intent = new Intent(CreateNewsActivity.this, CropperActivity.class);
                    intent.putExtra("DATA", result.toString());
                    startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
                } else {
                    // Xử lý khi result là null (nếu cần)
                }
            }
        });

        loadBasicData();
    }

    public void clicklistener() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        select_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (ActivityCompat.checkSelfPermission(CreateNewsActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(CreateNewsActivity.this,new String[]{Manifest.permission.CAMERA},1);
                    return;
                }
                capturedImageUri = createImageUri();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);

                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });
        select_imagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });
        btnPost.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           if (resulturi != null) {
                                               progressBar.setVisibility(View.VISIBLE);
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
                                                                           progressBar.setVisibility(View.GONE);
                                                                           Toast.makeText(CreateNewsActivity.this, "Đã đăng", Toast.LENGTH_SHORT).show();
                                                                           finish();

                                                                           // Khởi động lại activity
                                                                           startActivity(new Intent(CreateNewsActivity.this, CreateNewsActivity.class));
                                                                       }
                                                                   });
                                                               else {
                                                                   Log.d("Loi", "" + task.getException().toString());
                                                                   progressBar.setVisibility(View.GONE);
                                                               }
                                                           }
                                                       });
                                           } else {
                                               Toast.makeText(CreateNewsActivity.this, getString(R.string.canoneimage), Toast.LENGTH_SHORT).show();
                                           }

                                       }
                                   }
        );
    }

    public void init() {
        avatar = findViewById(R.id.avatar);
        displayname = findViewById(R.id.displayname);
        select_imagePost = findViewById(R.id.select_imagepost);
        backButton = findViewById(R.id.back);
        textpost = findViewById(R.id.textpost);
        spn_statuspost = findViewById(R.id.spn_statuspost);
        imagePost = findViewById(R.id.imagepost);
        btnPost = findViewById(R.id.btnPost);
        progressBar = findViewById(R.id.progressBar);
        select_Camera = findViewById(R.id.select_camera);
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
        map.put("profileImage", imageavatar);
        map.put("likes", listlike);


        reference.document(id).set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        } else {
                            Toast.makeText(CreateNewsActivity.this, "Error: " + task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        resulturi = null;
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_SELECT_IMAGE)
        {
            String result = data.getStringExtra("RESULT");
            if (result != null) {
                resulturi = Uri.parse(result);
            }
            imagePost.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(resulturi)
                    .into(imagePost);
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CAMERA)
        {
            if (capturedImageUri != null) {

                imagePost.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(capturedImageUri)
                        .into(imagePost);
                resulturi=capturedImageUri;
            }
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
                    imageavatar = value.getString("profileImage");
                    String name = value.getString("name");
                    displayname.setText(name + "");

                    // Kiểm tra trạng thái của Activity trước khi sử dụng Glide
                    if (!isDestroyed() && !isFinishing()) {
                        Glide.with(CreateNewsActivity.this)
                                .load(imageavatar)
                                .timeout(6500)
                                .into(avatar);
                    }
                }
            }
        });
    }
    private Uri createImageUri() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (imageFile != null) {
            return FileProvider.getUriForFile(CreateNewsActivity.this, "com.example.ltdd_app_mang_xa_hoi.fileprovider", imageFile);
        }
        return null;
    }
}