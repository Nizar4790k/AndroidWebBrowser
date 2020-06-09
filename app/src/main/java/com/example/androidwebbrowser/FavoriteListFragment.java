package com.example.androidwebbrowser;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidwebbrowser.models.WebBrowserHistoryItem;

import java.util.List;

public class FavoriteListFragment extends Fragment {


    private RecyclerView mFavoriteRecyclerView;
    private FavoriteAdapter mFavoriteAdapter;
    private BrowserLab mBrowserLab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_list_fragment,container,false);

         mFavoriteRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
         mFavoriteRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
         mFavoriteRecyclerView.addItemDecoration(new SimpleItemDecorator(15));


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallBack());
        itemTouchHelper.attachToRecyclerView(mFavoriteRecyclerView);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        setActionBar(toolbar);
        updateUI();

        mBrowserLab = BrowserLab.get(getContext());




        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void updateUI(){
        BrowserLab browserLab = BrowserLab.get(getContext());

        List<WebBrowserHistoryItem> favorites = browserLab.getFavorites();

        if(favorites.size()==0){

            View view=  getLayoutInflater().inflate(R.layout.empty_favorites,null,false);
            Toolbar toolbar = view.findViewById(R.id.toolbar);
            setActionBar(toolbar);
            getActivity().setContentView(view);


            return;
        }



        if(mFavoriteAdapter ==null){


            mFavoriteAdapter = new FavoriteAdapter(favorites);
            mFavoriteRecyclerView.setAdapter(mFavoriteAdapter);


            return;

        } else {
            mFavoriteAdapter.setFavorites(favorites);
            mFavoriteAdapter.notifyDataSetChanged();

        }



    }

    private class FavoriteAdapter extends RecyclerView.Adapter<FavoriteHolder> {


        private List<WebBrowserHistoryItem> mFavorites;


        public FavoriteAdapter(List<WebBrowserHistoryItem> favorites){
            mFavorites=favorites;

        }


        public void setFavorites(List<WebBrowserHistoryItem> favorites){
            this.mFavorites=favorites;
        }

        @NonNull
        @Override
        public FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(getActivity());

            return new FavoriteHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull FavoriteHolder holder, int position) {
            WebBrowserHistoryItem favorite = mFavorites.get(position);
            holder.bind(favorite);
        }

        @Override
        public int getItemCount() {
            return mFavorites.size();
        }
    }

    private class  FavoriteHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        private TextView mTvTitle;
        private TextView mTvUrl;
        private WebBrowserHistoryItem mWebBrowserHistoryItem;


        public FavoriteHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.favorite_list_item,parent,false));


            mTvUrl= itemView.findViewById(R.id.text_view_url);
            mTvTitle = itemView.findViewById(R.id.text_view_title);
            itemView.setOnClickListener(this);
        }


        public void bind (WebBrowserHistoryItem webBrowserHistoryItem){
            mWebBrowserHistoryItem = webBrowserHistoryItem;
            mTvTitle.setText(webBrowserHistoryItem.getTitle());

            String htmlString = "<u>"+webBrowserHistoryItem.getUrl()+"</u>";

            mTvUrl.setText(Html.fromHtml(htmlString));

        }


        @Override
        public void onClick(View v) {

            MainFragment.changeUrl(mWebBrowserHistoryItem.getUrl());
            getActivity().finish();
        }
    }


    private class SwipeToDeleteCallBack extends ItemTouchHelper.SimpleCallback {



        public SwipeToDeleteCallBack() {
            super(0, ItemTouchHelper.RIGHT);

        }


        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            int position = viewHolder.getAdapterPosition();
            WebBrowserHistoryItem favorite =  mFavoriteAdapter.mFavorites.get(position);

            BrowserLab browserLab = BrowserLab.get(getContext());
            browserLab.removeFavorite(favorite);
            mFavoriteAdapter.mFavorites.remove(favorite);
            mFavoriteAdapter.notifyItemRemoved(position);
            updateUI();


        }


    }

    private void setActionBar(Toolbar toolbar){

        FavoriteListActivity activity = (FavoriteListActivity) getActivity();
        activity.setSupportActionBar(toolbar);


        ActionBar actionBar = activity.getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(R.string.favorites_list);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.favorite_list_fragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.delete_all:

                mBrowserLab.removeAllFavorites();
                updateUI();
                return true;

            default:
                return super.onOptionsItemSelected(item);


        }


    }
}




