package com.randomappsinc.aroundme.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.dialogs.PlaceTypeDeleter;
import com.randomappsinc.aroundme.dialogs.PlaceTypeEditor;
import com.randomappsinc.aroundme.models.PlaceType;
import com.randomappsinc.aroundme.persistence.DatabaseManager;
import com.randomappsinc.aroundme.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlaceTypesAdapter extends RecyclerView.Adapter<PlaceTypesAdapter.PlaceTypeViewHolder> {

    public interface Listener {
        void onItemClick(PlaceType placeType);

        void scrollToItem(int position);
    }

    @NonNull private Listener listener;
    private List<PlaceType> placeTypes;
    private View parent;
    private int lastClickedItem;
    private PlaceTypeEditor placeTypeEditor;
    private PlaceTypeDeleter placeTypeDeleter;

    public PlaceTypesAdapter(Context context, @NonNull Listener listener, View parent) {
        this.listener = listener;
        placeTypes = DatabaseManager.get().getPlaceTypesDBManager().getPlaceTypes();
        this.parent = parent;
        placeTypeEditor = new PlaceTypeEditor(context, mEditedListener);
        placeTypeDeleter = new PlaceTypeDeleter(context, mDeletionListener);
    }

    public void updateWithAdded() {
        PlaceType newlyAdded = DatabaseManager.get().getPlaceTypesDBManager().getLastUpdatedPlaceType();
        for (int i = 0; i < placeTypes.size(); i++) {
            // If the newly added place type comes before the current one, insert it here
            if (newlyAdded.getText().toLowerCase().compareTo(placeTypes.get(i).getText().toLowerCase()) < 0) {
                placeTypes.add(i, newlyAdded);
                notifyItemInserted(i);
                listener.scrollToItem(i);
                return;
            }
        }
        placeTypes.add(newlyAdded);
        notifyItemInserted(placeTypes.size() - 1);
        listener.scrollToItem(placeTypes.size() - 1);
    }

    private void updateWithEdited() {
        placeTypes.remove(lastClickedItem);

        PlaceType newlyAdded = DatabaseManager.get().getPlaceTypesDBManager().getLastUpdatedPlaceType();
        int newPosition = 0;
        for (; newPosition < placeTypes.size(); newPosition++) {
            // If the edited place type comes before the current one, this is its new position
            if (newlyAdded.getText().toLowerCase().compareTo(getItem(newPosition).getText().toLowerCase()) < 0) {
                break;
            }
        }

        placeTypes.add(newPosition, newlyAdded);
        if (newPosition == lastClickedItem) {
            notifyItemChanged(lastClickedItem);
        } else {
            notifyDataSetChanged();
        }
    }

    private PlaceType getItem(int position) {
        return placeTypes.get(position);
    }

    private final PlaceTypeEditor.Listener mEditedListener = new PlaceTypeEditor.Listener() {
        @Override
        public void onPlaceTypeEdited() {
            updateWithEdited();
            UIUtils.showSnackbar(parent, R.string.place_type_edited);
        }
    };

    private final PlaceTypeDeleter.Listener mDeletionListener = new PlaceTypeDeleter.Listener() {
        @Override
        public void onPlaceTypeDeleted() {
            placeTypes.remove(lastClickedItem);
            notifyItemRemoved(lastClickedItem);
            UIUtils.showSnackbar(parent, R.string.place_type_deleted);
        }
    };

    @Override
    public PlaceTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_type_cell, parent, false);
        return new PlaceTypeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaceTypeViewHolder holder, int position) {
        holder.loadPlaceType(position);
    }

    @Override
    public int getItemCount() {
        return placeTypes.size();
    }

    class PlaceTypeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.place_type) TextView mPlaceType;

        PlaceTypeViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void loadPlaceType(int position) {
            mPlaceType.setText(getItem(position).getText());
        }

        @OnClick(R.id.edit_icon)
        void editPlaceType() {
            lastClickedItem = getAdapterPosition();
            placeTypeEditor.show(getItem(lastClickedItem));
        }

        @OnClick(R.id.delete_icon)
        void deletePlaceType() {
            lastClickedItem = getAdapterPosition();
            placeTypeDeleter.show(getItem(lastClickedItem));
        }

        @OnClick(R.id.parent)
        void onPlaceTypeSelected() {
            listener.onItemClick(getItem(getAdapterPosition()));
        }
    }
}
