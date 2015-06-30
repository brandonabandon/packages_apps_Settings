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
import com.android.settings.screwd.NumberPickerPreference;

import com.android.internal.util.cm.QSUtils;

public class PowerMenu extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
		
		private static final String KEY_ADVANCED_REBOOT = "advanced_reboot";
        private static final String SCREENSHOT_DELAY = "screenshot_delay";
		private static final String POWERMENU_TORCH = "powermenu_torch";
		
		private ListPreference mAdvancedReboot;
        private NumberPickerPreference mScreenshotDelay;
		private SwitchPreference mPowermenuTorch;

        private static final int MIN_DELAY_VALUE = 1;
        private static final int MAX_DELAY_VALUE = 30;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.screwd_powermenu_settings);
		
		ContentResolver resolver = getActivity().getContentResolver();
		
		mAdvancedReboot = (ListPreference) findPreference(KEY_ADVANCED_REBOOT);
        mAdvancedReboot.setValue(String.valueOf(Settings.Secure.getInt(
        		getContentResolver(), Settings.Secure.ADVANCED_REBOOT, 0)));
        mAdvancedReboot.setSummary(mAdvancedReboot.getEntry());
        mAdvancedReboot.setOnPreferenceChangeListener(this);
        	
        mScreenshotDelay = (NumberPickerPreference) findPreference(
                SCREENSHOT_DELAY);
        mScreenshotDelay.setOnPreferenceChangeListener(this);
        mScreenshotDelay.setMinValue(MIN_DELAY_VALUE);
        mScreenshotDelay.setMaxValue(MAX_DELAY_VALUE);
        int ssDelay = Settings.System.getInt(getContentResolver(),
                Settings.System.SCREENSHOT_DELAY, 1);
        mScreenshotDelay.setCurrentValue(ssDelay);
		
		mPowermenuTorch = (SwitchPreference) findPreference(POWERMENU_TORCH);
        mPowermenuTorch.setOnPreferenceChangeListener(this);
        if (!QSUtils.deviceSupportsFlashLight(getActivity())) {
            mPrefSet.removePreference(mPowermenuTorch);
        } else {
        mPowermenuTorch.setChecked((Settings.System.getInt(resolver,
                Settings.System.POWERMENU_TORCH, 0) == 1));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == mAdvancedReboot) {
            Settings.Secure.putInt(getContentResolver(), Settings.Secure.ADVANCED_REBOOT,
                    Integer.valueOf((String) newValue));
            mAdvancedReboot.setValue(String.valueOf(newValue));
            mAdvancedReboot.setSummary(mAdvancedReboot.getEntry());
			return true;
		} else if (preference == mScreenshotDelay) {
            int value = Integer.parseInt(newValue.toString());
            Settings.System.putInt(getContentResolver(), Settings.System.SCREENSHOT_DELAY,
                    value);
            return true;
		} else if (preference == mPowermenuTorch) {
            boolean checked = ((SwitchPreference)preference).isChecked();
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.POWERMENU_TORCH, checked ? 1:0);
            return true;		
		}	
        return false;
    }
	
	@Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
	
        return false;
    }
	
}
