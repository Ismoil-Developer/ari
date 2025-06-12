package uz.mrx.arigo.presentation.ui.screen.fragment.main.page

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.PageOrderBinding
import uz.mrx.arigo.presentation.adapter.AssignedAdapter
import uz.mrx.arigo.presentation.ui.viewmodel.order.OrderPageViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.order.impl.OrderPageViewModelImpl

@AndroidEntryPoint
class OrderPage:Fragment(R.layout.page_order) {

    private val binding:PageOrderBinding by viewBinding(PageOrderBinding::bind)
    private val viewModel:OrderPageViewModel by viewModels<OrderPageViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapterAssigned = AssignedAdapter {
            viewModel.openOrderDeliveryScreen(it.id)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.assignedResponse.collectLatest {

                adapterAssigned.submitList(it)
//                viewModel.getActiveOrder()
                Log.d("RRRRRRRR", "onViewCreated: ${it.map { it.items }}")
            }
        }

        binding.rv.adapter = adapterAssigned


    }

}