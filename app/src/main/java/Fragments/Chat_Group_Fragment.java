package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ltdd_app_mang_xa_hoi.R;

import java.util.ArrayList;

import Adapters.Chat_Friend_GroupAdapter;
import Adapters.Friend_GroupAdapter;
import Dto.Chat_Friend_Group;
import Dto.Friend_Group;

public class Chat_Group_Fragment extends Fragment {

    ListView listview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat_group, container, false);
        listview = view.findViewById(R.id.listviewgroup_chat);
        ArrayList<Chat_Friend_Group> arrayList = new ArrayList<>();
        arrayList.add(new Chat_Friend_Group(R.drawable.two_person, "Nhóm LT Di dộng", "online", 60, 4));
        Chat_Friend_GroupAdapter adapter = new Chat_Friend_GroupAdapter(getContext(), R.layout.friend_group_status, arrayList);
        listview.setAdapter(adapter);
        return view;
    }
}