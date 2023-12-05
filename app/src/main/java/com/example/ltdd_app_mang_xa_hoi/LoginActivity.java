package com.example.ltdd_app_mang_xa_hoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    LinearLayout LL_GotoSignup;
    ProgressBar progressBar;
    TextView txt_ForgotPassword;
    TextInputLayout til_Username, til_Password;
    MaterialButton btn_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        init();
        //làm thông báo err ko đc để trống
        TextChangedListener_errEmty(til_Username);
        TextChangedListener_errEmty(til_Password);
        clicklistener();

    }
    public void clicklistener(){
        LL_GotoSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        txt_ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = til_Username.getEditText().getText().toString();
                String password = til_Password.getEditText().getText().toString();
                if (username.isEmpty()) {
                    til_Username.setError("Input valid username");
                    return;
                }
                if (password.isEmpty()) {
                    til_Password.setError("Input valid password");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    FirebaseUser user = auth.getCurrentUser();

                                    if (user.isEmailVerified()) {
                                        sendUserToMainActivity();
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.verify), Toast.LENGTH_SHORT).show();
                                        auth.signOut();
                                    }

                                } else {
                                    String exception = "Error: " + task.getException().getMessage();
                                    Toast.makeText(LoginActivity.this, exception, Toast.LENGTH_SHORT).show();

                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }
    public void init(){
        progressBar = findViewById(R.id.progressBar);
        //ánh xạ id
        LL_GotoSignup = findViewById(R.id.LL_GotoSignup);
        txt_ForgotPassword = findViewById(R.id.txt_ForgotPassword);
        til_Username = findViewById(R.id.til_Username);
        til_Password = findViewById(R.id.til_Password);
        btn_Login = findViewById(R.id.btn_Login);
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
    private void sendUserToMainActivity() {
        progressBar.setVisibility(View.GONE);
        Intent intent = new Intent(LoginActivity.this, MainContainerActivity.class);
        startActivity(intent);
        finish();
    }
}