package uz.mrx.arigo.presentation.ui.screen.fragment.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.ScreenOrderCompletedBinding

@AndroidEntryPoint
class OrderCompletedScreen:Fragment(R.layout.screen_order_completed) {

    private val binding:ScreenOrderCompletedBinding by viewBinding(ScreenOrderCompletedBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }
}