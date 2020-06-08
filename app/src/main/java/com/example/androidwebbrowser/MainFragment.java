package com.example.androidwebbrowser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;

import com.example.androidwebbrowser.models.WebBrowserHistoryItem;

import java.util.Date;

public class MainFragment extends Fragment {



    private Toolbar mToolbar;
    private WebView mWebView;
    private EditText mEditText;
    private ProgressBar mProgressBar;
    private boolean mIsFavorite;
    private BrowserLab mBrowserLab;
    private MenuItem mFavoriteItem;
    private Menu mMenu;



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
        actionBar.setDisplayShowTitleEnabled(false);


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

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_GO){
                    mWebView.loadUrl(mEditText.getText().toString());
                    return  true;
                }

                return  false;
            }
        });

        mProgressBar=view.findViewById(R.id.progressBar);

        mBrowserLab= BrowserLab.get(getContext());





        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_fragment,menu);
        mFavoriteItem=menu.findItem(R.id.add_or_remove_favorites);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent;

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
            case R.id.add_or_remove_favorites:

                WebHistoryItem webHistoryItem = mWebView.copyBackForwardList().getCurrentItem();

                if(!mIsFavorite){
                    mBrowserLab.addFavorite(new WebBrowserHistoryItem(
                            webHistoryItem.getUrl(),
                            webHistoryItem.getTitle()));
                    mIsFavorite=true;
                    mFavoriteItem.setIcon(R.drawable.ic_favorite_on);
                }else{
                    mBrowserLab.removeFavorite(new WebBrowserHistoryItem(mWebView.getUrl()));
                    mFavoriteItem.setIcon(R.drawable.ic_favorite_off);
                    mIsFavorite=false;
                }

                return true;

            case R.id.favorite_list:
                intent = new Intent(getContext(),FavoriteListActivity.class);
                startActivity(intent);

                return true;

            case R.id.history:
                 intent = new Intent(getContext(),HistoryListActivity.class);
                startActivity(intent);

                return true;

            case R.id.share:
                shareUrl(mWebView.getUrl());
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }



    }

    private void shareUrl(String url){

        Activity activity = getActivity();
        Intent i = ShareCompat.IntentBuilder.from(activity)
                .setType("text/plain").getIntent();

        i.putExtra(Intent.EXTRA_TEXT,url);
        i = Intent.createChooser(i,getString(R.string.share_app));
        if(i.resolveActivity(activity.getPackageManager())!=null){
            startActivity(i);
        }
    }

    private class MyBrowser extends WebViewClient {


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }



        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mEditText.setText(url);
            mProgressBar.setVisibility(View.VISIBLE);

            mIsFavorite = mBrowserLab.isFavorite(url);


        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.GONE);

            checkFavorite(url);

            WebHistoryItem item = mWebView.copyBackForwardList().getCurrentItem();

            mBrowserLab.addHistoryItem(new WebBrowserHistoryItem(
                    item.getUrl(),
                    item.getTitle(),
                    new Date()));




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

    @Override
    public void onResume() {
        super.onResume();

        if(mFavoriteItem!=null){
            checkFavorite(mWebView.getUrl());

        }





    }

    public void checkFavorite(String url){
        mIsFavorite = BrowserLab.get(getContext()).isFavorite(url);

        if(mIsFavorite){

            mFavoriteItem.setIcon(R.drawable.ic_favorite_on);
        }else{
            mFavoriteItem.setIcon(R.drawable.ic_favorite_off);
        }
    }
}
