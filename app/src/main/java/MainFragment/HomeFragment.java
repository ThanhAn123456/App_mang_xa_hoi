package MainFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ltdd_app_mang_xa_hoi.CreateNewsActivity;
import com.example.ltdd_app_mang_xa_hoi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Adapters.ListNews_Adapter;
import Entity.HomeModel;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
   private FirebaseUser  user;
    ListNews_Adapter adapter;
    private List<HomeModel> list;
    Activity activity;
    public static int LIST_SIZE =0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_home, container, false);
        activity = getActivity();
        recyclerView = view.findViewById(R.id.lv_listnews);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user= auth.getCurrentUser();

        list = new ArrayList<>();
        adapter = new ListNews_Adapter(getContext(),list);
        recyclerView.setAdapter(adapter);




        TextView textView = view.findViewById(R.id.taobaiviet);

                textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), CreateNewsActivity.class);
                startActivity(intent);
            }

        });
        loadDataFromFireStore();
        return view;

    }
    private void loadDataFromFireStore() {
        CollectionReference reference = FirebaseFirestore.getInstance()
                .collection("User")
                .document(user.getUid())
                .collection("Post Images");
        reference.addSnapshotListener((value, error) -> {

            if (error != null) {
                Log.d("Error: ", error.getMessage());
                return;
            }

            if (value == null)
                return;
            list.clear();
            for (QueryDocumentSnapshot snapshot : value) {
                if (!snapshot.exists()) {
                    return;
                }

//                HomeModel model = snapshot.toObject(HomeModel.class);
                list.add(new HomeModel(
                        snapshot.get("userName").toString(),
                        snapshot.get("profileImage").toString(),
                        snapshot.get("imageUrl").toString(),
                        snapshot.get("uid").toString(),
                        snapshot.get("comments").toString(),
                        snapshot.get("description").toString(),
                        snapshot.get("id").toString(),
                        new Date(),
                        Integer.parseInt(snapshot.get("likeCount").toString())
                ));
            adapter.notifyDataSetChanged();

            LIST_SIZE = list.size();
        }
    });


    }
}