package com.example.suggestion

import android.os.Bundle

class MonthActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_month)
        setupBottomNavigation(R.id.bottom_navigation_month, R.id.bottom_month)
    }

}