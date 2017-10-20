package com.randomappsinc.aroundme.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.randomappsinc.aroundme.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingViewHolder> {

    public interface ItemSelectionListener {
        void onItemClick(int position);
    }

    @NonNull private ItemSelectionListener mItemSelectionListener;
    private Context mContext;
    private String[] mOptions;
    private String[] mIcons;

    public SettingsAdapter(Context context, ItemSelectionListener itemSelectionListener) {
        mItemSelectionListener = itemSelectionListener;
        mContext = context;
        mOptions = context.getResources().getStringArray(R.array.settings_options);
        mIcons = context.getResources().getStringArray(R.array.settings_icons);
    }

    @Override
    public SettingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.settings_list_item, parent, false);
        return new SettingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SettingViewHolder holder, int position) {
        holder.loadSetting(position);
    }

    @Override
    public int getItemCount() {
        return mOptions.length;
    }

    class SettingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.settings_icon) IconTextView icon;
        @BindView(R.id.settings_option) TextView option;

        SettingViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void loadSetting(int position) {
            option.setText(mOptions[position]);
            icon.setText(mIcons[position]);
        }

        @OnClick(R.id.parent)
        public void onSettingSelected() {
            mItemSelectionListener.onItemClick(getAdapterPosition());
        }
    }
}
