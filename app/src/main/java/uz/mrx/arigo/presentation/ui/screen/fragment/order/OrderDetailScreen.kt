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
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.data.remote.request.order.UpdateOrderRetryRequest
import uz.mrx.arigo.databinding.ScreenOrderDetailBinding
import uz.mrx.arigo.presentation.ui.viewmodel.order.OrderDetailScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.order.impl.OrderDetailScreenViewModelImpl

@AndroidEntryPoint
class OrderDetailScreen : Fragment(R.layout.screen_order_detail) {

    private val binding: ScreenOrderDetailBinding by viewBinding(ScreenOrderDetailBinding::bind)
    private val viewModel: OrderDetailScreenViewModel by viewModels<OrderDetailScreenViewModelImpl>()
    private val args: OrderDetailScreenArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backRes.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.yes.setOnClickListener {
            viewModel.openOrderUpdateRetryScreen(args.id)
        }

        if (args.id != -1) {
            viewModel.getOrderDetail(args.id)
            binding.no.setOnClickListener {
                viewModel.openCancelScreen(args.id)
            }
        }

        binding.btnContinue.setOnClickListener {

            val orderItems = binding.edtOrder.text.toString()
            val houseNumber = binding.houseNumber.text.toString().substringAfter(":").trim()
            val appartmentNumer =
                binding.appartmentNumber.text.toString().substringAfter(":").trim()
            val damophone = binding.damophone.text.toString().substringAfter(":").trim()
            val floor = binding.floor.text.toString().substringAfter(":").trim().toIntOrNull() ?: 0
            val otherMessage = binding.otherMessage.text.toString()

            val request = UpdateOrderRetryRequest(
                orderItems,
                houseNumber,
                appartmentNumer,
                floor,
                damophone,
                otherMessage
            )

            viewModel.updateOrderRetry(args.id, request)

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.updateOrderRetryResponse.collectLatest {

                    viewModel.openSearchDeliveryScreen()

                    Log.d("UUUUUUU", "onViewCreated: ${it.status}")

                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.getOrderDetailResponse.collectLatest {

                binding.workLocationTxt.text = it.user.active_location.address
                binding.workPhoneTxt.text = it.user.phone_number
                binding.edtOrder.text = it.items
                binding.otherMessage.text = it.additional_note
                binding.textRestaurant.text = it.shop.title

                binding.floor.text = "Qavat: " + it.floor.toString()
                binding.damophone.text = "Damofon: " + it.intercom_code
                binding.appartmentNumber.text = "Podyezd: " + it.apartment_number
                binding.houseNumber.text = "Uy raqami: " + it.house_number

                Glide.with(requireContext()).load(it.shop.image).into(binding.viewPagerRes)

                if (it.allow_other_shops) {
                    binding.imageQuestionsCheck.visibility = View.VISIBLE
                    binding.imageQuestionsUnCheck.visibility = View.GONE
                } else {
                    binding.imageQuestionsCheck.visibility = View.GONE
                    binding.imageQuestionsUnCheck.visibility = View.VISIBLE
                }

            }
        }
    
        }

}