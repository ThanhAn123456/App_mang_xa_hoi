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

import Dto.Friend_Group;
import Dto.Lv_Setting;

public class Lv_ContentAdapter extends BaseAdapter {
    Context context;
    int myLayout;
    View convertView;
    List<Lv_Setting> list;

    public Lv_ContentAdapter(Context context, int myLayout, List<Lv_Setting> list) {
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
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView= inflater.inflate(myLayout, null);
        ImageView image = convertView.findViewById(R.id.image_setting);
        TextView content = convertView.findViewById(R.id.txt_setting);
        image.setImageResource(list.get(i).image);
        content.setText(context.getString(list.get(i).content));
        return convertView;
    }
}