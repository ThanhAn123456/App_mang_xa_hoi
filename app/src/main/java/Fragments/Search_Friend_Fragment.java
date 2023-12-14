package Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ltdd_app_mang_xa_hoi.R;
import com.example.ltdd_app_mang_xa_hoi.SearchActivity;
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

import java.util.ArrayList;
import java.util.List;

import Adapters.UserAdapter;
import Entity.Users;

public class Search_Friend_Fragment extends Fragment {
    RecyclerView recyclerViewuser;
    CollectionReference reference;
    SearchView searchView;
    UserAdapter userAdapter;
    List<String> listfriend;
    List<Users> list;
    FirebaseUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search__friend_, container, false);
        user= FirebaseAuth.getInstance().getCurrentUser();
        recyclerViewuser = view.findViewById(R.id.recyclerViewfriend);
        recyclerViewuser.setHasFixedSize(true);
        recyclerViewuser.setLayoutManager(new LinearLayoutManager(getContext()));
        searchView = SearchActivity.searchView;
        reference = FirebaseFirestore.getInstance().collection("User");
        listfriend = new ArrayList<>();
        list = new ArrayList<>();
        loadFriend();
        userAdapter= new UserAdapter(list);
        recyclerViewuser.setAdapter(userAdapter);
        searchUser();
        return view;
    }
    public void loadFriend() {
        reference.document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        listfriend = (List<String>) documentSnapshot.get("following");
                        if (listfriend != null && !listfriend.isEmpty()) {
                            queryFriends();
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
                            userAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
    private void searchUser(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                reference.orderBy("search")
                        .startAt(query.toLowerCase())
                        .endAt(query.toLowerCase() + "\uf8ff")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    list.clear();
                                    for (DocumentSnapshot snapshot :task.getResult()){
                                        if (!snapshot.exists())
                                            return;
                                        Users users =snapshot.toObject(Users.class);
                                        list.add(users);
                                    }
                                    userAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals(""))
                    loadFriend();
                return false;
            }
        });
    }
}