package com.threedee.nature.util

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar

fun Activity.showSnackbar(
    message: String,
    length: Int = Snackbar.LENGTH_LONG,
    actionString: String = "",
    `listener`: View.OnClickListener? = null,
    view: View? = null
) {
    val snackbar = Snackbar.make(view ?: findViewById(android.R.id.content), message, length)

    if (actionString.isNotEmpty()) {
        snackbar.setAction(actionString, `listener`)
    }
    snackbar.show()
}

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View = LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
