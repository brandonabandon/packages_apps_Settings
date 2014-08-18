/*
 * Copyright (C) 2010 The Android Open Source Project
 * Code has been modified - Copyright (C) 2014 Corey "Mr. Apocalypse" Edwards
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

import android.view.WindowManagerGlobal;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.apocalypse.AppMultiSelectListPreference;
import com.android.settings.cyanogenmod.NEWSeekBarPreference;
import com.android.settings.util.CMDProcessor;

import java.io.File;
import java.lang.Thread;
import java.util.HashSet;
import java.util.Set;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.Arrays;
import com.android.settings.util.Helpers;

public class SystemSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
		private static final String TAG = "System Settings";	
		
    	private static final String KEY_PROXIMITY_WAKE = "proximity_on_wake";
		private static final String DISABLE_FC_NOTIFICATIONS = "disable_fc_notifications";
		private static final String DISABLE_BOOTAUDIO = "disable_bootaudio";
	

    	private CheckBoxPreference mDisableFC;
		private CheckBoxPreference mDisableBootAudio;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentResolver resolver = getActivity().getContentResolver();

        addPreferencesFromResource(R.xml.system_settings);
		
		PreferenceScreen prefScreen = getPreferenceScreen();
		
		Resources res = getResources();
		
		boolean proximityCheckOnWait = res.getBoolean(
                com.android.internal.R.bool.config_proximityCheckOnWake);
        if (!proximityCheckOnWait) {
            Settings.System.putInt(getContentResolver(), Settings.System.PROXIMITY_ON_WAKE, 1);
        }
		
		// Disable FC notification
        mDisableFC = (CheckBoxPreference) findPreference(DISABLE_FC_NOTIFICATIONS);
        mDisableFC.setChecked((Settings.System.getInt(resolver,
                Settings.System.DISABLE_FC_NOTIFICATIONS, 0) == 1));
				
		// Boot audio
        mDisableBootAudio = (CheckBoxPreference) findPreference("disable_bootaudio");

        if(!new File("/system/media/audio.mp3").exists() &&
                !new File("/system/media/boot_audio").exists() ) {
            mDisableBootAudio.setEnabled(false);
            mDisableBootAudio.setSummary(R.string.disable_bootaudio_summary_disabled);
        } else {
            mDisableBootAudio.setChecked(!new File("/system/media/audio.mp3").exists());
            if (mDisableBootAudio.isChecked())
                mDisableBootAudio.setSummary(R.string.disable_bootaudio_summary);
        }		
		        		
    }
	
    @Override
    public void onResume() {
        super.onResume();        
    }
	
    @Override
    public void onPause() {
        super.onPause();
        
    }
	
    
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
       	
		ContentResolver resolver = getActivity().getContentResolver();	
        if  (preference == mDisableFC) {
            boolean checked = ((CheckBoxPreference)preference).isChecked();
            Settings.System.putInt(resolver,
                    Settings.System.DISABLE_FC_NOTIFICATIONS, checked ? 1:0);
		} else if (preference == mDisableBootAudio) {
            boolean checked = ((CheckBoxPreference) preference).isChecked();
            if (checked) {
                Helpers.getMount("rw");
                CMDProcessor.runSuCommand(
                        "mv /system/media/audio.mp3 /system/media/boot_audio");
                Helpers.getMount("ro");
                preference.setSummary(R.string.disable_bootaudio_summary);
            } else {
                Helpers.getMount("rw");
                CMDProcessor.runSuCommand(
                        "mv /system/media/boot_audio /system/media/audio.mp3");
                Helpers.getMount("ro");
            }					
        } else {
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }
		
		return true;
	
    }
	
    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();
        		

        return true;
    }
	
}
