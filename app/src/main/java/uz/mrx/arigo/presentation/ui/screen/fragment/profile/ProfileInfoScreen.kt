package uz.mrx.arigo.presentation.ui.screen.fragment.profile

import android.Manifest
import android.app.AlertDialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
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
import java.io.File

@AndroidEntryPoint
class ProfileInfoScreen : Fragment(R.layout.screen_profile_info) {

    private val binding: ScreenProfileInfoBinding by viewBinding(ScreenProfileInfoBinding::bind)
    private val viewModel: ProfileScreenViewModel by viewModels<ProfileScreenViewModelImpl>()

    private lateinit var getContentLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>

    private var selectedImageUri: Uri? = null
    private var progressDialog: ProgressDialogFragment? = null
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Galereya launcher
        getContentLauncher = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                setImageProfile(it)
                viewModel.putProfileImage(ProfileRequestPhoto(it))
            }
        }

        // Kamera launcher
        cameraLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                imageUri?.let {
                    setImageProfile(it)
                    viewModel.putProfileImage(ProfileRequestPhoto(it))
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Profile ma'lumotlarini kuzatish
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.profileResponse.collectLatest { response ->
                response.avatar?.let { uri ->
                    Glide.with(requireContext())
                        .load(uri)
                        .apply(
                            RequestOptions()
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                        )
                        .into(binding.profileImg)
                }
                binding.edtName.setText(response.full_name)
                binding.edtNumber.setText(response.phone_number)
            }
        }

        // Profile yangilash natijasi
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
            icBack.setOnClickListener { findNavController().popBackStack() }

            // ✅ Dialog orqali tanlash
            edtImg.setOnClickListener { showImageSourceDialog() }

            btnUpdate.setOnClickListener {
                showProgressDialog()
                val fullName = edtName.text.toString()
                val phone = edtNumber.text.toString()
                viewModel.putProfile(ProfileRequest(fullName, phone))
            }
        }

        selectedImageUri?.let { setImageProfile(it) }
    }

    // ✅ Dialog
    private fun showImageSourceDialog() {
        val options = arrayOf("Kamera", "Galereya")
        AlertDialog.Builder(requireContext())
            .setTitle("Rasm tanlash")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCameraProfile()
                    1 -> openGalleryProfile()
                }
            }
            .show()
    }

    private fun openGalleryProfile() {
        getContentLauncher.launch("image/*")
    }

    private fun openCameraProfile() {
        val photoFile = File.createTempFile("profile_", ".jpg", requireContext().cacheDir)
        val uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            photoFile
        )
        imageUri = uri
        cameraLauncher.launch(uri)
    }

    private fun setImageProfile(uri: Uri) {
        Glide.with(requireContext())
            .load(uri)
            .placeholder(R.drawable.loading)
            .into(binding.profileImg)
    }

    private fun showProgressDialog() {
        progressDialog = ProgressDialogFragment(100) {}
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
