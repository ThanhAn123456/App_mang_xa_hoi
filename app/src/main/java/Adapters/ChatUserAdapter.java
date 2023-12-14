package Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ltdd_app_mang_xa_hoi.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Date;
import java.util.List;
import java.util.Random;

import Entity.ChatUserModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.ChatUserHolder> {
    public OnStartChat startChat;
    List<ChatUserModel> list;
    DocumentReference reference;

    public ChatUserAdapter(List<ChatUserModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ChatUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_listchat, parent, false);
        return new ChatUserHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ChatUserHolder holder, int position) {
        if (list.get(position).getUid().size() == 2 && list.get(position).isGroupChat()==false)
            fetchImageUrl(list.get(position).getUid(), holder);
        else
            loadGroup(list.get(position).getId(),holder);
        holder.time.setText(calculateTime(list.get(position).getTime()));
        holder.lastMessage.setText(list.get(position).getLastMessage());
        holder.itemView.setOnClickListener(v ->
                startChat.clicked(position, list.get(position).getUid(), list.get(position).getId()));

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    String calculateTime(Date date) {
        long millis = date.toInstant().toEpochMilli();
        return DateUtils.getRelativeTimeSpanString(millis, System.currentTimeMillis(), 60000, DateUtils.FORMAT_ABBREV_TIME).toString();
    }
    void loadGroup(String id,ChatUserHolder holder){
        DocumentReference reference= FirebaseFirestore.getInstance().collection("Messages").document(id);
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null)
                    return;
                if (value==null)
                    return;
                String name = value.getString("name");
                holder.name.setText(name);
                holder.imageView.setImageResource(R.drawable.groups);
            }
        });
    }

    void fetchImageUrl(List<String> uids, ChatUserHolder holder) {

        String oppositeUID;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        if (!uids.get(0).equalsIgnoreCase(user.getUid())) {
            oppositeUID = uids.get(0);
        } else {
            oppositeUID = uids.get(1);
        }
        reference = FirebaseFirestore.getInstance().collection("User")
                .document(oppositeUID);
        reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    return;
                }
                assert value != null;
                if (value.exists()) {
                    String profileImageUrl = value.getString("profileImage");
                    String name = value.getString("name");
                    Glide.with(holder.imageView.getContext())
                            .load(profileImageUrl)
                            .into(holder.imageView);

                    holder.name.setText(name);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void OnStartChat(OnStartChat startChat) {
        this.startChat = startChat;
    }

    public interface OnStartChat {
        void clicked(int position, List<String> uids, String chatID);
    }

    static class ChatUserHolder extends RecyclerView.ViewHolder {

        CircleImageView imageView;
        TextView name, lastMessage, time, count;


        public ChatUserHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.avatar_relationship);
            name = itemView.findViewById(R.id.name_relationship);
            lastMessage = itemView.findViewById(R.id.mess);
            time = itemView.findViewById(R.id.status);
            count = itemView.findViewById(R.id.number_chat);
            count.setVisibility(View.GONE);

        }
    }
}
