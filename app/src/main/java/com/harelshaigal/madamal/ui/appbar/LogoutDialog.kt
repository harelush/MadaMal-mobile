package com.harelshaigal.madamal.ui.appbar

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object LogoutDialog {
    fun createLogoutDialog(context: Context, onConfirm: () -> Unit) {
        MaterialAlertDialogBuilder(context)
            .setTitle("התנתקות")
            .setMessage("האם אתה בטוח שברצונך להתנתק?")
            .setNegativeButton("חזור") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("התנתק") { _, _ ->
                onConfirm()
            }
            .show()
    }
}