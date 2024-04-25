package com.harelshaigal.madamal.ui.reportDialogs

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.harelshaigal.madamal.data.report.ReportRepository

object DeleteReportDialog {
    fun createDeleteDialog(context: Context, reportId: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle("מחיקת דיווח")
            .setMessage("האם אתה בטוח שברצונך למחוק דיווח זה?")
            .setNegativeButton("חזור") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("מחק") { _, _ ->
                ReportRepository().deleteReportById(reportId)
            }
            .show()
    }
}