package com.randomappsinc.aroundme.dialogs;

import android.content.Context;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.constants.DistanceUnit;
import com.randomappsinc.aroundme.persistence.PreferencesManager;
import com.randomappsinc.aroundme.utils.UIUtils;

public class DistanceUnitChooser {

    private MaterialDialog mDialog;

    public DistanceUnitChooser(Context context) {
        @DistanceUnit String currentUnit = PreferencesManager.get().getDistanceUnit();
        int currentPosition = currentUnit.equals(DistanceUnit.MILES) ? 0 : 1;

        mDialog = new MaterialDialog.Builder(context)
                .title(R.string.set_distance_unit_title)
                .content(R.string.distance_unit_prompt)
                .items(R.array.distance_unit_options)
                .itemsCallbackSingleChoice(currentPosition, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog,
                                               View itemView,
                                               int which,
                                               CharSequence text) {
                        @DistanceUnit String chosenUnit = which == 0
                                ? DistanceUnit.MILES
                                : DistanceUnit.KILOMETERS;
                        PreferencesManager.get().setDistanceUnit(chosenUnit);
                        UIUtils.showToast(R.string.distance_unit_set);
                        return true;
                    }
                })
                .positiveText(R.string.choose)
                .negativeText(R.string.cancel)
                .build();
    }

    public void show() {
        mDialog.show();
    }
}
