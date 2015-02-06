package com.example.zweitun;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

public class AdvancedListViewTouchListener implements View.OnTouchListener {
    private ListView listView;

    private int slop;
    private int minFlingVelocity;
    private int maxFlingVelocity;
    private long animationTime;
    private int viewWidth = 1; // 1 and not 0 to prevent dividing by zero

    private float downX;
    private float downY;
    private View downView;
    private int downPosition;
    private VelocityTracker velocityTracker;
    private boolean swiping;

    private List<PendingDismissData> pendingDismisses = new ArrayList<PendingDismissData>();

    private int dismissAnimationRefCount = 0;

    public AdvancedListViewTouchListener(ListView listView, OnDismissListener onDismissListener) {
        this.listView = listView;

        this.onDismissListener = onDismissListener;
        ViewConfiguration vc = ViewConfiguration.get(listView.getContext());
        slop = vc.getScaledTouchSlop();
        minFlingVelocity = vc.getScaledMinimumFlingVelocity();
        maxFlingVelocity = vc.getScaledMaximumFlingVelocity();
        animationTime = listView.getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (viewWidth < 2) {
            viewWidth = listView.getWidth();
        }

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // find selected item or return
                Rect rect = new Rect();
                int childCount = listView.getChildCount();
                int[] listViewCoords = new int[2];
                listView.getLocationOnScreen(listViewCoords);
                int x1 = (int) event.getRawX() - listViewCoords[0];
                int y1 = (int) event.getRawY() - listViewCoords[1];
                View child;
                for (int i = 0; i < childCount; i++) {
                    child = listView.getChildAt(i);
                    child.getHitRect(rect);
                    if (rect.contains(x1, y1)) {
                        downView = child;
                        break;
                    }
                }

                if (downView != null) {
                    downX = event.getRawX();
                    downY = event.getRawY();
                    downPosition = listView.getPositionForView(downView);
                    velocityTracker = VelocityTracker.obtain();
                    velocityTracker.addMovement(event);
                }

                return false;

            case MotionEvent.ACTION_UP:
                if (velocityTracker == null) {
                    break;
                }

                float deltaX = event.getRawX() - downX;
                velocityTracker.addMovement(event);
                velocityTracker.computeCurrentVelocity(1000);
                float velocityX = Math.abs(velocityTracker.getXVelocity());
                float velocityY = Math.abs(velocityTracker.getYVelocity());

                boolean dismiss = false;

                if (Math.abs(deltaX) > viewWidth / 2) {
                    dismiss = true;
                } else if (Math.abs(deltaX) > viewWidth / 4
                        && minFlingVelocity <= velocityX
                        && velocityX <= maxFlingVelocity
                        && velocityY < velocityX / 2) {
                    dismiss = true;
                }
                if (dismiss) {
                    // dismiss
                    final View downView = this.downView; // mDownView gets
                    // null'd
                    // before animation
                    // ends
                    final int downPosition = this.downPosition;
                    Log.d("dismiss","dismiss");
                    ++dismissAnimationRefCount;

                    TranslateAnimation swipeAnim = new TranslateAnimation(deltaX, viewWidth, 0, 0);
                    AlphaAnimation alphaAnim = new AlphaAnimation(1, 0);
                    AnimationSet set = new AnimationSet(true);
                    set.addAnimation(swipeAnim);
                    set.addAnimation(alphaAnim);
                    set.setDuration(animationTime);
                    downView.startAnimation(set);
                    set.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Log.d("ENDANIM","ENDANIM");
                            dismiss(downView, listView.getPositionForView(downView));

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                } else {

                    float fraction = Math.abs(deltaX) / viewWidth;

                    float currentAlpha = Math.max(0f, Math.min(1f, 1f - 2f * fraction));

                    TranslateAnimation swipeAnim = new TranslateAnimation(deltaX, 0, 0, 0);
                    AlphaAnimation alphaAnim = new AlphaAnimation(currentAlpha, 1);
                    AnimationSet set = new AnimationSet(true);
                    set.addAnimation(swipeAnim);
                    set.addAnimation(alphaAnim);
                    set.setDuration(animationTime);
                    downView.startAnimation(set);

                    // cancel
                    //animate(mDownView).translationX(0).alpha(1)
                      //      .setDuration(mAnimationTime).setListener(null);
                }
                velocityTracker = null;
                downX = 0;
                downView = null;
                downPosition = AdapterView.INVALID_POSITION;
                swiping = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (velocityTracker == null) {
                    break;
                }

                velocityTracker.addMovement(event);
                deltaX = event.getRawX() - downX;
                float deltaY = event.getRawY() - downY;
                if (deltaX > slop && Math.abs(deltaX) > Math.abs(deltaY)) {
                    swiping = true;
                    listView.requestDisallowInterceptTouchEvent(true);

                    // Cancel ListView's touch (un-highlighting the item)
                    MotionEvent cancelEvent = MotionEvent.obtain(event);
                    cancelEvent
                            .setAction(MotionEvent.ACTION_CANCEL
                                    | (event.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
                    listView.onTouchEvent(cancelEvent);
                }

                if (swiping) {

                    TranslateAnimation swipeAnim = new TranslateAnimation(deltaX, deltaX, 0, 0);

                    float fraction = Math.abs(deltaX) / viewWidth;

                    float currentAlpha = Math.max(0f, Math.min(1f, 1f - 2f * fraction));

                    AlphaAnimation alphaAnim = new AlphaAnimation(currentAlpha, currentAlpha);
                    AnimationSet set = new AnimationSet(true);
                    set.addAnimation(swipeAnim);
                    set.addAnimation(alphaAnim);
                    set.setFillAfter(true);
                    set.setFillEnabled(true);
                    downView.startAnimation(set);

                    return true;
                }
                break;
            }

        return false;
    }

    private void dismiss(final View dismissView, final int dismissPosition) {
        final ViewGroup.LayoutParams lp = dismissView.getLayoutParams();
        final int originalHeight = dismissView.getHeight();

        ValueAnimator animator = ValueAnimator.ofInt(originalHeight, 1)
                .setDuration(animationTime);

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                --dismissAnimationRefCount;
                if (dismissAnimationRefCount == 0) {
                    // No active animations, process all pending dismisses.
                    // Sort by descending position
                    Collections.sort(pendingDismisses);

                    int[] dismissPositions = new int[pendingDismisses.size()];
                    for (int i = pendingDismisses.size() - 1; i >= 0; i--) {
                        dismissPositions[i] = pendingDismisses.get(i).position;
                    }
                    //mCallback.onDismiss(mListView, dismissPositions);
                    onDismissListener.dismiss(listView.getItemIdAtPosition(dismissPosition));

                    ViewGroup.LayoutParams lp;
                    for (PendingDismissData pendingDismiss : pendingDismisses) {
                        // Reset view presentation
                        ViewHelper.setAlpha(pendingDismiss.view, 1f);
                        ViewHelper.setTranslationX(pendingDismiss.view, 0);
                        lp = pendingDismiss.view.getLayoutParams();
                        lp.height = 0;
                        pendingDismiss.view.setLayoutParams(lp);
                    }

                    pendingDismisses.clear();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                lp.height = (Integer) valueAnimator.getAnimatedValue();
                dismissView.setLayoutParams(lp);
            }
        });

        pendingDismisses.add(new PendingDismissData(dismissPosition,
                dismissView));
        animator.start();
    }

    private OnDismissListener onDismissListener;
    public interface OnDismissListener {
        public void dismiss(long id);
    }

    class PendingDismissData implements Comparable<PendingDismissData> {
        public int position;
        public View view;

        public PendingDismissData(int position, View view) {
            this.position = position;
            this.view = view;
        }

        @Override
        public int compareTo(PendingDismissData other) {
            // Sort by descending position
            return other.position - position;
        }
    }
}
