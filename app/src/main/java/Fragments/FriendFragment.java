package Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ltdd_app_mang_xa_hoi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

import Adapters.UserAdapter;
import Entity.HomeModel;
import Entity.Users;

public class FriendFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseUser user;
    CollectionReference reference;

    List<String> listfriend;
    List<Users> list;
    UserAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseFirestore.getInstance().collection("User");
        recyclerView = view.findViewById(R.id.recyclerViewfriend);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        listfriend = new ArrayList<>();
        list = new ArrayList<>();
        loadFriend();

        adapter = new UserAdapter(list);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void loadFriend() {
        reference.document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        listfriend = (List<String>) documentSnapshot.get("following");
                        if (listfriend != null && !listfriend.isEmpty()) {
                            queryFriends(); // Gọi phương thức để truy vấn danh sách bạn bè
                        } else {
                            // Xử lý trường hợp listfriend là null hoặc rỗng
                        }
                    }
                });
    }

    private void queryFriends() {
        reference.whereIn("uid", listfriend)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            list.clear();
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                if (snapshot.exists()) {
                                    Users model = snapshot.toObject(Users.class);
                                    list.add(model);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}