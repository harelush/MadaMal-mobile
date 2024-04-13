package com.harelshaigal.madamal.ui.appbar

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.harelshaigal.madamal.ui.login.LoginActivity

object LogoutDialog {
    fun createLogoutDialog(context: Context, postDeleteAction: () -> Unit) {
        MaterialAlertDialogBuilder(context)
            .setTitle("התנתקות")
            .setMessage("האם אתה בטוח שברצונך להתנתק?")
            .setNegativeButton("חזור") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("התנתק") { _, _ ->
                FirebaseAuth.getInstance().signOut()
                postDeleteAction()
            }
            .show()
    }
}