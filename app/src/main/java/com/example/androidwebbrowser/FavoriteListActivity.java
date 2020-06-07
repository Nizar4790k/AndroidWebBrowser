package com.example.androidwebbrowser;

import androidx.fragment.app.Fragment;

public class FavoriteListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new FavoriteListFragment();
    }


    @Override
    public boolean onSupportNavigateUp() {
        super.onSupportNavigateUp();
        finish();
        return true;
    }


}
