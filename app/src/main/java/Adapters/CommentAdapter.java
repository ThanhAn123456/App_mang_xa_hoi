package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ltdd_app_mang_xa_hoi.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import Entity.CommentModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    Context context;
    List<CommentModel> list;


    public CommentAdapter(Context context, List<CommentModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.comment_items, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {

        Glide.with(holder.profileImage.getContext())
                .load(list.get(position).getProfileImageUrl())
                .into(holder.profileImage);

        holder.nameTv.setText(list.get(position).getName());
        holder.commentTv.setText(list.get(position).getComment());
        if (list.get(position).getTimestamp() != null) {
            // Định dạng timestamp
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
            String formattedTimestamp = sdf.format(list.get(position).getTimestamp());

            holder.timestampTv.setText(formattedTimestamp);
        } else {
            // Xử lý trường hợp timestamp là null
            holder.timestampTv.setText("Unknown Timestamp");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class CommentHolder extends RecyclerView.ViewHolder{

        CircleImageView profileImage;
        TextView nameTv, commentTv, timestampTv;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            nameTv = itemView.findViewById(R.id.nameTV);
            commentTv = itemView.findViewById(R.id.commentTV);
            timestampTv = itemView.findViewById(R.id.timestamp);

        }
    }
}
