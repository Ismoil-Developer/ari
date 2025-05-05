package uz.mrx.arigo.presentation.ui.screen.fragment.auth.login

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
import uz.mrx.arigo.data.remote.request.register.RegisterRequest
import uz.mrx.arigo.databinding.ScreenLoginBinding
import uz.mrx.arigo.presentation.ui.viewmodel.login.LoginScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.login.impl.LoginScreenViewModelImpl
import uz.mrx.arigo.utils.OnSwipeTouchListener

@AndroidEntryPoint
class LoginScreen : Fragment(R.layout.screen_login) {

    private val binding: ScreenLoginBinding by viewBinding(ScreenLoginBinding::bind)
    private val viewModel: LoginScreenViewModel by viewModels<LoginScreenViewModelImpl>()

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

        // Davom etish tugmasi
        binding.btnContinue.setOnClickListener {
            val phoneNumber = binding.phoneNumberEditText.text.toString().trim()

            if (phoneNumber.isNotEmpty()) {
                viewModel.postRegister(RegisterRequest(phoneNumber))
                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.toastMessage.collectLatest { message ->
                            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

                            viewModel.openConfirmScreen(
                                phoneNumber = binding.phoneNumberEditText.text.toString(),
                                code = message
                            )
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Iltimos, telefon raqamingizni kiriting", Toast.LENGTH_SHORT).show()
            }
        }

        // Register javobi (hozircha ishlatilmayapti, kelajakda foydali bo'lishi mumkin)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.registerResponse.collectLatest {

            }
        }

        // Toast xabari + ConfirmScreen ga o'tish

    }
}
