/*
 * Copyright (C) 2014 Slimroms
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

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;

import com.android.settings.screwd.qs.QSTiles;

import com.android.internal.widget.LockPatternUtils;

import android.provider.SearchIndexableResource;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QsSettings extends SettingsPreferenceFragment
            implements OnPreferenceChangeListener, Indexable  {		

    	public static final String TAG = "QsSettings";

    	private static final String PREF_QUICK_PULLDOWN = "quick_pulldown";
    	private static final String PREF_SMART_PULLDOWN = "smart_pulldown";
    	private static final String PREF_BLOCK_ON_SECURE_KEYGUARD = "block_on_secure_keyguard";
	private static final String PREF_QS_TYPE = "qs_type";
	private static final String PREF_QS_TYPE_CATEGORY = "qs_type_cat";
	private static final String PREF_QS_TILES_CATEGORY = "qs_tile_op_cat";
	private static final String PREF_QS_BAR_CONFIG = "qs_bar_buttons";
	private static final String PREF_QS_TILE_CONFIG = "qs_panel_tiles";
	private static final String PREF_QS_SETTINGS = "qs_settings";
	
	private static final String PREF_QS_OPTIONS_CATEGORY = "qs_ops";
	
	private static final String PREF_ADV_LOC = "qs_location_advanced";
	private static final String PREF_AUTO_CLOSE = "quick_settings_collapse_panel";
	private static final String PREF_VIB_TOUCH = "quick_settings_vibrate";
	private static final String PREF_BRIGHTNESS = "qs_show_brightness_slider";

    	ListPreference mQuickPulldown;
    	ListPreference mSmartPulldown;
    	SwitchPreference mBlockOnSecureKeyguard;
	private ListPreference mQSType;
	PreferenceScreen mQSTileConfig;
	PreferenceScreen mQSBarConfig;
	PreferenceCategory mQSTypeCat;
	PreferenceCategory mQSTilesCat;
	PreferenceScreen mQSSettings;
	
	SwitchPreference mQSAdvLoc;
	SwitchPreference mQSAutoClosePanel;
	SwitchPreference mQSVibOnTouch;
	SwitchPreference mQSBrightnessSlider;
	PreferenceCategory mQSOptions;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.screwd_qs_settings);

        PreferenceScreen prefs = getPreferenceScreen();
		
	mQSSettings = (PreferenceScreen) findPreference(PREF_QS_SETTINGS);

        mQuickPulldown = (ListPreference) findPreference(PREF_QUICK_PULLDOWN);
        mSmartPulldown = (ListPreference) findPreference(PREF_SMART_PULLDOWN);
		
	mQSAdvLoc = (SwitchPreference) findPreference(PREF_ADV_LOC);
	mQSAutoClosePanel = (SwitchPreference) findPreference(PREF_AUTO_CLOSE);
	mQSVibOnTouch = (SwitchPreference) findPreference(PREF_VIB_TOUCH);
	mQSBrightnessSlider = (SwitchPreference) findPreference(PREF_BRIGHTNESS);
	mQSOptions = (PreferenceCategory) findPreference(PREF_QS_OPTIONS_CATEGORY);

        // Quick Pulldown
        mQuickPulldown.setOnPreferenceChangeListener(this);
        int statusQuickPulldown = Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_QUICK_QS_PULLDOWN, 1);
        mQuickPulldown.setValue(String.valueOf(statusQuickPulldown));
        updateQuickPulldownSummary(statusQuickPulldown);

        // Smart Pulldown
        mSmartPulldown.setOnPreferenceChangeListener(this);
        int smartPulldown = Settings.System.getInt(getContentResolver(),
                Settings.System.QS_SMART_PULLDOWN, 0);
        mSmartPulldown.setValue(String.valueOf(smartPulldown));
        updateSmartPulldownSummary(smartPulldown);
		
		//QS Type
		mQSType = (ListPreference) findPreference(PREF_QS_TYPE);
        int type = Settings.System.getInt(getContentResolver(),
               Settings.System.QS_TYPE, 0);
        mQSType.setValue(String.valueOf(type));
        mQSType.setSummary(mQSType.getEntry());
        mQSType.setOnPreferenceChangeListener(this);
		
	mQSTypeCat = (PreferenceCategory) findPreference(PREF_QS_TYPE_CATEGORY);
	mQSTilesCat = (PreferenceCategory) findPreference(PREF_QS_TILES_CATEGORY);
	mQSTileConfig = (PreferenceScreen) findPreference(PREF_QS_TILE_CONFIG);
	mQSBarConfig = (PreferenceScreen) findPreference(PREF_QS_BAR_CONFIG);
		
	int blah = Settings.System.getInt(mContext.getContentResolver(),
		Settings.System.QS_TYPE, 0);
				
	updateQSOptions(blah);		
				
				
		/*
        final LockPatternUtils lockPatternUtils = new LockPatternUtils(getActivity());
        mBlockOnSecureKeyguard = (SwitchPreference) findPreference(PREF_BLOCK_ON_SECURE_KEYGUARD);
        if (lockPatternUtils.isSecure()) {
            mBlockOnSecureKeyguard.setChecked(Settings.Secure.getInt(getContentResolver(),
                    Settings.Secure.STATUS_BAR_LOCKED_ON_SECURE_KEYGUARD, 1) == 1);
            mBlockOnSecureKeyguard.setOnPreferenceChangeListener(this);
        } else {
            prefs.removePreference(mBlockOnSecureKeyguard);
        }
		*/

    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        int intValue;
		if (preference == mQSType) {
            intValue = Integer.valueOf((String) newValue);
            int index = mQSType.findIndexOfValue((String) newValue);
            Settings.System.putInt(getContentResolver(),
                Settings.System.QS_TYPE, intValue);
            preference.setSummary(mQSType.getEntries()[index]);
	    updateQSOptions(index);	
            return true;
		} else if (preference == mQuickPulldown) {
            int statusQuickPulldown = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.STATUS_BAR_QUICK_QS_PULLDOWN,
                    statusQuickPulldown);
            updateQuickPulldownSummary(statusQuickPulldown);
            return true;
        } else if (preference == mSmartPulldown) {
            int smartPulldown = Integer.valueOf((String) newValue);
            Settings.System.putInt(getContentResolver(), Settings.System.QS_SMART_PULLDOWN,
                    smartPulldown);
            updateSmartPulldownSummary(smartPulldown);
            return true;
		/*	
        } else if (preference == mBlockOnSecureKeyguard) {
            Settings.Secure.putInt(getContentResolver(),
                    Settings.Secure.STATUS_BAR_LOCKED_ON_SECURE_KEYGUARD,
                    (Boolean) newValue ? 1 : 0);
            return true;
		*/	
		}
		
        return false;
    }
	
    private void updateQSOptions (int value) {
	Resources res = getResources();
		
	if (value == 0) { //QS panel selected
		/* REMOVE */
		mQSTypeCat.removePreference(mQSBarConfig); // QS bar customization
		/* ADD */
		mQSTypeCat.addPreference(mQSTileConfig); //add QS tile customization
		mQSSettings.addPreference(mQSTilesCat); //ADD ENTIRE TILE LAYOUT CATEGORY
		mQSOptions.addPreference(mQSAdvLoc); // Advanced location
		mQSOptions.addPreference(mQSAutoClosePanel); //auto-collapse
		mQSOptions.addPreference(mQSVibOnTouch); // vibrate on touch
		mQSOptions.addPreference(mQSBrightnessSlider); // brightness slider
	} else if (value == 1) { //QS bar selected
		/* REMOVE */
		mQSTypeCat.removePreference(mQSTileConfig); // QS tile customization			
		mQSSettings.removePreference(mQSTilesCat); // ENTIRE TILE LAYOUT CATEGORY
		mQSOptions.removePreference(mQSAdvLoc); // Advanced location
		mQSOptions.removePreference(mQSAutoClosePanel); //auto-collapse
		mQSOptions.removePreference(mQSVibOnTouch); // vibrate on touch
		mQSOptions.removePreference(mQSBrightnessSlider); // brightness slider
		/* ADD */
		mQSTypeCat.addPreference(mQSBarConfig); //add QS bar customization
	} else if (value == 2) { //QS hidden
		/* REMOVE */
		mQSTypeCat.removePreference(mQSBarConfig); // QS bar customization
		mQSTypeCat.removePreference(mQSTileConfig); // QS tile customization
		mQSSettings.removePreference(mQSTilesCat); // ENTIRE TILE LAYOUT CATEGORY
		mQSOptions.removePreference(mQSAdvLoc); // Advanced location
		mQSOptions.removePreference(mQSAutoClosePanel); //auto-collapse
		mQSOptions.removePreference(mQSVibOnTouch); // vibrate on touch
		mQSOptions.removePreference(mQSBrightnessSlider); // brightness slider
	}
    }

    private void updateSmartPulldownSummary(int value) {
        Resources res = getResources();

        if (value == 0) {
            // Smart pulldown deactivated
            mSmartPulldown.setSummary(res.getString(R.string.smart_pulldown_off));
        } else {
            String type = null;
            switch (value) {
                case 1:
                    type = res.getString(R.string.smart_pulldown_dismissable);
                    break;
                case 2:
                    type = res.getString(R.string.smart_pulldown_persistent);
                    break;
                default:
                    type = res.getString(R.string.smart_pulldown_all);
                    break;
            }
            // Remove title capitalized formatting
            type = type.toLowerCase();
            mSmartPulldown.setSummary(res.getString(R.string.smart_pulldown_summary, type));
        }
    }

    private void updateQuickPulldownSummary(int value) {
        Resources res = getResources();

        if (value == 0) {
            // quick pulldown deactivated
            mQuickPulldown.setSummary(res.getString(R.string.quick_pulldown_off));
        } else {
            Locale l = Locale.getDefault();
            boolean isRtl = TextUtils.getLayoutDirectionFromLocale(l) == View.LAYOUT_DIRECTION_RTL;
            String direction = res.getString(value == 2
                    ? (isRtl ? R.string.quick_pulldown_right : R.string.quick_pulldown_left)
                    : (isRtl ? R.string.quick_pulldown_left : R.string.quick_pulldown_right));
            mQuickPulldown.setSummary(res.getString(R.string.summary_quick_pulldown, direction));
        }
    }
	
	public static final Indexable.SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
        new BaseSearchIndexProvider() {
        @Override
        public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                                                                    boolean enabled) {
            ArrayList<SearchIndexableResource> result =
                new ArrayList<SearchIndexableResource>();

            SearchIndexableResource sir = new SearchIndexableResource(context);
            sir.xmlResId = R.xml.screwd_qs_settings;
            result.add(sir);

            return result;
        }

        @Override
        public List<String> getNonIndexableKeys(Context context) {
            ArrayList<String> result = new ArrayList<String>();
            return result;
        }
    };

}

