package com.randomappsinc.aroundme.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.models.Event;
import com.squareup.picasso.Picasso;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EventInfoView {

    @BindView(R.id.event_thumbnail) ImageView thumbnail;
    @BindView(R.id.event_cost) TextView cost;
    @BindView(R.id.event_name) TextView name;
    @BindView(R.id.event_address) TextView address;
    @BindView(R.id.event_start) TextView eventStart;
    @BindView(R.id.event_end) TextView eventEnd;

    @BindColor(R.color.green) int green;
    @BindColor(R.color.gray_800) int darkGray;
    @BindDrawable(R.drawable.gray_border) Drawable grayBorder;

    private Context mContext;
    private Drawable mDefaultThumbnail;

    public EventInfoView(Context context, View view, Drawable defaultThumbnail) {
        mContext = context;
        mDefaultThumbnail = defaultThumbnail;
        ButterKnife.bind(this, view);
    }

    public void loadEvent(Event event) {
        String thumbnailUrl = event.getImageUrl();
        if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
            thumbnail.setBackground(null);
            Picasso.with(mContext)
                    .load(thumbnailUrl)
                    .error(mDefaultThumbnail)
                    .fit().centerCrop()
                    .into(thumbnail);
        } else {
            thumbnail.setBackground(grayBorder);
            thumbnail.setImageDrawable(mDefaultThumbnail);
        }

        if (event.isFree()) {
            cost.setTextColor(green);
            cost.setText(R.string.free);
        } else {
            String costInfo = event.getCostText();
            if (costInfo.isEmpty()) {
                cost.setVisibility(View.GONE);
            } else {
                cost.setTextColor(darkGray);
                cost.setText(costInfo);
                cost.setVisibility(View.VISIBLE);
            }
        }

        name.setText(event.getName());
        address.setText(event.getAddress());

        String startTime = event.getStartText();
        if (startTime.isEmpty()) {
            eventStart.setVisibility(View.GONE);
        } else {
            eventStart.setText(Html.fromHtml(startTime));
            eventStart.setVisibility(View.VISIBLE);
        }

        String endTime = event.getEndText();
        if (endTime.isEmpty()) {
            eventEnd.setVisibility(View.GONE);
        } else {
            eventEnd.setText(Html.fromHtml(endTime));
            eventEnd.setVisibility(View.VISIBLE);
        }
    }
}
