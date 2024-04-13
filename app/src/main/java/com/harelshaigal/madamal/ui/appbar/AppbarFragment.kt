package com.harelshaigal.madamal.ui.appbar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.harelshaigal.madamal.databinding.FragmentAppbarBinding
import com.harelshaigal.madamal.ui.login.LoginActivity

class AppbarFragment : Fragment() {
    private var _binding: FragmentAppbarBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppbarBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = FirebaseAuth.getInstance()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logoutButton.setOnClickListener {
            LogoutDialog.createLogoutDialog(requireContext()) {
                FirebaseAuth.getInstance().signOut()
                navigateToLogin()
            }
        }
    }

    private fun navigateToLogin() {
        val loginIntent = Intent(context, LoginActivity::class.java)
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(loginIntent)
        activity?.finish()
    }

}