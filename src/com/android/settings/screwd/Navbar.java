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

import android.provider.SearchIndexableResource;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;

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
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.PreferenceCategory;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.SlimSeekBarPreference;
import android.preference.SwitchPreference;

import android.text.TextUtils;
import android.widget.Toast;


import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.util.slim.Action;
import java.util.ArrayList;
import java.util.List;

import com.android.internal.logging.MetricsLogger;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class Navbar extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {
	
	private static final String KILL_APP_LONGPRESS_BACK = "kill_app_longpress_back";
	private static final String NAVIGATION_BAR_TINT = "navigation_bar_tint";
    private static final String DIM_NAV_BUTTONS = "dim_nav_buttons";
    private static final String DIM_NAV_BUTTONS_TIMEOUT = "dim_nav_buttons_timeout";
    private static final String DIM_NAV_BUTTONS_ALPHA = "dim_nav_buttons_alpha";
    private static final String DIM_NAV_BUTTONS_ANIMATE = "dim_nav_buttons_animate";
    private static final String DIM_NAV_BUTTONS_ANIMATE_DURATION = "dim_nav_buttons_animate_duration";
    private static final String DIM_NAV_BUTTONS_TOUCH_ANYWHERE = "dim_nav_buttons_touch_anywhere";

    private SwitchPreference mKillAppLongPressBack;
	private ColorPickerPreference mNavbarButtonTint;
    private SwitchPreference mDimNavButtons;
    private SlimSeekBarPreference mDimNavButtonsTimeout;
    private SlimSeekBarPreference mDimNavButtonsAlpha;
    private SwitchPreference mDimNavButtonsAnimate;
    private SlimSeekBarPreference mDimNavButtonsAnimateDuration;
    private SwitchPreference mDimNavButtonsTouchAnywhere;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.screwd_navbar_settings);
		
		Activity activity = getActivity();
		
		ContentResolver resolver = getActivity().getContentResolver();
		PreferenceScreen prefSet = getPreferenceScreen();
			
		 // kill-app long press back
        mKillAppLongPressBack = (SwitchPreference) findPreference(KILL_APP_LONGPRESS_BACK);
        mKillAppLongPressBack.setOnPreferenceChangeListener(this);
        int killAppLongPressBack = Settings.Secure.getInt(getContentResolver(),
                KILL_APP_LONGPRESS_BACK, 0);
        mKillAppLongPressBack.setChecked(killAppLongPressBack != 0);
		
		// Navigation bar button color
        mNavbarButtonTint = (ColorPickerPreference) findPreference(NAVIGATION_BAR_TINT);
        mNavbarButtonTint.setOnPreferenceChangeListener(this);
        int intColor = Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.NAVIGATION_BAR_TINT, 0xffffffff);
        String hexColor = String.format("#%08x", (0xffffffff & intColor));
        mNavbarButtonTint.setSummary(hexColor);
        mNavbarButtonTint.setNewPreviewColor(intColor);

        // SlimDim
        mDimNavButtons = (SwitchPreference) findPreference(DIM_NAV_BUTTONS);
        mDimNavButtons.setOnPreferenceChangeListener(this);

        mDimNavButtonsTouchAnywhere = (SwitchPreference) findPreference(DIM_NAV_BUTTONS_TOUCH_ANYWHERE);
        mDimNavButtonsTouchAnywhere.setOnPreferenceChangeListener(this);

        mDimNavButtonsTimeout = (SlimSeekBarPreference) findPreference(DIM_NAV_BUTTONS_TIMEOUT);
        mDimNavButtonsTimeout.setDefault(3000);
        mDimNavButtonsTimeout.isMilliseconds(true);
        mDimNavButtonsTimeout.setInterval(1);
        mDimNavButtonsTimeout.minimumValue(100);
        mDimNavButtonsTimeout.multiplyValue(100);
        mDimNavButtonsTimeout.setOnPreferenceChangeListener(this);

        mDimNavButtonsAlpha = (SlimSeekBarPreference) findPreference(DIM_NAV_BUTTONS_ALPHA);
        mDimNavButtonsAlpha.setDefault(50);
        mDimNavButtonsAlpha.setInterval(1);
        mDimNavButtonsAlpha.setOnPreferenceChangeListener(this);

        mDimNavButtonsAnimate = (SwitchPreference) findPreference(DIM_NAV_BUTTONS_ANIMATE);
        mDimNavButtonsAnimate.setOnPreferenceChangeListener(this);

        mDimNavButtonsAnimateDuration = (SlimSeekBarPreference) findPreference(DIM_NAV_BUTTONS_ANIMATE_DURATION);
        mDimNavButtonsAnimateDuration.setDefault(2000);
        mDimNavButtonsAnimateDuration.isMilliseconds(true);
        mDimNavButtonsAnimateDuration.setInterval(1);
        mDimNavButtonsAnimateDuration.minimumValue(100);
        mDimNavButtonsAnimateDuration.multiplyValue(100);
        mDimNavButtonsAnimateDuration.setOnPreferenceChangeListener(this);
        updateSettings();
    }
	
	private void updateSettings() {
        boolean enableNavigationBar = Action.isNavBarEnabled(getActivity());

        if (mDimNavButtons != null) {
            mDimNavButtons.setChecked(Settings.System.getInt(getContentResolver(),
                    Settings.System.DIM_NAV_BUTTONS, 0) == 1);
        }

        if (mDimNavButtonsTouchAnywhere != null) {
            mDimNavButtonsTouchAnywhere.setChecked(Settings.System.getInt(getContentResolver(),
                    Settings.System.DIM_NAV_BUTTONS_TOUCH_ANYWHERE, 0) == 1);
        }

        if (mDimNavButtonsTimeout != null) {
            final int dimTimeout = Settings.System.getInt(getContentResolver(),
                    Settings.System.DIM_NAV_BUTTONS_TIMEOUT, 3000);
            // minimum 100 is 1 interval of the 100 multiplier
            mDimNavButtonsTimeout.setInitValue((dimTimeout / 100) - 1);
        }

        if (mDimNavButtonsAlpha != null) {
            int alphaScale = Settings.System.getInt(getContentResolver(),
                    Settings.System.DIM_NAV_BUTTONS_ALPHA, 50);
            mDimNavButtonsAlpha.setInitValue(alphaScale);
        }

        if (mDimNavButtonsAnimate != null) {
            mDimNavButtonsAnimate.setChecked(Settings.System.getInt(getContentResolver(),
                    Settings.System.DIM_NAV_BUTTONS_ANIMATE, 0) == 1);
        }

        if (mDimNavButtonsAnimateDuration != null) {
            final int animateDuration = Settings.System.getInt(getContentResolver(),
                    Settings.System.DIM_NAV_BUTTONS_ANIMATE_DURATION, 2000);
            // minimum 100 is 1 interval of the 100 multiplier
            mDimNavButtonsAnimateDuration.setInitValue((animateDuration / 100) - 1);
        }

        updateNavbarPreferences(enableNavigationBar);
    }
	
	private void updateNavbarPreferences(boolean show) {
        mDimNavButtons.setEnabled(show);
        mDimNavButtonsTouchAnywhere.setEnabled(show);
        mDimNavButtonsTimeout.setEnabled(show);
        mDimNavButtonsAlpha.setEnabled(show);
        mDimNavButtonsAnimate.setEnabled(show);
        mDimNavButtonsAnimateDuration.setEnabled(show);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == mKillAppLongPressBack) {
            boolean value = (Boolean) newValue;
            Settings.Secure.putInt(getContentResolver(), KILL_APP_LONGPRESS_BACK,
                    value ? 1 : 0);
            return true;
        } else if (preference == mDimNavButtons) {
            Settings.System.putInt(getActivity().getContentResolver(),
                Settings.System.DIM_NAV_BUTTONS,
                    ((Boolean) newValue) ? 1 : 0);
            return true;
        } else if (preference == mDimNavButtonsTouchAnywhere) {
            Settings.System.putInt(getActivity().getContentResolver(),
                Settings.System.DIM_NAV_BUTTONS_TOUCH_ANYWHERE,
                    ((Boolean) newValue) ? 1 : 0);
            return true;
        } else if (preference == mDimNavButtonsTimeout) {
            Settings.System.putInt(getActivity().getContentResolver(),
                Settings.System.DIM_NAV_BUTTONS_TIMEOUT, Integer.parseInt((String) newValue));
            return true;
        } else if (preference == mDimNavButtonsAlpha) {
            Settings.System.putInt(getActivity().getContentResolver(),
                Settings.System.DIM_NAV_BUTTONS_ALPHA, Integer.parseInt((String) newValue));
            return true;
        } else if (preference == mDimNavButtonsAnimate) {
            Settings.System.putInt(getActivity().getContentResolver(),
                Settings.System.DIM_NAV_BUTTONS_ANIMATE,
                    ((Boolean) newValue) ? 1 : 0);
            return true;
        } else if (preference == mDimNavButtonsAnimateDuration) {
            Settings.System.putInt(getActivity().getContentResolver(),
                Settings.System.DIM_NAV_BUTTONS_ANIMATE_DURATION,
                Integer.parseInt((String) newValue));
            return true;
        } else if (preference == mNavbarButtonTint) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.NAVIGATION_BAR_TINT, intHex);
            return true;
        }
        return false;
    }
	
	public static final Indexable.SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
        new BaseSearchIndexProvider() {
        @Override
        public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                                                                    boolean enabled) {
            ArrayList<SearchIndexableResource> result =
                new ArrayList<SearchIndexableResource>();

            SearchIndexableResource sir = new SearchIndexableResource(context);
            sir.xmlResId = R.xml.screwd_navbar_settings;
            result.add(sir);

            return result;
        }

        @Override
        public List<String> getNonIndexableKeys(Context context) {
            ArrayList<String> result = new ArrayList<String>();
            return result;
        }
    };
	
	@Override
	protected int getMetricsCategory() {
        return MetricsLogger.APPLICATION;
    }

}