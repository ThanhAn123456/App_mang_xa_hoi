package com.example.ltdd_app_mang_xa_hoi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Main_container extends AppCompatActivity {
    BottomNavigationView bottom_nav;

    HomeFragment homeFragment = new HomeFragment();
    FriendsFragment friendsFragment = new FriendsFragment();
    ChatFragment chatFragment = new ChatFragment();
    NotificationsFragment notificationsFragment = new NotificationsFragment();
    AccountFragment accountFragment = new AccountFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        bottom_nav = findViewById(R.id.bottom_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.FL_Container,homeFragment).commit();
        bottom_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.getItemId()==R.id.action_home){
                        getSupportFragmentManager().beginTransaction().replace(R.id.FL_Container,homeFragment).commit();
                        return true;
                }
                if (item.getItemId()==R.id.action_friend){
                    getSupportFragmentManager().beginTransaction().replace(R.id.FL_Container,friendsFragment).commit();
                    return true;
                }
                if (item.getItemId()==R.id.action_chat){
                    getSupportFragmentManager().beginTransaction().replace(R.id.FL_Container,chatFragment).commit();
                    return true;
                }
                if (item.getItemId()==R.id.action_notifications){
                    getSupportFragmentManager().beginTransaction().replace(R.id.FL_Container,notificationsFragment).commit();
                    return true;
                }
                if (item.getItemId()==R.id.action_account){
                    getSupportFragmentManager().beginTransaction().replace(R.id.FL_Container,accountFragment).commit();
                    return true;
                }
                return false;
            }
        });
    }
}