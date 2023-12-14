package Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import MainFragment.AccountFragment;
import MainFragment.ChatFragment;
import MainFragment.RelationshipFragment;
import MainFragment.HomeFragment;
import MainFragment.NotificationsFragment;

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
                return new ChatFragment();
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
    @Override
    public long getItemId(int position) {
        return position;
    }

}
