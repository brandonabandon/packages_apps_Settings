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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
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
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.PreferenceCategory;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.SwitchPreference;
import android.provider.SearchIndexableResource;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.util.cm.ScreenType;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

import java.util.ArrayList;
import java.util.List;

import com.android.internal.util.slim.DeviceUtils;

public class NavBar extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
		
		
	 private static final String PREF_NAVIGATION_BAR_HEIGHT = "navigation_bar_height";
    private static final String PREF_NAVIGATION_BAR_HEIGHT_LANDSCAPE = "navigation_bar_height_landscape";
    private static final String PREF_NAVIGATION_BAR_WIDTH = "navigation_bar_width";
	
	private static final String NAVIGATION_BAR_TINT = "navigation_bar_tint";
	
	private static final int MENU_RESET = Menu.FIRST;
    private static final int DLG_RESET = 0;

    ListPreference mNavigationBarHeight;
    ListPreference mNavigationBarHeightLandscape;
    ListPreference mNavigationBarWidth;
	private ColorPickerPreference mNavbarButtonTint;
	


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.screwd_navbar_settings);
		
		PreferenceScreen prefSet = getPreferenceScreen();
		
		mNavigationBarHeight =
            (ListPreference) findPreference(PREF_NAVIGATION_BAR_HEIGHT);
        mNavigationBarHeight.setOnPreferenceChangeListener(this);

        mNavigationBarHeightLandscape =
            (ListPreference) findPreference(PREF_NAVIGATION_BAR_HEIGHT_LANDSCAPE);

        if (ScreenType.isPhone(getActivity())) {
            prefSet.removePreference(mNavigationBarHeightLandscape);
            mNavigationBarHeightLandscape = null;
        } else {
            mNavigationBarHeightLandscape.setOnPreferenceChangeListener(this);
        }

        mNavigationBarWidth =
            (ListPreference) findPreference(PREF_NAVIGATION_BAR_WIDTH);

        if (!ScreenType.isPhone(getActivity())) {
            prefSet.removePreference(mNavigationBarWidth);
            mNavigationBarWidth = null;
        } else {
            mNavigationBarWidth.setOnPreferenceChangeListener(this);
        }

        updateDimensionValues();
        setHasOptionsMenu(true);
		

    }
	
	private void updateDimensionValues() {
        int navigationBarHeight = Settings.System.getInt(getContentResolver(),
                Settings.System.NAVIGATION_BAR_HEIGHT, -1);
        if (navigationBarHeight == -1) {
            navigationBarHeight = (int) (getResources().getDimension(
                    com.android.internal.R.dimen.navigation_bar_height)
                    / getResources().getDisplayMetrics().density);
        }
        mNavigationBarHeight.setValue(String.valueOf(navigationBarHeight));
        mNavigationBarHeight.setSummary(mNavigationBarHeight.getEntry());

        if (mNavigationBarHeightLandscape != null) {
            int navigationBarHeightLandscape = Settings.System.getInt(getContentResolver(),
                                Settings.System.NAVIGATION_BAR_HEIGHT_LANDSCAPE, -1);
            if (navigationBarHeightLandscape == -1) {
                navigationBarHeightLandscape = (int) (getResources().getDimension(
                        com.android.internal.R.dimen.navigation_bar_height_landscape)
                        / getResources().getDisplayMetrics().density);
            }
            mNavigationBarHeightLandscape.setValue(String.valueOf(navigationBarHeightLandscape));
            mNavigationBarHeightLandscape.setSummary(mNavigationBarHeightLandscape.getEntry());
        }

        if (mNavigationBarWidth != null) {
            int navigationBarWidth = Settings.System.getInt(getContentResolver(),
                                Settings.System.NAVIGATION_BAR_WIDTH, -1);
            if (navigationBarWidth == -1) {
                navigationBarWidth = (int) (getResources().getDimension(
                        com.android.internal.R.dimen.navigation_bar_width)
                        / getResources().getDisplayMetrics().density);
            }
            mNavigationBarWidth.setValue(String.valueOf(navigationBarWidth));
            mNavigationBarWidth.setSummary(mNavigationBarWidth.getEntry());
        }
		
		mNavbarButtonTint = (ColorPickerPreference) findPreference(NAVIGATION_BAR_TINT);
        mNavbarButtonTint.setOnPreferenceChangeListener(this);
        int intColor = Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.NAVIGATION_BAR_TINT, 0xffffffff);
        String hexColor = String.format("#%08x", (0xffffffff & intColor));
        mNavbarButtonTint.setSummary(hexColor);
        mNavbarButtonTint.setNewPreviewColor(intColor);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mNavigationBarWidth) {
            int index = mNavigationBarWidth.findIndexOfValue((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.NAVIGATION_BAR_WIDTH, Integer.parseInt((String) newValue));
            updateDimensionValues();
            return true;
        } else if (preference == mNavigationBarHeight) {
            int index = mNavigationBarHeight.findIndexOfValue((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.NAVIGATION_BAR_HEIGHT, Integer.parseInt((String) newValue));
            updateDimensionValues();
            return true;
        } else if (preference == mNavigationBarHeightLandscape) {
            int index = mNavigationBarHeightLandscape.findIndexOfValue((String) newValue);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.NAVIGATION_BAR_HEIGHT_LANDSCAPE, Integer.parseInt((String) newValue));
            updateDimensionValues();
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
	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, MENU_RESET, 0, R.string.menu_restore)
                .setIcon(R.drawable.ic_menu_reset)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_RESET:
                showDialogInner(DLG_RESET);
                return true;
             default:
                return super.onContextItemSelected(item);
        }
    }

    private void showDialogInner(int id) {
        DialogFragment newFragment = MyAlertDialogFragment.newInstance(id);
        newFragment.setTargetFragment(this, 0);
        newFragment.show(getFragmentManager(), "dialog " + id);
    }

    public static class MyAlertDialogFragment extends DialogFragment {

        public static MyAlertDialogFragment newInstance(int id) {
            MyAlertDialogFragment frag = new MyAlertDialogFragment();
            Bundle args = new Bundle();
            args.putInt("id", id);
            frag.setArguments(args);
            return frag;
        }

        NavBar getOwner() {
            return (NavBar) getTargetFragment();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int id = getArguments().getInt("id");
            switch (id) {
                case DLG_RESET:
                    return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.menu_restore)
                    .setMessage(R.string.navigation_bar_dimensions_reset_message)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.dlg_ok,
                        new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Settings.System.putInt(getActivity().getContentResolver(),
                                    Settings.System.NAVIGATION_BAR_HEIGHT, -2);
                            Settings.System.putInt(getActivity().getContentResolver(),
                                    Settings.System.NAVIGATION_BAR_HEIGHT_LANDSCAPE, -2);
                            Settings.System.putInt(getActivity().getContentResolver(),
                                    Settings.System.NAVIGATION_BAR_WIDTH, -2);
                            getOwner().updateDimensionValues();
                        }
                    })
                    .create();
            }
            throw new IllegalArgumentException("unknown id " + id);
        }

        @Override
        public void onCancel(DialogInterface dialog) {

        }
    }
}
