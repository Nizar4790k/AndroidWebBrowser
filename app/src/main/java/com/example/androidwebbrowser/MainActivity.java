package com.example.androidwebbrowser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends SingleFragmentActivity{


    private MainFragment mFragment;

    @Override
    protected Fragment createFragment() {

        mFragment= new MainFragment();
        return  mFragment;
    }

    @Override
    public boolean onSupportNavigateUp() {
        mFragment.goBack();
        return true;
    }


}
