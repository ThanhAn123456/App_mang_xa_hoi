package com.example.ltdd_app_mang_xa_hoi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {
    LinearLayout LL_BacktoLogin;
    TextInputLayout til_Birthday,til_Name,til_Gmail,til_Username,til_Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //ánh xạ id
        LL_BacktoLogin = findViewById(R.id.LL_BacktoLogin);
        til_Birthday = findViewById(R.id.til_Birthday);
        til_Name = findViewById(R.id.til_Name);
        til_Gmail = findViewById(R.id.til_Gmail);
        til_Username = findViewById(R.id.til_Username);
        til_Password = findViewById(R.id.til_Password);
        //làm thông báo err ko đc để trống
        TextChangedListener_errEmty(til_Birthday);
        TextChangedListener_errEmty(til_Name);
        TextChangedListener_errEmty(til_Gmail);
        TextChangedListener_errEmty(til_Username);
        TextChangedListener_errEmty(til_Password);
        LL_BacktoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        til_Birthday.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }
    private void openDialog(){
        Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        DatePickerDialog dialog = new DatePickerDialog(this,R.style.DialogThemes , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                til_Birthday.getEditText().setText(String.valueOf(day)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year));
            }
        },nam,thang,ngay);
        dialog.show();
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