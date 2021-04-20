package com.randomappsinc.aroundme.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.models.Event;
import com.randomappsinc.aroundme.persistence.DatabaseManager;
import com.randomappsinc.aroundme.views.EventInfoView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FavoriteEventsAdapter extends RecyclerView.Adapter<FavoriteEventsAdapter.EventViewHolder> {

    public interface ItemSelectionListener {
        void onEventClicked(Event event);
    }

    @NonNull private ItemSelectionListener itemSelectionListener;
    private List<Event> events;
    private Drawable defaultThumbnail;

    public FavoriteEventsAdapter(Context context, @NonNull ItemSelectionListener itemSelectionListener) {
        this.itemSelectionListener = itemSelectionListener;
        events = DatabaseManager.get().getEventsDBManager().getFavoriteEvents();
        defaultThumbnail = new IconDrawable(
                context,
                IoniconsIcons.ion_android_calendar).colorRes(R.color.dark_gray);    }

    public void resyncWithDB() {
        events.clear();
        events.addAll(DatabaseManager.get().getEventsDBManager().getFavoriteEvents());
        notifyDataSetChanged();
    }

    private Event getItem(int position) {
        return events.get(position);
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_cell, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        holder.loadEvent(position);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.event_info_parent) View eventInfo;

        private EventInfoView mEventInfoView;

        EventViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mEventInfoView = new EventInfoView(eventInfo, defaultThumbnail);
        }

        void loadEvent(int position) {
            mEventInfoView.loadEvent(getItem(position));
        }

        @OnClick(R.id.event_info_parent)
        void onEventClicked() {
            Event event = getItem(getAdapterPosition());
            itemSelectionListener.onEventClicked(event);
        }
    }
}
