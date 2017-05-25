package com.hzn.easytransition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;

/**
 * Tool for transition between two activities
 * <br/>
 * Created by huzn on 2017/5/8.
 */
public class EasyTransition {

    public static final String EASY_TRANSITION_OPTIONS = "easy_transition_options";
    public static final long DEFAULT_TRANSITION_ANIM_DURATION = 1000;

    /**
     * Start Activity with transition options
     *
     * @param intent  The intent to start
     * @param options Transition options, using {@link EasyTransitionOptions#makeTransitionOptions(Activity, View...)}
     *                to build your options
     */
    public static void startActivity(Intent intent, EasyTransitionOptions options) {
        options.update();
        intent.putParcelableArrayListExtra(EASY_TRANSITION_OPTIONS, options.getAttrs());
        Activity activity = options.getActivity();
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    /**
     * Start Activity for result, with transition options
     *
     * @param intent      The intent to start
     * @param requestCode If >= 0, this code will be returned in onActivityResult() when the activity exits,
     *                    see {@link Activity#startActivityForResult(Intent, int)}
     * @param options     Transition options, using {@link EasyTransitionOptions#makeTransitionOptions(Activity, View...)}
     *                    to build your options
     */
    public static void startActivityForResult(Intent intent, int requestCode, EasyTransitionOptions options) {
        options.update();
        intent.putParcelableArrayListExtra(EASY_TRANSITION_OPTIONS, options.getAttrs());
        Activity activity = options.getActivity();
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(0, 0);
    }

    /**
     * Enter the Activity, invoking this method to start enter transition animations
     *
     * @param activity     The Activity entering
     * @param duration     The duration of enter transition animation
     * @param interpolator The TimeInterpolator of enter transition animation
     * @param listener     Animator listener, normally you can do your initial after animation end
     */
    public static void enter(Activity activity, long duration, TimeInterpolator interpolator, Animator.AnimatorListener listener) {
        Intent intent = activity.getIntent();
        ArrayList<EasyTransitionOptions.ViewAttrs> attrs =
                intent.getParcelableArrayListExtra(EASY_TRANSITION_OPTIONS);
        runEnterAnimation(activity, attrs, duration, interpolator, listener);
    }

    /**
     * The same as {@link EasyTransition#enter(Activity, long, TimeInterpolator, Animator.AnimatorListener)}
     * with no interpolator
     */
    public static void enter(Activity activity, long duration, Animator.AnimatorListener listener) {
        enter(activity, duration, null, listener);
    }

    /**
     * The same as {@link EasyTransition#enter(Activity, long, TimeInterpolator, Animator.AnimatorListener)}
     * with default duration
     */
    public static void enter(Activity activity, TimeInterpolator interpolator, Animator.AnimatorListener listener) {
        enter(activity, DEFAULT_TRANSITION_ANIM_DURATION, interpolator, listener);
    }

    /**
     * The same as {@link EasyTransition#enter(Activity, long, TimeInterpolator, Animator.AnimatorListener)}
     * with default duration and no interpolator
     */
    public static void enter(Activity activity, Animator.AnimatorListener listener) {
        enter(activity, DEFAULT_TRANSITION_ANIM_DURATION, null, listener);
    }

    /**
     * The same as {@link EasyTransition#enter(Activity, long, TimeInterpolator, Animator.AnimatorListener)}
     * with default duration, no interpolator and no listener
     */
    public static void enter(Activity activity) {
        enter(activity, DEFAULT_TRANSITION_ANIM_DURATION, null, null);
    }

    private static void runEnterAnimation(Activity activity,
                                          ArrayList<EasyTransitionOptions.ViewAttrs> attrs,
                                          final long duration,
                                          final TimeInterpolator interpolator,
                                          final Animator.AnimatorListener listener) {
        if (null == attrs || attrs.size() == 0)
            return;

        for (final EasyTransitionOptions.ViewAttrs attr : attrs) {
            final View view = activity.findViewById(attr.id);

            if (null == view)
                continue;

            view.getViewTreeObserver()
                    .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            view.getViewTreeObserver().removeOnPreDrawListener(this);

                            int[] location = new int[2];
                            view.getLocationOnScreen(location);
                            view.setPivotX(0);
                            view.setPivotY(0);
                            view.setScaleX(attr.width / view.getWidth());
                            view.setScaleY(attr.height / view.getHeight());
                            view.setTranslationX(attr.startX - location[0]); // xDelta
                            view.setTranslationY(attr.startY - location[1]); // yDelta

                            view.animate()
                                    .scaleX(1)
                                    .scaleY(1)
                                    .translationX(0)
                                    .translationY(0)
                                    .setDuration(duration)
                                    .setInterpolator(interpolator)
                                    .setListener(listener);
                            return true;
                        }
                    });
        }
    }

    /**
     * Exit the Activity, invoke this method to start exit transition animation,
     * the shared views must have same ids, or it will throws NullPointerException
     *
     * @param activity     The Activity Exiting
     * @param interpolator The TimeInterpolator of exit transition animation
     * @param duration     The duration of exit transition animation
     * @throws NullPointerException throws if shared views not found in The Activity Exiting
     */
    public static void exit(Activity activity, long duration, TimeInterpolator interpolator) {
        Intent intent = activity.getIntent();
        ArrayList<EasyTransitionOptions.ViewAttrs> attrs = intent.getParcelableArrayListExtra(EASY_TRANSITION_OPTIONS);
        runExitAnimation(activity, attrs, duration, interpolator);
    }

    /**
     * The same as {@link EasyTransition#exit(Activity, long, TimeInterpolator)}
     * with default duration
     */
    public static void exit(Activity activity, TimeInterpolator interpolator) {
        exit(activity, DEFAULT_TRANSITION_ANIM_DURATION, interpolator);
    }

    /**
     * The same as {@link EasyTransition#exit(Activity, long, TimeInterpolator)}
     * with no interpolator
     */
    public static void exit(Activity activity, long duration) {
        exit(activity, duration, null);
    }

    /**
     * The same as {@link EasyTransition#exit(Activity, long, TimeInterpolator)}
     * with default duration and no interpolator
     */
    public static void exit(Activity activity) {
        exit(activity, DEFAULT_TRANSITION_ANIM_DURATION, null);
    }

    private static void runExitAnimation(final Activity activity,
                                         ArrayList<EasyTransitionOptions.ViewAttrs> attrs,
                                         long duration,
                                         TimeInterpolator interpolator) {
        if (null == attrs || attrs.size() == 0)
            return;

        for (final EasyTransitionOptions.ViewAttrs attr : attrs) {
            View view = activity.findViewById(attr.id);
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            view.setPivotX(0);
            view.setPivotY(0);

            view.animate()
                    .scaleX(attr.width / view.getWidth())
                    .scaleY(attr.height / view.getHeight())
                    .translationX(attr.startX - location[0])
                    .translationY(attr.startY - location[1])
                    .setInterpolator(interpolator)
                    .setDuration(duration);
        }

        activity.findViewById(attrs.get(0).id).postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.finish();
                activity.overridePendingTransition(0, 0);
            }
        }, duration);
    }
}
