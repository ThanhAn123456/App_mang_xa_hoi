package MainFragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ltdd_app_mang_xa_hoi.R;

import java.util.ArrayList;

import Adapters.ListNotificatonAdapter;
import Entity.Lv_ListNotification;


public class NotificationsFragment extends Fragment {
    ListView list_notification;
    TextView readed;
    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        activity =getActivity();
//        list_notification = view.findViewById(R.id.lv_listnotification);
//        readed = view.findViewById(R.id.readed);
//        ArrayList<Lv_ListNotification> arrayList = new ArrayList<>();
//        arrayList.add(new Lv_ListNotification(R.drawable.avatar, "Đình Yên", 1, 1));
//        arrayList.add(new Lv_ListNotification(R.drawable.avatar, "Đình Yên", 2, 1));
//        arrayList.add(new Lv_ListNotification(R.drawable.avatar, "Hoàng Phúc", 2, 0));
//        arrayList.add(new Lv_ListNotification(R.drawable.avatar, "Tấn Hên", 1, 0));
//        arrayList.add(new Lv_ListNotification(R.drawable.avatar, "Xuân Việt", 1, 0));
//        ListNotificatonAdapter adapter = new ListNotificatonAdapter(getContext(), R.layout.lv_notification, arrayList);
//        list_notification.setAdapter(adapter);
//
//        readed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                for (Lv_ListNotification notification : arrayList) {
//                    notification.status = 0;
//                }
//                adapter.notifyDataSetChanged();
//            }
//        });
        return view;
    }
}