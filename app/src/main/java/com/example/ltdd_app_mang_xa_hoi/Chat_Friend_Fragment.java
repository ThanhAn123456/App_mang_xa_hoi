package com.example.ltdd_app_mang_xa_hoi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class Chat_Friend_Fragment extends Fragment {
    ListView lw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_friend, container, false);
        lw = view.findViewById(R.id.listviewfriend_chat);

        return view;
    }
}