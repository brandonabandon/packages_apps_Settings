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

public class Misc extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
		
		
	private static final String KILL_APP_LONGPRESS_BACK = "kill_app_longpress_back";
	private static final String DISABLE_IMMERSIVE_MESSAGE = "disable_immersive_message";
	
	private CheckBoxPreference mKillAppLongpressBack;
	private SwitchPreference mDisableIM;
	
	private final ArrayList<Preference> mAllPrefs = new ArrayList<Preference>();
	
	private final ArrayList<CheckBoxPreference> mResetCbPrefs
            = new ArrayList<CheckBoxPreference>();	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.screwd_misc_settings);
		
		ContentResolver resolver = getActivity().getContentResolver();
		
		mKillAppLongpressBack = findAndInitCheckboxPref(KILL_APP_LONGPRESS_BACK);
		
		mDisableIM = (SwitchPreference) findPreference(DISABLE_IMMERSIVE_MESSAGE);
        mDisableIM.setChecked((Settings.System.getInt(resolver,
                Settings.System.DISABLE_IMMERSIVE_MESSAGE, 0) == 1));
		mDisableIM.setOnPreferenceChangeListener(this);		

    }

    @Override
    public void onResume() {
        super.onResume();
		updateKillAppLongpressBackOptions();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
		if  (preference == mDisableIM) {  
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.DISABLE_IMMERSIVE_MESSAGE,
					(Boolean) newValue ? 1 : 0);
            return true;
		}	
        return false;
    }
	
	private CheckBoxPreference findAndInitCheckboxPref(String key) {
        CheckBoxPreference pref = (CheckBoxPreference) findPreference(key);
        if (pref == null) {
            throw new IllegalArgumentException("Cannot find preference with key = " + key);
        }
        mAllPrefs.add(pref);
        mResetCbPrefs.add(pref);
        return pref;
    }
	
	private void writeKillAppLongpressBackOptions() {
        Settings.Secure.putInt(getActivity().getContentResolver(),
                Settings.Secure.KILL_APP_LONGPRESS_BACK,
                mKillAppLongpressBack.isChecked() ? 1 : 0);
    }

    private void updateKillAppLongpressBackOptions() {
        mKillAppLongpressBack.setChecked(Settings.Secure.getInt(
            getActivity().getContentResolver(), Settings.Secure.KILL_APP_LONGPRESS_BACK, 0) != 0);
    }
	
	@Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mKillAppLongpressBack) {
            writeKillAppLongpressBackOptions();
        } else {
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }

        return false;
    }
}
