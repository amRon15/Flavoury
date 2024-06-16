package com.example.flavoury;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.rxjava2.RxDataStore;

import java.util.Map;

import io.reactivex.Single;

public class UserDataStore {
    RxDataStore<Preferences> dataStore;

    public UserDataStore(RxDataStore<Preferences> dataStore) {
        this.dataStore = dataStore;
    }

    private Preferences pref_error = new Preferences() {
        @Override
        public <T> boolean contains(@NonNull Key<T> key) {
            return false;
        }

        @Nullable
        @Override
        public <T> T get(@NonNull Key<T> key) {
            return null;
        }

        @NonNull
        @Override
        public Map<Key<?>, Object> asMap() {
            return null;
        }
    };

    public boolean putBoolValue(Boolean value){
        boolean returnValue;
        Preferences.Key<Boolean> PREF_KEY = PreferencesKeys.booleanKey("loggedIn");
        Single<Preferences> updateResult = dataStore.updateDataAsync((prefsIn ->{
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            mutablePreferences.set(PREF_KEY, value);
            Log.d("DataStore", "Saved: "+mutablePreferences);
            return Single.just(mutablePreferences);
        })).onErrorReturnItem(pref_error);

        returnValue = updateResult.blockingGet() != pref_error;
        return returnValue;
    }

    public Boolean getBoolValue(){
        Preferences.Key<Boolean> PREF_KEY = PreferencesKeys.booleanKey("loggedIn");
        Single<Boolean> value = dataStore.data().firstOrError().map(prefs -> prefs.get(PREF_KEY)).onErrorReturnItem(false);
        Log.d("DataStore", "Logged In: " + value.blockingGet());
        return value.blockingGet();
    }

    public void clearValue(){
        Preferences.Key<Boolean> PREF_KEY = PreferencesKeys.booleanKey("loggedIn");
        Single<Preferences> updateResult = dataStore.updateDataAsync((prefsIn -> {
            MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
            mutablePreferences.clear();
            Log.d("DataStore", "Cleared: " + mutablePreferences);
            return Single.just(mutablePreferences);
        }));
        updateResult.blockingGet();
    }

}
