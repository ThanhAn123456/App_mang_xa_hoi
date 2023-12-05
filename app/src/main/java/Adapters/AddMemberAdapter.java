package Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ltdd_app_mang_xa_hoi.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

import Entity.AddMemberModel;


public class AddMemberAdapter extends RecyclerView.Adapter<AddMemberAdapter.AddMemberHolder> {
    Activity context;
    List<AddMemberModel> listfriend;
    OnPressed onPressed;

    public AddMemberAdapter(Activity context, List<AddMemberModel> listfriend) {
        this.context = context;
        this.listfriend = listfriend;
    }

    @NonNull
    @Override
    public AddMemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addmember, parent, false);
        return new AddMemberHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddMemberHolder holder, int position) {
        holder.txtname.setText(listfriend.get(position).getName());
        Glide.with(context.getApplicationContext())
                .load(listfriend.get(position).getProfileImage())
                .into(holder.imageavatar);
        holder.clicklistener(position,listfriend.get(position).getUid());
    }
    @Override
    public int getItemCount() {
        return listfriend.size();
    }

     class AddMemberHolder extends RecyclerView.ViewHolder {
        TextView txtname;
        CheckBox checkadd;
        ImageView imageavatar;

        public AddMemberHolder(@NonNull View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.name_relationship);
            imageavatar = itemView.findViewById(R.id.avatar_relationship);
            checkadd = itemView.findViewById(R.id.checkitem);
        }

        public void clicklistener(int position, String id) {
            checkadd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onPressed.onCheck(position,id,isChecked);
                }
            });
        }
    }

    public void OnPressed(OnPressed onPressed) {
        this.onPressed = onPressed;
    }
    //Tạo interface chơi qua bên Activity
    public interface OnPressed {
        void onCheck(int position, String id,boolean isChecked);
    }


}
