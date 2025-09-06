package uz.mrx.arigo.presentation.ui.screen.fragment.auth.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
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
import uz.mrx.arigo.utils.toast

@AndroidEntryPoint
class LoginScreen : Fragment(R.layout.screen_login) {

    private val binding: ScreenLoginBinding by viewBinding(ScreenLoginBinding::bind)

    private val viewModel: LoginScreenViewModel by viewModels<LoginScreenViewModelImpl>()

    private var doubleBackToExitPressedOnce = false



    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Dastlab tugma disable bo'lsin
        binding.btnContinue.isEnabled = false
        binding.btnContinue.alpha = 0.5f

        // falseCheck bosilganda
        binding.falseCheck.setOnClickListener {
            binding.falseCheck.visibility = View.GONE
            binding.trueCheck.visibility = View.VISIBLE

            binding.btnContinue.isEnabled = true
            binding.btnContinue.alpha = 1f
        }

        // trueCheck bosilganda
        binding.trueCheck.setOnClickListener {
            binding.trueCheck.visibility = View.GONE
            binding.falseCheck.visibility = View.VISIBLE

            binding.btnContinue.isEnabled = false
            binding.btnContinue.alpha = 0.5f
        }

        // Offerta matniga bosilganda ham privacyScreen ochiladi
        binding.textOfferta.setOnClickListener {
            findNavController().navigate(R.id.action_loginScreen_to_privacyScreen)
        }


        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (doubleBackToExitPressedOnce) {
                        requireActivity().finish()
                    } else {
                        doubleBackToExitPressedOnce = true
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.ilovadan_chiqish_uchun_yana_bir_marta_orqaga_tugmasini_bosing),
                            Toast.LENGTH_SHORT
                        ).show()

                        Handler(Looper.getMainLooper()).postDelayed({
                            doubleBackToExitPressedOnce = false
                        }, 2000) // 2 second delay
                    }
                }
            })



        binding.container.setOnTouchListener(object : OnSwipeTouchListener(requireContext()) {

            override fun onSwipeTop() {
                binding.container.animate().translationY(-200f).setDuration(100).start()
            }

            override fun onSwipeBottom() {
                binding.container.animate().translationY(50f).setDuration(100).start()
            }

        })


        binding.btnContinue.setSafeOnClickListener {

            if (binding.trueCheck.visibility == View.VISIBLE) {
                val phoneNumber = binding.phoneNumberEditText.text.toString().trim()
                val phoneNumber_ = "+998$phoneNumber"

                if (phoneNumber_.isEmpty()) {
                    Toast.makeText(requireContext(), "Iltimos, telefon raqamingizni kiriting", Toast.LENGTH_SHORT).show()
                    return@setSafeOnClickListener
                }

                if (!phoneNumber_.startsWith("+998") || phoneNumber_.length != 13) {
                    Toast.makeText(requireContext(), "Raqam formati xato. Namuna: +998991234567", Toast.LENGTH_SHORT).show()
                    return@setSafeOnClickListener
                }

                // Hamma shartlar to'g'ri bo'lsa:
                viewModel.postRegister(RegisterRequest(phoneNumber_))

                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.registerResponse.collectLatest { message ->
                            if (message.detail == "Kod yuborildi.") {
                                viewModel.openConfirmScreen(phoneNumber_, "")
                            } else {
                                Toast.makeText(requireContext(), "Xatolik yuz berdi", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                lifecycleScope.launchWhenStarted {
                    viewModel.isLoading.collectLatest { isLoading ->
                        binding.btnContinue.isEnabled = !isLoading
                        binding.btnContinue.alpha = if (isLoading) 0.5f else 1f
                        binding.btnContinue.text = if (isLoading) "Yuklanmoqda..." else "Davom etish"
                    }
                }

                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.isLoading.collectLatest { isLoading ->
                            binding.btnContinue.setLoading(isLoading, "Yuklanmoqda...")
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Davom etish uchun ommaviy offertaga rozilik bildiring", Toast.LENGTH_SHORT).show()
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.toastMessage.collectLatest { message ->
                    toast(message)
                }
            }
        }

    }

    private var lastClickTime = 0L

    fun View.setSafeOnClickListener(interval: Long = 1000L, onSafeClick: (View) -> Unit) {
        setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime > interval) {
                lastClickTime = currentTime
                onSafeClick(it)
            }
        }
    }

    fun AppCompatButton.setLoading(isLoading: Boolean, loadingText: String = "Yuklanmoqda...") {
        isEnabled = !isLoading
        text = if (isLoading) loadingText else "Davom etish"
        alpha = if (isLoading) 0.6f else 1f
    }

}