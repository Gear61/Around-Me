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

    @NonNull private Listener mListener;
    private Context mContext;
    private List<PlaceType> mPlaceTypes;
    private View mParent;
    private int mLastClickedItem;
    private PlaceTypeEditor mPlaceTypeEditor;
    private PlaceTypeDeleter mPlaceTypeDeleter;

    public PlaceTypesAdapter(Context context, @NonNull Listener listener, View parent) {
        mListener = listener;
        mContext = context;
        mPlaceTypes = DatabaseManager.get().getPlaceTypesDBManager().getPlaceTypes();
        mParent = parent;
        mPlaceTypeEditor = new PlaceTypeEditor(mContext, mEditedListener);
        mPlaceTypeDeleter = new PlaceTypeDeleter(mContext, mDeletionListener);
    }

    public void updateWithAdded() {
        PlaceType newlyAdded = DatabaseManager.get().getPlaceTypesDBManager().getLastUpdatedPlaceType();
        for (int i = 0; i < mPlaceTypes.size(); i++) {
            // If the newly added place type comes before the current one, insert it here
            if (newlyAdded.getText().compareTo(mPlaceTypes.get(i).getText()) < 0) {
                mPlaceTypes.add(i, newlyAdded);
                notifyItemInserted(i);
                mListener.scrollToItem(i);
                return;
            }
        }
        mPlaceTypes.add(newlyAdded);
        notifyItemInserted(mPlaceTypes.size() - 1);
        mListener.scrollToItem(mPlaceTypes.size() - 1);
    }

    private void updateWithEdited() {
        mPlaceTypes.remove(mLastClickedItem);

        PlaceType newlyAdded = DatabaseManager.get().getPlaceTypesDBManager().getLastUpdatedPlaceType();
        int newPosition = 0;
        for (; newPosition < mPlaceTypes.size(); newPosition++) {
            // If the edited place type comes before the current one, this is its new position
            if (newlyAdded.getText().compareTo(getItem(newPosition).getText()) < 0) {
                break;
            }
        }

        mPlaceTypes.add(newPosition, newlyAdded);
        if (newPosition == mLastClickedItem) {
            notifyItemChanged(mLastClickedItem);
        } else {
            notifyDataSetChanged();
        }
    }

    public PlaceType getItem(int position) {
        return mPlaceTypes.get(position);
    }

    private final PlaceTypeEditor.Listener mEditedListener = new PlaceTypeEditor.Listener() {
        @Override
        public void onPlaceTypeEdited() {
            updateWithEdited();
            UIUtils.showSnackbar(mParent, R.string.place_type_edited);
        }
    };

    private final PlaceTypeDeleter.Listener mDeletionListener = new PlaceTypeDeleter.Listener() {
        @Override
        public void onPlaceTypeDeleted() {
            mPlaceTypes.remove(mLastClickedItem);
            notifyItemRemoved(mLastClickedItem);
            UIUtils.showSnackbar(mParent, R.string.place_type_deleted);
        }
    };

    @Override
    public PlaceTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.place_type_cell, parent, false);
        return new PlaceTypeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlaceTypeViewHolder holder, int position) {
        holder.loadPlaceType(position);
    }

    @Override
    public int getItemCount() {
        return mPlaceTypes.size();
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
            mLastClickedItem = getAdapterPosition();
            mPlaceTypeEditor.show(getItem(mLastClickedItem));
        }

        @OnClick(R.id.delete_icon)
        void deletePlaceType() {
            mLastClickedItem = getAdapterPosition();
            mPlaceTypeDeleter.show(getItem(mLastClickedItem));
        }

        @OnClick(R.id.parent)
        void onPlaceTypeSelected() {
            mListener.onItemClick(getItem(getAdapterPosition()));
        }
    }
}
