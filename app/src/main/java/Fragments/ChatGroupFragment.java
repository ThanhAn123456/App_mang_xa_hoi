package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ltdd_app_mang_xa_hoi.R;

import java.util.ArrayList;

import Adapters.ListChatAdapter;
import Entity.Lv_ListChat;


public class ChatGroupFragment extends Fragment {

    ListView lv_listchat;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_chat_group, container, false);
        ArrayList<Lv_ListChat> arrayList = new ArrayList<>();
        lv_listchat= view.findViewById(R.id.lv_listchat);
        arrayList.add(new Lv_ListChat(R.drawable.groups, "LTDD", -1,2));
        ListChatAdapter adapter = new ListChatAdapter(getContext(),R.layout.lv_listchat,arrayList);
        lv_listchat.setAdapter(adapter);
        return view;
    }
}