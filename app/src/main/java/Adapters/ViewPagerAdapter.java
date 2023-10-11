package Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.ltdd_app_mang_xa_hoi.AccountFragment;
import com.example.ltdd_app_mang_xa_hoi.Chat_Friend_Fragment;
import com.example.ltdd_app_mang_xa_hoi.RelationshipFragment;
import com.example.ltdd_app_mang_xa_hoi.HomeFragment;
import com.example.ltdd_app_mang_xa_hoi.NotificationsFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new HomeFragment();
            case 1:
                return new RelationshipFragment();
            case 2:
                return new Chat_Friend_Fragment();
            case 3:
                return new NotificationsFragment();
            case 4:
                return new AccountFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
