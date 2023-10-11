package com.example.ltdd_app_mang_xa_hoi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;

import Adapters.Lv_ContentAdapter;
import Dto.Lv_Setting;

public class SettingActivity extends AppCompatActivity {
ImageView backButton;
ListView listViewSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        backButton = findViewById(R.id.back);
        listViewSetting = findViewById(R.id.lv_setting);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ArrayList<Lv_Setting> arrayList = new ArrayList<>();
        arrayList.add(new Lv_Setting(R.drawable.ic_profile_select,R.string.setting_1));
        arrayList.add(new Lv_Setting(R.drawable.ic_report,R.string.setting_2));
        arrayList.add(new Lv_Setting(R.drawable.ic_profile_select,R.string.setting_3));
        arrayList.add(new Lv_Setting(R.drawable.ic_logout,R.string.setting_4));
        Lv_ContentAdapter lvContentAdapter= new Lv_ContentAdapter(SettingActivity.this,R.layout.lv_setting,arrayList);
        listViewSetting.setAdapter(lvContentAdapter);

        listViewSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent intent0 = new Intent(SettingActivity.this, UpdateAccountActivity.class);
                        startActivity(intent0);
                        break;
                    case 1:
                        Intent intent1 = new Intent(SettingActivity.this, ReportActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        });
    }

}