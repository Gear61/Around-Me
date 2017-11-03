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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Event implements Parcelable {

    private String mId;
    private String mImageUrl;
    private String mName;
    private int mNumAttending;
    private int mNumInterested;
    private double mCost;
    private double mCostMax;
    private String mDescription;
    private String mUrl;
    private boolean mIsCanceled;
    private boolean mIsFree;
    @Nullable private String mTicketsUrl;
    private String mTimeStart;
    private String mTimeEnd;
    private String mCity;
    private String mZipCode;
    private String mCountry;
    private String mState;
    private String mAddress;
    private double mLatitude;
    private double mLongitude;
    private boolean mIsFavorited;

    public Event() {}

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getNumAttending() {
        return mNumAttending;
    }

    public void setNumAttending(int numAttending) {
        mNumAttending = numAttending;
    }

    public int getNumInterested() {
        return mNumInterested;
    }

    public void setNumInterested(int numInterested) {
        mNumInterested = numInterested;
    }

    public double getCost() {
        return mCost;
    }

    public void setCost(double cost) {
        mCost = cost;
    }

    public double getCostMax() {
        return mCostMax;
    }

    public void setCostMax(double costMax) {
        mCostMax = costMax;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public boolean isCanceled() {
        return mIsCanceled;
    }

    public void setCanceled(boolean canceled) {
        mIsCanceled = canceled;
    }

    public boolean isFree() {
        return mIsFree;
    }

    public void setFree(boolean free) {
        mIsFree = free;
    }

    public String getTicketsUrl() {
        return mTicketsUrl;
    }

    public void setTicketsUrl(@Nullable String ticketsUrl) {
        mTicketsUrl = ticketsUrl;
    }

    public String getTimeStart() {
        return mTimeStart;
    }

    public void setTimeStart(String timeStart) {
        mTimeStart = timeStart;
    }

    public String getTimeEnd() {
        return mTimeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        mTimeEnd = timeEnd;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getZipCode() {
        return mZipCode;
    }

    public void setZipCode(String zipCode) {
        mZipCode = zipCode;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public boolean isFavorited() {
        return mIsFavorited;
    }

    public void setIsFavorited(boolean isFavorited) {
        mIsFavorited = isFavorited;
    }

    public String getCostText() {
        StringBuilder costText = new StringBuilder();
        if (mCost > 0) {
            NumberFormat formatter = new DecimalFormat("#0.00");
            String costFormatted = formatter.format(mCost);

            costText.append(StringUtils.getCurrencySymbol()).append(costFormatted);

            if (mCostMax > 0) {
                String costMaxFormatted = formatter.format(mCostMax);
                costText.append(" - ").append(StringUtils.getCurrencySymbol()).append(costMaxFormatted);
            }
        }
        return costText.length() == 0 ? "" : costText.toString();
    }

    public String getStartText() {
        return mTimeStart.isEmpty()
                ? ""
                : "<b>" + MyApplication.getAppContext().getString(R.string.start) + "</b> " + mTimeStart;
    }

    public String getEndText() {
        return mTimeEnd.isEmpty()
                ? ""
                : "<b>" + MyApplication.getAppContext().getString(R.string.end) + "</b> " + mTimeEnd;
    }

    public long getStartTimeMillis() {
        DateFormat targetFormat = new SimpleDateFormat("EEE, MM/dd/yy - h:mm a", Locale.US);
        targetFormat.setTimeZone(TimeZone.getDefault());
        Date date;
        try {
            date = targetFormat.parse(mTimeStart);
        } catch (ParseException exception) {
            return 0L;
        }
        return date.getTime();
    }

    public long getEndTimeMillis() {
        DateFormat targetFormat = new SimpleDateFormat("EEE, MM/dd/yy - h:mm a", Locale.US);
        targetFormat.setTimeZone(TimeZone.getDefault());
        Date date;
        try {
            date = targetFormat.parse(mTimeEnd);
        } catch (ParseException exception) {
            return 0L;
        }
        return date.getTime();
    }

    public Spannable getDescriptionText() {
        if (mDescription.endsWith("...")) {
            String readMore = MyApplication.getAppContext().getString(R.string.read_more);
            Spannable descriptionText = new SpannableString(mDescription + " " + readMore);
            int colorAccent = MyApplication.getAppContext().getResources().getColor(R.color.colorAccent);
            int start = mDescription.length() + 1;
            int end = start + readMore.length();
            descriptionText.setSpan(
                    new ForegroundColorSpan(colorAccent),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return descriptionText;
        } else {
            return new SpannableString(mDescription);
        }
    }

    public EventDO toEventDO() {
        EventDO eventDO = new EventDO();
        eventDO.setId(mId);
        eventDO.setImageUrl(mImageUrl);
        eventDO.setName(mName);
        eventDO.setNumAttending(mNumAttending);
        eventDO.setNumInterested(mNumInterested);
        eventDO.setCost(mCost);
        eventDO.setCostMax(mCostMax);
        eventDO.setDescription(mDescription);
        eventDO.setUrl(mUrl);
        eventDO.setIsCanceled(mIsCanceled);
        eventDO.setIsFree(mIsFree);
        eventDO.setTicketsUrl(mTicketsUrl);
        eventDO.setTimeStart(mTimeStart);
        eventDO.setTimeEnd(mTimeEnd);
        eventDO.setCity(mCity);
        eventDO.setZipCode(mZipCode);
        eventDO.setCountry(mCountry);
        eventDO.setState(mState);
        eventDO.setAddress(mAddress);
        eventDO.setLatitude(mLatitude);
        eventDO.setLongitude(mLongitude);
        eventDO.setIsFavorited(mIsFavorited);
        return eventDO;
    }

    protected Event(Parcel in) {
        mId = in.readString();
        mImageUrl = in.readString();
        mName = in.readString();
        mNumAttending = in.readInt();
        mNumInterested = in.readInt();
        mCost = in.readDouble();
        mCostMax = in.readDouble();
        mDescription = in.readString();
        mUrl = in.readString();
        mIsCanceled = in.readByte() != 0x00;
        mIsFree = in.readByte() != 0x00;
        mTicketsUrl = in.readString();
        mTimeStart = in.readString();
        mTimeEnd = in.readString();
        mCity = in.readString();
        mZipCode = in.readString();
        mCountry = in.readString();
        mState = in.readString();
        mAddress = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mIsFavorited = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mImageUrl);
        dest.writeString(mName);
        dest.writeInt(mNumAttending);
        dest.writeInt(mNumInterested);
        dest.writeDouble(mCost);
        dest.writeDouble(mCostMax);
        dest.writeString(mDescription);
        dest.writeString(mUrl);
        dest.writeByte((byte) (mIsCanceled ? 0x01 : 0x00));
        dest.writeByte((byte) (mIsFree ? 0x01 : 0x00));
        dest.writeString(mTicketsUrl);
        dest.writeString(mTimeStart);
        dest.writeString(mTimeEnd);
        dest.writeString(mCity);
        dest.writeString(mZipCode);
        dest.writeString(mCountry);
        dest.writeString(mState);
        dest.writeString(mAddress);
        dest.writeDouble(mLatitude);
        dest.writeDouble(mLongitude);
        dest.writeByte((byte) (mIsFavorited ? 0x01 : 0x00));
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
