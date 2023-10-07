package com.example.ltdd_app_mang_xa_hoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    LinearLayout LL_GotoSignup;
    TextView txt_ForgotPassword;
    TextInputLayout til_Username,til_Password;
    MaterialButton btn_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //ánh xạ id
        LL_GotoSignup = findViewById(R.id.LL_GotoSignup);
        txt_ForgotPassword =findViewById(R.id.txt_ForgotPassword);
        til_Username = findViewById(R.id.til_Username);
        til_Password = findViewById(R.id.til_Password);
        btn_Login = findViewById(R.id.btn_Login);
        //làm thông báo err ko đc để trống
        TextChangedListener_errEmty(til_Username);
        TextChangedListener_errEmty(til_Password);
        LL_GotoSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,signup.class);
                startActivity(intent);
            }
        });
        txt_ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(MainActivity.this, forgotpassword.class);
                startActivity(intent);
            }
        });
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(MainActivity.this, Main_container.class);
                startActivity(intent);
            }
        });
    }
    private void TextChangedListener_errEmty(TextInputLayout textinputlayout){
        textinputlayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String TaiKhoan = textinputlayout.getEditText().getText().toString();
                if(TaiKhoan.equals("")) {
                    textinputlayout.setError("Not Empty");
                }else {
                    textinputlayout.setError("");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
}