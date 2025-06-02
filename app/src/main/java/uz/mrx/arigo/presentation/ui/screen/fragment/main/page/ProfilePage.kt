package uz.mrx.arigo.presentation.ui.screen.fragment.main.page

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
import uz.mrx.arigo.data.remote.request.profile.ProfileRequestPhoto
import uz.mrx.arigo.databinding.PageProfileBinding
import uz.mrx.arigo.presentation.ui.dialog.ContactDialog
import uz.mrx.arigo.presentation.ui.dialog.LanguageDialog
import uz.mrx.arigo.presentation.ui.dialog.LogoutDialog
import uz.mrx.arigo.presentation.ui.dialog.ProgressDialogFragment
import uz.mrx.arigo.presentation.ui.viewmodel.profile.ProfileScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.profile.impl.ProfileScreenViewModelImpl

@AndroidEntryPoint
class ProfilePage:Fragment(R.layout.page_profile) {

    private val binding:PageProfileBinding by viewBinding(PageProfileBinding::bind)
    private val viewModel: ProfileScreenViewModel by viewModels<ProfileScreenViewModelImpl>()
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.logOut.setOnClickListener {
            val dialog = LogoutDialog()
            dialog.show(parentFragmentManager, "LogoutDialog")
        }

        binding.btnEdt.setOnClickListener {
            viewModel.openProfileScreen()
        }

        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.edtLanguage.setOnClickListener {
            val dialog = LanguageDialog()
            dialog.show(parentFragmentManager, "LanguageDialog")
        }

        binding.edtContactUs.setOnClickListener {
            val dialog = ContactDialog(
                onCallClick = { number ->
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
                    startActivity(intent)
                },
                onTelegramClick = { link ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    startActivity(intent)
                },
                onChatClick = {
                    viewModel.openChatScreen()
                }
            )

            // ViewModel orqali maʼlumot olib, dialogga yuboriladi
            lifecycleScope.launch {
                viewModel.getContact.collectLatest { response ->
                    dialog.setContactData(response.phone_number, response.telegram_link)
                }
            }

            dialog.show(parentFragmentManager, "ContactDialog")
        }

        setupActivityResultLaunchers()

        binding.edtImg.setOnClickListener {
            requestPermissionsProfileImage()
        }

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

                    binding.profileName.text = response.full_name
                    binding.profileNumber.text = response.phone_number

                }
            }

        }
    }

    private fun setupActivityResultLaunchers() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (permissions[Manifest.permission.READ_MEDIA_IMAGES] == true) {
                        openGalleryProfile()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Galereyaga ruxsat kerak",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    if (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
                        openGalleryProfile()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Galereyaga ruxsat kerak",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val imageUri: Uri? = result.data?.data
                    imageUri?.let { uri ->
                        setImageProfile(uri)
                        viewModel.putProfileImage(
                            ProfileRequestPhoto(
                                uri
                            )
                        )
                        Log.d("GalleryLauncher", "Image selected: $uri")
                    } ?: Log.e("GalleryLauncher", "Image URI is null")
                }
            }

        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                    imageBitmap?.let { bitmap ->
                        setImageProfile(bitmap)
                    }
                }
            }

    }

    private fun requestPermissionsProfileImage() {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }
            )
        )
    }

    private fun openGalleryProfile() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun openCameraProfile() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private fun setImageProfile(imageData: Any) {
        when (imageData) {
            is Uri -> binding.profileImg.setImageURI(imageData)
            is Bitmap -> binding.profileImg.setImageBitmap(imageData)
        }
    }

}