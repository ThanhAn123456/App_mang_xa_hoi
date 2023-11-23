package com.example.ltdd_app_mang_xa_hoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    LinearLayout LL_BacktoLogin;
    TextInputLayout til_Email;
    ProgressBar progressBar;
    private FirebaseAuth auth;

    Button btnRecover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        btnRecover = findViewById(R.id.btnRecover);
        //ánh xạ id
        LL_BacktoLogin = findViewById(R.id.LL_BacktoLogin);
        til_Email = findViewById(R.id.til_Gmail);
        //làm thông báo err ko đc để trống
        LL_BacktoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = til_Email.getEditText().getText().toString();
                if (email.isEmpty()) {
                    til_Email.setError("Email chưa đúng");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgotPasswordActivity.this, "Khôi phục mật khẩu thành công, vui lòng kiểm tra email", Toast.LENGTH_SHORT).show();
                                    til_Email.getEditText().setText("");
                                } else {
                                    String errMes = task.getException().getMessage();
                                    Toast.makeText(ForgotPasswordActivity.this, "Bị lỗi:" + errMes, Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }


}