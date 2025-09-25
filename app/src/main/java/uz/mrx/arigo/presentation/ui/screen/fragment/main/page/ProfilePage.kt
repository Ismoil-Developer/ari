package uz.mrx.arigo.presentation.ui.screen.fragment.main.page

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.data.remote.request.profile.ProfileRequestPhoto
import uz.mrx.arigo.databinding.PageProfileBinding
import uz.mrx.arigo.presentation.ui.dialog.ContactDialog
import uz.mrx.arigo.presentation.ui.dialog.LanguageDialog
import uz.mrx.arigo.presentation.ui.dialog.LogoutDialog
import uz.mrx.arigo.presentation.ui.dialog.ProgressDialogFragment
import uz.mrx.arigo.presentation.ui.viewmodel.profile.ProfileScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.profile.impl.ProfileScreenViewModelImpl
import java.io.File

@AndroidEntryPoint
class ProfilePage : Fragment(R.layout.page_profile) {

    private val binding: PageProfileBinding by viewBinding(PageProfileBinding::bind)
    private val viewModel: ProfileScreenViewModel by viewModels<ProfileScreenViewModelImpl>()

    private lateinit var galleryLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>

    private var progressDialog: ProgressDialogFragment? = null
    private var imageUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupActivityResultLaunchers()

        binding.btnEdt.setOnClickListener { viewModel.openProfileScreen() }
        binding.icBack.setOnClickListener { findNavController().popBackStack() }

        binding.logOut.setOnClickListener {
            LogoutDialog { }.show(parentFragmentManager, "LogoutDialog")
        }

        binding.edtLanguage.setOnClickListener {
            LanguageDialog().show(parentFragmentManager, "LanguageDialog")
        }

        binding.edtContactUs.setOnClickListener {
            val dialog = ContactDialog(
                onCallClick = { number ->
                    startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")))
                },
                onTelegramClick = { link ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                },
                onChatClick = { viewModel.openChatScreen() }
            )
            lifecycleScope.launch {
                viewModel.getContact.collectLatest {
                    dialog.setContactData(it.phone_number, it.telegram_link)
                }
            }
            dialog.show(parentFragmentManager, "ContactDialog")
        }

        binding.edtImg.setOnClickListener { showImageSourceDialog() }

        // lifecycle-aware collectors
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.profileResponse.collectLatest { response ->
                        binding.profileName.text = response.full_name ?: ""
                        binding.profileNumber.text = response.phone_number ?: ""

                        response.avatar?.let { avatarUrl ->
                            Glide.with(requireContext())
                                .load(avatarUrl)
                                .into(binding.profileImg)
                        }
                        Log.d("PPPPPPPPPPP", "onViewCreated: ${response.avatar} kirdi getProfile")


                    }
                }

                launch {

                    viewModel.profilePhotoResponse.collectLatest { response ->
                        binding.profileName.text = response.full_name ?: ""
                        binding.profileNumber.text = response.phone_number ?: ""

                        response.avatar?.let { avatarUrl ->
                            Glide.with(requireContext())
                                .load(avatarUrl)
                                .into(binding.profileImg)
                        }
                        Log.d("PPPPPPPPPPP", "onViewCreated: ${response.avatar} kirdi getProfile")

                    }

                }


            }
        }
    }

    private fun setupActivityResultLaunchers() {
            // Gallery
        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                uri?.let {
                    setImageProfile(it)
                    viewModel.putProfileImage(ProfileRequestPhoto(it))
                }
            }

        // Camera
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success) {
                    imageUri?.let {
                        setImageProfile(it)
                        viewModel.putProfileImage(ProfileRequestPhoto(it))
                    }
                }
            }
    }

    private fun showProgressDialog() {
        progressDialog = ProgressDialogFragment(100) {}
        progressDialog?.show(parentFragmentManager, "progressDialog")
    }

    private fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }

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
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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



}
