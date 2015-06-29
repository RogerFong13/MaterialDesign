package com.wanda.fangke.instagram.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wanda.fangke.instagram.R;
import com.wanda.fangke.instagram.Utils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fangke on 2015/6/25.
 */
public class FeedContextMenu extends LinearLayout {

    private static final int CONTEXT_MENU_WIDTH = Utils.dpToPx(240);


    private onFeedContextMenuItemClickListener onItemClickListener;

    private int feedItem = -1;

    public FeedContextMenu(Context context) {
        super(context);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_context_menu,this,true);
        setBackgroundResource(R.mipmap.bg_container_shadow);
        setOrientation(VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(CONTEXT_MENU_WIDTH, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void bindToItem(int feedItem){
        this.feedItem = feedItem;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ButterKnife.inject(this);
    }

    public void dissmiss(){
        ((ViewGroup)getParent()).removeView(FeedContextMenu.this);
    }

    @OnClick(R.id.btnReport)
    public void onReportClick(){
        if(onItemClickListener!=null){
            onItemClickListener.onReportClick(feedItem);
        }
    }
    @OnClick(R.id.btnSharePhoto)
    public void onSharePhotoClick() {
        if (onItemClickListener != null) {
            onItemClickListener.onSharePhotoClick(feedItem);
        }
    }

    @OnClick(R.id.btnCopyShareUrl)
    public void onCopyShareUrlClick() {
        if (onItemClickListener != null) {
            onItemClickListener.onCopyShareUrlClick(feedItem);
        }
    }

    @OnClick(R.id.btnCancel)
    public void onCancelClick() {
        if (onItemClickListener != null) {
            onItemClickListener.onCancelClick(feedItem);
        }
    }

    public void setOnItemClickListener(onFeedContextMenuItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface onFeedContextMenuItemClickListener {

        void onReportClick(int feedItem);

        void onSharePhotoClick(int feedItem);

        void onCopyShareUrlClick(int feedItem);

        void onCancelClick(int feedItem);
    }
}
