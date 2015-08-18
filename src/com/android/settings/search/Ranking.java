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

import com.android.settings.ChooseLockGeneric;
import com.android.settings.DataUsageSummary;
import com.android.settings.DateTimeSettings;
import com.android.settings.DevelopmentSettings;
import com.android.settings.DeviceInfoSettings;
import com.android.settings.DisplaySettings;
import com.android.settings.HomeSettings;
import com.android.settings.ScreenPinningSettings;
import com.android.settings.PrivacySettings;
import com.android.settings.SecuritySettings;
import com.android.settings.WallpaperTypeSettings;
import com.android.settings.WirelessSettings;
import com.android.settings.accessibility.AccessibilitySettings;
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
import com.android.settings.users.UserSettings;
import com.android.settings.voice.VoiceInputSettings;
import com.android.settings.wifi.AdvancedWifiSettings;
import com.android.settings.wifi.SavedAccessPointsWifiSettings;
import com.android.settings.wifi.WifiSettings;
import com.android.settings.WifiCallingSettings;

import java.util.HashMap;

/**
 * Utility class for dealing with Search Ranking.
 */
public final class Ranking {

    public static final int RANK_WIFI = 1;
    public static final int RANK_BT = 2;
    public static final int RANK_SIM = 3;
    public static final int RANK_DATA_USAGE = 4;
    public static final int RANK_WIRELESS = 5;
    public static final int RANK_HOME = 6;
    public static final int RANK_DISPLAY = 7;
	public static final int RANK_ANIMATIONS = 8;
	public static final int RANK_APPCIRCLEBAR = 9;
	public static final int RANK_APPSIDEBAR = 10;
	public static final int RANK_CARRIERLABEL = 11;
	public static final int RANK_HEADSUP = 12;
	public static final int RANK_INTERFACE = 13;
	public static final int RANK_SCREWDSETTINGS = 14;
	public static final int RANK_KEYBOARDANI = 15;
	public static final int RANK_LEGACYMENU = 16;
	public static final int RANK_LOCKCOLORS = 17;
	public static final int RANK_LOCKWEATHER = 18;
	public static final int RANK_MISC = 19;
	public static final int RANK_NAVBARBUTTON = 20;
	public static final int RANK_NAVBARSETTINGS = 21;
	public static final int RANK_NETTRAFFIC = 22;
	public static final int RANK_NOTCOLOR = 23;
	public static final int RANK_NOTS = 24;
	public static final int RANK_OMNISWITCH = 25;
	public static final int RANK_OVERSCROLL = 26;
	public static final int RANK_PIESTYLE = 27;
    public static final int RANK_PIECONTROL = 28;
    public static final int RANK_PIEBUTTON = 29;
    public static final int RANK_PIETRIGGER = 29;
	public static final int RANK_POWERMENU = 30;
	public static final int RANK_QSCOLOR = 31;
	public static final int RANK_QSSETTINGS = 32;
	public static final int RANK_RECENTPAN = 33;
	public static final int RANK_RECENTS = 34;
	
	public static final int RANK_SOUNDSETT = 35;
	public static final int RANK_STATUSBAR = 36;
	public static final int RANK_STATUSWEATHER = 37;
	public static final int RANK_SYSTEM= 38;
	public static final int RANK_VOLUMESTEPS = 39;
    public static final int RANK_WALLPAPER = 40;
    public static final int RANK_NOTIFICATIONS = 41;
    public static final int RANK_STORAGE = 42;
    public static final int RANK_POWER_USAGE = 43;
    public static final int RANK_USERS = 44;
    public static final int RANK_LOCATION = 45;
    public static final int RANK_SECURITY = 46;
    public static final int RANK_IME = 47;
    public static final int RANK_PRIVACY = 48;
    public static final int RANK_DATE_TIME = 49;
    public static final int RANK_ACCESSIBILITY = 50;
    public static final int RANK_PRINTING = 51;
    public static final int RANK_DEVELOPEMENT = 52;
    public static final int RANK_DEVICE_INFO = 53;
	

