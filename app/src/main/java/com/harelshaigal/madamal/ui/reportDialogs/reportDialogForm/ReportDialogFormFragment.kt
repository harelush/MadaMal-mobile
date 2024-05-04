package com.harelshaigal.madamal.ui.reportDialogs.reportDialogForm

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.harelshaigal.madamal.R
import com.harelshaigal.madamal.data.report.Report
import com.harelshaigal.madamal.data.report.ReportDto
import com.harelshaigal.madamal.data.report.ReportRepository
import com.harelshaigal.madamal.databinding.FragmentReportDialogFormBinding
import com.harelshaigal.madamal.helpers.ImagePickerHelper
import com.harelshaigal.madamal.helpers.LocationHelper
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

    private var originReport: Report? = null
    private lateinit var titleTextInput: TextInputLayout
    private lateinit var reportBodyTextInput: TextInputLayout
    private lateinit var saveReportButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ReportDialogFormViewModel::class.java]

        _binding = FragmentReportDialogFormBinding.inflate(inflater, container, false)
        val root: View = binding.root

        imagePickerHelper = ImagePickerHelper(this, this)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleTextInput = binding.addReportTitleLayout
        reportBodyTextInput = binding.addReportContentLayout
        saveReportButton = binding.saveButton

        // Add TextWatcher to monitor changes in TextInputEditText
        titleTextInput.editText?.addTextChangedListener(textWatcher)
        reportBodyTextInput.editText?.addTextChangedListener(textWatcher)

        disableSaveButton()

        binding.buttonClose.setOnClickListener {
            dismiss()
        }

        binding.ImageButton.setOnClickListener {
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
                setEditData(arguments?.getString("reportId")!!)

            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateTextInputLayoutBorder(textInputLayout: TextInputLayout, isValid: Boolean) {
        if (!isValid) {
            textInputLayout.boxStrokeColor = Color.RED
        } else {
            textInputLayout.boxStrokeColor = Color.GRAY
        }
    }

    private fun enableSaveButton() {
        saveReportButton.isEnabled = true
        saveReportButton.alpha = 1.0f
        saveReportButton.setBackgroundColor(Color.parseColor("#FF6750A3"))
    }

    private fun disableSaveButton() {
        saveReportButton.isEnabled = false
        saveReportButton.alpha = 0.5f
        saveReportButton.setBackgroundColor(Color.GRAY)
    }

    private fun setEditData(reportId: String) {
        viewModel.getReportData(reportId).observe(viewLifecycleOwner) { currReport ->
            if (currReport != null) {

                binding.reportDialogTitle.text = getString(R.string.title_edit_report_dialog)
                binding.addReportContent.setText(currReport.data)
                binding.addReportTitle.setText(currReport.title)

                if (currReport.image != "null" && currReport.image != null) {
                    Picasso.get().load(Uri.parse(currReport.image)).into(binding.addReportImageView)
                    binding.addReportImageView.visibility = View.VISIBLE
                }
                originReport = currReport
            }
        }
    }

    companion object {
        fun display(fragmentManager: FragmentManager?, reportId: String?) {
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
        uri.also {
            selectedImageUri = it
            binding.addReportImageView.visibility = View.VISIBLE
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            validateFields()
        }
    }

    private fun validateFields() {
        val titleText = titleTextInput.editText?.text.toString().trim()
        val reportBodyText = reportBodyTextInput.editText?.text.toString().trim()

        val titleTextValidator = titleText.isNotEmpty()
        val reportBodyTextValidator = reportBodyText.isNotEmpty()

        updateTextInputLayoutBorder(titleTextInput, titleTextValidator)
        updateTextInputLayoutBorder(reportBodyTextInput, reportBodyTextValidator)

        if (titleTextValidator && reportBodyTextValidator) {
            enableSaveButton()
        } else {
            disableSaveButton()
        }
    }

    private suspend fun saveReport() {
        withContext(Dispatchers.Main) {
            binding.addReportProgressBar.visibility = View.VISIBLE
        }

        val user = Firebase.auth.currentUser

        if (user != null) {
            val reportToSave: ReportDto = if (originReport === null)
                ReportDto(
                    userId = user.uid,
                    title = binding.addReportTitle.text.toString(),
                    data = binding.addReportContent.text.toString(),
                    lat = LocationHelper.lat,
                    lng = LocationHelper.lng,
                )
            else
                ReportDto(
                    image = originReport!!.image,
                    userId = originReport!!.userId,
                    lat = originReport!!.lat,
                    lng = originReport!!.lng,
                    title = binding.addReportTitle.text.toString(),
                    data = binding.addReportContent.text.toString(),
                )

            try {
                if (originReport === null) {
                    repostRepository.addReport(reportToSave, selectedImageUri)
                } else {
                    repostRepository.updateReport(
                        reportToSave,
                        originReport!!.id,
                        selectedImageUri
                    )
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