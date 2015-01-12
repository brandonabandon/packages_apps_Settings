/*

 * Copyright (C) 2014 Screw'd Android
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

package com.android.settings.screwd;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.SystemProperties;
import android.provider.Settings;
import android.preference.ListPreference;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.PreferenceCategory;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.SwitchPreference;
import android.provider.SearchIndexableResource;

import android.text.TextUtils;
import android.widget.Toast;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import java.util.ArrayList;
import java.util.List;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class Recents extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
		

	private static final String SHOW_CLEAR_ALL_RECENTS = "show_clear_all_recents";
	private static final String RECENTS_CLEAR_ALL_LOCATION = "recents_clear_all_location";
	
	private SwitchPreference mRecentsClearAll;
	private ListPreference mRecentsClearAllLocation;
	
	private final ArrayList<Preference> mAllPrefs = new ArrayList<Preference>();
	
	private final ArrayList<SwitchPreference> mResetSbPrefs
            = new ArrayList<SwitchPreference>();	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.screwd_recents_settings);
		
		ContentResolver resolver = getActivity().getContentResolver();
		
		mRecentsClearAll = (SwitchPreference) findPreference(SHOW_CLEAR_ALL_RECENTS);
		mRecentsClearAll.setChecked(Settings.System.getIntForUser(resolver,
			Settings.System.SHOW_CLEAR_ALL_RECENTS, 1, UserHandle.USER_CURRENT) == 1);
			
		mRecentsClearAll.setOnPreferenceChangeListener(this);
		mRecentsClearAllLocation = (ListPreference) findPreference(RECENTS_CLEAR_ALL_LOCATION);
		int location = Settings.System.getIntForUser(resolver,
			Settings.System.RECENTS_CLEAR_ALL_LOCATION, 0, UserHandle.USER_CURRENT);
		mRecentsClearAllLocation.setValue(String.valueOf(location));
		mRecentsClearAllLocation.setOnPreferenceChangeListener(this);
		updateRecentsLocation(location);		

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == mRecentsClearAll) {
            boolean show = (Boolean) newValue;
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.SHOW_CLEAR_ALL_RECENTS, show ? 1 : 0, UserHandle.USER_CURRENT);
            return true;
        } else if (preference == mRecentsClearAllLocation) {
            int location = Integer.valueOf((String) newValue);
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.RECENTS_CLEAR_ALL_LOCATION, location, UserHandle.USER_CURRENT);
            updateRecentsLocation(location);
            return true;	
		}	
        return false;
    }
	
	private SwitchPreference findAndInitSwitchPref(String key) {
        SwitchPreference pref = (SwitchPreference) findPreference(key);
        if (pref == null) {
            throw new IllegalArgumentException("Cannot find preference with key = " + key);
        }
        mAllPrefs.add(pref);
        mResetSbPrefs.add(pref);
        return pref;
    }
	
	private void updateRecentsLocation(int value) {
        ContentResolver resolver = getContentResolver();
        Resources res = getResources();
        int summary = -1;

        Settings.System.putInt(resolver, Settings.System.RECENTS_CLEAR_ALL_LOCATION, value);

        if (value == 0) {
            Settings.System.putInt(resolver, Settings.System.RECENTS_CLEAR_ALL_LOCATION, 0);
            summary = R.string.recents_clear_all_location_top_right;
        } else if (value == 1) {
            Settings.System.putInt(resolver, Settings.System.RECENTS_CLEAR_ALL_LOCATION, 1);
            summary = R.string.recents_clear_all_location_top_left;
        } else if (value == 2) {
            Settings.System.putInt(resolver, Settings.System.RECENTS_CLEAR_ALL_LOCATION, 2);
            summary = R.string.recents_clear_all_location_top_center;
        } else if (value == 3) {
            Settings.System.putInt(resolver, Settings.System.RECENTS_CLEAR_ALL_LOCATION, 3);
            summary = R.string.recents_clear_all_location_bottom_right;
        } else if (value == 4) {
            Settings.System.putInt(resolver, Settings.System.RECENTS_CLEAR_ALL_LOCATION, 4);
            summary = R.string.recents_clear_all_location_bottom_left;
        } else if (value == 5) {
            Settings.System.putInt(resolver, Settings.System.RECENTS_CLEAR_ALL_LOCATION, 5);
            summary = R.string.recents_clear_all_location_bottom_center;
        }
        if (mRecentsClearAllLocation != null && summary != -1) {
            mRecentsClearAllLocation.setSummary(res.getString(summary));
        }
    }
}
