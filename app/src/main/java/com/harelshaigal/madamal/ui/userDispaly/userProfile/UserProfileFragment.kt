package com.harelshaigal.madamal.ui.userDispaly.userProfile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.harelshaigal.madamal.databinding.FragmentUserProfileBinding

class UserProfileFragment : Fragment() {

    private lateinit var viewModel: UserProfileViewModel

    private var _binding: FragmentUserProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel =
            ViewModelProvider(this)[UserProfileViewModel::class.java]

        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.editUserButton.setOnClickListener {
//            EditUserProfileDialogFragment.display(getParentFragmentManager())
            val db = Firebase.firestore
            val city = hashMapOf(
                "name" to "Los Angeles",
                "state" to "CA",
                "country" to "USA",
            )

            db.collection("cities").document("LA")
                .set(city)
                .addOnSuccessListener { Log.d("harelush", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w("harelush", "Error writing document", e) }
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}