package com.otonomis.marvelcomics.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.otonomis.marvelcomics.DetailActivity;
import com.otonomis.marvelcomics.R;
import com.otonomis.marvelcomics.helper.Constant;
import com.otonomis.marvelcomics.pojo.ComicsModel;

import java.util.List;


public class ComicsAdapter extends RecyclerView.Adapter<ComicsAdapter.MyViewHolder> {

    private List<ComicsModel> comicsModelList;
    Context context;
    String slug;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle, mContent;
        public ImageView mImage;
        public LinearLayout mLinear;
        ImageView mBorder, mSave, mShare;

        public MyViewHolder(View view) {
            super(view);
            mTitle = (TextView) view.findViewById(R.id.comics_title);
            mImage = (ImageView) view.findViewById(R.id.image_view);
            mLinear = (LinearLayout) view.findViewById(R.id.linear);
            mBorder = (ImageView) view.findViewById(R.id.item_border);

        }
    }


    public ComicsAdapter(List<ComicsModel> comicsModelList, Context context) {
        this.comicsModelList = comicsModelList;
        this.context = context;
        setHasStableIds(true);


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comics_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ComicsModel categoryModel = comicsModelList.get(position);
        holder.mTitle.setText(categoryModel.getTitle());
        Glide.with(context).load(categoryModel.getImage()).diskCacheStrategy(DiskCacheStrategy.ALL).override(500, 500).into(holder.mImage);
        if (position % 2 != 0) {
            holder.mBorder.setBackgroundResource(R.color.black);
        }


        holder.mLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("single_comic", categoryModel);
                context.startActivity(intent);


            }
        });

    }


    @Override
    public int getItemCount() {
        return comicsModelList.size();
    }


}