package Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ltdd_app_mang_xa_hoi.ChatActivity;
import com.example.ltdd_app_mang_xa_hoi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import Adapters.ChatUserAdapter;
import Entity.ChatUserModel;
import MainFragment.ChatFragment;


public class ChatGroupFragment extends Fragment {

    RecyclerView recyclerView;
    ChatUserAdapter adapter;
    List<ChatUserModel> list;
    FirebaseUser user;
    SearchView searchView;
    CollectionReference reference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_chat_group, container, false);
        init(view);
        clickListener();
        loadChatGroupData();
        return view;
    }
    void init(View view){
        recyclerView= view.findViewById(R.id.listchatgroup);
        searchView= ChatFragment.searchView;
        list = new ArrayList<>();
        adapter = new ChatUserAdapter((Activity) getContext(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }
    void loadChatGroupData() {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Messages");
        reference.whereArrayContains("uid", user.getUid())
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        return;
                    }

                    if (value == null) {
                        return;
                    }

                    if (value.isEmpty()) {
                        return;
                    }

                    list.clear();
                    for (QueryDocumentSnapshot snapshot : value) {
                        if (snapshot.exists()) {
                            // Kiểm tra điều kiện trước khi thêm vào danh sách
                            List<String> uidList = (List<String>) snapshot.get("uid");
                            if (uidList != null && uidList.size()>2 && uidList.contains(user.getUid())) {
                                ChatUserModel model = snapshot.toObject(ChatUserModel.class);
                                list.add(model);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }
    void clickListener() {

        adapter.OnStartChat((position, uids, chatID) -> {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra("uid", "1");
            intent.putExtra("id", chatID);
            startActivity(intent);
        });

    }
}