package MainFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ltdd_app_mang_xa_hoi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import Adapters.NotificationAdapter;
import Entity.NotificationModel;


public class NotificationsFragment extends Fragment {
    RecyclerView recyclerView;
    NotificationAdapter adapter;
    CollectionReference reference;
    List<NotificationModel> list;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        init(view);
        loadNotification();
        checkNotificationCount();
        return view;
    }
    void init(View view) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = view.findViewById(R.id.recyclerViewNotify);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        adapter = new NotificationAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

    }
    void loadNotification() {

        reference = FirebaseFirestore.getInstance().collection("Notifications");

        reference.whereEqualTo("uid", user.getUid())
                .orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {

                    if (error != null)
                        return;


                    list.clear();
                    for (QueryDocumentSnapshot snapshot : value) {
                        Log.d("NotificationData", "Notification data: " + snapshot.getData());
                        NotificationModel model = snapshot.toObject(NotificationModel.class);
                        list.add(model);

                    }
                    adapter.notifyDataSetChanged();

                });

    }
    void checkNotificationCount() {
        CollectionReference reference = FirebaseFirestore.getInstance().collection("Notifications");

        reference.whereEqualTo("uid", user.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int count = queryDocumentSnapshots.size();
                    Log.d("NotificationCount", "Number of notifications: " + count);
                })
                .addOnFailureListener(e -> {
                    Log.e("NotificationCount", "Error getting notifications", e);
                });
    }
}