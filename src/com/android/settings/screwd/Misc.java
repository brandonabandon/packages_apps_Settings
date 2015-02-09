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
import com.android.settings.screwd.util.Helpers;

import java.util.ArrayList;
import java.util.List;
import com.android.settings.Utils;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class Misc extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
		
		
	private static final String KILL_APP_LONGPRESS_BACK = "kill_app_longpress_back";
	private static final String KILL_APP_LONGPRESS_TIMEOUT = "kill_app_longpress_timeout";
	private static final String DISABLE_IMMERSIVE_MESSAGE = "disable_immersive_message";
	private static final String KEY_LOCKSCREEN_DIALER_WIDGET_HIDE = "dialer_widget_hide";
	
	private SwitchPreference mKillAppLongpressBack;
	private ListPreference mKillAppLongpressTimeout;
	private SwitchPreference mDisableIM;
	private SwitchPreference mDialerWidgetHide;
	
	private final ArrayList<Preference> mAllPrefs = new ArrayList<Preference>();
	
	private final ArrayList<SwitchPreference> mResetSbPrefs
            = new ArrayList<SwitchPreference>();	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.screwd_misc_settings);
		
		ContentResolver resolver = getActivity().getContentResolver();
		
		mKillAppLongpressBack = findAndInitSwitchPref(KILL_APP_LONGPRESS_BACK);
		
        mKillAppLongpressTimeout = addListPreference(KILL_APP_LONGPRESS_TIMEOUT);
        int killAppLongpressTimeout = Settings.Secure.getIntForUser(getActivity().getContentResolver(),
                Settings.Secure.KILL_APP_LONGPRESS_TIMEOUT, 2000, UserHandle.USER_CURRENT);
        mKillAppLongpressTimeout.setOnPreferenceChangeListener(this);
		
		mDisableIM = (SwitchPreference) findPreference(DISABLE_IMMERSIVE_MESSAGE);
        mDisableIM.setChecked((Settings.System.getInt(resolver,
                Settings.System.DISABLE_IMMERSIVE_MESSAGE, 0) == 1));
		mDisableIM.setOnPreferenceChangeListener(this);
		
        mDialerWidgetHide = (SwitchPreference) findPreference(KEY_LOCKSCREEN_DIALER_WIDGET_HIDE);
        mDialerWidgetHide.setChecked(Settings.System.getIntForUser(resolver,
            Settings.System.DIALER_WIDGET_HIDE, 0, UserHandle.USER_CURRENT) == 1);
        mDialerWidgetHide.setOnPreferenceChangeListener(this);
        if (!Utils.isVoiceCapable(getActivity())){
            getPreferenceScreen().removePreference(mDialerWidgetHide);
        }		

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
		} else if (preference == mDialerWidgetHide) {
            boolean value = (Boolean) newValue;
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    Settings.System.DIALER_WIDGET_HIDE, value ? 1 : 0, UserHandle.USER_CURRENT);
            Helpers.restartSystem();
		} else if (preference == mKillAppLongpressTimeout) {
            writeKillAppLongpressTimeoutOptions(newValue);
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
        } else {
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }

        return false;
    }

}
