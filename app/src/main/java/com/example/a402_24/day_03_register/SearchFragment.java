package com.example.a402_24.day_03_register;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private RecyclerView recycleView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private static ArrayList<Search> searchArraylist;


    public SearchFragment() {
        // Required empty public constructor
    }

    public void initRef(View v){
        recycleView = (RecyclerView) v.findViewById(R.id.recycle_view);
    }

    public void setEvent(View v){

        searchArraylist = new ArrayList<>();
        searchArraylist.add(new Search("naka", "hehehe","blahblah"));
        searchArraylist.add(new Search("naka", "hehehe","blahblah"));
        searchArraylist.add(new Search("naka", "hehehe","blahblah"));
        searchArraylist.add(new Search("naka", "hehehe","blahblah"));

        recycleView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recycleView.setLayoutManager(layoutManager);

        adapter = new SearchAdapter(searchArraylist);
        recycleView.setAdapter(adapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        initRef(v);
        setEvent(v);
        return v;
    }

}
