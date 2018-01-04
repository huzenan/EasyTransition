package com.hzn.easytransition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hzn.lib.EasyTransition;

public class DetailActivity extends AppCompatActivity {

    private LinearLayout layoutAbout;
    private ImageView ivAdd;
    private boolean finishEnter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // pre init some views and data
        initViews();

        // if re-initialized, do not play any anim
        long transitionDuration = 800;
        if (null != savedInstanceState)
            transitionDuration = 0;

        // transition enter
        finishEnter = false;
        EasyTransition.enter(
                this,
                transitionDuration,
                new DecelerateInterpolator(),
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // init other views after transition anim
                        finishEnter = true;
                        initOtherViews();
                    }
                });
    }

    private void initViews() {
        TextView tvName = (TextView) findViewById(R.id.tv_name);
        tvName.setText(getIntent().getStringExtra("name"));
    }

    private void initOtherViews() {
        layoutAbout = (LinearLayout) findViewById(R.id.layout_about);
        layoutAbout.setVisibility(View.VISIBLE);
        layoutAbout.setAlpha(0);
        layoutAbout.setTranslationY(-30);
        layoutAbout.animate()
                .setDuration(300)
                .alpha(1)
                .translationY(0);

        ivAdd = (ImageView) findViewById(R.id.iv_add);
        ivAdd.setVisibility(View.VISIBLE);
        ivAdd.setScaleX(0);
        ivAdd.setScaleY(0);
        ivAdd.animate()
                .setDuration(200)
                .scaleX(1)
                .scaleY(1);
    }

    @Override
    public void onBackPressed() {
        if (finishEnter) {
            finishEnter = false;
            startBackAnim();
        }
    }

    private void startBackAnim() {
        // forbidden scrolling
        ScrollView sv = (ScrollView) findViewById(R.id.sv);
        sv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        // start our anim
        ivAdd.animate()
                .setDuration(200)
                .scaleX(0)
                .scaleY(0);

        layoutAbout.animate()
                .setDuration(300)
                .alpha(0)
                .translationY(-30)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // transition exit after our anim
                        EasyTransition.exit(DetailActivity.this, 800, new DecelerateInterpolator());
                    }
                });
    }
}