    public static final int RANK_UNDEFINED = -1;
    public static final int RANK_OTHERS = 1024;
    public static final int BASE_RANK_DEFAULT = 2048;

    public static int sCurrentBaseRank = BASE_RANK_DEFAULT;

    private static HashMap<String, Integer> sRankMap = new HashMap<String, Integer>();
    private static HashMap<String, Integer> sBaseRankMap = new HashMap<String, Integer>();

    static {
        // Wi-Fi
        sRankMap.put(WifiSettings.class.getName(), RANK_WIFI);
        sRankMap.put(AdvancedWifiSettings.class.getName(), RANK_WIFI);
        sRankMap.put(SavedAccessPointsWifiSettings.class.getName(), RANK_WIFI);

        // BT
        sRankMap.put(BluetoothSettings.class.getName(), RANK_BT);

        // SIM Cards
        sRankMap.put(SimSettings.class.getName(), RANK_SIM);

        // DataUsage
        sRankMap.put(DataUsageSummary.class.getName(), RANK_DATA_USAGE);
        sRankMap.put(DataUsageMeteredSettings.class.getName(), RANK_DATA_USAGE);

        // Other wireless settinfs
        sRankMap.put(WirelessSettings.class.getName(), RANK_WIRELESS);
		//Animations
        sRankMap.put(AnimationControls.class.getName(), RANK_ANIMATIONS);

        // App CircleBar
        sRankMap.put(AppCircleBar.class.getName(), RANK_APPCIRCLEBAR);

        // App CircleBar
        sRankMap.put(AppSidebar.class.getName(), RANK_APPSIDEBAR);

        // Carrier Label
        sRankMap.put(CarrierLabel.class.getName(), RANK_CARRIERLABEL);

        // Heads Up
        sRankMap.put(HeadsUpSettings.class.getName(), RANK_HEADSUP);

        // Screw'd Interface
        sRankMap.put(InterfaceSettings.class.getName(), RANK_INTERFACE);

        // Keyboard settings
        sRankMap.put(KeyboardAnimationInterfaceSettings.class.getName(), RANK_KEYBOARDANI);

        // Legacy Menu
        sRankMap.put(LegacyMenuSettings.class.getName(), RANK_LEGACYMENU);

        // Lockscreen Color Settings
        sRankMap.put(LockScreenColorSettings.class.getName(), RANK_LOCKCOLORS);

        // Lockscreen Weather
        sRankMap.put(LockScreenWeatherSettings.class.getName(), RANK_LOCKWEATHER);

        // Screw'd Misc
        sRankMap.put(Misc.class.getName(), RANK_MISC);

        // Navbar Button style
        sRankMap.put(NavBarButtonStyle.class.getName(), RANK_NAVBARBUTTON);

        // Navbar Settings
        sRankMap.put(NavbarSettings.class.getName(), RANK_NAVBARSETTINGS);
		
		// Network traffic
        sRankMap.put(NetworkTraffic.class.getName(), RANK_NETTRAFFIC);
		
		// Notification colors
        sRankMap.put(NotificationColorSettings.class.getName(), RANK_NOTCOLOR);
		
		// Navbar Settings
        sRankMap.put(Notifications.class.getName(), RANK_NOTS);
		
		// Omniswitch
        sRankMap.put(OmniSwitch.class.getName(), RANK_OMNISWITCH);
		
		// Omniswitch
        sRankMap.put(OverscrollEffects.class.getName(), RANK_OVERSCROLL);

        // PIE Button Style
        sRankMap.put(PieButtonStyleSettings.class.getName(), RANK_PIEBUTTON);

        // PIE Control
        sRankMap.put(PieControl.class.getName(), RANK_PIECONTROL);

        // PIE Style
        sRankMap.put(PieStyleSettings.class.getName(), RANK_PIESTYLE);

        // PIE Trigger
        sRankMap.put(PieStyleSettings.class.getName(), RANK_PIETRIGGER);

        // QS Colors
        sRankMap.put(QSColors.class.getName(), RANK_QSCOLOR);
		
		// QS Settings
        sRankMap.put(QsSettings.class.getName(), RANK_QSSETTINGS);

        // Recents Panel
        sRankMap.put(RecentPanel.class.getName(), RANK_RECENTPAN);
		
		// Screw'd Recents
        sRankMap.put(Recents.class.getName(), RANK_RECENTS);

        // Statusbar Weather
        sRankMap.put(StatusBarWeather.class.getName(), RANK_STATUSWEATHER);

        // Screw'd Settings
        sRankMap.put(ScrewdSettings.class.getName(), RANK_SCREWDSETTINGS);

        // Volume Steps
        sRankMap.put(VolumeSteps.class.getName(), RANK_VOLUMESTEPS);

        // StatusBar
        sRankMap.put(StatusBar.class.getName(), RANK_STATUSBAR);
		
		// System
        sRankMap.put(System.class.getName(), RANK_SYSTEM);

        // Screwd Sound
        sRankMap.put(SoundSettings.class.getName(), RANK_SOUNDSETT);

        // Home
        sRankMap.put(HomeSettings.class.getName(), RANK_HOME);

        // Display
        sRankMap.put(DisplaySettings.class.getName(), RANK_DISPLAY);

        // Wallpapers
        sRankMap.put(WallpaperTypeSettings.class.getName(), RANK_WALLPAPER);

        // Notifications
        sRankMap.put(NotificationSettings.class.getName(), RANK_NOTIFICATIONS);
        sRankMap.put(OtherSoundSettings.class.getName(), RANK_NOTIFICATIONS);
        sRankMap.put(ZenModeSettings.class.getName(), RANK_NOTIFICATIONS);

        // Storage
        sRankMap.put(Memory.class.getName(), RANK_STORAGE);
        sRankMap.put(UsbSettings.class.getName(), RANK_STORAGE);

        // Battery
        sRankMap.put(PowerUsageSummary.class.getName(), RANK_POWER_USAGE);
        sRankMap.put(BatterySaverSettings.class.getName(), RANK_POWER_USAGE);

        // Users
        sRankMap.put(UserSettings.class.getName(), RANK_USERS);

        // Location
        sRankMap.put(LocationSettings.class.getName(), RANK_LOCATION);

        // Security
        sRankMap.put(SecuritySettings.class.getName(), RANK_SECURITY);
        sRankMap.put(ChooseLockGeneric.ChooseLockGenericFragment.class.getName(), RANK_SECURITY);
        sRankMap.put(ScreenPinningSettings.class.getName(), RANK_SECURITY);

        // IMEs
        sRankMap.put(InputMethodAndLanguageSettings.class.getName(), RANK_IME);
        sRankMap.put(VoiceInputSettings.class.getName(), RANK_IME);

        // Privacy
        sRankMap.put(PrivacySettings.class.getName(), RANK_PRIVACY);

        // Date / Time
        sRankMap.put(DateTimeSettings.class.getName(), RANK_DATE_TIME);

        // Accessibility
        sRankMap.put(AccessibilitySettings.class.getName(), RANK_ACCESSIBILITY);

        // Print
        sRankMap.put(PrintSettingsFragment.class.getName(), RANK_PRINTING);

        // Development
        sRankMap.put(DevelopmentSettings.class.getName(), RANK_DEVELOPEMENT);

        // Device infos
        sRankMap.put(DeviceInfoSettings.class.getName(), RANK_DEVICE_INFO);

        sBaseRankMap.put("com.android.settings", 0);
    }

    public static int getRankForClassName(String className) {
        Integer rank = sRankMap.get(className);
        return (rank != null) ? (int) rank: RANK_OTHERS;
    }

    public static int getBaseRankForAuthority(String authority) {
        synchronized (sBaseRankMap) {
            Integer base = sBaseRankMap.get(authority);
            if (base != null) {
                return base;
            }
            sCurrentBaseRank++;
            sBaseRankMap.put(authority, sCurrentBaseRank);
            return sCurrentBaseRank;
        }
    }
}
