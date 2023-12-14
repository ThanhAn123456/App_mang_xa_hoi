package Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ltdd_app_mang_xa_hoi.ChatActivity;
import com.example.ltdd_app_mang_xa_hoi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import Adapters.ChatUserAdapter;
import Adapters.UserAdapter;
import Entity.ChatUserModel;
import Entity.Users;

public class GroupFragment extends Fragment {
    RecyclerView recyclerView;
    FirebaseUser user;
    CollectionReference reference;

    List<String> listgroup;
    List<ChatUserModel> list;
    ChatUserAdapter groupadapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_group, container, false);
        recyclerView = view.findViewById(R.id.listviewgroup);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseFirestore.getInstance().collection("User");
        listgroup = new ArrayList<>();
        list = new ArrayList<>();
        groupadapter = new ChatUserAdapter(list);
        recyclerView.setAdapter(groupadapter);
        loadGroup();
        clickListener();
        return view;
    }
    public void loadGroup(){
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Messages");
        reference.whereArrayContains("uid", user.getUid())
                .orderBy("time", Query.Direction.DESCENDING)
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
                            boolean isGroupChat= snapshot.getBoolean("isGroupChat");
                            if (uidList != null  && uidList.contains(user.getUid()) && isGroupChat==true) {
                                ChatUserModel model = snapshot.toObject(ChatUserModel.class);
                                list.add(model);
                            }
                        }
                    }
                    groupadapter.notifyDataSetChanged();
                });

    }
    void clickListener() {

        groupadapter.OnStartChat((position, uids, chatID) -> {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putStringArrayListExtra("uid", (ArrayList<String>) uids);
            intent.putExtra("id", chatID);
            intent.putExtra("isGroupChat",true);
            startActivity(intent);
        });

    }
}