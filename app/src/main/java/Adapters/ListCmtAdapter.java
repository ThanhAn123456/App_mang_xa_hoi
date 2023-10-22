package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ltdd_app_mang_xa_hoi.R;

import java.util.List;

import Entity.Lv_ListCmt;

public class ListCmtAdapter extends RecyclerView.Adapter<ListCmtAdapter.ViewHolder> {
    private Context context;
    private List<Lv_ListCmt> list;

    public ListCmtAdapter(Context context, List<Lv_ListCmt> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lv_listcmt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lv_ListCmt item = list.get(position);

        holder.avatar.setImageResource(item.avatar);
        holder.name.setText(item.name);
        holder.cmt.setText(item.cmt);

        if (item.image_cmt == -1) {
            holder.img_cmt.setVisibility(View.GONE);
        } else {
            holder.img_cmt.setImageResource(item.image_cmt);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView name;
        TextView cmt;
        ImageView img_cmt;

        public ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar_relationship);
            name = itemView.findViewById(R.id.name_relationship);
            cmt = itemView.findViewById(R.id.cmt);
            img_cmt = itemView.findViewById(R.id.imgcmt);
        }
    }
}