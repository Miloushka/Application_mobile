package com.example.suggestion

import android.os.Bundle;

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setupBottomNavigation(R.id.bottom_navigation_setting, R.id.bottom_setting)
    }
}