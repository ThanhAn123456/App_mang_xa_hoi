package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ltdd_app_mang_xa_hoi.R;

import java.util.List;

import Dto.ThongBao;

public class NotificationAdapter extends BaseAdapter {
    Context context;
    int myLayout;
    View convertView;
    List<ThongBao> list;

    public NotificationAdapter(Context context, int myLayout, List<ThongBao> list) {
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
        ImageView avatar = convertView.findViewById(R.id.avatar_relationship);
        TextView name = convertView.findViewById(R.id.name_relationship);
        TextView thongBao = convertView.findViewById(R.id.action_notifications);
        thongBao.setText(list.get(i).thongBao);
        avatar.setImageResource(list.get(i).avatar);
        name.setText(list.get(i).name);
        return convertView;
    }
}
