package com.wanda.fangke.instagram.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wanda.fangke.instagram.R;
import com.wanda.fangke.instagram.utils.RoundedTransformation;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by fangke on 2015/6/23.
 */
public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int itemsCount = 0;
    private int lastAnimatedPosition = -1;
    private int avatarSize;

    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;


    public CommentsAdapter(Context context){
        this.context = context;
        avatarSize = context.getResources().getDimensionPixelSize(R.dimen.btn_fab_size);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_comments,parent,false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        runEnterAnimation(viewHolder.itemView, position);

        CommentsViewHolder holder = (CommentsViewHolder)viewHolder;
        switch (position%2) {
            case 0:
                holder.tvComment.setText("Live a life you will remember");
                break;
            case 1:
                holder.tvComment.setText("Cause we could be Immortals");
                break;
        }
        Picasso.with(context).load(R.drawable.user_profile_photo).centerCrop()
                .resize(avatarSize,avatarSize).transform(new RoundedTransformation())
                .into(holder.ivUserAvatar);
    }

    private void runEnterAnimation(View view,int position){
        if(animationsLocked) return;

        if(position>lastAnimatedPosition){

            lastAnimatedPosition = position;
            view.setTranslationY(100);
            view.setAlpha(0.f);
            view.animate().translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation?20*(position):0)
                    .setInterpolator(new DecelerateInterpolator(2.f))
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;
                        }
                    }).start();
        }
    }

    @Override
    public int getItemCount() {
        return itemsCount;
    }

    public void updateItems(){
        itemsCount = 10;
        notifyDataSetChanged();
    }

    public void addItem(){
        itemsCount++;
        notifyItemChanged(itemsCount-1);
    }

    public void setAnimationsLocked(boolean animationsLocked) {
        this.animationsLocked = animationsLocked;
    }

    public void setDelayEnterAnimation(boolean delayEnterAnimation) {
        this.delayEnterAnimation = delayEnterAnimation;
    }

    public static class CommentsViewHolder extends RecyclerView.ViewHolder{

        @InjectView(R.id.ivUserAvatar)
        ImageView ivUserAvatar;
        @InjectView(R.id.tvComment)
        TextView tvComment;

        public CommentsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }
    }
}
