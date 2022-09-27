package com.example.appioasys.utils

import android.app.Activity
import android.app.AlertDialog
import com.example.appioasys.R

fun Activity.showAlertDialog(message: String, positiveButtonAction: (() -> Unit)? = null){
    AlertDialog.Builder(this)
        .setTitle(getString(R.string.error_dialog_title_text))
        .setMessage(message)
        .setPositiveButton(getString(R.string.error_dialog_positive_button_message_text)) { _, _ ->
            positiveButtonAction?.invoke()
        }
        .setNegativeButton(getString(R.string.error_dialog_negative_button_message_text)) { _, _ ->}
            .show()
}