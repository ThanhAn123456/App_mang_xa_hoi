package Fragments;

import android.content.Context;
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
import android.widget.ListView;
import android.widget.Toast;

import com.example.ltdd_app_mang_xa_hoi.R;
import com.example.ltdd_app_mang_xa_hoi.SearchActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import Adapters.Friend_GroupAdapter;
import Adapters.UserAdapter;
import Entity.Lv_Friend_Group;
import Entity.Users;

public class Search_All_Fragment extends Fragment {
    RecyclerView recyclerViewuser;
    CollectionReference reference;
    SearchView searchView;
    UserAdapter userAdapter;
    private List<Users> listuser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search__all_, container, false);
        recyclerViewuser = view.findViewById(R.id.searchuser);
        recyclerViewuser.setHasFixedSize(true);
        recyclerViewuser.setLayoutManager(new LinearLayoutManager(getContext()));
        searchView = SearchActivity.searchView;
        reference = FirebaseFirestore.getInstance().collection("User");
        loadUserData();
        searchUser();
        listuser = new ArrayList<>();
        userAdapter= new UserAdapter(listuser);
        recyclerViewuser.setAdapter(userAdapter);
        return view;
    }

    private void loadUserData() {

        reference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null)
                    return;
                if (value == null)
                    return;
                listuser.clear();
                for (QueryDocumentSnapshot snapshot : value) {
                        Users users =snapshot.toObject(Users.class);
                        listuser.add(users);
                }
                userAdapter.notifyDataSetChanged();
            }
        });
    }
    private void searchUser(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                reference.orderBy("search").startAt(query).endAt(query+"\uf8ff")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    listuser.clear();
                                    for (DocumentSnapshot snapshot :task.getResult()){
                                        if (!snapshot.exists())
                                            return;
                                        Users users =snapshot.toObject(Users.class);
                                        listuser.add(users);
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
                    loadUserData();
                return false;
            }
        });
    }
}