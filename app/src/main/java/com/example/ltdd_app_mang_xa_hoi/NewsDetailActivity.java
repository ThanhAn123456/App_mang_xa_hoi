package com.example.ltdd_app_mang_xa_hoi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import Adapters.ListCmtAdapter;
import Adapters.ListNews_Adapter;
import Entity.Lv_ListCmt;
import Entity.Lv_ListNews;

public class NewsDetailActivity extends AppCompatActivity {
    ImageView backButton;
    ListView lv_listnews;
    RecyclerView lv_cmt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        backButton = findViewById(R.id.back);
        lv_listnews = findViewById(R.id.lv_listnews);
        lv_cmt = findViewById(R.id.lv_listcmt);
        EditText editText = findViewById(R.id.writecmt); // Thay thế bằng ID thật của EditText
        if (getIntent().getBooleanExtra("focusOnEditText", false)) {
            editText.requestFocus();
        }
        ArrayList<Lv_ListNews> arrayList1 = new ArrayList<>();
        arrayList1.add(new Lv_ListNews(R.drawable.avatar, "Nguyen Thanh An","Hôm qua lúc 7:00","Ahihi",R.drawable.anh,1,5,2,3));
        ListNews_Adapter adapter1 = new ListNews_Adapter(this,R.layout.lv_listnews,arrayList1);
        lv_listnews.setAdapter(adapter1);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ArrayList<Lv_ListCmt> arrayList = new ArrayList<>();
        arrayList.add(new Lv_ListCmt(R.drawable.avatar, "Nguyen Thanh An","Ahihi",-1,0));
        arrayList.add(new Lv_ListCmt(R.drawable.avatar, "Nguyen Thanh An","Leu",-1,0));
        arrayList.add(new Lv_ListCmt(R.drawable.avatar, "Nguyen Thanh An","Leu",-1,0));
        arrayList.add(new Lv_ListCmt(R.drawable.avatar, "Nguyen Thanh An","Leu",-1,0));
        arrayList.add(new Lv_ListCmt(R.drawable.avatar, "Nguyen Thanh An","Leu",-1,0));
        ListCmtAdapter adapter = new ListCmtAdapter(this, arrayList);
        lv_cmt.setAdapter(adapter);
        lv_cmt.setLayoutManager(new LinearLayoutManager(this));
    }
}