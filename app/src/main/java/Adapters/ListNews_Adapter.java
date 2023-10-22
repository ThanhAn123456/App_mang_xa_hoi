package Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ltdd_app_mang_xa_hoi.NewsDetailActivity;
import com.example.ltdd_app_mang_xa_hoi.R;

import java.util.List;

import Entity.Lv_ListNews;

public class ListNews_Adapter extends BaseAdapter {
    Context context;
    int myLayout;
    View convertView;
    List<Lv_ListNews> list;
    boolean isLiked = false;

    public ListNews_Adapter(Context context, int myLayout, List<Lv_ListNews> list) {
        this.context = context;
        this.myLayout = myLayout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(myLayout, null);
        ImageView avatar = convertView.findViewById(R.id.image_avatar_home);
        TextView name = convertView.findViewById(R.id.home_hovaten);
        TextView time = convertView.findViewById(R.id.home_time);
        TextView text_content = convertView.findViewById(R.id.contentnews);
        ImageView poststatus = convertView.findViewById(R.id.statuspost);
        TextView numberlike = convertView.findViewById(R.id.numberlike);
        TextView numbercmt = convertView.findViewById(R.id.numbercmt);
        TextView numbershare = convertView.findViewById(R.id.numbershare);
        ImageView image_content = convertView.findViewById(R.id.image_content_home);
        ImageView deletenew = convertView.findViewById(R.id.deletenew);
        avatar.setImageResource(list.get(i).avatar);
        name.setText(list.get(i).name);
        time.setText(list.get(i).time);
        image_content.setImageResource(list.get(i).image_content);
        if (list.get(i).statuspost==1)
            poststatus.setImageResource(R.drawable.ic_public);
        else if (list.get(i).statuspost==2)
            poststatus.setImageResource(R.drawable.ic_tag);

        text_content.setText(list.get(i).content);
        numberlike.setText(String.valueOf(list.get(i).numberlike));
        numbershare.setText(String.valueOf(list.get(i).numbershare));
        numbercmt.setText(String.valueOf(list.get(i).numbercmt));
        LinearLayout content = convertView.findViewById(R.id.content);
        ImageView like = convertView.findViewById(R.id.like);
        ImageView cmt= convertView.findViewById(R.id.cmt);
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                context.startActivity(intent);
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isLiked) {
                    like.setImageResource(R.drawable.ic_unlike); // Đổi hình khi không được thích
                    isLiked = false;
                    list.get(i).numberlike--;
                } else {
                    like.setImageResource(R.drawable.liked); // Đổi hình khi được thích
                    isLiked = true;
                    list.get(i).numberlike++;
                }
                numberlike.setText(String.valueOf(list.get(i).numberlike));
            }
        });
        cmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra("focusOnEditText", true);
                context.startActivity(intent);
            }
        });
        deletenew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int positionToDelete = i;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn có chắc muốn xoá bài viết này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            list.remove(positionToDelete);
                            notifyDataSetChanged(); // Cập nhật ListView sau khi xoá
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Đóng dialog nếu người dùng chọn "Không"
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return convertView;
    }
}
