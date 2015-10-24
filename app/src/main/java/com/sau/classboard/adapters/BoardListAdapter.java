package com.sau.classboard.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sau.classboard.R;
import com.sau.classboard.model.BoardData;

import java.util.ArrayList;

/**
 * Created by saurabh on 2015-10-24.
 */
public class BoardListAdapter extends RecyclerView.Adapter<BoardListAdapter.BoardViewHolder>  {
    private Context context;
    private ArrayList<BoardData> boards;

    public BoardListAdapter(Context context, ArrayList<BoardData> boards){
        this.context = context;
        this.boards = boards;
    }

    @Override
    public BoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.list_item_boards, parent, false);
        return new BoardViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(BoardViewHolder holder, int position) {
        holder.title.setText(boards.get(position).title);
        holder.code.setText(boards.get(position).code);
        if(boards.get(position).isPrivate){
        }
        else
        {

        }
    }

    @Override
    public int getItemCount() {
        return boards.size();
    }

    public class BoardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageView;
        public TextView title, code;

        public BoardViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.img_type);
            title = (TextView) itemView.findViewById(R.id.txt_title);
            code = (TextView) itemView.findViewById(R.id.txt_code);
        }
        @Override
        public void onClick(View view) {

        }


    }
}
