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
		
    	private static final String HOLDER = "holder";
	

    	private CheckBoxPreference blah;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentResolver resolver = getActivity().getContentResolver();

        addPreferencesFromResource(R.xml.system_settings);
		
		PreferenceScreen prefScreen = getPreferenceScreen();
		
		

        		
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
        boolean value;
				
        if (preference == blah) {
            boolean checked = ((CheckBoxPreference) preference).isChecked();
            if (checked) {
                //do stuff
            } else {
                //do stuff
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
