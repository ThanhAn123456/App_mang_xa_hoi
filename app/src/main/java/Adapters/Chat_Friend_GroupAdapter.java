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

import Dto.Chat_Friend_Group;

public class Chat_Friend_GroupAdapter extends BaseAdapter {
    Context context;
    int myLayout;
    View convertView;
    List<Chat_Friend_Group> list;

    public Chat_Friend_GroupAdapter(Context context, int myLayout, List<Chat_Friend_Group> list) {
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

        ImageView avatar = convertView.findViewById(R.id.avatar_chat_relationship);
        TextView name = convertView.findViewById(R.id.name_chat_relationship);
        TextView online = convertView.findViewById(R.id.online_status);
        TextView TimeLastRead = convertView.findViewById(R.id.message_status);
        TextView mess_num = convertView.findViewById(R.id.message_unread_number);

        avatar.setImageResource(list.get(i).avatar);
        name.setText(list.get(i).name);
        //online.setText(list.get(i).status); //(lỗi tại đây --không thể hiện thị dữ liệu, cần kiểm tra lại file friend_group_status.xml)
        //TimeLastRead.setText(list.get(i).offline_time); //tương tự
        //mess_num.setText(list.get(i).notification); //tương tự

        return convertView;
    }
}
