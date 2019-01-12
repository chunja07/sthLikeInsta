package com.example.a402_24.day_03_register;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchAdapterViewholder> {

    private List<Search> searchList;

    public void setEvent(){
        
    }



    public static class SearchAdapterViewholder extends RecyclerView.ViewHolder{

        ImageView search_img;
        TextView search_id, search_intro;

        public void initRefs(View v){
            search_img = (ImageView) v.findViewById(R.id.search_img);
            search_id = (TextView) v.findViewById(R.id.search_id);
            search_intro = (TextView) v.findViewById(R.id.search_intro);
        }

        public SearchAdapterViewholder(@NonNull View itemView) {
            super(itemView);
            initRefs(itemView);

        }
    }

    public  SearchAdapter(List<Search> searchList){

        this.searchList = searchList;
    }

    @Override
    public SearchAdapterViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_list_view, viewGroup, false);
        SearchAdapterViewholder holder = new SearchAdapterViewholder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapterViewholder holder, int i) {
            holder.search_id.setText(searchList.get(i).getSearch_id());
            holder.search_intro.setText(searchList.get(i).getSearch_intro());
            setEvent();
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }
}
