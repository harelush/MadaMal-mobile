package com.harelshaigal.madamal.ui.reportDialogs.reportDialogForm

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.harelshaigal.madamal.R
import com.harelshaigal.madamal.data.LocationDataViewModel
import com.harelshaigal.madamal.data.Report
import com.harelshaigal.madamal.data.ReportRepository
import com.harelshaigal.madamal.databinding.FragmentReportDialogFormBinding
import com.harelshaigal.madamal.helpers.ImagePickerHelper
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReportDialogFormFragment : DialogFragment(), ImagePickerHelper.ImagePickerCallback {

    private lateinit var imagePickerHelper: ImagePickerHelper
    val TAG = "add_or_edit_report_dialog"
    private lateinit var viewModel: ReportDialogFormViewModel
    private var _binding: FragmentReportDialogFormBinding? = null
    private val binding get() = _binding!!
    private var selectedImageUri: Uri? = null

    private val repostRepository: ReportRepository = ReportRepository()
    private lateinit var locationViewModel: LocationDataViewModel

    private var originReport: Report? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ReportDialogFormViewModel::class.java]

        _binding = FragmentReportDialogFormBinding.inflate(inflater, container, false)
        val root: View = binding.root

        imagePickerHelper = ImagePickerHelper(this, this)
        locationViewModel =
            ViewModelProvider(requireActivity()).get(LocationDataViewModel::class.java)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonClose.setOnClickListener {
            dismiss()
        }

        binding.addReportImageView.setOnClickListener {
            imagePickerHelper.openImagePicker()
        }

        binding.saveButton.setOnClickListener {
            lifecycleScope.launch {
                saveReport()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            if (arguments?.getString("reportId") != null)
                setEditData(arguments?.getString("reportId")!!.toLong())

            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setEditData(reportId: Long) {

        viewModel.getReportData(reportId).observe(viewLifecycleOwner) { currReport ->
            if (currReport != null) {

                binding.reportDialogTitle.text = getString(R.string.title_edit_report_dialog)
                binding.addReportContent.setText(currReport.data)
                binding.addReportTitle.setText(currReport.title)

                if (currReport.image != "null"  && currReport.image != null) {
                    Picasso.get().load(Uri.parse(currReport.image)).into(binding.addReportImageView)
                }
                originReport = currReport
            }
        }
    }

    companion object {
        fun display(fragmentManager: FragmentManager?, reportId: Long?) {
            if (fragmentManager != null) {
                val reportDialogFormFragment = ReportDialogFormFragment()

                if (reportId != null) {
                    val args = Bundle()
                    args.putString("reportId", reportId.toString())
                    reportDialogFormFragment.arguments = args
                }

                reportDialogFormFragment.show(
                    fragmentManager, reportDialogFormFragment.TAG
                )
            }
        }
    }

    override fun getImageViewForLoad(): ImageView {
        return binding.addReportImageView
    }

    override fun selectedImageExtraLogic(uri: Uri?) {
        uri.also { selectedImageUri = it }
    }

    private suspend fun saveReport() {
        withContext(Dispatchers.Main) {
            binding.addReportProgressBar.visibility = View.VISIBLE
        }

        val user = Firebase.auth.currentUser

        if (user != null) {
            val lat = locationViewModel.latitude
            val lng = locationViewModel.longtitude

            val reportToSave: Report = if (originReport === null)
                Report(
                    userId = user.uid,
                    title = binding.addReportTitle.text.toString(),
                    data = binding.addReportContent.text.toString(),
                    lat = lat,
                    lng = lng,
                )
            else
                originReport!!.copy(
                    title = binding.addReportTitle.text.toString(),
                    data = binding.addReportContent.text.toString()
                )

            val fileName = "reportsImages/${reportToSave.id}/reportImage.jpg"

            try {
                if (selectedImageUri != null) {
                    reportToSave.image = ImagePickerHelper.uploadImageToFirebaseStorage(
                        selectedImageUri, fileName, context
                    ).toString()
                }


                if (originReport === null) {
                    repostRepository.insertReport(reportToSave)
                } else {
                    repostRepository.updateReport(reportToSave)
                }

                withContext(Dispatchers.Main) {
                    binding.addReportProgressBar.visibility = View.GONE
                    Toast.makeText(context, "הדיווח נשמר בהצלחה", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
                dismiss()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.addReportProgressBar.visibility = View.GONE
                    Toast.makeText(
                        context,
                        "שגיאה בשמירת הדיווח : ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }
}