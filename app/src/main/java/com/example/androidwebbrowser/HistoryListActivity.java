package com.example.androidwebbrowser;

import androidx.fragment.app.Fragment;

import com.example.androidwebbrowser.SingleFragmentActivity;

public class HistoryListActivity extends SingleFragmentActivity  {
    @Override
    protected Fragment createFragment() {
        return new HistoryListFragment();
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
