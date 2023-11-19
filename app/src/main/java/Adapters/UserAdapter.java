package Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ltdd_app_mang_xa_hoi.ProfileActivity;
import com.example.ltdd_app_mang_xa_hoi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import Entity.Users;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    private List<Users> list;

    public UserAdapter(List<Users> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_friend_group,parent,false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (list.get(position).getUid().equals(user.getUid())){
            holder.layout.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
        }
        else {
            holder.layout.setVisibility(View.VISIBLE);
        }
        holder.name.setText(list.get(position).getName());
        Glide.with(holder.itemView.getContext().getApplicationContext())
                .load(list.get(position).getProfileImage())
                .placeholder(R.drawable.avatar)
                .timeout(6500)
                .into(holder.imageProfile);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserHolder extends  RecyclerView.ViewHolder{
        private ImageView imageProfile;
        private TextView name;
    private LinearLayout layout;
        public UserHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile= itemView.findViewById(R.id.avatar_relationship);
            name = itemView.findViewById(R.id.name_relationship);
            layout = itemView.findViewById(R.id.relativeLayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Lấy người dùng tại vị trí được nhấn
                        Users clickedUser = list.get(position);

                        // Thực hiện hành động khi người dùng nhấn vào item, ví dụ: Intent tới một Activity mới
                        Intent intent = new Intent(v.getContext(), ProfileActivity.class);
                        intent.putExtra("userId", clickedUser.getUid()); // Truyền dữ liệu cần thiết qua Intent
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }

    }
}
