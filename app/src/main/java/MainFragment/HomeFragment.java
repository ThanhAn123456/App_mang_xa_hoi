package MainFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ltdd_app_mang_xa_hoi.CreateNewsActivity;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.ListNews_Adapter;
import Entity.HomeModel;

public class HomeFragment extends Fragment {
    private final MutableLiveData<Integer> commentCount = new MutableLiveData<>();
    RecyclerView recyclerView;
    private FirebaseUser user;
    ListNews_Adapter adapter;
    private List<HomeModel> list;
    Activity activity;
    ImageView avatar_image;
    DocumentReference userRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        activity = getActivity();
        recyclerView = view.findViewById(R.id.lv_listnews);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        avatar_image = view.findViewById(R.id.image_avatar);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        list = new ArrayList<>();
        adapter = new ListNews_Adapter(getContext(), list);
        recyclerView.setAdapter(adapter);


        TextView textView = view.findViewById(R.id.taobaiviet);
        userRef = FirebaseFirestore.getInstance().collection("User")
                .document(user.getUid());
        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                if (value == null) {
                    return;
                }
                if (value.exists()) {
                    String profileURL = value.getString("profileImage");

                    // Kiểm tra xem getContext() có trả về null hay không trước khi sử dụng
                    Context context = getContext();
                    if (context != null) {
                        Glide.with(context)
                                .load(profileURL)
                                .placeholder(R.drawable.avatar)
                                .timeout(6500)
                                .into(avatar_image);
                    }
                }
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), CreateNewsActivity.class);
                startActivity(intent);
            }

        });
        loadDataFromFireStore();
        adapter.OnPressed(new ListNews_Adapter.OnPressed() {
            @Override
            public void onLiked(int position, String id, String uid, List<String> likeList, boolean isChecked) {
                DocumentReference reference = FirebaseFirestore.getInstance().collection("User")
                        .document(uid)
                        .collection("Post Images")
                        .document(id);
                if (likeList.contains(user.getUid())) {
                    likeList.remove(user.getUid()); //bỏ like
                } else
                    likeList.add(user.getUid()); //like
                Map<String, Object> map = new HashMap<>();
                map.put("likes", likeList);
                reference.update(map);
            }

            @Override
            public void setCommentCount(TextView textView) {
                commentCount.observe((LifecycleOwner) activity, integer -> {
                    assert commentCount.getValue() != null;

                    if (commentCount.getValue() == 0)
                        textView.setVisibility(View.GONE);
                    else
                        textView.setVisibility(View.GONE);
                    textView.setText(commentCount.getValue()+"");
                });

            }
        });
        return view;

    }

    private void loadDataFromFireStore() {
        final DocumentReference reference = FirebaseFirestore.getInstance().collection("User")
                .document(user.getUid());
        final CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("User");
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null)
                    return;
                if (value == null)
                    return;
                List<String> uidList = (List<String>) value.get("following");

                if (uidList == null || uidList.isEmpty())
                    return;
                collectionReference.whereIn("uid", uidList)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null)
                                    return;
                                if (value == null) {
                                    return;
                                }
                                for (QueryDocumentSnapshot snapshot : value) {
                                    snapshot.getReference().collection("Post Images")
                                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable QuerySnapshot value1, @Nullable FirebaseFirestoreException error) {
                                                    if (error != null) {
                                                        Log.d("Error: ", error.getMessage());
                                                        return;
                                                    }

                                                    if (value1 == null)
                                                        return;
                                                    list.clear();
                                                    for (final QueryDocumentSnapshot snapshot1 : value1) {
                                                        if (!snapshot1.exists()) {
                                                            return;
                                                        }
                                                        HomeModel model = snapshot1.toObject(HomeModel.class);
                                                        list.add(new HomeModel
                                                                (
                                                                        snapshot1.get("name").toString(),
                                                                        snapshot1.get("profileImage").toString(),
                                                                        snapshot1.get("imageUrl").toString(),
                                                                        snapshot1.get("uid").toString(),
                                                                        snapshot1.get("comments").toString(),
                                                                        snapshot1.get("description").toString(),
                                                                        snapshot1.get("id").toString(),
                                                                        model.getTimestamp(),
                                                                        model.getLikes()
                                                                )
                                                        );
                                                        snapshot1.getReference().collection("Comments").get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            int commentCountValue = task.getResult().size();
                                                                            commentCount.setValue(commentCountValue);
                                                                        }
                                                                    }
                                                                });



                                                    }
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });

                                }
                            }
                        });
            }
        });
    }
}