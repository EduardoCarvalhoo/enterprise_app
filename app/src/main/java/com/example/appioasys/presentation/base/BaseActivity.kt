package com.example.appioasys.presentation.base

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

open class BaseActivity : AppCompatActivity() {
    protected fun setupToolbar(
        toolbar: Toolbar,
        toolbarTitle: String?,
        setIndicator: Int,
        showBackButton: Boolean = false

    ) {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = toolbarTitle
            setHomeAsUpIndicator(setIndicator)
        }
        if (showBackButton) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }
}