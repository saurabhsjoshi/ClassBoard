package com.sau.classboard.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sau.classboard.R;
import com.sau.classboard.adapters.BoardListAdapter;
import com.sau.classboard.model.BoardData;

import java.util.ArrayList;

import co.uk.rushorm.core.RushSearch;

/**
 * Created by saurabh on 2015-10-18.
 */
public class BoardFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<BoardData> boards;
    private BoardListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boards, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_boards);
        boards = new ArrayList<>(new RushSearch().find(BoardData.class));
        adapter = new BoardListAdapter(getActivity(), boards);
        RecyclerView.LayoutManager boardLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(boardLayoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
