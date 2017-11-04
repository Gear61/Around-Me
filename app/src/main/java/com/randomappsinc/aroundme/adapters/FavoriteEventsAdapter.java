package com.randomappsinc.aroundme.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.models.Event;
import com.randomappsinc.aroundme.persistence.DatabaseManager;
import com.randomappsinc.aroundme.views.EventInfoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FavoriteEventsAdapter extends RecyclerView.Adapter<FavoriteEventsAdapter.EventViewHolder> {

    public interface ItemSelectionListener {
        void onEventClicked(Event event);
    }

    @NonNull private ItemSelectionListener mItemSelectionListener;
    private Context mContext;
    private List<Event> mEvents = new ArrayList<>();
    private Drawable mDefaultThumbnail;

    public FavoriteEventsAdapter(Context context, @NonNull ItemSelectionListener itemSelectionListener) {
        mItemSelectionListener = itemSelectionListener;
        mContext = context;
        mEvents = DatabaseManager.get().getEventsDBManager().getFavoriteEvents();
        mDefaultThumbnail = new IconDrawable(
                mContext,
                IoniconsIcons.ion_android_calendar).colorRes(R.color.dark_gray);    }

    public void resyncWithDB() {
        mEvents.clear();
        mEvents.addAll(DatabaseManager.get().getEventsDBManager().getFavoriteEvents());
        notifyDataSetChanged();
    }

    private Event getItem(int position) {
        return mEvents.get(position);
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.event_cell, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.loadEvent(position);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.event_info_parent) View eventInfo;

        private EventInfoView mEventInfoView;

        EventViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mEventInfoView = new EventInfoView(mContext, eventInfo, mDefaultThumbnail);
        }

        void loadEvent(int position) {
            mEventInfoView.loadEvent(getItem(position));
        }

        @OnClick(R.id.event_info_parent)
        void onEventClicked() {
            Event event = getItem(getAdapterPosition());
            mItemSelectionListener.onEventClicked(event);
        }
    }
}
