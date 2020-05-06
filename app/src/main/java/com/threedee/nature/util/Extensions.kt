package com.threedee.nature.util

import android.app.Activity
import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import java.util.Calendar

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


fun Long.getFormattedDate(context: Context): String {
    val smsTime = Calendar.getInstance()
    smsTime.timeInMillis = this

    val now = Calendar.getInstance()

    val timeFormatString = "h:mm aa"
    val dateTimeFormatString = "EEEE, MMMM d, h:mm aa"
    val HOURS = (60 * 60 * 60).toLong()
    return if (now.get(Calendar.DATE) === smsTime.get(Calendar.DATE)) {
        "Today " + DateFormat.format(timeFormatString, smsTime)
    } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) === 1) {
        "Yesterday " + DateFormat.format(timeFormatString, smsTime)
    } else if (now.get(Calendar.YEAR) === smsTime.get(Calendar.YEAR)) {
        DateFormat.format(dateTimeFormatString, smsTime).toString()
    } else {
        DateFormat.format("MMMM dd yyyy, h:mm aa", smsTime).toString()
    }
}

fun Long.isToday(context: Context): Boolean {
    val smsTime = Calendar.getInstance()
    smsTime.timeInMillis = this

    val now = Calendar.getInstance()
    return now.get(Calendar.DATE) === smsTime.get(Calendar.DATE)
}