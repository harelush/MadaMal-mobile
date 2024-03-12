package com.harelshaigal.madamal.ui.reportsList.utils

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DeleteDialogUtils {
    fun createDeleteDialog(context: Context, onConfirm: () -> Unit) {
        MaterialAlertDialogBuilder(context)
            .setTitle("מחיקת דיווח")
            .setMessage("האם אתה בטוח שברצונך למחוק דיווח זה?")
            .setNegativeButton("חזור") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("מחק") { _, _ ->
                onConfirm()
            }
            .show()
    }
}