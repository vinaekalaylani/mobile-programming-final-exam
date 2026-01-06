package com.vinaekal.sisehat.util;

import android.animation.ObjectAnimator;
import android.view.MotionEvent;
import android.view.View;

public class AnimationUtils {

    public static void setClickAnimation(View view) {
        view.setOnTouchListener((v, event) -> {
            if (v.hasOnClickListeners()) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        animateScale(v, 0.97f, 150);
                        break;
                    case MotionEvent.ACTION_UP:
                        // Jaga agar onClickListener tetap berjalan
                        v.performClick();
                    case MotionEvent.ACTION_CANCEL:
                        animateScale(v, 1f, 150);
                        break;
                }
            }
            // Mengembalikan 'true' untuk menandakan bahwa kita telah menangani event ini
            return true;
        });
    }

    private static void animateScale(View view, float scale, long duration) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", scale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", scale);
        scaleX.setDuration(duration);
        scaleY.setDuration(duration);
        scaleX.start();
        scaleY.start();
    }
}
