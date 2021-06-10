package com.aapanavyapar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aapanavyapar.aapanavyapar.R;
import com.aapanavyapar.aapanavyapar.services.RatingOfShop;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    ArrayList<RatingOfShop> ratingOfShopList;
    Context context;

    public CommentAdapter(ArrayList<RatingOfShop> ratingOfShopList, Context context) {
        this.ratingOfShopList = ratingOfShopList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.comment_card, parent,false);
        CommentAdapter.ViewHolder viewHolder = new CommentAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        final RatingOfShop ratingOfShop = ratingOfShopList.get(position);

        int rating = ratingOfShop.getRating().getNumber();
        if(rating != 0){
            rating ++;
        }

        holder.userName.setText(ratingOfShop.getUserName());
        holder.comment.setText(ratingOfShop.getComment());
        holder.ratingBar.setRating(rating);
        holder.timeStamp.setText(ratingOfShop.getTimestamp().substring(0, 17));

//        String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
//        LocalDateTime localDateTime = LocalDateTime.from(formatter.parse(ratingOfShop.getTimestamp().substring(0, 23)));

    }

    @Override
    public int getItemCount() {
        return ratingOfShopList.size();
    }

    public void notifyData(ArrayList<RatingOfShop> myList) {
        this.ratingOfShopList = myList;
        notifyDataSetChanged();
    }

    public void addNewData(RatingOfShop data) {
        this.ratingOfShopList.add(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView userName;
        TextView comment;
        RatingBar ratingBar;
        TextView timeStamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.username_textview);
            comment = itemView.findViewById(R.id.shop_comments);
            ratingBar = itemView.findViewById(R.id.searched_shop_rating_bar);
            timeStamp = itemView.findViewById(R.id.comment_date_time);
        }
    }
}

