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

public class Friend_GroupAdapter extends BaseAdapter {
    Context context;
    int myLayout;
    View convertView;
    List<Friend_Group> list;

    public Friend_GroupAdapter(Context context, int myLayout, List<Friend_Group> list) {
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
        TextView numberfriend = convertView.findViewById(R.id.numberfriend_relationship);
        ImageView send = convertView.findViewById(R.id.send_relationship);
        ImageView disablefriend = convertView.findViewById(R.id.disablefriend_relationship);
        avatar.setImageResource(list.get(i).avatar);
        name.setText(list.get(i).name);
        if (list.get(i).numberfriend != -1)
            numberfriend.setText(String.valueOf(list.get(i).numberfriend)+ " " + context.getResources().getString(R.string.banchung));
        send.setImageResource(list.get(i).sendmess);
        disablefriend.setImageResource(list.get(i).disablefriend);
        return convertView;
    }
}
