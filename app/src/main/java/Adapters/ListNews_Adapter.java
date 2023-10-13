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

import Dto.Lv_ListNews;

public class ListNews_Adapter extends BaseAdapter {
    Context context;
    int myLayout;
    View convertView;
    List<Lv_ListNews> list;

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
        ImageView image_content_home = convertView.findViewById(R.id.image_content_home);
        avatar.setImageResource(list.get(i).avatar);
        name.setText(list.get(i).name);
        time.setText(list.get(i).time);
        image_content_home.setImageResource(list.get(i).image_content);
        return convertView;
    }
}
