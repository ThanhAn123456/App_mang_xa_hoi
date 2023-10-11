package com.example.ltdd_app_mang_xa_hoi;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class AccountFragment extends Fragment {

    ImageView ic_setting;
    public interface OnMenuClickListener {
        void onMenuClick();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ic_setting = view.findViewById(R.id.ic_setting);
        ic_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra xem hoạt động có triển khai interface OnMenuClickListener không
                if (getActivity() instanceof OnMenuClickListener) {
                    // Gọi phương thức onMenuClick thông qua interface
                    ((OnMenuClickListener) getActivity()).onMenuClick();
                }
            }
        });




        return view;
    }
}