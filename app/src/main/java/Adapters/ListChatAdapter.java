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

import Entity.Lv_ListChat;

public class ListChatAdapter extends BaseAdapter {
    Context context;
    int myLayout;
    View convertView;
    List<Lv_ListChat> list;

    public ListChatAdapter(Context context, int myLayout, List<Lv_ListChat> list) {
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
        TextView status = convertView.findViewById(R.id.status);
        TextView number_chat = convertView.findViewById(R.id.number_chat);
        avatar.setImageResource(list.get(i).image);
        name.setText(list.get(i).name);
        if (list.get(i).status ==0)
            status.setText(context.getResources().getString(R.string.activity));
        else if (list.get(i).status ==-1){
            status.setVisibility(View.GONE);
        }
        else{
            status.setText(context.getResources().getString(R.string.acti) + " " + String.valueOf(list.get(i).status) + " " + context.getResources().getString(R.string.minute));
            status.setTextColor(ContextCompat.getColor(context, R.color.pink));
        }
        if (list.get(i).numberchat !=0)
            number_chat.setText(String.valueOf(list.get(i).numberchat));
        else
            number_chat.setVisibility(View.GONE);
        return convertView;
    }
}
