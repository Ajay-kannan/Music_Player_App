package com.example.myapplicationdemo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplicationdemo.fragments.HomeFragment;
import com.example.myapplicationdemo.fragments.NotificationFragment;
import com.example.myapplicationdemo.fragments.SettingFragment;

public class MyAdapterClass extends FragmentStateAdapter {
    public MyAdapterClass(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0 :
                return new HomeFragment();
            case 1 :
                return new NotificationFragment();
            case 2:
                return new SettingFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
