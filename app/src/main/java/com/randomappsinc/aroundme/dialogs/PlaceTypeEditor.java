package com.randomappsinc.aroundme.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.InputType;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.models.PlaceType;
import com.randomappsinc.aroundme.persistence.DatabaseManager;

public class PlaceTypeEditor {

    public interface Listener {
        void onPlaceTypeEdited();
    }

    @NonNull private Listener mListener;
    private MaterialDialog mEditorDialog;
    private PlaceType mPlaceType;

    public PlaceTypeEditor(Context context, @NonNull Listener listener) {
        mListener = listener;
        mEditorDialog = new MaterialDialog.Builder(context)
                .title(R.string.edit_place_type)
                .alwaysCallInputCallback()
                .inputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
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
                .positiveText(R.string.edit)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String placeType = dialog.getInputEditText().getText().toString().trim();
                        mPlaceType.setText(placeType);
                        DatabaseManager.get().getPlaceTypesDBManager().updatePlaceType(mPlaceType);
                        mListener.onPlaceTypeEdited();
                    }
                })
                .build();
    }

    public void show(PlaceType placeType) {
        mPlaceType = placeType;
        mEditorDialog.getInputEditText().setText(mPlaceType.getText());
        mEditorDialog.getInputEditText().setSelection(mPlaceType.getText().length());
        mEditorDialog.show();
    }
}
