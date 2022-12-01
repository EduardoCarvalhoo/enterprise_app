package com.example.appioasys.utils

import android.app.Activity
import android.app.AlertDialog
import com.example.appioasys.R

fun Activity.showErrorAlertDialog(message: Int, negativeButtonAction: (() -> Unit)? = null) {
    AlertDialog.Builder(this)
        .setMessage(message)
        .setNegativeButton(R.string.error_dialog_back_button_message_text) { _, _ ->
            negativeButtonAction?.invoke()
        }
        .show()
}