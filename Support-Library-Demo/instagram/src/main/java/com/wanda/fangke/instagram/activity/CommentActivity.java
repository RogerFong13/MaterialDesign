package com.wanda.fangke.instagram.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.wanda.fangke.instagram.R;
import com.wanda.fangke.instagram.Utils;
import com.wanda.fangke.instagram.adapter.CommentsAdapter;
import com.wanda.fangke.instagram.view.SendCommentButton;

import butterknife.InjectView;

/**
 * Created by fangke on 2015/6/23.
 */
public class CommentActivity extends BaseActivity implements SendCommentButton.OnSendClickListener{

    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";
    private int drawingStartLocation;
    private CommentsAdapter commentsAdapter;

    @InjectView(R.id.contentRoot)
    LinearLayout contentRoot;
    @InjectView(R.id.rvComments)
    RecyclerView rvComments;
    @InjectView(R.id.llAddComment)
    LinearLayout llAddComment;
    @InjectView(R.id.etComment)
    EditText etComment;
    @InjectView(R.id.btnSendComment)
    SendCommentButton btnSendComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        setupComments();
        setupSendCommentButton();

        drawingStartLocation = getIntent().getIntExtra(ARG_DRAWING_START_LOCATION, 0);
        if (savedInstanceState == null){
            contentRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    contentRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                    startIntroAnimation();
                    return true;
                }
            });
        }
    }

    private void setupComments(){

        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(manager);
        rvComments.setHasFixedSize(true);

        commentsAdapter = new CommentsAdapter(this);
        rvComments.setAdapter(commentsAdapter);
        rvComments.setOverScrollMode(View.OVER_SCROLL_NEVER);
            rvComments.setOnScrollListener(new OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        commentsAdapter.setAnimationsLocked(true);
                    }
                }
            });
    }

    private void setupSendCommentButton(){
        btnSendComment.setOnSendClickListener(this);
    }
    private void startIntroAnimation(){

        contentRoot.setScaleY(0.1f);
        contentRoot.setPivotY(drawingStartLocation);
        llAddComment.setTranslationY(300);

        contentRoot.animate().scaleY(1).setDuration(500).setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animateContent();
                    }
                }).start();
    }

    private  void animateContent(){
        commentsAdapter.updateItems();
        llAddComment.animate().translationY(0)
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(500).start();
    }

    @Override
    public void onBackPressed() {
        contentRoot.animate().translationY(Utils.getScreenHeight(this))
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        CommentActivity.super.onBackPressed();
                        overridePendingTransition(0, 0);
                    }
                }).start();
    }

    @Override
    public void onSendClickListener(View view) {
        if(validateComment()){
            commentsAdapter.addItem();
            commentsAdapter.setAnimationsLocked(false);
            commentsAdapter.setDelayEnterAnimation(false);
            rvComments.smoothScrollBy(0, rvComments.getChildAt(0).getHeight() * commentsAdapter.getItemCount());

            etComment.setText(null);
            btnSendComment.setCurrentState(SendCommentButton.STATE_DONE);
        }
    }

    private boolean validateComment(){
        if(TextUtils.isEmpty(etComment.getText())){
            btnSendComment.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake_error));
            return false;
        }
        return true;
    }
}
