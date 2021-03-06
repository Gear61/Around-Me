package com.randomappsinc.aroundme.location;

import android.content.Context;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.aroundme.R;

/** Widget that allows users to enter in their location manually */
public class LocationForm {

    public interface Listener {
        void onLocationEntered(String location);
    }

    @NonNull private Listener listener;
    private MaterialDialog locationDialog;

    LocationForm(Context context, @NonNull Listener listener) {
        this.listener = listener;

        String location = context.getString(R.string.location);
        locationDialog = new MaterialDialog.Builder(context)
                .title(R.string.location_form_title)
                .content(R.string.location_form_prompt)
                .positiveText(android.R.string.yes)
                .negativeText(android.R.string.cancel)
                .alwaysCallInputCallback()
                .input(location, "", (dialog, input) -> {
                    boolean inputEnabled = !input.toString().trim().isEmpty();
                    dialog.getActionButton(DialogAction.POSITIVE).setEnabled(inputEnabled);
                })
                .onPositive((dialog, which) -> {
                    String locationInput = dialog.getInputEditText().getText().toString().trim();
                    LocationForm.this.listener.onLocationEntered(locationInput);
                })
                .build();
    }

    public void show() {
        locationDialog.show();
    }
}
