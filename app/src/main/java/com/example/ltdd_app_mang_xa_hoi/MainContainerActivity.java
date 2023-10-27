package com.example.ltdd_app_mang_xa_hoi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import Adapters.ViewPagerAdapter;

public class MainContainerActivity extends AppCompatActivity implements AccountFragment.OnMenuClickListener {
    public ViewPager2 mViewPager;
    public TextView mTextView;
    public BottomNavigationView mBottomNavigationView;
    public ImageView image_search;
    public DrawerLayout drawerLayout;
    public NavigationView  navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        mViewPager = findViewById(R.id.view_pager);
        mTextView = findViewById(R.id.title_header);
        image_search = findViewById(R.id.image_search);
        mBottomNavigationView = findViewById(R.id.bottom_nav);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        mViewPager.setAdapter(adapter);
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);
                        mTextView.setText(getString(R.string.news));
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.action_friend).setChecked(true);
                        mTextView.setText(getString(R.string.sub));
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.action_chat).setChecked(true);
                        mTextView.setText(getString(R.string.chat));
                        break;
                    case 3:
                        mBottomNavigationView.getMenu().findItem(R.id.action_notifications).setChecked(true);
                        mTextView.setText(getString(R.string.notification));
                        break;
                    case 4:
                        mBottomNavigationView.getMenu().findItem(R.id.action_account).setChecked(true);
                        mTextView.setText(getString(R.string.profile));
                        break;

                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Xử lý sự kiện cho từng icon trong menu
                int itemId = item.getItemId();
                if (itemId == R.id.setting_1) {
                    Intent intent = new Intent(MainContainerActivity.this, UpdateAccountActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.setting_2) {
                    Intent intent = new Intent(MainContainerActivity.this, ReportActivity.class);
                    startActivity(intent);
                }
                else if (itemId == R.id.setting_3) {
                    Dialog dialog = DialogThem(R.layout.mgb_disableaccount);

                    // Tìm nút "Có" trong layout của dialog
                    Button cofirmdisacount = dialog.findViewById(R.id.button_disableaccount);

                    // Gắn sự kiện click cho nút "Có"
                    cofirmdisacount.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Điều hướng đến trang đăng nhập
                            Intent intent = new Intent(MainContainerActivity.this, LoginActivity.class);
                            startActivity(intent);

                            // Đóng dialog sau khi chuyển trang
                            dialog.dismiss();
                            finish();
                        }
                    });

                }
                else if (itemId == R.id.setting_4) {
                    Dialog dialog = DialogThem(R.layout.mgb_logout);

                    // Tìm nút "Có" trong layout của dialog
                    Button cofirmdisacount = dialog.findViewById(R.id.button_logout);

                    // Gắn sự kiện click cho nút "Có"
                    cofirmdisacount.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Điều hướng đến trang đăng nhập
                            Intent intent = new Intent(MainContainerActivity.this, LoginActivity.class);
                            startActivity(intent);

                            // Đóng dialog sau khi chuyển trang
                            dialog.dismiss();
                            finish();
                        }
                    });
                }
                else if (itemId == R.id.setting_5) {
                    Intent intent = new Intent(MainContainerActivity.this, AboutUsActivity.class);
                    startActivity(intent);
                }

                drawerLayout.closeDrawer(GravityCompat.END);
                return true;
            }
        });
        mBottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.action_home)
                {
                    mViewPager.setCurrentItem(0);
                    mTextView.setText(getString(R.string.news));
                }
                if (itemId == R.id.action_friend)
                {
                    mViewPager.setCurrentItem(1);
                    mTextView.setText(getString(R.string.sub));
                }
                if (itemId == R.id.action_chat)
                {
                    mViewPager.setCurrentItem(2);
                    mTextView.setText(getString(R.string.chat));
                }
                if (itemId == R.id.action_notifications)
                {
                    mViewPager.setCurrentItem(3);
                    mTextView.setText(getString(R.string.notification));
                }
                if (itemId == R.id.action_account)
                {
                    mViewPager.setCurrentItem(4);
                    mTextView.setText(getString(R.string.profile));
                }
                return false;
            }
        });
        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainContainerActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }
    private Dialog DialogThem(int idLayout) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(idLayout);
        dialog.show();
        return dialog;
    }
    @Override
    public void onMenuClick() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.openDrawer(GravityCompat.END);
        } else {
            drawerLayout.closeDrawer(GravityCompat.END);
        }
    }
}