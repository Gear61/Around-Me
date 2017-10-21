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

    public interface ItemSelectionListener {
        void onItemClick(int position);
    }

    @NonNull private ItemSelectionListener mItemSelectionListener;
    private Context mContext;
    private List<PlaceType> mPlaceTypes;
    private View mParent;
    private PlaceTypeEditor mPlaceTypeEditor;
    private PlaceTypeDeleter mPlaceTypeDeleter;

    public PlaceTypesAdapter(Context context, @NonNull ItemSelectionListener itemSelectionListener, View parent) {
        mItemSelectionListener = itemSelectionListener;
        mContext = context;
        mPlaceTypes = DatabaseManager.get().getPlaceTypesDBManager().getPlaceTypes();
        mParent = parent;
        mPlaceTypeEditor = new PlaceTypeEditor(mContext, mEditedListener);
        mPlaceTypeDeleter = new PlaceTypeDeleter(mContext, mDeletionListener);
    }

    public void onPlaceTypeUpserted() {
        mPlaceTypes = DatabaseManager.get().getPlaceTypesDBManager().getPlaceTypes();
        notifyDataSetChanged();
    }

    public PlaceType getItem(int position) {
        return mPlaceTypes.get(position);
    }

    private final PlaceTypeEditor.Listener mEditedListener = new PlaceTypeEditor.Listener() {
        @Override
        public void onPlaceTypeEdited() {
            onPlaceTypeUpserted();
            UIUtils.showSnackbar(mParent, R.string.place_type_edited);
        }
    };

    private final PlaceTypeDeleter.Listener mDeletionListener = new PlaceTypeDeleter.Listener() {
        @Override
        public void onPlaceTypeDeleted(int position) {
            notifyItemRemoved(position);
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
            mPlaceTypeEditor.show(getItem(getAdapterPosition()));
        }

        @OnClick(R.id.delete_icon)
        void deletePlaceType() {
            mPlaceTypeDeleter.show(getItem(getAdapterPosition()), getAdapterPosition());
        }

        @OnClick(R.id.parent)
        void onPlaceTypeSelected() {
            mItemSelectionListener.onItemClick(getAdapterPosition());
        }
    }
}
