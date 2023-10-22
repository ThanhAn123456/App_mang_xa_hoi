package com.example.ltdd_app_mang_xa_hoi;


import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CreateNewsActivity extends AppCompatActivity {
    ImageView backButton;
    Spinner spn_statuspost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_news);
        backButton = findViewById(R.id.back);
        spn_statuspost=findViewById(R.id.spn_statuspost);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ArrayList arrayList =new ArrayList();
        arrayList.add(getResources().getString(R.string.stt_public));
        arrayList.add(getResources().getString(R.string.stt_private));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_statuspost.setAdapter(arrayAdapter);

    }
}