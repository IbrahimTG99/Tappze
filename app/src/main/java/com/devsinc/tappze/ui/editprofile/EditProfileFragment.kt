package com.devsinc.tappze.ui.editprofile

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.devsinc.tappze.R
import com.devsinc.tappze.data.Resource
import com.devsinc.tappze.databinding.FragmentEditProfileBinding
import com.devsinc.tappze.model.AppIcon
import com.devsinc.tappze.model.UserData
import com.devsinc.tappze.ui.BindingFragment
import com.devsinc.tappze.ui.editinfo.EditInfoFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : BindingFragment<FragmentEditProfileBinding>() {

    companion object {
        fun newInstance() = EditProfileFragment()
    }

    private val viewModel: EditProfileViewModel by viewModels()
    private lateinit var appIconArrayList: ArrayList<AppIcon>

    private lateinit var imageId: Array<Int>
    private lateinit var appName: Array<String>
    private lateinit var userData: UserData

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentEditProfileBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            requireContext(), R.array.gender_array, android.R.layout.simple_spinner_item
        )

        binding.spGender.adapter = adapter

        initRecyclerView()
        val editAdapter = RecyclerAdapterEdit(appIconArrayList)
        binding.recyclerView.adapter = editAdapter
        editAdapter.setOnItemClickListener(object : RecyclerAdapterEdit.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val editAppInfoBottomSheet = EditInfoFragment(appIconArrayList[position])
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
                        binding.btnDateOfBirth.text = event.result.birthDate
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
            val datePickerDialog = DatePickerDialog(
                requireContext(), { _, year, month, dayOfMonth ->
                    val date = "$dayOfMonth/${month + 1}/$year"
                    binding.btnDateOfBirth.text = date
                }, 1990, 0, 1
            )
            datePickerDialog.show()
        }

        binding.btnSave.setOnClickListener {
            val user = UserData(
                binding.etFullName.text.toString(),
                binding.etAbout.text.toString(),
                binding.etPhone.text.toString(),
                binding.etCompany.text.toString(),
                binding.spGender.selectedItem.toString(),
                binding.btnDateOfBirth.text.toString(),
                // get data somehow
                userData.infoMap
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

        binding.profileImage.setOnClickListener {
            // @TODO: add image picker
        }

        binding.btnCancel.setOnClickListener {
            // go back
            findNavController().navigateUp()
//            OnBackPressedDispatcher().onBackPressed()
        }
    }

    private fun initRecyclerView() {
        appIconArrayList = arrayListOf<AppIcon>()

        imageId = arrayOf(
            R.drawable.ic_facebook,
            R.drawable.ic_twitter,
            R.drawable.ic_linkedin,
            R.drawable.ic_snapchat,
            R.drawable.ic_youtube,
            R.drawable.ic_tiktok,
            R.drawable.ic_pinterest,
            R.drawable.ic_whatsapp,
            R.drawable.ic_telegram,
            R.drawable.ic_vimeo,
            R.drawable.ic_website,
            R.drawable.ic_spotify,
            R.drawable.ic_phone,
            R.drawable.ic_email,
            R.drawable.ic_calendly,
            R.drawable.ic_paypal,
            R.drawable.ic_instagram
        )

        appName = arrayOf(
            "Facebook",
            "Twitter",
            "LinkedIn",
            "Snapchat",
            "Youtube",
            "TikTok",
            "Pinterest",
            "Whatsapp",
            "Telegram",
            "Vimeo",
            "Website",
            "Spotify",
            "Phone",
            "Email",
            "Calendly",
            "Paypal",
            "Instagram"
        )

        for (i in imageId.indices) {
            appIconArrayList.add(AppIcon(imageId[i], appName[i]))
        }
    }
}
