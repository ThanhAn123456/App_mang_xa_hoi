package MainFragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ltdd_app_mang_xa_hoi.R;
import com.example.ltdd_app_mang_xa_hoi.UpdateAccountActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.List;
import java.util.Random;

import Config.CustomGridLayoutManager;
import Entity.PostImageModel;
import de.hdodenhof.circleimageview.CircleImageView;


public class AccountFragment extends Fragment {
    Activity activity;
    int count;
    ImageView coverimage;
    List<String> followersList, followingList, followingList_2;
    CircleImageView avatar;
    TextView name, status, numberfollower, numberfollowing, numberpost;
    ImageView ic_setting;
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter<PostImageModel,PostImageHolder> adapter;
    Button btn_edit;
    private FirebaseUser user;
    public interface OnMenuClickListener {
        void onMenuClick();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        activity = getActivity();
        ic_setting = view.findViewById(R.id.ic_setting);
        btn_edit= view.findViewById(R.id.btn_edit);
        recyclerView = view.findViewById(R.id.lv_profileimage);
        name = view.findViewById(R.id.profile_name);
        avatar = view.findViewById(R.id.avatar);
        coverimage = view.findViewById(R.id.cover_photo);
        numberfollower = view.findViewById(R.id.numberfollower);
        numberfollowing = view.findViewById(R.id.numberfollowing);
        numberpost = view.findViewById(R.id.numberpost);
        status = view.findViewById(R.id.status);
        ic_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra xem hoạt động có triển khai interface OnMenuClickListener không
                if (getActivity() instanceof OnMenuClickListener) {
                    // Gọi phương thức onMenuClick thông qua interface
                    ((OnMenuClickListener) getActivity()).onMenuClick();
                }
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getContext(), UpdateAccountActivity.class);
                startActivity(intent);
            }
        });
        FirebaseAuth auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        loadBasicData();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( new CustomGridLayoutManager(getContext(), 3));
        loadPostImages();
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
        return view;
    }

    private void loadBasicData(){
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("User")
                .document(user.getUid());
        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                if (value == null || !value.exists()) {
                    return;
                }

                String fullname = value.getString("name");
                String textstatus = value.getString("status");
                followersList = (List<String>) value.get("followers");
                followingList = (List<String>) value.get("following");
                String profileURL = value.getString("profileImage");
                String coverURL = value.getString("coverImage");
                // Kiểm tra null cho getContext()
                Context context = getContext();
                if (context != null) {
                    name.setText(fullname);
                    status.setText(textstatus);
                    numberfollower.setText(String.valueOf(followersList.size()));
                    numberfollowing.setText(String.valueOf(followingList.size()));
                    Random random = new Random();
                    int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    Glide.with(context)
                            .load(profileURL)
                            .placeholder(colorDrawable)
                            .timeout(6500)
                            .into(avatar);

                    Glide.with(context)
                            .load(coverURL)
                            .placeholder(colorDrawable)
                            .timeout(6500)
                            .into(coverimage);
                }
            }
        });

    }
    private void loadPostImages()
    {
        DocumentReference reference = FirebaseFirestore.getInstance().collection("User").document(user.getUid());

        Query query = reference.collection("Post Images");

        FirestoreRecyclerOptions<PostImageModel> options = new FirestoreRecyclerOptions.Builder<PostImageModel>()
                .setQuery(query, PostImageModel.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<PostImageModel, PostImageHolder>(options) {
            @NonNull
            @Override
            public PostImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_imageinprofile, parent, false);
                return new PostImageHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PostImageHolder holder, int position, @NonNull PostImageModel model) {
                Log.d("AdapterDebug", "onBindViewHolder called for position: " + position);
                Glide.with(holder.itemView.getContext().getApplicationContext())
                        .load(model.getImageUrl())
                        .timeout(6500)

                        .into(holder.imageView);
                count = getItemCount();

            }

            @Override
            public int getItemCount() {
                numberpost.setText(super.getItemCount()+"");
                return super.getItemCount();

            }

        };

    }
    private static class PostImageHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public PostImageHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);

        }

    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}