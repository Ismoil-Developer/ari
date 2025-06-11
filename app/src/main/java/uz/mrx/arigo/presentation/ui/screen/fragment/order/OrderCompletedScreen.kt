package uz.mrx.arigo.presentation.ui.screen.fragment.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.ScreenOrderCompletedBinding
import uz.mrx.arigo.presentation.ui.viewmodel.order.OrderCompletedScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.order.impl.OrderCompletedScreenViewModelImpl

@AndroidEntryPoint
class OrderCompletedScreen:Fragment(R.layout.screen_order_completed) {

    private val binding:ScreenOrderCompletedBinding by viewBinding(ScreenOrderCompletedBinding::bind)
    private val viewModel: OrderCompletedScreenViewModel by viewModels<OrderCompletedScreenViewModelImpl>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            println("Tanlangan yulduzlar soni: $rating")
        }

        binding.btnSkip.setOnClickListener {
            viewModel.openOrderCompletedScreen()
        }

        binding.btnSubmit.setOnClickListener {

//            comment = binding.edtNumber.text.toString()
//
//            viewModel.postFeedBack(args.id, OrderFeedBackRequest(ratingg, comment))
            viewModel.openOrderCompletedScreen()

        }

    }
}