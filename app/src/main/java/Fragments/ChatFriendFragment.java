package Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ltdd_app_mang_xa_hoi.ChatActivity;
import com.example.ltdd_app_mang_xa_hoi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Adapters.ChatUserAdapter;
import Entity.ChatUserModel;
import Entity.Users;
import MainFragment.ChatFragment;


public class ChatFriendFragment extends Fragment {

    RecyclerView recyclerViewchat;
    ChatUserAdapter adapter;
    List<ChatUserModel> list;
    FirebaseUser user;
    SearchView searchView;
    CollectionReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_friend, container, false);
        reference = FirebaseFirestore.getInstance().collection("Messages");
        init(view);
        clickListener();
        loadChatFriendData();
        searchChatFriend();
        return view;
    }

    void init(View view) {
        searchView= ChatFragment.searchView;
        recyclerViewchat = view.findViewById(R.id.recyclerViewChat);
        list = new ArrayList<>();
        adapter = new ChatUserAdapter((Activity) getContext(), list);
        recyclerViewchat.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewchat.setAdapter(adapter);
        user = FirebaseAuth.getInstance().getCurrentUser();
    }
    void searchChatFriend(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("adadadaw", "onQueryTextSubmit: " + query);
                reference.orderBy("name")
                        .startAt(query.toLowerCase())
                        .endAt(query.toLowerCase() + "\uf8ff")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    list.clear();
                                    for (DocumentSnapshot snapshot : task.getResult()) {
                                        if (snapshot.exists()) {
                                            ChatUserModel model = snapshot.toObject(ChatUserModel.class);
                                            list.add(model);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals(""))
                    loadChatFriendData();
                return false;
            }
        });
    }
    void loadChatFriendData() {
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
                            if (uidList != null && uidList.size() == 2 && uidList.contains(user.getUid())) {
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

            String oppositeUID;
            if (!uids.get(0).equalsIgnoreCase(user.getUid())) {
                oppositeUID = uids.get(0);
            } else {
                oppositeUID = uids.get(1);
            }

            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra("uid", oppositeUID);
            intent.putExtra("id", chatID);
            startActivity(intent);
        });

    }
}