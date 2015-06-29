package com.wanda.fangke.instagram.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;

import com.wanda.fangke.instagram.R;
import com.wanda.fangke.instagram.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by fangke on 2015/6/12.
 */
public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private static final int ANIMATED_ITEMS_COUNT = 2;

    private Context context;
    private int lastAnimatedPosition = -1;
    private int itemsCount = 0;
    private OnFeedItemClickListener onFeedItemClickListener;

    private final Map<Integer, Integer> likesCount = new HashMap<>();
    private final ArrayList<Integer> likedPositions = new ArrayList<>();

    public FeedAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_feed, parent, false);
        return new CellFeedViewHolder(view);
    }

    private void runEnterAnimation(View view, int position) {
        if (position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(Utils.getScreenHeight(context));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        runEnterAnimation(viewHolder.itemView, position);
        CellFeedViewHolder holder = (CellFeedViewHolder) viewHolder;
        if (position % 2 == 0) {
            holder.ivFeedCenter.setImageResource(R.mipmap.img_feed_center_1);
            holder.ivFeedBottom.setImageResource(R.mipmap.img_feed_bottom_1);
        } else {
            holder.ivFeedCenter.setImageResource(R.mipmap.img_feed_center_2);
            holder.ivFeedBottom.setImageResource(R.mipmap.img_feed_bottom_2);
        }
        holder.ivFeedBottom.setOnClickListener(this);
        holder.btnMore.setOnClickListener(this);
        holder.ivFeedBottom.setTag(position);
        holder.btnMore.setTag(position);
        holder.btnLike.setOnClickListener(this);
        holder.btnLike.setTag(holder);

        updateLikesCounter(holder,false);
    }


    @Override
    public int getItemCount() {
        return itemsCount;
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.ivFeedBottom){
            if(onFeedItemClickListener!=null){
                onFeedItemClickListener.onCommentsClick(v,(Integer)v.getTag());
            }
        }else if(v.getId() == R.id.btnMore){
            if(onFeedItemClickListener!=null){
                onFeedItemClickListener.onMoreClick(v, (Integer) v.getTag());
            }
        }else if(v.getId() == R.id.btnLike){
            CellFeedViewHolder holder = (CellFeedViewHolder)v.getTag();
            updateLikesCounter(holder,true);
        }
    }

    public static class CellFeedViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.ivFeedCenter)
        ImageView ivFeedCenter;
        @InjectView(R.id.ivFeedBottom)
        ImageView ivFeedBottom;
        @InjectView(R.id.btnMore)
        ImageButton btnMore;
        @InjectView(R.id.btnLike)
        ImageButton btnLike;
        @InjectView(R.id.vBgLike)
        View vBgLike;
        @InjectView(R.id.ivLike)
        ImageView ivLike;
        @InjectView(R.id.tsLikesCounter)
        TextSwitcher tsLikesCounter;

        public CellFeedViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    public void updateItems() {
        itemsCount = 10;
        fillLikesWithRandomValues();
        notifyDataSetChanged();
    }

    private void fillLikesWithRandomValues(){
        for (int i = 0;i<getItemCount();i++){
            likesCount.put(i,new Random().nextInt(100));
        }
    }

    public void setOnFeedItemClickListener(OnFeedItemClickListener onFeedItemClickListener){
        this.onFeedItemClickListener = onFeedItemClickListener;
    }

    public interface OnFeedItemClickListener {
         void onCommentsClick(View v, int position);
         void onMoreClick(View v,int position);
    }

    private void updateLikesCounter(CellFeedViewHolder holder,boolean animated){
        int currentLikesCount = likesCount.get(holder.getPosition())+1;
        String likesCountText = context.getResources().getQuantityString(R.plurals.likes_count, currentLikesCount, currentLikesCount);
        if(animated){
            holder.tsLikesCounter.setText(likesCountText);
        }else{
            holder.tsLikesCounter.setCurrentText(likesCountText);
        }
        likesCount.put(holder.getPosition(),currentLikesCount);
    }
}
