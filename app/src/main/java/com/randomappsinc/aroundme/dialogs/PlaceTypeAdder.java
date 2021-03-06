package com.randomappsinc.aroundme.dialogs;

import android.content.Context;
import android.text.InputType;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.persistence.DatabaseManager;

public class PlaceTypeAdder {

    public interface Listener {
        void onPlaceTypeAdded();
    }

    @NonNull
    private Listener listener;
    private MaterialDialog adderDialog;

    public PlaceTypeAdder(Context context, @NonNull Listener listener) {
        this.listener = listener;
        adderDialog = new MaterialDialog.Builder(context)
                .title(R.string.add_place_type)
                .alwaysCallInputCallback()
                .inputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .input(context.getString(R.string.place_type), "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        boolean notEmpty = !input.toString().trim().isEmpty();
                        boolean notDupe = !DatabaseManager.get()
                                .getPlaceTypesDBManager()
                                .placeTypeAlreadyExists(input.toString().trim());
                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(notEmpty && notDupe);
                    }
                })
                .positiveText(R.string.add)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String placeType = dialog.getInputEditText().getText().toString().trim();
                        DatabaseManager.get().getPlaceTypesDBManager().addPlaceType(placeType);
                        PlaceTypeAdder.this.listener.onPlaceTypeAdded();
                    }
                })
                .build();
    }

    public void show() {
        adderDialog.getInputEditText().setText("");
        adderDialog.show();
    }
}
