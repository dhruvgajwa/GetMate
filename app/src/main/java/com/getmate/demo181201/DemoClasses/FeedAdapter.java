/*
package com.getmate.demo181201.DemoClasses;

import android.animation.AnimatorSet;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.getmate.demo181201.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    private static final int ANIMATED_ITEMS_COUNT = 2;

    private Context context;
    //@@ -36,6 +39,9 @@
    private boolean animateItems = false;

    private final Map<Integer, Integer> likesCount = new HashMap<>();
    private final Map<RecyclerView.ViewHolder, AnimatorSet> likeAnimations = new HashMap<>();
    private final ArrayList<Integer> likedPositions = new ArrayList<>();

    private OnFeedItemClickListener onFeedItemClickListener;

    public FeedAdapter(Context context) {
        @@ -76,11 +82,19 @@ public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            holder.ivFeedBottom.setImageResource(R.drawable.img_feed_bottom_2);
        }
        updateLikesCounter(holder, false);
        updateHeartButton(holder, false);

        holder.btnComments.setOnClickListener(this);
        holder.btnComments.setTag(position);
        holder.btnMore.setOnClickListener(this);
        holder.btnMore.setTag(position);
        holder.btnLike.setOnClickListener(this);
        holder.btnLike.setTag(holder);

        if (likeAnimations.containsKey(holder)) {
            likeAnimations.get(holder).cancel();
        }
        resetLikeAnimationState(holder);
    }

    @Override
    @@ -103,20 +117,79 @@ private void updateLikesCounter(CellFeedViewHolder holder, boolean animated) {
        likesCount.put(holder.getPosition(), currentLikesCount);
    }

    private void updateHeartButton(final CellFeedViewHolder holder, boolean animated) {
        if (animated) {
            if (!likeAnimations.containsKey(holder)) {
                AnimatorSet animatorSet = new AnimatorSet();
                likeAnimations.put(holder, animatorSet);

                ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder.btnLike, "rotation", 0f, 360f);
                rotationAnim.setDuration(300);
                rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

                ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder.btnLike, "scaleX", 0.2f, 1f);
                bounceAnimX.setDuration(300);
                bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

                ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder.btnLike, "scaleY", 0.2f, 1f);
                bounceAnimY.setDuration(300);
                bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
                bounceAnimY.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        holder.btnLike.setImageResource(R.drawable.ic_heart_red);
                    }
                });

                animatorSet.play(rotationAnim);
                animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);

                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        resetLikeAnimationState(holder);
                    }
                });

                animatorSet.start();
            }
        } else {
            if (likedPositions.contains(holder.getPosition())) {
                holder.btnLike.setImageResource(R.drawable.ic_heart_red);
            } else {
                holder.btnLike.setImageResource(R.drawable.ic_heart_outline_grey);
            }
        }
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        public void onClick(View view) {
            final int viewId = view.getId();
            if (viewId == R.id.btnComments) {
                if (onFeedItemClickListener != null) {
                    onFeedItemClickListener.onCommentsClick(v, (Integer) v.getTag());
                    onFeedItemClickListener.onCommentsClick(view, (Integer) view.getTag());
                }
            } else if (viewId == R.id.btnMore) {
                if (onFeedItemClickListener != null) {
                    onFeedItemClickListener.onMoreClick(v, (Integer) v.getTag());
                    onFeedItemClickListener.onMoreClick(view, (Integer) view.getTag());
                }
            } else if (viewId == R.id.btnLike) {
                CellFeedViewHolder holder = (CellFeedViewHolder) view.getTag();
                if (!likedPositions.contains(holder.getPosition())) {
                    likedPositions.add(holder.getPosition());
                    updateLikesCounter(holder, true);
                    updateHeartButton(holder, true);
                }
            }
        }
    }

    private void resetLikeAnimationState(CellFeedViewHolder holder) {
        likeAnimations.remove(holder);
        holder.vBgLike.setVisibility(View.GONE);
        holder.ivLike.setVisibility(View.GONE);
    }

    public void updateItems(boolean animated) {
        itemsCount = 10;
        animateItems = animated;*/
