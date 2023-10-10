package com.example.ltdd_app_mang_xa_hoi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputLayout;

public class ForgotPasswordActivity extends AppCompatActivity {
    LinearLayout LL_BacktoLogin;
    TextInputLayout til_Gmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        //ánh xạ id
        LL_BacktoLogin = findViewById(R.id.LL_BacktoLogin);
        til_Gmail = findViewById(R.id.til_Gmail);
        //làm thông báo err ko đc để trống
        TextChangedListener_errEmty(til_Gmail);
        LL_BacktoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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