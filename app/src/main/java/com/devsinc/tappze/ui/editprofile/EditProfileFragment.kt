package com.devsinc.tappze.ui.editprofile

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.devsinc.tappze.R
import com.devsinc.tappze.data.Resource
import com.devsinc.tappze.data.utils.Constants
import com.devsinc.tappze.databinding.FragmentEditProfileBinding
import com.devsinc.tappze.model.UserData
import com.devsinc.tappze.ui.BindingFragment
import com.devsinc.tappze.ui.editinfo.EditInfoFragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : BindingFragment<FragmentEditProfileBinding>() {

    companion object {
        fun newInstance() = EditProfileFragment()
    }

    private val viewModel: EditProfileViewModel by viewModels()
    private lateinit var userData: UserData
    private var mProfileUri: Uri? = null


    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentEditProfileBinding::inflate

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!

                mProfileUri = fileUri
                binding.profileImage.setImageURI(fileUri)
                binding.progressBar.visibility = View.VISIBLE
                viewModel.updateUserImage(mProfileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this.context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this.context, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireContext(), R.array.gender_array, android.R.layout.simple_spinner_item
        )

        binding.spGender.adapter = adapter

        val editAdapter = RecyclerAdapterEdit(Constants.appIconArrayList)
        binding.recyclerView.adapter = editAdapter
        editAdapter.setOnItemClickListener(object : RecyclerAdapterEdit.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val editAppInfoBottomSheet = EditInfoFragment(Constants.appIconArrayList[position])
                editAppInfoBottomSheet.show(parentFragmentManager, EditInfoFragment.TAG)
            }
        })

        viewModel.getUserDatabase()
        lifecycleScope.launchWhenStarted {
            viewModel.getDatabaseFlow.collect { event ->
                when (event) {
                    is Resource.Success -> {
                        binding.etFullName.setText(event.result.fullName)
                        binding.etAbout.setText(event.result.about)
                        binding.etPhone.setText(event.result.phone)
                        binding.etCompany.setText(event.result.company)
                        val spinnerPosition: Int = adapter.getPosition(event.result.gender)
                        binding.spGender.setSelection(spinnerPosition)
                        binding.btnDateOfBirth.text =
                            event.result.birthDate ?: getString(R.string.select_dob)
                        // set profile image
                        if (event.result.profileImage != null) {
                            Glide.with(this@EditProfileFragment)
                                .load(event.result.profileImage)
                                .placeholder(R.drawable.ic_baseline_account_circle_24)
                                .error(R.drawable.ic_baseline_no_accounts_24)
                                .centerCrop()
                                .into(binding.profileImage)
                        }
                        userData = event.result
                    }
                    is Resource.Error -> {
                        Toast.makeText(
                            requireContext(),
                            "Error: ${event.exception.message.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {}
                }
            }
        }

        binding.btnDateOfBirth.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(
                    CalendarConstraints.Builder().setValidator(
                        DateValidatorPointBackward.now()
                    ).build()
                )
                .build()

            datePicker.show(parentFragmentManager, "DATE_PICKER_TAG")

            datePicker.addOnPositiveButtonClickListener {
                val date = datePicker.headerText
                binding.btnDateOfBirth.text = date
            }
        }

        binding.btnSave.setOnClickListener {
            val user = UserData(
                binding.etFullName.text.toString(),
                binding.etAbout.text.toString(),
                binding.etPhone.text.toString(),
                binding.etCompany.text.toString(),
                binding.spGender.selectedItem.toString(),
                binding.btnDateOfBirth.text.toString(),
                // get data already in database
                userData.infoMap,
                userData.profileStatus,
                userData.profileImage
            )

            viewModel.updateUserDatabase(user)

            lifecycleScope.launchWhenStarted {
                viewModel.updateDatabaseFlow.collect { event ->
                    when (event) {
                        is Resource.Success -> {
                            Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                            // back to profile
                            findNavController().navigateUp()

                        }
                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(),
                                "Error: ${event.exception.message.toString()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {}
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.updateImageFlow.collect { event ->
                when (event) {
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnSave.isEnabled = true
                        userData.profileImage = event.result.toString()
                        Toast.makeText(
                            requireContext(),
                            "Profile image uploaded",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is Resource.Error -> {
                        Toast.makeText(
                            requireContext(),
                            "Error: ${event.exception.message.toString()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Resource.Loading -> {
                        binding.btnSave.isEnabled = false
                    }
                    else -> {}
                }
            }
        }

        binding.profileImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        binding.btnCancel.setOnClickListener {
            // go back
            findNavController().navigateUp()
//            OnBackPressedDispatcher().onBackPressed()
        }
    }
}
