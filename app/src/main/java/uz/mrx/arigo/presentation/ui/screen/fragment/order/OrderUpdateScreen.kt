package uz.mrx.arigo.presentation.ui.screen.fragment.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRequest
import uz.mrx.arigo.databinding.ScreenOrderUpdateBinding
import uz.mrx.arigo.presentation.ui.viewmodel.order.UpdateOrderScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.order.impl.UpdateOrderScreenViewModelImpl


@AndroidEntryPoint
class OrderUpdateScreen : Fragment(R.layout.screen_order_update) {

    private val binding: ScreenOrderUpdateBinding by viewBinding(ScreenOrderUpdateBinding::bind)
    private val viewModel:UpdateOrderScreenViewModel by viewModels<UpdateOrderScreenViewModelImpl>()

    private val args:OrderUpdateScreenArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnContinueLn.setOnClickListener {

            val houseNumber = binding.houseNumber.text.toString()
            val appartmentNumer = binding.appartmentNumber.text.toString()
            val damophone = binding.damophone.text.toString()
            val floor = binding.floor.text.toString()
            val otherMessage = binding.otherMessage.text.toString()

            if (args.id != -1){
                viewModel.updateOrder(args.id, UpdateOrderRequest(houseNumber, appartmentNumer, floor.toInt(), damophone, otherMessage))
            }


        }

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.updateResponse.collectLatest {

                viewModel.openSearchScreenViewModel()

            }

        }


    }
}