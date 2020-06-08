package com.example.androidwebbrowser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebHistoryItem;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidwebbrowser.models.WebBrowserHistoryItem;

import java.util.ArrayList;
import java.util.List;

public class HistoryListFragment extends Fragment {


    private RecyclerView mHistoryRecyclerView;
    private HistoryAdapter mHistoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_list_fragment,container,false);

        mHistoryRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallBack());
        itemTouchHelper.attachToRecyclerView(mHistoryRecyclerView);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        setActionBar(toolbar);
        updateUI();



        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void updateUI(){
        BrowserLab browserLab = BrowserLab.get(getContext());

        List<WebBrowserHistoryItem> historyItems = browserLab.getHisoryItems();



        if(historyItems.size()==0){

            View view=  getLayoutInflater().inflate(R.layout.empty_history,null,false);
            Toolbar toolbar = view.findViewById(R.id.toolbar);
            setActionBar(toolbar);
            getActivity().setContentView(view);


            return;
        }



        if(mHistoryAdapter ==null){


            mHistoryAdapter = new HistoryAdapter(historyItems);
            mHistoryRecyclerView.setAdapter(mHistoryAdapter);


            return;

        } else {
            mHistoryAdapter.setFavorites(historyItems);
            mHistoryAdapter.notifyDataSetChanged();

        }



    }

    private class HistoryAdapter extends RecyclerView.Adapter<HistoryHolder> {


        private List<WebBrowserHistoryItem> mHistoryItems;


        public HistoryAdapter(List<WebBrowserHistoryItem> favorites){
            mHistoryItems =favorites;

        }


        public void setFavorites(List<WebBrowserHistoryItem> favorites){
            this.mHistoryItems =favorites;
        }

        @NonNull
        @Override
        public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(getActivity());

            return new HistoryHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
            WebBrowserHistoryItem favorite = mHistoryItems.get(position);
            holder.bind(favorite);
        }

        @Override
        public int getItemCount() {
            return mHistoryItems.size();
        }
    }

    private class HistoryHolder extends RecyclerView.ViewHolder{


        private TextView mTvTitle;
        private TextView mTvUrl;
        private TextView mTvDate;
        private WebBrowserHistoryItem mWebBrowserHistoryItem;

        public HistoryHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.history_list_item,parent,false));


            mTvUrl= itemView.findViewById(R.id.text_view_url);
            mTvTitle = itemView.findViewById(R.id.text_view_title);
            mTvDate=itemView.findViewById(R.id.text_view_date);

        }


        public void bind (WebBrowserHistoryItem webBrowserHistoryItem){
            mWebBrowserHistoryItem = webBrowserHistoryItem;
            mTvTitle.setText(webBrowserHistoryItem.getTitle());
            mTvUrl.setText(webBrowserHistoryItem.getUrl());
            mTvDate.setText(webBrowserHistoryItem.getDate().toString());
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
            WebBrowserHistoryItem hystoryItems =  mHistoryAdapter.mHistoryItems.get(position);

            BrowserLab browserLab = BrowserLab.get(getContext());
            browserLab.removeHistoryItem(hystoryItems);
            mHistoryAdapter.mHistoryItems.remove(hystoryItems);
            mHistoryAdapter.notifyItemRemoved(position);
            updateUI();


        }


    }

    private void setActionBar(Toolbar toolbar){

        HistoryListActivity activity = (HistoryListActivity) getActivity();
        activity.setSupportActionBar(toolbar);


        ActionBar actionBar = activity.getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(R.string.search_history);
    }


}




