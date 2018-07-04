package com.randomappsinc.aroundme.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.randomappsinc.aroundme.R;
import com.randomappsinc.aroundme.persistence.models.EventDO;
import com.randomappsinc.aroundme.utils.MyApplication;
import com.randomappsinc.aroundme.utils.StringUtils;
import com.randomappsinc.aroundme.utils.TimeUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Event implements Parcelable {

    private String id;
    private String imageUrl;
    private String name;
    private int numAttending;
    private int numInterested;
    private double cost;
    private double costMax;
    private String description;
    private String url;
    private boolean isCanceled;
    private boolean isFree;
    @Nullable private String ticketsUrl;
    private long timeStart;
    private long timeEnd;
    private String city;
    private String zipCode;
    private String country;
    private String state;
    private String address;
    private double latitude;
    private double longitude;
    private boolean isFavorited;

    public Event() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumAttending() {
        return numAttending;
    }

    public void setNumAttending(int numAttending) {
        this.numAttending = numAttending;
    }

    public int getNumInterested() {
        return numInterested;
    }

    public void setNumInterested(int numInterested) {
        this.numInterested = numInterested;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setCostMax(double costMax) {
        this.costMax = costMax;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public String getTicketsUrl() {
        return ticketsUrl;
    }

    public void setTicketsUrl(@Nullable String ticketsUrl) {
        this.ticketsUrl = ticketsUrl;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setIsFavorited(boolean isFavorited) {
        this.isFavorited = isFavorited;
    }

    public String getCostText() {
        StringBuilder costText = new StringBuilder();
        if (cost > 0) {
            NumberFormat formatter = new DecimalFormat("#0.00");
            String costFormatted = formatter.format(cost);

            costText.append(StringUtils.getCurrencySymbol()).append(costFormatted);

            if (costMax > 0) {
                String costMaxFormatted = formatter.format(costMax);
                costText.append(" - ").append(StringUtils.getCurrencySymbol()).append(costMaxFormatted);
            }
        }
        return costText.length() == 0 ? "" : costText.toString();
    }

    public String getStartText() {
        return timeStart == 0L
                ? ""
                : "<b>" + MyApplication.getAppContext().getString(R.string.start) + "</b> " + getStartTime();
    }

    public String getEndText() {
        return timeEnd == 0L
                ? ""
                : "<b>" + MyApplication.getAppContext().getString(R.string.end) + "</b> " + getEndTime();
    }

    private String getStartTime() {
        return TimeUtils.getEventTime(timeStart);
    }

    private String getEndTime() {
        return TimeUtils.getEventTime(timeEnd);
    }

    public Spannable getDescriptionText() {
        if (description.endsWith("...")) {
            String readMore = MyApplication.getAppContext().getString(R.string.read_more);
            Spannable descriptionText = new SpannableString(description + " " + readMore);
            int colorAccent = MyApplication.getAppContext().getResources().getColor(R.color.colorAccent);
            int start = description.length() + 1;
            int end = start + readMore.length();
            descriptionText.setSpan(
                    new ForegroundColorSpan(colorAccent),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return descriptionText;
        } else {
            return new SpannableString(description);
        }
    }

    public void toggleFavorite() {
        isFavorited = !isFavorited;
    }

    public EventDO toEventDO() {
        EventDO eventDO = new EventDO();
        eventDO.setId(id);
        eventDO.setImageUrl(imageUrl);
        eventDO.setName(name);
        eventDO.setNumAttending(numAttending);
        eventDO.setNumInterested(numInterested);
        eventDO.setCost(cost);
        eventDO.setCostMax(costMax);
        eventDO.setDescription(description);
        eventDO.setUrl(url);
        eventDO.setIsCanceled(isCanceled);
        eventDO.setIsFree(isFree);
        eventDO.setTicketsUrl(ticketsUrl);
        eventDO.setTimeStart(timeStart);
        eventDO.setTimeEnd(timeEnd);
        eventDO.setCity(city);
        eventDO.setZipCode(zipCode);
        eventDO.setCountry(country);
        eventDO.setState(state);
        eventDO.setAddress(address);
        eventDO.setLatitude(latitude);
        eventDO.setLongitude(longitude);
        eventDO.setIsFavorited(isFavorited);
        return eventDO;
    }

    protected Event(Parcel in) {
        id = in.readString();
        imageUrl = in.readString();
        name = in.readString();
        numAttending = in.readInt();
        numInterested = in.readInt();
        cost = in.readDouble();
        costMax = in.readDouble();
        description = in.readString();
        url = in.readString();
        isCanceled = in.readByte() != 0x00;
        isFree = in.readByte() != 0x00;
        ticketsUrl = in.readString();
        timeStart = in.readLong();
        timeEnd = in.readLong();
        city = in.readString();
        zipCode = in.readString();
        country = in.readString();
        state = in.readString();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        isFavorited = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(imageUrl);
        dest.writeString(name);
        dest.writeInt(numAttending);
        dest.writeInt(numInterested);
        dest.writeDouble(cost);
        dest.writeDouble(costMax);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeByte((byte) (isCanceled ? 0x01 : 0x00));
        dest.writeByte((byte) (isFree ? 0x01 : 0x00));
        dest.writeString(ticketsUrl);
        dest.writeLong(timeStart);
        dest.writeLong(timeEnd);
        dest.writeString(city);
        dest.writeString(zipCode);
        dest.writeString(country);
        dest.writeString(state);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeByte((byte) (isFavorited ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
