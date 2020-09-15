/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.launcher3.anim;

import static com.android.launcher3.anim.AnimatorPlaybackController.addAnimationHoldersRecur;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.util.FloatProperty;

import com.android.launcher3.anim.AnimatorPlaybackController.Holder;

import java.util.ArrayList;

/**
 * Utility class to keep track of a running animation.
 *
 * This class allows attaching end callbacks to an animation is intended to be used with
 * {@link com.android.launcher3.anim.AnimatorPlaybackController}, since in that case
 * AnimationListeners are not properly dispatched.
 *
 * TODO: Find a better name
 */
public class PendingAnimation extends AnimatedPropertySetter {

    private final ArrayList<Holder> mAnimHolders = new ArrayList<>();
    private final long mDuration;

    public PendingAnimation(long  duration) {
        mDuration = duration;
    }

    public long getDuration() {
        return mDuration;
    }

    /**
     * Utility method to sent an interpolator on an animation and add it to the list
     */
    public void add(Animator anim, TimeInterpolator interpolator, SpringProperty springProperty) {
        anim.setInterpolator(interpolator);
        add(anim, springProperty);
    }

    @Override
    public void add(Animator anim) {
        add(anim, SpringProperty.DEFAULT);
    }

    public void add(Animator a, SpringProperty springProperty) {
        mAnim.play(a.setDuration(mDuration));
        addAnimationHoldersRecur(a, mDuration, springProperty, mAnimHolders);
    }

<<<<<<< HEAD
    /**
     * Configures interpolator of the underlying AnimatorSet.
     */
    public void setInterpolator(TimeInterpolator interpolator) {
        mAnim.setInterpolator(interpolator);
=======
    public void finish(boolean isSuccess) {
        for (Consumer<EndState> listeners : mEndListeners) {
            listeners.accept(new EndState(isSuccess));
        }
        mEndListeners.clear();
    }

    @Override
    public void setViewAlpha(View view, float alpha, TimeInterpolator interpolator) {
        if (view == null || view.getAlpha() == alpha) {
            return;
        }
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, View.ALPHA, alpha);
        anim.addListener(new AlphaUpdateListener(view));
        anim.setInterpolator(interpolator);
        add(anim);
    }

    @Override
    public <T> void setFloat(T target, FloatProperty<T> property, float value,
            TimeInterpolator interpolator) {
        if (property.get(target) == value) {
            return;
        }
        Animator anim = ObjectAnimator.ofFloat(target, property, value);
        anim.setDuration(mDuration).setInterpolator(interpolator);
        add(anim);
>>>>>>> 95786e077d (Good riddance UserEventDispatcher)
    }

    public <T> void addFloat(T target, FloatProperty<T> property, float from, float to,
            TimeInterpolator interpolator) {
        Animator anim = ObjectAnimator.ofFloat(target, property, from, to);
        anim.setInterpolator(interpolator);
        add(anim);
    }

    /**
     * Creates and returns the underlying AnimatorSet
     */
    @Override
    public AnimatorSet buildAnim() {
        if (mAnimHolders.isEmpty()) {
            // Add a placeholder animation to that the duration is respected
            add(ValueAnimator.ofFloat(0, 1).setDuration(mDuration));
        }
        return super.buildAnim();
    }

    /**
     * Creates a controller for this animation
     */
    public AnimatorPlaybackController createPlaybackController() {
        return new AnimatorPlaybackController(buildAnim(), mDuration, mAnimHolders);
    }
<<<<<<< HEAD
=======

    /**
     * Add a listener of receiving the end state.
     * Note that the listeners are called as a result of calling {@link #finish(boolean)}
     * and not automatically
     */
    public void addEndListener(Consumer<EndState> listener) {
        mEndListeners.add(listener);
    }

    public static class EndState {
        public boolean isSuccess;

        public EndState(boolean isSuccess) {
            this.isSuccess = isSuccess;
        }
    }
>>>>>>> 95786e077d (Good riddance UserEventDispatcher)
}
