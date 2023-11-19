package Adapters;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ltdd_app_mang_xa_hoi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Random;

import Entity.HomeModel;
import de.hdodenhof.circleimageview.CircleImageView;

public class ListNews_Adapter extends RecyclerView.Adapter<ListNews_Adapter.HomeHolder> {
    private List<HomeModel> list;
    Context context;
    public ListNews_Adapter(Context context, List<HomeModel> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_listnews,parent,false);
        return new HomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        HomeModel item = list.get(position);
        holder.name.setText(item.getUserName());
        Random random = new Random();

        Glide.with(context.getApplicationContext())
                        .load(item.getProfileImage())
                                .placeholder(R.drawable.avatar)
                                        .timeout(6500)
                                                .into(holder.profileImage);
        holder.statuspost.setImageResource(R.drawable.ic_public);
        holder.time.setText(""+item.getTimestamp());
        holder.content.setText(item.getDescription());
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        ColorDrawable colorDrawable = new ColorDrawable(color);

        Glide.with(context.getApplicationContext())
                .load(item.getImageUrl())
                .placeholder(colorDrawable)
                .timeout(7000)
                .into(holder.image_content);
//        int likeList = item.getLikeCount();
//
//        if (likeList == 0) {
//            holder.numlike.setText("0");
//        } else if (likeList == 1) {
//            holder.numlike.setText(likeList);
//        } else {
//            holder.numlike.setText(likeList);
//        }
//        holder.likeCheckBox.setChecked(likeList.contains(user.getUid()));
//
//        holder.descriptionTv.setText(list.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class HomeHolder extends RecyclerView.ViewHolder{
        private CircleImageView profileImage;
        private TextView name, time, content, numlike, numcmt,numshare;
        private ImageView image_content,statuspost;
        private ImageButton imagelike,imagecmt,imageshare;
        public HomeHolder(@NonNull View itemview){
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
        }
    }


}
