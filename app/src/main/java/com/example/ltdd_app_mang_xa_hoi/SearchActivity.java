package com.example.ltdd_app_mang_xa_hoi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

import Fragments.FriendFragment;
import Fragments.GroupFragment;
import Fragments.Search_All_Fragment;
import Fragments.Search_Friend_Fragment;
import Fragments.Search_Group_Fragment;
import Fragments.Search_New_Fragment;

public class SearchActivity extends AppCompatActivity {
    ImageButton backButton;
    TabLayout tabLayout;
    public static SearchView searchView;
    RecyclerView recyclerViewFriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        backButton = findViewById(R.id.back);
        searchView = findViewById(R.id.searchView);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tabLayout = findViewById(R.id.tab_layout);
        TabLayout.Tab tab1 = tabLayout.newTab().setText(R.string.search_all);
        TabLayout.Tab tab2 = tabLayout.newTab().setText(R.string.search_new);
        TabLayout.Tab tab3 = tabLayout.newTab().setText(R.string.search_friend);
        TabLayout.Tab tab4 = tabLayout.newTab().setText(R.string.search_group);
        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);
        tabLayout.addTab(tab3);
        tabLayout.addTab(tab4);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Search_All_Fragment())
                .commit();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        // Hiển thị Fragment tin nhắn bạn bè
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new Search_All_Fragment())
                                .commit();
                        break;
                    case 1:
                        // Hiển thị Fragment tin nhắn nhóm
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new Search_New_Fragment())
                                .commit();
                        break;
                    case 2:
                        // Hiển thị Fragment tin nhắn nhóm
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new Search_Friend_Fragment())
                                .commit();
                        break;
                    case 3:
                        // Hiển thị Fragment tin nhắn nhóm
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new Search_Group_Fragment())
                                .commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}