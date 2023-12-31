package edu.iliauni.scheduler.Utils

import android.app.AlertDialog
import android.content.Context

object AlertDialogHelper {
    fun showAlert(context: Context, title: String, message: String) {
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, "OK"
        ) { dialog, which -> dialog.dismiss() }
        alertDialog.show()
    }
}