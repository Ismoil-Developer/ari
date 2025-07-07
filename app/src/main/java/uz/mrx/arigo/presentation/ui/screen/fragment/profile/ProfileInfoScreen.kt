package uz.mrx.arigo.presentation.ui.screen.fragment.profile

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.data.remote.request.profile.ProfileRequest
import uz.mrx.arigo.data.remote.request.profile.ProfileRequestPhoto
import uz.mrx.arigo.databinding.ScreenProfileInfoBinding
import uz.mrx.arigo.presentation.ui.dialog.ProgressDialogFragment
import uz.mrx.arigo.presentation.ui.viewmodel.profile.ProfileScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.profile.impl.ProfileScreenViewModelImpl

@AndroidEntryPoint
class ProfileInfoScreen:Fragment(R.layout.screen_profile_info) {

    private val binding: ScreenProfileInfoBinding by viewBinding(ScreenProfileInfoBinding::bind)
    private val viewModel: ProfileScreenViewModel by viewModels<ProfileScreenViewModelImpl>()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var getContentLauncher: ActivityResultLauncher<String>
    private var selectedImageUri: Uri? = null
    private var progressDialog: ProgressDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (permissions[Manifest.permission.READ_MEDIA_IMAGES] == true) {
                        openGallery()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Galereyaga kirish uchun ruxsat kerak",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    if (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
                        openGallery()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Galereyaga kirish uchun ruxsat kerak",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        getContentLauncher = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                binding.profileImg.setImageURI(it) // ✅ Galereyadan tanlangandan so‘ng rasmni ko‘rsatish
                viewModel.putProfileImage(ProfileRequestPhoto(it))
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.profileResponse.collectLatest { response ->
                response.avatar?.let { uri ->
                    Glide.with(requireContext())
                        .load(uri)
                        .apply(
                            RequestOptions().skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                        )
                        .into(binding.profileImg)
                }

                binding.edtName.setText(response.full_name)
                binding.edtNumber.setText(response.phone_number)

            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.putProfile.collectLatest {
                val isAvatarValid = it.avatar?.isNotEmpty() == true
                val isNameValid = !it.full_name.isNullOrEmpty()
                val isPhoneValid = !it.phone_number.isNullOrEmpty()

                if (isAvatarValid || isNameValid || isPhoneValid) {
                    viewModel.openMainScreen()
                }
            }
        }

        binding.apply {

            icBack.setOnClickListener {
                findNavController().popBackStack()
            }

            binding.edtImg.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))
                } else {
                    requestPermissionLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                }
            }

            btnUpdate.setOnClickListener {
                showProgressDialog()

                val fullName = edtName.text.toString()
                val phone = edtNumber.text.toString()

                viewModel.putProfile(ProfileRequest(fullName, phone))

            }

        }

        selectedImageUri?.let {
            binding.profileImg.setImageURI(it)
        }

    }

    private fun openGallery() {
        getContentLauncher.launch("image/*")
    }

    private fun showProgressDialog() {
        progressDialog = ProgressDialogFragment(100) {
            // Progress dialog dismiss qilish uchun callback
        }
        progressDialog?.show(parentFragmentManager, "progressDialog")

        viewLifecycleOwner.lifecycleScope.launch {
            kotlinx.coroutines.delay(2000)
            dismissProgressDialog()
        }
    }

    private fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }
}