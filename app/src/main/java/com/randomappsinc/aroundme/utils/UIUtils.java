package com.randomappsinc.aroundme.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.google.android.material.snackbar.Snackbar;
import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconDrawable;
import com.randomappsinc.aroundme.R;

public class UIUtils {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) {
            return;
        }

        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void loadMenuIcon(Menu menu, int itemId, Icon icon, Context context) {
        menu.findItem(itemId).setIcon(
                new IconDrawable(context, icon)
                        .colorRes(R.color.white)
                        .actionBarSize());
    }

    public static void showSnackbar(View parent, @StringRes int resId) {
        showSnackbar(parent, MyApplication.getAppContext().getString(resId));
    }

    private static void showSnackbar(View parent, String message) {
        Context context = MyApplication.getAppContext();
        Snackbar snackbar = Snackbar.make(parent, message, Snackbar.LENGTH_LONG);
        View rootView = snackbar.getView();
        rootView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    public static int getRatingDrawableId(double rating) {
        if (rating < 1.0) {
            return R.drawable.stars_0;
        }
        if (rating < 1.25) {
            return R.drawable.stars_1;
        }
        if (rating < 1.75) {
            return R.drawable.stars_1_and_a_half;
        }
        if (rating < 2.25) {
            return R.drawable.stars_2;
        }
        if (rating < 2.75) {
            return R.drawable.stars_2_and_a_half;
        }
        if (rating < 3.25) {
            return R.drawable.stars_3;
        }
        if (rating < 3.75) {
            return R.drawable.stars_3_and_a_half;
        }
        if (rating < 4.25) {
            return R.drawable.stars_4;
        }
        if (rating < 4.75) {
            return R.drawable.stars_4_and_a_half;
        }
        return R.drawable.stars_5;
    }

    public static void showToast(@StringRes int messageId) {
        Toast.makeText(MyApplication.getAppContext(), messageId, Toast.LENGTH_SHORT).show();
    }

    public static void animateFavoriteToggle(final TextView favoriteToggle, final boolean isFavorited) {
        Context context = MyApplication.getAppContext();
        final int animLength = context.getResources().getInteger(R.integer.shorter_anim_length);
        final int lightRed = context.getResources().getColor(R.color.light_red);
        final int darkGray = context.getResources().getColor(R.color.dark_gray);

        if (favoriteToggle.getAnimation() == null || favoriteToggle.getAnimation().hasEnded()) {
            ObjectAnimator animX = ObjectAnimator.ofFloat(
                    favoriteToggle, "scaleX", 0.75f);
            ObjectAnimator animY = ObjectAnimator.ofFloat(
                    favoriteToggle, "scaleY", 0.75f);
            AnimatorSet shrink = new AnimatorSet();
            shrink.playTogether(animX, animY);
            shrink.setDuration(animLength);
            shrink.setInterpolator(new AccelerateInterpolator());
            shrink.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}

                @Override
                public void onAnimationEnd(Animator animation) {
                    favoriteToggle.setText(isFavorited ? R.string.heart_filled_icon : R.string.heart_icon);
                    favoriteToggle.setTextColor(isFavorited ? lightRed : darkGray);

                    ObjectAnimator animX = ObjectAnimator.ofFloat(
                            favoriteToggle, "scaleX", 1.0f);
                    ObjectAnimator animY = ObjectAnimator.ofFloat(
                            favoriteToggle, "scaleY", 1.0f);
                    AnimatorSet grow = new AnimatorSet();
                    grow.playTogether(animX, animY);
                    grow.setDuration(animLength);
                    grow.setInterpolator(new AnticipateOvershootInterpolator());
                    grow.start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {}

                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
            shrink.start();
        }
    }

    public static void setCheckedImmediately(CheckBox checkbox, boolean checked) {
        checkbox.setChecked(checked);
        checkbox.jumpDrawablesToCurrentState();
    }

    public static void showLongToast(@StringRes int stringId) {
        showToast(stringId, Toast.LENGTH_LONG);
    }

    private static void showToast(@StringRes int stringId, int toastLength) {
        Toast.makeText(MyApplication.getAppContext(), stringId, toastLength).show();
    }
}
