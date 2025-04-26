package uz.mrx.arigo.presentation.ui.screen.fragment.auth.register

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.data.local.shp.MySharedPreference
import uz.mrx.arigo.data.remote.request.register.RegisterRequest
import uz.mrx.arigo.databinding.ScreenRegisterBinding
import uz.mrx.arigo.presentation.ui.viewmodel.register.RegisterScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.register.impl.RegisterScreenViewModelImpl
import uz.mrx.arigo.utils.OnSwipeTouchListener
import javax.inject.Inject

@AndroidEntryPoint
class RegisterScreen : Fragment(R.layout.screen_register) {

    private val binding: ScreenRegisterBinding by viewBinding(ScreenRegisterBinding::bind)
    private val viewModel: RegisterScreenViewModel by viewModels<RegisterScreenViewModelImpl>()


    @Inject
    lateinit var sharedPref: MySharedPreference
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.container.setOnTouchListener(object : OnSwipeTouchListener(requireContext()) {

            override fun onSwipeTop() {
                binding.container.animate().translationY(-200f).setDuration(100).start()
            }

            override fun onSwipeBottom() {
                binding.container.animate().translationY(50f).setDuration(100).start()
            }

        })

        binding.loginTxt.setOnClickListener {
            viewModel.openLoginScreen()
        }

        binding.btnContinue.setOnClickListener {

            val phoneNumber = binding.phoneNumberEditText.text.toString()

            viewModel.postRegister(RegisterRequest(phoneNumber))
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.registerResponse.collectLatest {
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.toastMessage.collectLatest { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    viewModel.openConfirmScreen(binding.phoneNumberEditText.text.toString(), message)
                }
            }
        }


    }
}