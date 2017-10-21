package com.randomappsinc.aroundme.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.models.PlaceType;
import com.randomappsinc.aroundme.persistence.DatabaseManager;

public class PlaceTypeDeleter {

    public interface Listener {
        void onPlaceTypeDeleted();
    }

    @NonNull private Listener mListener;
    private MaterialDialog mDeleterDialog;
    private PlaceType mPlaceType;

    public PlaceTypeDeleter(Context context, @NonNull Listener listener) {
        mListener = listener;
        mDeleterDialog = new MaterialDialog.Builder(context)
                .title("")
                .content(R.string.confirm_place_type_deletion)
                .positiveText(R.string.yes)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        DatabaseManager.get().getPlaceTypesDBManager().deletePlaceType(mPlaceType);
                        mListener.onPlaceTypeDeleted();
                    }
                })
                .build();
    }

    public void show(PlaceType placeType) {
        mPlaceType = placeType;
        mDeleterDialog.setTitle(placeType.getText());
        mDeleterDialog.show();
    }
}
