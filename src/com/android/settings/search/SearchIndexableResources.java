/*
 * Copyright (C) 2014 The Android Open Source Project
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

package com.android.settings.search;

import android.provider.SearchIndexableResource;

import com.android.settings.DataUsageSummary;
import com.android.settings.DateTimeSettings;
import com.android.settings.DevelopmentSettings;
import com.android.settings.DeviceInfoSettings;
import com.android.settings.DisplaySettings;
import com.android.settings.HomeSettings;
import com.android.settings.ScreenPinningSettings;
import com.android.settings.PrivacySettings;
import com.android.settings.R;
import com.android.settings.SecuritySettings;
import com.android.settings.WallpaperTypeSettings;
import com.android.settings.WirelessSettings;
import com.android.settings.accessibility.AccessibilitySettings;
import com.android.settings.bluetooth.BluetoothSettings;
import com.android.settings.deviceinfo.Memory;
import com.android.settings.deviceinfo.UsbSettings;
import com.android.settings.fuelgauge.BatterySaverSettings;
import com.android.settings.fuelgauge.PowerUsageSummary;
import com.android.settings.inputmethod.InputMethodAndLanguageSettings;
import com.android.settings.location.LocationSettings;
import com.android.settings.net.DataUsageMeteredSettings;
import com.android.settings.notification.NotificationSettings;
import com.android.settings.notification.OtherSoundSettings;
import com.android.settings.notification.ZenModeSettings;
import com.android.settings.print.PrintSettingsFragment;
import com.android.settings.sim.SimSettings;
import com.android.settings.screwd.AmbientSettings;
import com.android.settings.screwd.AnimationControls;
import com.android.settings.screwd.AppCircleBar;
import com.android.settings.screwd.AppSidebar;
import com.android.settings.screwd.CarrierLabel;
import com.android.settings.screwd.HeadsUpSettings;
import com.android.settings.screwd.InterfaceSettings;
import com.android.settings.screwd.KeyboardAnimationInterfaceSettings;
import com.android.settings.screwd.LegacyMenuSettings;
import com.android.settings.screwd.LockScreenColorSettings;
import com.android.settings.screwd.LockScreenWeatherSettings;
import com.android.settings.screwd.Misc;
import com.android.settings.screwd.NavBarButtonStyle;
import com.android.settings.screwd.NavbarSettings;
import com.android.settings.screwd.NetworkTraffic;
import com.android.settings.screwd.NotificationColorSettings;
import com.android.settings.screwd.Notifications;
import com.android.settings.screwd.OmniSwitch;
import com.android.settings.screwd.OverscrollEffects;
import com.android.settings.screwd.PieButtonStyleSettings;
import com.android.settings.screwd.PieControl;
import com.android.settings.screwd.PieStyleSettings;
import com.android.settings.screwd.PieTriggerSettings;
import com.android.settings.screwd.PowerMenu;
import com.android.settings.screwd.QSColors;
import com.android.settings.screwd.QsSettings;
import com.android.settings.screwd.RecentPanel;
import com.android.settings.screwd.Recents;
import com.android.settings.screwd.ScrewdSettings;
import com.android.settings.screwd.SoundSettings;
import com.android.settings.screwd.StatusBar;
import com.android.settings.screwd.StatusBarWeather;
import com.android.settings.screwd.System;
import com.android.settings.screwd.VolumeSteps;
import com.android.settings.users.UserSettings;
import com.android.settings.voice.VoiceInputSettings;
import com.android.settings.wifi.AdvancedWifiSettings;
import com.android.settings.wifi.SavedAccessPointsWifiSettings;
import com.android.settings.wifi.WifiSettings;
import com.android.settings.WifiCallingSettings;

import java.util.Collection;
import java.util.HashMap;

public final class SearchIndexableResources {

    public static int NO_DATA_RES_ID = 0;

    private static HashMap<String, SearchIndexableResource> sResMap =
            new HashMap<String, SearchIndexableResource>();

    static {
        sResMap.put(WifiSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(WifiSettings.class.getName()),
                        NO_DATA_RES_ID,
                        WifiSettings.class.getName(),
                        R.drawable.ic_settings_wireless));

        sResMap.put(AdvancedWifiSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(AdvancedWifiSettings.class.getName()),
                        R.xml.wifi_advanced_settings,
                        AdvancedWifiSettings.class.getName(),
                        R.drawable.ic_settings_wireless));

        sResMap.put(SavedAccessPointsWifiSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(SavedAccessPointsWifiSettings.class.getName()),
                        R.xml.wifi_display_saved_access_points,
                        SavedAccessPointsWifiSettings.class.getName(),
                        R.drawable.ic_settings_wireless));

        sResMap.put(BluetoothSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(BluetoothSettings.class.getName()),
                        NO_DATA_RES_ID,
                        BluetoothSettings.class.getName(),
                        R.drawable.ic_settings_bluetooth2));

        sResMap.put(SimSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(SimSettings.class.getName()),
                        NO_DATA_RES_ID,
                        SimSettings.class.getName(),
                        R.drawable.ic_sim_sd));

        sResMap.put(DataUsageSummary.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(DataUsageSummary.class.getName()),
                        NO_DATA_RES_ID,
                        DataUsageSummary.class.getName(),
                        R.drawable.ic_settings_data_usage));

        sResMap.put(DataUsageMeteredSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(DataUsageMeteredSettings.class.getName()),
                        NO_DATA_RES_ID,
                        DataUsageMeteredSettings.class.getName(),
                        R.drawable.ic_settings_data_usage));

        sResMap.put(WirelessSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(WirelessSettings.class.getName()),
                        NO_DATA_RES_ID,
                        WirelessSettings.class.getName(),
                        R.drawable.ic_settings_more));

        sResMap.put(HomeSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(HomeSettings.class.getName()),
                        NO_DATA_RES_ID,
                        HomeSettings.class.getName(),
                        R.drawable.ic_settings_home));

        sResMap.put(DisplaySettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(DisplaySettings.class.getName()),
                        NO_DATA_RES_ID,
                        DisplaySettings.class.getName(),
                        R.drawable.ic_settings_display));

        sResMap.put(WallpaperTypeSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(WallpaperTypeSettings.class.getName()),
                        NO_DATA_RES_ID,
                        WallpaperTypeSettings.class.getName(),
                        R.drawable.ic_settings_display));

        sResMap.put(NotificationSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(NotificationSettings.class.getName()),
                        NO_DATA_RES_ID,
                        NotificationSettings.class.getName(),
                        R.drawable.ic_settings_notifications));

        sResMap.put(OtherSoundSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(OtherSoundSettings.class.getName()),
                        NO_DATA_RES_ID,
                        OtherSoundSettings.class.getName(),
                        R.drawable.ic_settings_notifications));

        sResMap.put(ZenModeSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(ZenModeSettings.class.getName()),
                        NO_DATA_RES_ID,
                        ZenModeSettings.class.getName(),
                        R.drawable.ic_settings_notifications));

        sResMap.put(Memory.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(Memory.class.getName()),
                        NO_DATA_RES_ID,
                        Memory.class.getName(),
                        R.drawable.ic_settings_storage));

        sResMap.put(UsbSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(UsbSettings.class.getName()),
                        R.xml.usb_settings,
                        UsbSettings.class.getName(),
                        R.drawable.ic_settings_storage));

        sResMap.put(PowerUsageSummary.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(PowerUsageSummary.class.getName()),
                        R.xml.power_usage_summary,
                        PowerUsageSummary.class.getName(),
                        R.drawable.ic_settings_battery));

        sResMap.put(BatterySaverSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(BatterySaverSettings.class.getName()),
                        R.xml.battery_saver_settings,
                        BatterySaverSettings.class.getName(),
                        R.drawable.ic_settings_battery));

        sResMap.put(UserSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(UserSettings.class.getName()),
                        R.xml.user_settings,
                        UserSettings.class.getName(),
                        R.drawable.ic_settings_multiuser));

        sResMap.put(LocationSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(LocationSettings.class.getName()),
                        R.xml.location_settings,
                        LocationSettings.class.getName(),
                        R.drawable.ic_settings_location));

        sResMap.put(SecuritySettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(SecuritySettings.class.getName()),
                        NO_DATA_RES_ID,
                        SecuritySettings.class.getName(),
                        R.drawable.ic_settings_security));

        sResMap.put(ScreenPinningSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(ScreenPinningSettings.class.getName()),
                        NO_DATA_RES_ID,
                        ScreenPinningSettings.class.getName(),
                        R.drawable.ic_settings_security));

        sResMap.put(InputMethodAndLanguageSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(InputMethodAndLanguageSettings.class.getName()),
                        NO_DATA_RES_ID,
                        InputMethodAndLanguageSettings.class.getName(),
                        R.drawable.ic_settings_language));

        sResMap.put(VoiceInputSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(VoiceInputSettings.class.getName()),
                        NO_DATA_RES_ID,
                        VoiceInputSettings.class.getName(),
                        R.drawable.ic_settings_language));

        sResMap.put(PrivacySettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(PrivacySettings.class.getName()),
                        NO_DATA_RES_ID,
                        PrivacySettings.class.getName(),
                        R.drawable.ic_settings_backup));

        sResMap.put(DateTimeSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(DateTimeSettings.class.getName()),
                        R.xml.date_time_prefs,
                        DateTimeSettings.class.getName(),
                        R.drawable.ic_settings_date_time));

        sResMap.put(AccessibilitySettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(AccessibilitySettings.class.getName()),
                        NO_DATA_RES_ID,
                        AccessibilitySettings.class.getName(),
                        R.drawable.ic_settings_accessibility));

        sResMap.put(PrintSettingsFragment.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(PrintSettingsFragment.class.getName()),
                        NO_DATA_RES_ID,
                        PrintSettingsFragment.class.getName(),
                        R.drawable.ic_settings_print));

        sResMap.put(DevelopmentSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(DevelopmentSettings.class.getName()),
                        NO_DATA_RES_ID,
                        DevelopmentSettings.class.getName(),
                        R.drawable.ic_settings_development));

        sResMap.put(DeviceInfoSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(DeviceInfoSettings.class.getName()),
                        NO_DATA_RES_ID,
                        DeviceInfoSettings.class.getName(),
                        R.drawable.ic_settings_about));
						
		sResMap.put(AmbientSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(AmbientSettings.class.getName()),
                        NO_DATA_RES_ID,
                        AmbientSettings.class.getName(),
                        R.drawable.ic_settings_display));
////////////////////////////////////
        sResMap.put(AnimationControls.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(AnimationControls.class.getName()),
                        NO_DATA_RES_ID,
                        AnimationControls.class.getName(),
                        R.drawable.ic_screwd_settings));

        sResMap.put(AppCircleBar.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(AppCircleBar.class.getName()),
                        NO_DATA_RES_ID,
                        AppCircleBar.class.getName(),
                        R.drawable.ic_screwd_settings));

        sResMap.put(AppSidebar.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(AppSidebar.class.getName()),
                        NO_DATA_RES_ID,
                        AppSidebar.class.getName(),
                        R.drawable.ic_screwd_settings));

        sResMap.put(CarrierLabel.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(CarrierLabel.class.getName()),
                        NO_DATA_RES_ID,
                        CarrierLabel.class.getName(),
                        R.drawable.ic_screwd_settings));

        sResMap.put(HeadsUpSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(HeadsUpSettings.class.getName()),
                        NO_DATA_RES_ID,
                        HeadsUpSettings.class.getName(),
                        R.drawable.ic_screwd_settings));

        sResMap.put(InterfaceSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(InterfaceSettings.class.getName()),
                        NO_DATA_RES_ID,
                        InterfaceSettings.class.getName(),
                        R.drawable.ic_screwd_settings));

        sResMap.put(KeyboardAnimationInterfaceSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(KeyboardAnimationInterfaceSettings.class.getName()),
                        NO_DATA_RES_ID,
                        KeyboardAnimationInterfaceSettings.class.getName(),
                        R.drawable.ic_screwd_settings));

        sResMap.put(LegacyMenuSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(LegacyMenuSettings.class.getName()),
                        NO_DATA_RES_ID,
                        LegacyMenuSettings.class.getName(),
                        R.drawable.ic_screwd_settings));

        sResMap.put(LockScreenColorSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(LockScreenColorSettings.class.getName()),
                        NO_DATA_RES_ID,
                        LockScreenColorSettings.class.getName(),
                        R.drawable.ic_screwd_settings));

        sResMap.put(LockScreenWeatherSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(LockScreenWeatherSettings.class.getName()),
                        NO_DATA_RES_ID,
                        LockScreenWeatherSettings.class.getName(),
                        R.drawable.ic_screwd_settings));

        sResMap.put(Misc.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(Misc.class.getName()),
                        NO_DATA_RES_ID,
                        Misc.class.getName(),
                        R.drawable.ic_screwd_settings));

        sResMap.put(NavBarButtonStyle.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(NavBarButtonStyle.class.getName()),
                        NO_DATA_RES_ID,
                        NavBarButtonStyle.class.getName(),
                        R.drawable.ic_screwd_settings));

        sResMap.put(NavbarSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(NavbarSettings.class.getName()),
                        NO_DATA_RES_ID,
                        NavbarSettings.class.getName(),
                        R.drawable.ic_screwd_settings));

        sResMap.put(NetworkTraffic.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(NetworkTraffic.class.getName()),
                        NO_DATA_RES_ID,
                        NetworkTraffic.class.getName(),
                        R.drawable.ic_screwd_settings));

        sResMap.put(NotificationColorSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(NotificationColorSettings.class.getName()),
                        NO_DATA_RES_ID,
                        NotificationColorSettings.class.getName(),
                        R.drawable.ic_screwd_settings));
						
		sResMap.put(Notifications.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(Notifications.class.getName()),
                        NO_DATA_RES_ID,
                        Notifications.class.getName(),
                        R.drawable.ic_screwd_settings));
						
		sResMap.put(OmniSwitch.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(OmniSwitch.class.getName()),
                        NO_DATA_RES_ID,
                        OmniSwitch.class.getName(),
                        R.drawable.ic_screwd_settings));
						
		sResMap.put(OverscrollEffects.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(OverscrollEffects.class.getName()),
                        NO_DATA_RES_ID,
                        OverscrollEffects.class.getName(),
                        R.drawable.ic_screwd_settings));				
										
		
		sResMap.put(PieButtonStyleSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(PieButtonStyleSettings.class.getName()),
                        NO_DATA_RES_ID,
                        PieButtonStyleSettings.class.getName(),
                        R.drawable.ic_screwd_settings));

        sResMap.put(PieControl.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(PieControl.class.getName()),
                        NO_DATA_RES_ID,
                        PieControl.class.getName(),
                        R.drawable.ic_screwd_settings));

        sResMap.put(PieStyleSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(PieStyleSettings.class.getName()),
                        NO_DATA_RES_ID,
                        PieStyleSettings.class.getName(),
                        R.drawable.ic_screwd_settings));

        sResMap.put(PieTriggerSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(PieTriggerSettings.class.getName()),
                        NO_DATA_RES_ID,
                        PieTriggerSettings.class.getName(),
                        R.drawable.ic_screwd_settings));

        sResMap.put(QSColors.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(QSColors.class.getName()),
                        NO_DATA_RES_ID,
                        QSColors.class.getName(),
                        R.drawable.ic_screwd_settings));

        sResMap.put(QsSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(QsSettings.class.getName()),
                        NO_DATA_RES_ID,
                        QsSettings.class.getName(),
                        R.drawable.ic_screwd_settings));
		
		sResMap.put(RecentPanel.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(RecentPanel.class.getName()),
                        NO_DATA_RES_ID,
                        RecentPanel.class.getName(),
                        R.drawable.ic_screwd_settings));

        sResMap.put(Recents.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(Recents.class.getName()),
                        NO_DATA_RES_ID,
                        Recents.class.getName(),
                        R.drawable.ic_screwd_settings));
		
		sResMap.put(ScrewdSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(ScrewdSettings.class.getName()),
                        NO_DATA_RES_ID,
                        ScrewdSettings.class.getName(),
                        R.drawable.ic_screwd_settings));
		
		sResMap.put(SoundSettings.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(SoundSettings.class.getName()),
                        NO_DATA_RES_ID,
                        SoundSettings.class.getName(),
                        R.drawable.ic_screwd_settings));
		
		sResMap.put(StatusBar.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(StatusBar.class.getName()),
                        NO_DATA_RES_ID,
                        StatusBar.class.getName(),
                        R.drawable.ic_screwd_settings));
		
		sResMap.put(StatusBarWeather.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(StatusBarWeather.class.getName()),
                        NO_DATA_RES_ID,
                        StatusBarWeather.class.getName(),
                        R.drawable.ic_screwd_settings));

        sResMap.put(System.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(System.class.getName()),
                        NO_DATA_RES_ID,
                        System.class.getName(),
                        R.drawable.ic_screwd_settings));

        sResMap.put(VolumeSteps.class.getName(),
                new SearchIndexableResource(
                        Ranking.getRankForClassName(VolumeSteps.class.getName()),
                        NO_DATA_RES_ID,
                        VolumeSteps.class.getName(),
                        R.drawable.ic_screwd_settings));
    }

    private SearchIndexableResources() {
    }

    public static int size() {
        return sResMap.size();
    }

    public static SearchIndexableResource getResourceByName(String className) {
        return sResMap.get(className);
    }

    public static Collection<SearchIndexableResource> values() {
        return sResMap.values();
    }
}
