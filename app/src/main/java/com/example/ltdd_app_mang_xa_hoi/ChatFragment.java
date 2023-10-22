package com.example.ltdd_app_mang_xa_hoi;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

import Fragments.ChatFriendFragment;
import Fragments.ChatGroupFragment;


public class ChatFragment extends Fragment {

    ImageView btn_newchat;
    TabLayout tabLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        btn_newchat= view.findViewById(R.id.btn_newchat);
        tabLayout = view.findViewById(R.id.tab_layout);
        TabLayout.Tab tab1 = tabLayout.newTab().setText(R.string.friend);
        TabLayout.Tab tab2 = tabLayout.newTab().setText(R.string.group);
        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ChatFriendFragment())
                .commit();
        btn_newchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getContext(),NewChatActivity.class);
                startActivity(intent);
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        // Hiển thị Fragment tin nhắn bạn bè
                        getChildFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new ChatFriendFragment())
                                .commit();
                        break;
                    case 1:
                        // Hiển thị Fragment tin nhắn nhóm
                        getChildFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new ChatGroupFragment())
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