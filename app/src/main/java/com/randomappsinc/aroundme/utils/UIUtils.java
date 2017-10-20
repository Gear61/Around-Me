package com.randomappsinc.aroundme.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconDrawable;
import com.randomappsinc.aroundme.R;

public class UIUtils {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
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
}
