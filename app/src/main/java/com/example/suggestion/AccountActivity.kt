package com.example.suggestion

import android.os.Bundle;

class AccountActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        setupBottomNavigation(R.id.bottom_navigation_account, R.id.bottom_account)

    }
}