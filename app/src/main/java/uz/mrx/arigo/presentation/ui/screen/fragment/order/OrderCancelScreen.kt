package uz.mrx.arigo.presentation.ui.screen.fragment.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.ScreenCancelOrderBinding

@AndroidEntryPoint
class OrderCancelScreen:Fragment(R.layout.screen_cancel_order) {


    private val binding:ScreenCancelOrderBinding by viewBinding(ScreenCancelOrderBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }
}