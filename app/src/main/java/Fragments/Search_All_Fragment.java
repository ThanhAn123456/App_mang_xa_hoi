package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ltdd_app_mang_xa_hoi.R;

import java.util.ArrayList;

import Adapters.Friend_GroupAdapter;
import Dto.Friend_Group;

public class Search_All_Fragment extends Fragment {
    ListView lv_friend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search__all_, container, false);
        lv_friend = view.findViewById(R.id.lv_friend);
        ArrayList<Friend_Group> arrayList = new ArrayList<>();
        arrayList.add(new Friend_Group(R.drawable.avatar, "Nguyen Thanh An", 3, R.drawable.ic_send_sub, R.drawable.ic_tag));
        arrayList.add(new Friend_Group(R.drawable.avatar, "Nguyen Thanh", 3, R.drawable.ic_send_sub, R.drawable.ic_tag));
        arrayList.add(new Friend_Group(R.drawable.avatar, "Nguyen Thanh  2", 3, R.drawable.ic_send_sub, R.drawable.ic_tag));
        Friend_GroupAdapter adapter = new Friend_GroupAdapter(getContext(),R.layout.lv_friend_group,arrayList);
        lv_friend.setAdapter(adapter);
        return view;
    }
}