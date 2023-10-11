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

public class GroupFragment extends Fragment {
    ListView listview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_group, container, false);
        listview = view.findViewById(R.id.listviewgroup);
        ArrayList<Friend_Group> arrayList = new ArrayList<>();
        arrayList.add(new Friend_Group(R.drawable.two_person, "Nhóm LTDD",-1, R.drawable.ic_send_sub, R.drawable.ic_send_sub));
        Friend_GroupAdapter adapter = new Friend_GroupAdapter(getContext(),R.layout.lv_friend_group,arrayList);
        listview.setAdapter(adapter);
        return view;
    }
}