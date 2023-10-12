package com.example.ltdd_app_mang_xa_hoi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import Fragments.Chat_Friend_Fragment;
import Fragments.Chat_Group_Fragment;
import Fragments.GroupFragment;

public class Chat_RelationFragment extends Fragment {
    TabLayout tabLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat_relation, container, false);
        tabLayout = view.findViewById(R.id.chat_tab_layout);
        TabLayout.Tab tab1 = tabLayout.newTab().setText("Bạn Bè");

        TabLayout.Tab tab2 = tabLayout.newTab().setText("Nhóm");
        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.chat_fragment_container, new Chat_Friend_Fragment())
                .commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        // Hiển thị Fragment tin nhắn bạn bè
                        getChildFragmentManager().beginTransaction()
                                .replace(R.id.chat_fragment_container, new Chat_Friend_Fragment())
                                .commit();
                        break;
                    case 1:
                        // Hiển thị Fragment tin nhắn nhóm
                        getChildFragmentManager().beginTransaction()
                                .replace(R.id.chat_fragment_container, new Chat_Group_Fragment())
                                .commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }
}