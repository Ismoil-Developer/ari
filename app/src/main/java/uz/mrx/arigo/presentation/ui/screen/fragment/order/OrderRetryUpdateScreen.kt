package uz.mrx.arigo.presentation.ui.screen.fragment.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.ScreenOrderRetryUpdateBinding

@AndroidEntryPoint
class OrderRetryUpdateScreen:Fragment(R.layout.screen_order_retry_update) {

    private val binding:ScreenOrderRetryUpdateBinding by viewBinding(ScreenOrderRetryUpdateBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }


    }
}