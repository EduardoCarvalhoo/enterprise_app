package com.example.appioasys.utils

import android.app.Activity
import android.app.AlertDialog
import com.example.appioasys.R

fun Activity.showAlertDialog(message: Int, positiveButtonAction: (() -> Unit)? = null) {
    AlertDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton(getString(R.string.error_dialog_positive_button_message_text)) { _, _ ->
            positiveButtonAction?.invoke()
        }
        .show()
}