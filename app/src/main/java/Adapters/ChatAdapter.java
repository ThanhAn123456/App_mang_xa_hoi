package Adapters;

import android.content.Context;
import android.os.Build;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ltdd_app_mang_xa_hoi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Date;
import java.util.List;

import Entity.ChatModel;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {

    Context context;
    List<ChatModel> list;
    DocumentReference reference;

    public ChatAdapter(Context context, List<ChatModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_items, parent, false);
        return new ChatHolder(view);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    String calculateTime(Date date) {
        if (date != null) {
            long millis = date.toInstant().toEpochMilli();
            return DateUtils.getRelativeTimeSpanString(millis, System.currentTimeMillis(), 60000, DateUtils.FORMAT_ABBREV_TIME).toString();
        } else {
            return ""; // hoặc giá trị mặc định khác tùy vào yêu cầu của bạn
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        if(list.get(position).getSenderID().equalsIgnoreCase(user.getUid())){
            holder.leftChat.setVisibility(View.GONE);
            holder.rightChat.setVisibility(View.VISIBLE);
            holder.rightChat.setText(list.get(position).getMessage());
            holder.timeright.setText(calculateTime(list.get(position).getTime()));
            holder.avatar.setVisibility(View.GONE);
            holder.rightChat.setOnClickListener(view -> {
                if (holder.timeright.getVisibility() == View.GONE) {
                    holder.timeright.setVisibility(View.VISIBLE);
                } else {
                    holder.timeright.setVisibility(View.GONE);
                }
            });
        }else{
            reference = FirebaseFirestore.getInstance().collection("User").document(list.get(position).getSenderID());
            reference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error!=null)
                        return;
                    if (value.exists()){
                        String avatar = value.getString("profileImage");
                        Glide.with(context.getApplicationContext())
                                .load(avatar)
                                .into(holder.avatar);
                        holder.avatar.setVisibility(View.VISIBLE);
                    }
                }
            });

            holder.rightChat.setVisibility(View.GONE);
            holder.leftChat.setVisibility(View.VISIBLE);
            holder.leftChat.setText(list.get(position).getMessage());
            holder.timeleft.setText(calculateTime(list.get(position).getTime()));
            holder.leftChat.setOnClickListener(view -> {
                if (holder.timeleft.getVisibility() == View.GONE) {
                    holder.timeleft.setVisibility(View.VISIBLE);

                } else {
                    holder.timeleft.setVisibility(View.GONE);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ChatHolder extends  RecyclerView.ViewHolder{

        TextView leftChat, rightChat,timeleft,timeright;
        ImageView avatar;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            leftChat = itemView.findViewById(R.id.leftChat);
            rightChat = itemView.findViewById(R.id.rightChat);
            timeleft =itemView.findViewById(R.id.lefttime);
            timeright =itemView.findViewById(R.id.righttime);
            avatar = itemView.findViewById(R.id.avatar);

        }
    }

}
