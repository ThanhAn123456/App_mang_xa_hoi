package com.example.ltdd_app_mang_xa_hoi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import Adapters.NotificationAdapter;
import Dto.ThongBao;
import Dto.ThongBao;

public class NotificationsFragment extends Fragment {
    ListView listview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        listview = view.findViewById(R.id.listviewnotification);
        ArrayList<ThongBao> arrayList = new ArrayList<>();
        arrayList.add(new ThongBao(R.drawable.avatar, "Nguyen Thanh An", "đã thích bài viết của bạn"));
        arrayList.add(new ThongBao(R.drawable.avatar, "Nguyen Thanh An", "đã bình luận về bài viết của bạn"));
        arrayList.add(new ThongBao(R.drawable.avatar, "Nguyen Thanh An", "đã chia sẻ bài viết của bạn"));
        NotificationAdapter adapter = new NotificationAdapter(getContext(), R.layout.thongbao, arrayList);
        listview.setAdapter(adapter);
        return view;
    }
}
