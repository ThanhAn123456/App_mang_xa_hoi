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
import Dto.Lv_ListChat;


public class ChatFriendFragment extends Fragment {

    ListView lv_listchat;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat_friend, container, false);
        lv_listchat= view.findViewById(R.id.lv_listchat);

        ArrayList<Lv_ListChat> arrayList = new ArrayList<>();
        arrayList.add(new Lv_ListChat(R.drawable.avatar, "Thành An", 1,2));
        arrayList.add(new Lv_ListChat(R.drawable.avatar, "Đình Yên", 0,1));
        arrayList.add(new Lv_ListChat(R.drawable.avatar, "Hoàng Phúc", 3,0));
        arrayList.add(new Lv_ListChat(R.drawable.avatar, "Tấn Hên", 0,0));
        arrayList.add(new Lv_ListChat(R.drawable.avatar, "Xuân Việt", 5,0));
        ListChatAdapter adapter = new ListChatAdapter(getContext(),R.layout.lv_listchat,arrayList);
        lv_listchat.setAdapter(adapter);

        return view;
    }
}