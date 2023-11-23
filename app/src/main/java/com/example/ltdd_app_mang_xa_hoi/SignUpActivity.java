package com.example.ltdd_app_mang_xa_hoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    LinearLayout LL_BacktoLogin;
    TextInputLayout til_Name, til_Gmail, til_Password;
    Button btn_signup;
    ProgressBar progressBar;
    private FirebaseAuth auth;
    public static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //ánh xạ id
        progressBar = findViewById(R.id.progressBar);
        LL_BacktoLogin = findViewById(R.id.LL_BacktoLogin);
        til_Name = findViewById(R.id.til_Name);
        til_Gmail = findViewById(R.id.til_Gmail);
        btn_signup = findViewById(R.id.btn_Signup);

        til_Password = findViewById(R.id.til_Password);
        auth = FirebaseAuth.getInstance();
        //làm thông báo err ko đc để trống
        TextChangedListener_errEmty(til_Name);
        TextChangedListener_errEmty(til_Gmail);
        TextChangedListener_errEmty(til_Password);
        LL_BacktoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = til_Name.getEditText().getText().toString();
                String email = til_Gmail.getEditText().getText().toString();
                if (email.isEmpty())
                    til_Gmail.setError(getResources().getString(R.string.erroremail));

                String pass = til_Password.getEditText().getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                createAccount(name, email, pass);
            }
        });

    }

    private void createAccount(String name, String email, String pass) {
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> taskVerification) {
                                            if (taskVerification.isSuccessful()) {
                                                // Email verification link sent successfully
                                                Toast.makeText(SignUpActivity.this, "Email verification link sent", Toast.LENGTH_SHORT).show();

                                                // Cập nhật thông tin hồ sơ và upload dữ liệu khi email đã được xác nhận
                                                UserProfileChangeRequest.Builder request = new UserProfileChangeRequest.Builder();
                                                request.setDisplayName(name);
                                                user.updateProfile(request.build());
                                                uploadUser(user, name, email);
                                            } else {
                                                // Email verification link sending failed
                                                Toast.makeText(SignUpActivity.this, "Error sending verification email", Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        } else {
                            // Tạo tài khoản không thành công
                            progressBar.setVisibility(View.GONE);
                            String exception = task.getException().getMessage();
                            Toast.makeText(SignUpActivity.this, "Error" + exception, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void uploadUser(FirebaseUser user, String name, String email) {
        Map<String, Object> map = new HashMap<>();
        List<String> followers = new ArrayList<>();
        List<String> following = new ArrayList<>();
        map.put("name", name);
        map.put("email", email);
        map.put("profileImage", "https://inkythuatso.com/uploads/thumbnails/800/2023/03/8-anh-dai-dien-trang-inkythuatso-03-15-26-54.jpg");
        map.put("coverImage", " ");
        map.put("dob", "");
        map.put("followers", followers);
        map.put("following", following);
        map.put("uid", user.getUid());
        map.put("status", " ");
        map.put("search", name.toLowerCase());

        FirebaseFirestore.getInstance().collection("User").document(user.getUid())
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            assert SignUpActivity.this != null;
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            String exception = task.getException().getMessage();
                            Toast.makeText(SignUpActivity.this, "Error" + exception, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void TextChangedListener_errEmty(TextInputLayout textinputlayout) {
        textinputlayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String TaiKhoan = textinputlayout.getEditText().getText().toString();
                if (TaiKhoan.equals("")) {
                    textinputlayout.setError("Not Empty");
                } else {
                    textinputlayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}