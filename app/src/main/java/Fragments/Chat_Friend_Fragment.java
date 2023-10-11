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
import Dto.Chat_Friend_Group;


public class Chat_Friend_Fragment extends Fragment {
    ListView lw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat_friend, container, false);
        lw = view.findViewById(R.id.listviewfriend_chat);
        ArrayList<Chat_Friend_Group> arrayList = new ArrayList<>();
        arrayList.add(new Chat_Friend_Group(R.drawable.avatar, "Nguyen Thanh An","online",0,4));
        arrayList.add(new Chat_Friend_Group(R.drawable.avatar, "Phan Dinh Yen","offline",12,9));
        arrayList.add(new Chat_Friend_Group(R.drawable.avatar, "Nguyen Do Hoang Phuc","offline",90,0));
        Chat_Friend_GroupAdapter adapter = new Chat_Friend_GroupAdapter(getContext(),R.layout.friend_group_status, arrayList);
        lw.setAdapter(adapter);
        return view;
    }
}