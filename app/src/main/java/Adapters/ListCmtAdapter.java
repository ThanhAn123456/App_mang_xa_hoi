package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.ltdd_app_mang_xa_hoi.R;

import java.util.List;

import Dto.Lv_ListCmt;
import Dto.Lv_ListFriend;
import Dto.Lv_ListNotification;

public class ListCmtAdapter extends BaseAdapter {
    Context context;
    int myLayout;
    View convertView;
    List<Lv_ListCmt> list;
    public ListCmtAdapter(Context context, int myLayout, List<Lv_ListCmt> list) {
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
        TextView cmt = convertView.findViewById(R.id.cmt);
        avatar.setImageResource(list.get(i).avatar);
        name.setText(list.get(i).name);
        cmt.setText(list.get(i).cmt);
        return convertView;
    }
}
