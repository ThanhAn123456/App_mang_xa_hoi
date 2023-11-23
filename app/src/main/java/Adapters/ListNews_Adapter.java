package Adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ltdd_app_mang_xa_hoi.NewsDetailActivity;
import com.example.ltdd_app_mang_xa_hoi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import Entity.HomeModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class ListNews_Adapter extends RecyclerView.Adapter<ListNews_Adapter.HomeHolder> {
    private List<HomeModel> list;
    Context context;
    OnPressed onPressed;

    public ListNews_Adapter(Context context, List<HomeModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_listnews, parent, false);
        return new HomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        HomeModel item = list.get(position);
        holder.name.setText(item.getName());
        List<String> likeList = new ArrayList<>();
        likeList = item.getLikes();
        int count = likeList.size();
        Random random = new Random();
        Glide.with(context.getApplicationContext())
                .load(item.getProfileImage())

                .timeout(6500)
                .into(holder.profileImage);
        holder.statuspost.setImageResource(R.drawable.ic_public);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String formattedTimestamp = sdf.format( item.getTimestamp());
        holder.time.setText(formattedTimestamp);
        holder.content.setText(item.getDescription());
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        ColorDrawable colorDrawable = new ColorDrawable(color);

        Glide.with(context.getApplicationContext())
                .load(item.getImageUrl())
                .placeholder(colorDrawable)
                .timeout(7000)
                .into(holder.image_content);
        if (count == 0) {
            holder.numlike.setText("0");
        } else {
            holder.numlike.setText(count + "");
        }
        //check if already like
        assert user != null;
        holder.imagelike.setChecked(likeList.contains(user.getUid()));
        holder.clickListener(position,
                list.get(position).getId(),
                list.get(position).getName(),
                list.get(position).getUid(),
                list.get(position).getLikes(),
                list.get(position).getImageUrl()
        );

    }

    public interface OnPressed {
        void onLiked(int position, String id, String uid, List<String> likeList, boolean isChecked);

        void setCommentCount(TextView textView);
    }

    public void OnPressed(OnPressed onPressed) {
        this.onPressed = onPressed;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HomeHolder extends RecyclerView.ViewHolder {
        private CircleImageView profileImage;
        private TextView name, time, content, numlike, numcmt, numshare;
        private ImageView image_content, statuspost;
        private ImageButton imagecmt, imageshare;
        private CheckBox imagelike;

        public HomeHolder(@NonNull View itemview) {
            super(itemview);
            profileImage = itemview.findViewById(R.id.profileImage);
            statuspost = itemview.findViewById(R.id.statuspost);
            name = itemview.findViewById(R.id.name);
            time = itemview.findViewById(R.id.time);
            content = itemview.findViewById(R.id.contentnews);
            image_content = itemview.findViewById(R.id.imagecontent);
            numlike = itemview.findViewById(R.id.numberlike);
            numcmt = itemview.findViewById(R.id.numbercmt);
            numshare = itemview.findViewById(R.id.numbershare);
            imagelike = itemview.findViewById(R.id.like);
            imagecmt = itemview.findViewById(R.id.cmt);
            imageshare = itemview.findViewById(R.id.share);

            onPressed.setCommentCount(numcmt);
        }

        public void clickListener(final int position, final String id, String name, final String uid, final List<String> likes, final String imageUrl) {
            imagelike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onPressed.onLiked(position, id, uid, likes, isChecked);
                }
            });
            if (context != null) {
                imagecmt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, NewsDetailActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("uid", uid);
                        intent.putExtra("isComment", true);
                        context.startActivity(intent);
                    }
                });
            }
        }
    }


}
