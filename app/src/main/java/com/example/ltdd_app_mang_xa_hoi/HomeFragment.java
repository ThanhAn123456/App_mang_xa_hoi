package com.example.ltdd_app_mang_xa_hoi;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import Adapters.ListNews_Adapter;
import Dto.Lv_ListNews;

public class HomeFragment extends Fragment {
    ListView listview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        TextView textView = view.findViewById(R.id.taobaiviet);

                textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), CreateNewsActivity.class);
                startActivity(intent);
            }
        });

        listview = view.findViewById(R.id.listviewhome);
        ArrayList<Lv_ListNews> arrayList = new ArrayList<>();
        arrayList.add(new Lv_ListNews(R.drawable.avatar, "Nguyen Thanh An","Hôm qua lúc 7:00",R.drawable.img_content));
        arrayList.add(new Lv_ListNews(R.drawable.avatar, "Nguyen Thanh An","Hôm qua lúc 7:00",R.drawable.anh));
        arrayList.add(new Lv_ListNews(R.drawable.avatar, "Nguyen Thanh An","Hôm qua lúc 7:00",R.drawable.img_content));
        ListNews_Adapter adapter = new ListNews_Adapter(getContext(),R.layout.lv_listnews,arrayList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), NewsDetailActivity.class);
                startActivity(intent);
            }
        });
        return view;

    }
}