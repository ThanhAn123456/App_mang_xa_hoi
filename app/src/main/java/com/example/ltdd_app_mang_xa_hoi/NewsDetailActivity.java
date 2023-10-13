package com.example.ltdd_app_mang_xa_hoi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import Adapters.ListCmtAdapter;
import Adapters.ListNews_Adapter;
import Dto.Lv_ListCmt;
import Dto.Lv_ListNews;

public class NewsDetailActivity extends AppCompatActivity {
    ImageView backButton;
    ListView lv_listnews;
    ListView lv_cmt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        backButton = findViewById(R.id.back);
        lv_listnews = findViewById(R.id.lv_listnews);
        lv_cmt = findViewById(R.id.lv_listcmt);

        ArrayList<Lv_ListNews> arrayList1 = new ArrayList<>();
        arrayList1.add(new Lv_ListNews(R.drawable.avatar, "Nguyen Thanh An","Hôm qua lúc 7:00",R.drawable.img_content));
        ListNews_Adapter adapter1 = new ListNews_Adapter(this,R.layout.lv_listnews,arrayList1);
        lv_listnews.setAdapter(adapter1);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ArrayList<Lv_ListCmt> arrayList = new ArrayList<>();
        arrayList.add(new Lv_ListCmt(R.drawable.avatar, "Nguyen Thanh An","Ahihi",0));
        arrayList.add(new Lv_ListCmt(R.drawable.avatar, "Nguyen Thanh An","Leu",0));
        ListCmtAdapter adapter = new ListCmtAdapter(this,R.layout.lv_listcmt,arrayList);
        lv_cmt.setAdapter(adapter);
    }
}