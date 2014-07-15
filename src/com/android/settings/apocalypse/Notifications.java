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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
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

import com.android.settings.util.Helpers;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class Notifications extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    private static final String TAG = "NotificationSettings";
    
    private static final String KEY_NOTIFICATION_PULSE = "notification_pulse";
    private static final String KEY_PEEK = "notification_peek";
	private static final String KEY_PEEK_PICKUP_TIMEOUT = "peek_pickup_timeout";
	private static final String STATUS_BAR_CUSTOM_HEADER = "custom_status_bar_header";
	private static final String PREF_HEADS_UP_FLOATING_WINDOW = "heads_up_floating_window";
	private static final String PREF_HEADS_UP_BG_COLOR = "heads_up_bg_color";
    private static final String PREF_HEADS_UP_TEXT_COLOR = "heads_up_text_color";
	private static final String PREF_SHOW_HEADS_UP_BOTTOM = "show_heads_up_bottom";
	private static final String PREF_FORCE_EXPANDED_NOTIFICATIONS = "force_expanded_notifications";
	private static final String PREF_HEADS_UP_EXCLUDE_FROM_LOCK_SCREEN = "heads_up_exclude_from_lock_screen";
	private static final String PREF_HEADS_UP_EXPANDED = "heads_up_expanded";
	private static final String PREF_HEADS_UP_TIME_OUT = "heads_up_time_out";
	
	
	
    private CheckBoxPreference mNotificationPulse;
	private CheckBoxPreference mNotificationPeek;
	private ListPreference mPeekPickupTimeout;
	private CheckBoxPreference mStatusBarCustomHeader;
	CheckBoxPreference mHeadsUpFloatingWindow;
	private CheckBoxPreference mShowHeadsUpBottom;
	private CheckBoxPreference mForceExpandedNotifications;
	private CheckBoxPreference mHeadsExcludeFromLockscreen;
	private CheckBoxPreference mHeadsUpExpanded;
	private ListPreference mHeadsUpTimeOut;
	
	private ColorPickerPreference mHeadsUpBgColor;
    private ColorPickerPreference mHeadsUpTextColor;
	
	private static final int MENU_RESET = Menu.FIRST;
    private static final int DEFAULT_BACKGROUND_COLOR = 0x00ffffff;
    private static final int DEFAULT_TEXT_COLOR = 0xffffffff;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentResolver resolver = getActivity().getContentResolver();
		
		Resources res = getResources();
		PackageManager pm = getPackageManager();

        addPreferencesFromResource(R.xml.notifications_settings);
		
		//Peek
		mNotificationPeek = (CheckBoxPreference) findPreference(KEY_PEEK);
        mNotificationPeek.setPersistent(false);
		
		mPeekPickupTimeout = (ListPreference) findPreference(KEY_PEEK_PICKUP_TIMEOUT);
        int peekTimeout = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.PEEK_PICKUP_TIMEOUT, 0, UserHandle.USER_CURRENT);
        mPeekPickupTimeout.setValue(String.valueOf(peekTimeout));
        mPeekPickupTimeout.setSummary(mPeekPickupTimeout.getEntry());
        mPeekPickupTimeout.setOnPreferenceChangeListener(this);
		
		//Custom Status Bar header
		mStatusBarCustomHeader = (CheckBoxPreference) findPreference(STATUS_BAR_CUSTOM_HEADER);
		mStatusBarCustomHeader.setChecked(Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_CUSTOM_HEADER, 0) == 1);
        mStatusBarCustomHeader.setOnPreferenceChangeListener(this);
		
		//LED Notificaion Pulse
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
		
		//Heads Up in Floating Window
		mHeadsUpFloatingWindow = (CheckBoxPreference) findPreference(PREF_HEADS_UP_FLOATING_WINDOW);
		mHeadsUpFloatingWindow.setChecked(Settings.System.getIntForUser(getContentResolver(),
		Settings.System.HEADS_UP_FLOATING_WINDOW, 1, UserHandle.USER_CURRENT) == 1);
		mHeadsUpFloatingWindow.setOnPreferenceChangeListener(this);
		
		// Heads Up background color
        mHeadsUpBgColor =
                (ColorPickerPreference) findPreference(PREF_HEADS_UP_BG_COLOR);
        mHeadsUpBgColor.setOnPreferenceChangeListener(this);
        final int intColor = Settings.System.getInt(getContentResolver(),
                Settings.System.HEADS_UP_BG_COLOR, 0x00ffffff);
        String hexColor = String.format("#%08x", (0x00ffffff & intColor));
        if (hexColor.equals("#00ffffff")) {
            mHeadsUpBgColor.setSummary(R.string.default_color);
        } else {
            mHeadsUpBgColor.setSummary(hexColor);
        }
        mHeadsUpBgColor.setNewPreviewColor(intColor);
		
		//Exclude Heads Up from lockscreen
		mHeadsExcludeFromLockscreen = (CheckBoxPreference) findPreference(PREF_HEADS_UP_EXCLUDE_FROM_LOCK_SCREEN);
        mHeadsExcludeFromLockscreen.setChecked(Settings.System.getIntForUser(getContentResolver(),
                Settings.System.HEADS_UP_EXCLUDE_FROM_LOCK_SCREEN, 0, UserHandle.USER_CURRENT) == 1);
        mHeadsExcludeFromLockscreen.setOnPreferenceChangeListener(this);
		
		//Heads Up: Always Show Expanded
		mHeadsUpExpanded = (CheckBoxPreference) findPreference(PREF_HEADS_UP_EXPANDED);
        mHeadsUpExpanded.setChecked(Settings.System.getIntForUser(getContentResolver(),
                Settings.System.HEADS_UP_EXPANDED, 0, UserHandle.USER_CURRENT) == 1);
        mHeadsUpExpanded.setOnPreferenceChangeListener(this);

        // Heads Up text color
        mHeadsUpTextColor =
                (ColorPickerPreference) findPreference(PREF_HEADS_UP_TEXT_COLOR);
        mHeadsUpTextColor.setOnPreferenceChangeListener(this);
        final int intTextColor = Settings.System.getInt(getContentResolver(),
                Settings.System.HEADS_UP_TEXT_COLOR, 0x00000000);
        String hexTextColor = String.format("#%08x", (0x00000000 & intTextColor));
        if (hexTextColor.equals("#00000000")) {
            mHeadsUpTextColor.setSummary(R.string.default_color);
        } else {
            mHeadsUpTextColor.setSummary(hexTextColor);
        }
        mHeadsUpTextColor.setNewPreviewColor(intTextColor);
        setHasOptionsMenu(true);
		
		//Heads Up Timeout
		Resources systemUiResources;
        try {
            systemUiResources = pm.getResourcesForApplication("com.android.systemui");
        } catch (Exception e) {
            return;
        }

        int defaultTimeOut = systemUiResources.getInteger(systemUiResources.getIdentifier(
                    "com.android.systemui:integer/heads_up_notification_decay", null, null));
        mHeadsUpTimeOut = (ListPreference) findPreference(PREF_HEADS_UP_TIME_OUT);
        mHeadsUpTimeOut.setOnPreferenceChangeListener(this);
        int headsUpTimeOut = Settings.System.getInt(getContentResolver(),
                Settings.System.HEADS_UP_NOTIFCATION_DECAY, defaultTimeOut);
        mHeadsUpTimeOut.setValue(String.valueOf(headsUpTimeOut));
        updateHeadsUpTimeOutSummary(headsUpTimeOut);
		
		//Show HeadsUp at bottom		
		mShowHeadsUpBottom = (CheckBoxPreference) findPreference(PREF_SHOW_HEADS_UP_BOTTOM);
        mShowHeadsUpBottom.setChecked(Settings.System.getIntForUser(getContentResolver(),
                Settings.System.SHOW_HEADS_UP_BOTTOM, 0, UserHandle.USER_CURRENT) == 1);
        mShowHeadsUpBottom.setOnPreferenceChangeListener(this);
		
		//Force Expanded Notifications
		mForceExpandedNotifications = (CheckBoxPreference) findPreference(PREF_FORCE_EXPANDED_NOTIFICATIONS);
        mForceExpandedNotifications.setChecked((Settings.System.getInt(resolver,
                Settings.System.FORCE_EXPANDED_NOTIFICATIONS, 0) == 1));
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
		} else if (preference == mForceExpandedNotifications) {
            boolean value = mForceExpandedNotifications.isChecked();
            Settings.System.putInt(getContentResolver(),
                    Settings.System.FORCE_EXPANDED_NOTIFICATIONS, value ? 1 : 0);			
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
        } else if (preference == mHeadsUpBgColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(objValue)));
            if (hex.equals("#00ffffff")) {
                preference.setSummary(R.string.default_color);
            } else {
                preference.setSummary(hex);
            }
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.HEADS_UP_BG_COLOR,
                    intHex);
            return true;
		} else if (preference == mHeadsUpTimeOut) {
            int headsUpTimeOut = Integer.valueOf((String) objValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.HEADS_UP_NOTIFCATION_DECAY,
                    headsUpTimeOut);
            updateHeadsUpTimeOutSummary(headsUpTimeOut);
            return true;	
        } else if (preference == mHeadsUpTextColor) {
            String hexText = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(objValue)));
            if (hexText.equals("#00000000")) {
                preference.setSummary(R.string.default_color);
            } else {
                preference.setSummary(hexText);
            }
            int intHexText = ColorPickerPreference.convertToColorInt(hexText);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.HEADS_UP_TEXT_COLOR,
                    intHexText);
            return true;
		} else if (preference == mShowHeadsUpBottom) {
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.SHOW_HEADS_UP_BOTTOM,
                    (Boolean) objValue ? 1 : 0, UserHandle.USER_CURRENT);
            Helpers.restartSystemUI();
            return true;
		} else if (preference == mHeadsExcludeFromLockscreen) {
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.HEADS_UP_EXCLUDE_FROM_LOCK_SCREEN,
                    (Boolean) objValue ? 1 : 0, UserHandle.USER_CURRENT);
            return true;
		} else if (preference == mHeadsUpExpanded) {
            Settings.System.putIntForUser(getContentResolver(),
                    Settings.System.HEADS_UP_EXPANDED,
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
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, MENU_RESET, 0, R.string.reset_default_message)
                .setIcon(R.drawable.ic_settings_backup)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_RESET:
                resetToDefault();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void resetToDefault() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.reset);
        alertDialog.setMessage(R.string.color_reset_message);
        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                resetValues();
            }
        });
        alertDialog.setNegativeButton(R.string.cancel, null);
        alertDialog.create().show();
    }

    private void resetValues() {
        Settings.System.putInt(getContentResolver(),
                Settings.System.HEADS_UP_BG_COLOR, DEFAULT_BACKGROUND_COLOR);
        mHeadsUpBgColor.setNewPreviewColor(DEFAULT_BACKGROUND_COLOR);
        mHeadsUpBgColor.setSummary(R.string.default_color);
        Settings.System.putInt(getContentResolver(),
                Settings.System.HEADS_UP_TEXT_COLOR, 0);
        mHeadsUpTextColor.setNewPreviewColor(DEFAULT_TEXT_COLOR);
        mHeadsUpTextColor.setSummary(R.string.default_color);
    }
	
	private void updateHeadsUpTimeOutSummary(int value) {
        String summary = getResources().getString(R.string.heads_up_time_out_summary,
                value / 1000);
        if (value == 0) {
            mHeadsUpTimeOut.setSummary(
                    getResources().getString(R.string.heads_up_time_out_never_summary));
        } else {
            mHeadsUpTimeOut.setSummary(summary);
        }
    }
}
