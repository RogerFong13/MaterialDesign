package com.wanda.fangke.instagram.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.wanda.fangke.instagram.Utils;

/**
 * Created by fangke on 2015/6/25.
 */
public class FeedContextMenuManager extends RecyclerView.OnScrollListener implements View.OnAttachStateChangeListener {

    private static FeedContextMenuManager  manager;

    private FeedContextMenu menuView;
    private boolean isContextMenuShowing ;
    private boolean isContextMenuDismissing;

    public static FeedContextMenuManager getManager(){
        if(manager ==null){
            manager = new FeedContextMenuManager();
        }
        return  manager;
    }

    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        menuView = null;
    }

    public void toggleContextMenuFromView(View openingView,int feedItem,FeedContextMenu.onFeedContextMenuItemClickListener listener){
        if(menuView == null){
            showContextMenuFromView(openingView,feedItem,listener);
        }else{
            hideContextMenu();
        }
    }

    private void showContextMenuFromView(final View openingView,int feedItem,FeedContextMenu.onFeedContextMenuItemClickListener listener){

        if(!isContextMenuShowing){
            isContextMenuShowing = true;
            menuView = new FeedContextMenu(openingView.getContext());
            menuView.bindToItem(feedItem);
            menuView.addOnAttachStateChangeListener(this);
            menuView.setOnItemClickListener(listener);

            ((ViewGroup)openingView.getRootView().findViewById(android.R.id.content)).addView(menuView);
            menuView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    menuView.getViewTreeObserver().removeOnPreDrawListener(this);
                    setupContextMenuInitialPosition(openingView);
                    performShowAnimation();
                    return false;
                }
            });
        }
    }

    public void hideContextMenu(){
        if(!isContextMenuDismissing){
            isContextMenuDismissing = true;
            performDismissAnimation();
        }
    }

    private void setupContextMenuInitialPosition(View openingView){
        final int[] openingViewLocation = new int[2];
        openingView.getLocationOnScreen(openingViewLocation);
        menuView.setTranslationX(openingViewLocation[0] - menuView.getWidth() / 3);
        menuView.setTranslationY(openingViewLocation[1] - menuView.getHeight() - Utils.dpToPx(16));
    }

    private void performShowAnimation(){

        menuView.setPivotX(menuView.getWidth()/2);
        menuView.setPivotY(menuView.getHeight());
        menuView.setScaleX(0.1f);
        menuView.setScaleY(0.1f);
        menuView.animate().scaleX(1f).scaleY(1f).setDuration(300)
                .setInterpolator(new OvershootInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isContextMenuShowing = false;
                    }
                });
    }

    private void performDismissAnimation(){
        menuView.setPivotX(menuView.getWidth() / 2);
        menuView.setPivotY(menuView.getHeight());
        menuView.animate().scaleX(0.1f).scaleY(0.1f).setDuration(300)
                .setInterpolator(new AccelerateInterpolator())
                .setStartDelay(100)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                       if(menuView!=null){
                           menuView.dissmiss();
                       }
                       isContextMenuDismissing = false;
                    }
                });
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (menuView != null) {
            hideContextMenu();
            menuView.setTranslationY(menuView.getTranslationY() - dy);
        }
    }
}
