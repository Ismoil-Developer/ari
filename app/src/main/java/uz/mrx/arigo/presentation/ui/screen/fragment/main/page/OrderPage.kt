package uz.mrx.arigo.presentation.ui.screen.fragment.main.page

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.PageOrderBinding
import uz.mrx.arigo.presentation.adapter.AssignedAdapter
import uz.mrx.arigo.presentation.adapter.PendingAdapter
import uz.mrx.arigo.presentation.adapter.SearchingAdapter
import uz.mrx.arigo.presentation.ui.viewmodel.order.OrderPageViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.order.impl.OrderPageViewModelImpl

@AndroidEntryPoint
class OrderPage:Fragment(R.layout.page_order) {

    private val binding:PageOrderBinding by viewBinding(PageOrderBinding::bind)
    private val viewModel:OrderPageViewModel by viewModels<OrderPageViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val adapterAssigned = AssignedAdapter {
            viewModel.openOrderDeliveryScreen(it.id)
        }

        binding.rvActive.adapter = adapterAssigned

        val pendingAdapter = PendingAdapter {
            viewModel.openOrderDetailScreen(it.id)
        }

        binding.rvDisActive.adapter = pendingAdapter

        var assignedListIsEmpty = true
        var pendingListIsEmpty = true

        // Assigned
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.assignedResponse.collectLatest { list ->
                assignedListIsEmpty = list.isEmpty()
                adapterAssigned.submitList(list)

                binding.containerActive.visibility = if (list.isNotEmpty()) View.VISIBLE else View.GONE

                // Har ikkalasini tekshir va visibility yangila
                updateContainersVisibility(assignedListIsEmpty, pendingListIsEmpty)
            }
        }

        // Pending
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.orderPending.collectLatest { list ->
                pendingListIsEmpty = list.isEmpty()
                pendingAdapter.submitList(list)

                binding.containerDisActive.visibility = if (list.isNotEmpty()) View.VISIBLE else View.GONE

                // Har ikkalasini tekshir va visibility yangila
                updateContainersVisibility(assignedListIsEmpty, pendingListIsEmpty)
            }
        }
    }

    private fun updateContainersVisibility(assignedEmpty: Boolean, pendingEmpty: Boolean) {
        if (assignedEmpty && pendingEmpty) {
            binding.containerEmpty.visibility = View.VISIBLE
            binding.containerActive.visibility = View.GONE
            binding.containerDisActive.visibility = View.GONE
        } else {
            binding.containerEmpty.visibility = View.GONE
        }
    }

}