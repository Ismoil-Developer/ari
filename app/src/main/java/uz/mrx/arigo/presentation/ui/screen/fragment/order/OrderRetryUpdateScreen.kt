package uz.mrx.arigo.presentation.ui.screen.fragment.order

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRetryRequest
import uz.mrx.arigo.databinding.ScreenOrderRetryUpdateBinding
import uz.mrx.arigo.presentation.ui.viewmodel.order.OrderRetryUpdateScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.order.impl.OrderRetryUpdateScreenViewModelImpl

@AndroidEntryPoint
class OrderRetryUpdateScreen:Fragment(R.layout.screen_order_retry_update) {

    private val binding:ScreenOrderRetryUpdateBinding by viewBinding(ScreenOrderRetryUpdateBinding::bind)
    private val viewModel:OrderRetryUpdateScreenViewModel by viewModels<OrderRetryUpdateScreenViewModelImpl>()
    private val args:OrderRetryUpdateScreenArgs by navArgs()

    private var isChecked = false
    var locationId = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageQuestionsCheck.setOnClickListener {
            isChecked = false
            binding.imageQuestionsUnCheck.visibility = View.VISIBLE
            binding.imageQuestionsCheck.visibility = View.GONE
        }

        binding.imageQuestionsUnCheck.setOnClickListener {
            isChecked = true
            binding.imageQuestionsUnCheck.visibility = View.GONE
            binding.imageQuestionsCheck.visibility = View.VISIBLE
        }

        binding.no.setOnClickListener {
            Log.d("NNNNNNNNN", "onViewCreated: bosildi ${args.id}")
            viewModel.openCancelScreen(args.id)
        }

        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        if (args.id != -1){
            viewModel.getOrderDetail(args.id)
        }

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.getOrderDetailResponse.collectLatest {

                binding.edtOrder.setText(it.items)
                binding.floor.setText(it.floor)
                binding.damophone.setText(it.intercom_code)
                binding.houseNumber.setText(it.house_number)
                binding.otherMessage.setText(it.additional_note)
                binding.appartmentNumber.setText(it.apartment_number)
                binding.title.text = it.shop.title

                binding.address.text = it.user.active_location.address
                binding.customName.text = it.user.phone_number

                locationId = it.user.active_location.id

                if (it.allow_other_shops){
                    binding.imageQuestionsCheck.visibility = View.VISIBLE
                    binding.imageQuestionsUnCheck.visibility = View.GONE
                }else{
                    binding.imageQuestionsCheck.visibility = View.GONE
                    binding.imageQuestionsUnCheck.visibility = View.VISIBLE
                }

            }
        }

        binding.locationChange.setOnClickListener {
            if (locationId != -1){
                viewModel.openAddLocationScreen(locationId)
            }
        }


        binding.yes.setOnClickListener {

            val orderItems = binding.edtOrder.text.toString()
            val houseNumber = binding.houseNumber.text.toString()
            val appartmentNumer = binding.appartmentNumber.text.toString()
            val damophone = binding.damophone.text.toString()
            val floor = binding.floor.text.toString().toIntOrNull() ?: 0
            val otherMessage = binding.otherMessage.text.toString()

            val request = UpdateOrderRetryRequest(orderItems, houseNumber, appartmentNumer, floor, damophone, otherMessage)
            if (args.id != -1){
                viewModel.updateOrderRetry(args.id, request)
            }

        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.updateOrderRetryResponse.collectLatest {

                viewModel.openSearchDeliveryScreen()

                Log.d("UUUUUUU", "onViewCreated: ${it.status}")

            }
        }


    }
}