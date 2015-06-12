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

import com.android.settings.util.Helpers;

import com.android.internal.util.cm.QSUtils;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import java.util.ArrayList;
import java.util.List;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class Misc extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
		
		
	private static final String KILL_APP_LONGPRESS_BACK = "kill_app_longpress_back";
	private static final String KILL_APP_LONGPRESS_TIMEOUT = "kill_app_longpress_timeout";
	private static final String DISABLE_IMMERSIVE_MESSAGE = "disable_immersive_message";
	private static final String DISABLE_TORCH_ON_SCREEN_OFF = "disable_torch_on_screen_off";
    	private static final String DISABLE_TORCH_ON_SCREEN_OFF_DELAY = "disable_torch_on_screen_off_delay";
	private static final String RESTART_SYSTEMUI = "restart_systemui";
	
	private SwitchPreference mKillAppLongpressBack;
	private ListPreference mKillAppLongpressTimeout;
	private SwitchPreference mDisableIM;
	private ListPreference mRecentsClearAllLocation;
    	private SwitchPreference mTorchOff;
	private ListPreference mTorchOffDelay;
	private Preference mRestartSystemUI;
	
	private final ArrayList<Preference> mAllPrefs = new ArrayList<Preference>();
	
	private final ArrayList<SwitchPreference> mResetSbPrefs
            = new ArrayList<SwitchPreference>();	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.screwd_misc_settings);
		
		Activity activity = getActivity();
		
		ContentResolver resolver = getActivity().getContentResolver();
		PreferenceScreen prefSet = getPreferenceScreen();
		
		mKillAppLongpressBack = findAndInitSwitchPref(KILL_APP_LONGPRESS_BACK);
		
        mKillAppLongpressTimeout = addListPreference(KILL_APP_LONGPRESS_TIMEOUT);
        int killAppLongpressTimeout = Settings.Secure.getIntForUser(getActivity().getContentResolver(),
                Settings.Secure.KILL_APP_LONGPRESS_TIMEOUT, 2000, UserHandle.USER_CURRENT);
        mKillAppLongpressTimeout.setOnPreferenceChangeListener(this);
		
		mDisableIM = (SwitchPreference) findPreference(DISABLE_IMMERSIVE_MESSAGE);
        mDisableIM.setChecked((Settings.System.getInt(resolver,
                Settings.System.DISABLE_IMMERSIVE_MESSAGE, 0) == 1));
		mDisableIM.setOnPreferenceChangeListener(this);
		
		mTorchOff = (SwitchPreference) findPreference(DISABLE_TORCH_ON_SCREEN_OFF);
        mTorchOffDelay = (ListPreference) findPreference(DISABLE_TORCH_ON_SCREEN_OFF_DELAY);
        int torchOffDelay = Settings.System.getInt(resolver,
                Settings.System.DISABLE_TORCH_ON_SCREEN_OFF_DELAY, 10);
        mTorchOffDelay.setValue(String.valueOf(torchOffDelay));
        mTorchOffDelay.setSummary(mTorchOffDelay.getEntry());
        mTorchOffDelay.setOnPreferenceChangeListener(this);

        if (!QSUtils.deviceSupportsFlashLight(activity)) {
            prefSet.removePreference(mTorchOff);
            prefSet.removePreference(mTorchOffDelay);
        }
		
	mRestartSystemUI = findPreference(RESTART_SYSTEMUI);	

    }

    @Override
    public void onResume() {
        super.onResume();
		updateKillAppLongpressBackOptions();
		updateKillAppLongpressTimeoutOptions();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
	if  (preference == mDisableIM) {  
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.DISABLE_IMMERSIVE_MESSAGE,
					(Boolean) newValue ? 1 : 0);
            return true;
		} else if (preference == mKillAppLongpressTimeout) {
            writeKillAppLongpressTimeoutOptions(newValue);
            return true;
        } else if (preference == mTorchOffDelay) {
            int torchOffDelay = Integer.valueOf((String) newValue);
            int index = mTorchOffDelay.findIndexOfValue((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.DISABLE_TORCH_ON_SCREEN_OFF_DELAY, torchOffDelay);
            mTorchOffDelay.setSummary(mTorchOffDelay.getEntries()[index]);
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
	
	private void writeKillAppLongpressBackOptions() {
        Settings.Secure.putInt(getActivity().getContentResolver(),
                Settings.Secure.KILL_APP_LONGPRESS_BACK,
                mKillAppLongpressBack.isChecked() ? 1 : 0);
    }

    private void updateKillAppLongpressBackOptions() {
        mKillAppLongpressBack.setChecked(Settings.Secure.getInt(
            getActivity().getContentResolver(), Settings.Secure.KILL_APP_LONGPRESS_BACK, 0) != 0);
    }
	
    private void writeKillAppLongpressTimeoutOptions(Object newValue) {
        int index = mKillAppLongpressTimeout.findIndexOfValue((String) newValue);
        int value = Integer.valueOf((String) newValue);
        Settings.Secure.putInt(getActivity().getContentResolver(),
                Settings.Secure.KILL_APP_LONGPRESS_TIMEOUT, value);
        mKillAppLongpressTimeout.setSummary(mKillAppLongpressTimeout.getEntries()[index]);
    }

    private void updateKillAppLongpressTimeoutOptions() {
        String value = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.KILL_APP_LONGPRESS_TIMEOUT);
        if (value == null) {
            value = "";
        }

        CharSequence[] values = mKillAppLongpressTimeout.getEntryValues();
        for (int i = 0; i < values.length; i++) {
            if (value.contentEquals(values[i])) {
                mKillAppLongpressTimeout.setValueIndex(i);
                mKillAppLongpressTimeout.setSummary(mKillAppLongpressTimeout.getEntries()[i]);
                return;
            }
        }
        mKillAppLongpressTimeout.setValueIndex(0);
        mKillAppLongpressTimeout.setSummary(mKillAppLongpressTimeout.getEntries()[0]);
    }
	
    private ListPreference addListPreference(String prefKey) {
        ListPreference pref = (ListPreference) findPreference(prefKey);
        mAllPrefs.add(pref);
        pref.setOnPreferenceChangeListener(this);
        return pref;
    }
	
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mKillAppLongpressBack) {
            writeKillAppLongpressBackOptions();
	} else if (preference == mRestartSystemUI) {
            Helpers.restartSystemUI();	
        } else {
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }

        return false;
    }

}
