package com.example.androidwebbrowser;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Debug;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {



    private Toolbar mToolbar;
    private WebView mWebView;
    private EditText mEditText;
    private ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment,container,false);


      mToolbar = (Toolbar) view.findViewById(R.id.toolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Toast.makeText(getContext(),"Getting back",Toast.LENGTH_SHORT).show();
            }
        });

        MainActivity activity = (MainActivity) getActivity();

        activity.setSupportActionBar(mToolbar);


        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        setHasOptionsMenu(true);

        mWebView = view.findViewById(R.id.web_view);
        mWebView.setWebViewClient(new MyBrowser());

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressBar.setProgress(newProgress);
            }
        });

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("https://www.google.com");

        mEditText = view.findViewById(R.id.edit_text);
        mEditText.setText(mWebView.getUrl());

        mProgressBar=view.findViewById(R.id.progressBar);







        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_fragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.home:
                goHome();

                return true;
            case R.id.reload:
            mWebView.reload();
            return  true;
            case R.id.forward:
                goForward();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }



    }

    private class MyBrowser extends WebViewClient {

       /*
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        */


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mEditText.setText(url);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    void goBack(){
        if (mWebView.canGoBack()){
            mWebView.goBack();

        }

    }




    private void goHome(){
        mWebView.loadUrl("https://www.google.com");

    }

    private void goForward(){
        if(mWebView.canGoForward()){
            mWebView.goForward();


        }

    }


}
