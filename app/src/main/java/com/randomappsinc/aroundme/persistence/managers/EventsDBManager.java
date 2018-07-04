package com.randomappsinc.aroundme.persistence.managers;

import android.support.annotation.NonNull;

import com.randomappsinc.aroundme.models.Event;
import com.randomappsinc.aroundme.persistence.DBConverter;
import com.randomappsinc.aroundme.persistence.models.EventDO;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class EventsDBManager {

    private static EventsDBManager instance;

    public static EventsDBManager get() {
        if (instance == null) {
            instance = getSync();
        }
        return instance;
    }

    private static synchronized EventsDBManager getSync() {
        if (instance == null) {
            instance = new EventsDBManager();
        }
        return instance;
    }

    private Realm getRealm() {
        return Realm.getDefaultInstance();
    }

    public void addFavorite(final Event event) {
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                realm.insert(event.toEventDO());
            }
        });
    }

    public void removeFavorite(final Event event) {
        final EventDO eventDO = getRealm()
                .where(EventDO.class)
                .equalTo("id", event.getId())
                .findFirst();

        if (eventDO == null) {
            return;
        }

        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                eventDO.deleteFromRealm();
            }
        });
    }

    public List<Event> getFavoriteEvents() {
        List<EventDO> eventDOs = getRealm()
                .where(EventDO.class)
                .equalTo("isFavorited", true)
                .findAll();

        List<Event> favorites = new ArrayList<>();
        for (EventDO eventDO : eventDOs) {
            favorites.add(DBConverter.getEventFromDO(eventDO));
        }
        return favorites;
    }

    public boolean isEventFavorited(Event event) {
        EventDO eventDO = getRealm()
                .where(EventDO.class)
                .equalTo("id", event.getId())
                .findFirst();

        return eventDO != null && eventDO.isFavorited();
    }
}
