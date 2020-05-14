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
package com.vrem.wifianalyzer.navigation.availability

import com.vrem.wifianalyzer.MainActivity
import com.vrem.wifianalyzer.R
import org.apache.commons.lang3.StringUtils

internal class WiFiSwitchOff : NavigationOption {
    override fun apply(mainActivity: MainActivity) {
        applyToActionBar(mainActivity)
        applyToMenu(mainActivity)
    }

    private fun applyToActionBar(mainActivity: MainActivity) {
        val actionBar = mainActivity.supportActionBar
        if (actionBar != null) {
            actionBar.subtitle = StringUtils.EMPTY
        }
    }

    private fun applyToMenu(mainActivity: MainActivity) {
        val optionMenu = mainActivity.optionMenu
        if (optionMenu != null) {
            val menu = optionMenu.menu
            if (menu != null) {
                menu.findItem(R.id.action_wifi_band).isVisible = false
            }
        }
    }
}