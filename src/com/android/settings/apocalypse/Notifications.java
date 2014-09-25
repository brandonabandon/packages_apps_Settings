/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.apocalypse;


import android.app.ActivityManagerNative;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.view.RotationPolicy;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.DreamSettings;
import com.android.settings.slim.DisplayRotation;
import com.android.settings.apocalypse.AppMultiSelectListPreference;
import com.android.settings.cyanogenmod.NEWSeekBarPreference;
import java.util.HashSet;
import java.util.Set;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.Arrays;


public class Notifications extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private static final String TAG = "NotificationSettings";
    

    private static final String KEY_PROXIMITY_WAKE = "proximity_on_wake";
    private static final String KEY_NOTIFICATION_PULSE = "notification_pulse";
    private static final String KEY_PEEK = "notification_peek";
	private static final String KEY_PEEK_PICKUP_TIMEOUT = "peek_pickup_timeout";
	private static final String STATUS_BAR_CUSTOM_HEADER = "custom_status_bar_header";
	private static final String PREF_HEADS_UP_FLOATING_WINDOW = "heads_up_floating_window";
	
	
    private CheckBoxPreference mNotificationPulse;
	private CheckBoxPreference mNotificationPeek;
	private ListPreference mPeekPickupTimeout;
	private CheckBoxPreference mStatusBarCustomHeader;
	CheckBoxPreference mHeadsUpFloatingWindow;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentResolver resolver = getActivity().getContentResolver();
		
		Resources res = getResources();

        addPreferencesFromResource(R.xml.notifications_settings);

		mNotificationPeek = (CheckBoxPreference) findPreference(KEY_PEEK);
        mNotificationPeek.setPersistent(false);
		
		mPeekPickupTimeout = (ListPreference) findPreference(KEY_PEEK_PICKUP_TIMEOUT);
        int peekTimeout = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.PEEK_PICKUP_TIMEOUT, 0, UserHandle.USER_CURRENT);
        mPeekPickupTimeout.setValue(String.valueOf(peekTimeout));
        mPeekPickupTimeout.setSummary(mPeekPickupTimeout.getEntry());
        mPeekPickupTimeout.setOnPreferenceChangeListener(this);
		
		mStatusBarCustomHeader = (CheckBoxPreference) findPreference(STATUS_BAR_CUSTOM_HEADER);
		mStatusBarCustomHeader.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_CUSTOM_HEADER, 0) == 1);
        mStatusBarCustomHeader.setOnPreferenceChangeListener(this);
		
		 boolean proximityCheckOnWait = res.getBoolean(
                com.android.internal.R.bool.config_proximityCheckOnWake);
        if (!proximityCheckOnWait) {
            Settings.System.putInt(getContentResolver(), Settings.System.PROXIMITY_ON_WAKE, 1);
        }
        
		mNotificationPulse = (CheckBoxPreference) findPreference(KEY_NOTIFICATION_PULSE);
        if (mNotificationPulse != null
                && getResources().getBoolean(
                        com.android.internal.R.bool.config_intrusiveNotificationLed) == false) {
            getPreferenceScreen().removePreference(mNotificationPulse);
        } else {
            try {
                mNotificationPulse.setChecked(Settings.System.getInt(resolver,
                        Settings.System.NOTIFICATION_LIGHT_PULSE) == 1);
                mNotificationPulse.setOnPreferenceChangeListener(this);
            } catch (SettingNotFoundException snfe) {
                Log.e(TAG, Settings.System.NOTIFICATION_LIGHT_PULSE + " not found");
            }
        }
		
		mHeadsUpFloatingWindow = (CheckBoxPreference) findPreference(PREF_HEADS_UP_FLOATING_WINDOW);
		mHeadsUpFloatingWindow.setChecked(Settings.System.getIntForUser(getContentResolver(),
		Settings.System.HEADS_UP_FLOATING_WINDOW, 1, UserHandle.USER_CURRENT) == 1);
		mHeadsUpFloatingWindow.setOnPreferenceChangeListener(this);
    }
    @Override
    public void onResume() {
        super.onResume();
       	
	 	
        
        updateState();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    private void updateState() {
        
        updatePeekCheckbox();
    }
        
    
	private void updatePeekCheckbox() {
        boolean enabled = Settings.System.getInt(getContentResolver(),
                Settings.System.PEEK_STATE, 0) == 1;
        mNotificationPeek.setChecked(enabled);
    }
    
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
       if (preference == mNotificationPulse) {
            boolean value = mNotificationPulse.isChecked();
            Settings.System.putInt(getContentResolver(), Settings.System.NOTIFICATION_LIGHT_PULSE,
                    value ? 1 : 0);
            return true;
        } else if (preference == mNotificationPeek) {
            Settings.System.putInt(getContentResolver(), Settings.System.PEEK_STATE,
                    mNotificationPeek.isChecked() ? 1 : 0);
		}				
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();
		if (preference == mPeekPickupTimeout) {
            int peekTimeout = Integer.valueOf((String) objValue);
            Settings.System.putIntForUser(getContentResolver(),
                Settings.System.PEEK_PICKUP_TIMEOUT,
                    peekTimeout, UserHandle.USER_CURRENT);
            updatePeekTimeoutOptions(objValue);
            return true;
		} else if (preference == mStatusBarCustomHeader) {
            boolean value = (Boolean) objValue;
            Settings.System.putInt(getContentResolver(),
                Settings.System.STATUS_BAR_CUSTOM_HEADER, value ? 1 : 0);
            return true;
		} else if (preference == mHeadsUpFloatingWindow) {
	    	Settings.System.putIntForUser(getContentResolver(),
		    	Settings.System.HEADS_UP_FLOATING_WINDOW,
	    	(Boolean) objValue ? 1 : 0, UserHandle.USER_CURRENT);
	    	return true;	
        }		

        return true;
    }
	
	private void updatePeekTimeoutOptions(Object newValue) {
        int index = mPeekPickupTimeout.findIndexOfValue((String) newValue);
        int value = Integer.valueOf((String) newValue);
        Settings.Secure.putInt(getActivity().getContentResolver(),
                Settings.System.PEEK_PICKUP_TIMEOUT, value);
        mPeekPickupTimeout.setSummary(mPeekPickupTimeout.getEntries()[index]);
    }
}
