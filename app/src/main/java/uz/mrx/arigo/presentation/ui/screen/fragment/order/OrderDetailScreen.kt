package uz.mrx.arigo.presentation.ui.screen.fragment.order

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.Visibility
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.ScreenOrderDetailBinding
import uz.mrx.arigo.presentation.ui.viewmodel.order.OrderDetailScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.order.impl.OrderDetailScreenViewModelImpl
import uz.mrx.arigo.utils.toast

@AndroidEntryPoint
class OrderDetailScreen:Fragment(R.layout.screen_order_detail) {

    private val binding:ScreenOrderDetailBinding by viewBinding(ScreenOrderDetailBinding::bind)
    private val viewModel:OrderDetailScreenViewModel by viewModels<OrderDetailScreenViewModelImpl>()
    private val args:OrderDetailScreenArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backRes.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.yes.setOnClickListener {
            viewModel.openOrderUpdateRetryScreen(args.id)
        }

        if (args.id != -1){
            viewModel.getOrderDetail(args.id)
            binding.no.setOnClickListener {
                viewModel.openCancelScreen(args.id)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.getOrderDetailResponse.collectLatest {

                binding.workLocationTxt.text = it.user.active_location.address
                binding.workPhoneTxt.text = it.user.phone_number
                binding.edtOrder.text = it.items
                binding.floor.text = it.floor
                binding.damophone.text = it.intercom_code
                binding.appartmentNumber.text = it.apartment_number
                binding.houseNumber.text = it.house_number
                binding.otherMessage.text = it.additional_note
                binding.textRestaurant.text = it.shop.title

                Glide.with(requireContext()).load(it.shop.image).into(binding.viewPagerRes)

                if (it.allow_other_shops){
                    binding.imageQuestionsCheck.visibility = View.VISIBLE
                    binding.imageQuestionsUnCheck.visibility = View.GONE
                }else{
                    binding.imageQuestionsCheck.visibility = View.GONE
                    binding.imageQuestionsUnCheck.visibility = View.VISIBLE
                }

            }
        }

    }

}