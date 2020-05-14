/*
 * WiFiAnalyzer
 * Copyright (C) 2015 - 2020 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.vrem.wifianalyzer;

import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

import com.vrem.wifianalyzer.ActivityUtils.WiFiBandToggle;
import com.vrem.wifianalyzer.navigation.NavigationMenu;
import com.vrem.wifianalyzer.settings.Settings;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ActivityUtilsTest {

    @Mock
    private Window window;
    @Mock
    private ActionBar actionBar;
    @Mock
    private Toolbar toolbar;
    @Mock
    private Intent intent;

    private MainActivity mainActivity;
    private Settings settings;

    private ActivityUtils fixture;

    @Before
    public void setUp() {
        mainActivity = MainContextHelper.INSTANCE.getMainActivity();
        settings = MainContextHelper.INSTANCE.getSettings();
        fixture = Mockito.spy(new ActivityUtils());
    }

    @After
    public void tearDown() {
        MainContextHelper.INSTANCE.restore();
        verifyNoMoreInteractions(mainActivity);
        verifyNoMoreInteractions(toolbar);
        verifyNoMoreInteractions(actionBar);
        verifyNoMoreInteractions(window);
        verifyNoMoreInteractions(settings);
        verifyNoMoreInteractions(intent);
    }

    @Test
    public void testSetActionBarOptions() {
        // execute
        fixture.setActionBarOptions(actionBar);
        // validate
        verify(actionBar).setHomeButtonEnabled(true);
        verify(actionBar).setDisplayHomeAsUpEnabled(true);
    }

    @Test
    public void testSetActionBarOptionsWithNullActionBar() {
        // execute
        fixture.setActionBarOptions(null);
        // validate
        verify(actionBar, never()).setHomeButtonEnabled(true);
        verify(actionBar, never()).setDisplayHomeAsUpEnabled(true);
    }

    @Test
    public void testSetupToolbar() {
        // setup
        when(mainActivity.findViewById(R.id.toolbar)).thenReturn(toolbar);
        when(mainActivity.getSupportActionBar()).thenReturn(actionBar);
        // execute
        Toolbar actual = fixture.setupToolbar();
        // validate
        assertEquals(toolbar, actual);

        verify(mainActivity).findViewById(R.id.toolbar);
        verify(mainActivity).getSupportActionBar();

        verify(toolbar).setOnClickListener(any(WiFiBandToggle.class));
        verify(mainActivity).setSupportActionBar(toolbar);
        verify(actionBar).setHomeButtonEnabled(true);
        verify(actionBar).setDisplayHomeAsUpEnabled(true);
    }

    @Test
    public void testWiFiBandToggleOnClickToggles() {
        // setup
        when(mainActivity.currentNavigationMenu()).thenReturn(NavigationMenu.CHANNEL_GRAPH);
        WiFiBandToggle wiFiBandToggle = new WiFiBandToggle(mainActivity);
        // execute
        wiFiBandToggle.onClick(null);
        // validate
        verify(settings).toggleWiFiBand();
        verify(mainActivity).currentNavigationMenu();
    }

    @Test
    public void testWiFiBandToggleOnClickDoesNotToggles() {
        // setup
        when(mainActivity.currentNavigationMenu()).thenReturn(NavigationMenu.ACCESS_POINTS);
        WiFiBandToggle wiFiBandToggle = new WiFiBandToggle(mainActivity);
        // execute
        wiFiBandToggle.onClick(null);
        // validate
        verify(settings, never()).toggleWiFiBand();
        verify(mainActivity).currentNavigationMenu();
    }

    @Test
    public void testKeepScreenOnSwitchOn() {
        // setup
        when(settings.keepScreenOn()).thenReturn(true);
        when(mainActivity.getWindow()).thenReturn(window);
        // execute
        fixture.keepScreenOn();
        // validate
        verify(settings).keepScreenOn();
        verify(mainActivity).getWindow();
        verify(window).addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Test
    public void testKeepScreenOnSwitchOff() {
        // setup
        when(settings.keepScreenOn()).thenReturn(false);
        when(mainActivity.getWindow()).thenReturn(window);
        // execute
        fixture.keepScreenOn();
        // validate
        verify(settings).keepScreenOn();
        verify(mainActivity).getWindow();
        verify(window).clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Test
    public void testStartWiFiSettings() {
        // setup
        when(fixture.makeIntent(android.provider.Settings.Panel.ACTION_WIFI)).thenReturn(intent);
        // execute
        fixture.startWiFiSettings();
        // validate
        verify(mainActivity).startActivityForResult(intent, 0);
        verify(fixture).makeIntent(android.provider.Settings.Panel.ACTION_WIFI);
    }

    @Test
    public void testStartLocationSettings() {
        // setup
        when(fixture.makeIntent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)).thenReturn(intent);
        // execute
        fixture.startLocationSettings();
        // validate
        verify(mainActivity).startActivity(intent);
        verify(fixture).makeIntent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    }

}