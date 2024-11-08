package com.example.suggestion

import android.os.Bundle;

class AnnualActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_annual)
        setupBottomNavigation(R.id.bottom_navigation_annual, R.id.bottom_annual)
    }

}
