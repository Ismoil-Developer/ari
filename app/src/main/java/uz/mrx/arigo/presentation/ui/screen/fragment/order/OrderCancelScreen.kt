package uz.mrx.arigo.presentation.ui.screen.fragment.order

import android.os.Bundle
import android.util.Log
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
import uz.mrx.arigo.data.model.OrderCancelData
import uz.mrx.arigo.data.remote.request.order.OrderCancelRequest
import uz.mrx.arigo.databinding.ScreenCancelOrderBinding
import uz.mrx.arigo.presentation.adapter.CancelAdapter
import uz.mrx.arigo.presentation.ui.viewmodel.order.OrderCancelViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.order.impl.OrderCancelViewModelImpl

@AndroidEntryPoint
class OrderCancelScreen : Fragment(R.layout.screen_cancel_order) {

    private val binding: ScreenCancelOrderBinding by viewBinding(ScreenCancelOrderBinding::bind)
    private val viewModel: OrderCancelViewModel by viewModels<OrderCancelViewModelImpl>()
    private val args: OrderCancelScreenArgs by navArgs()

    lateinit var list: ArrayList<OrderCancelData>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()

        var reason = ""

        val adapter = CancelAdapter {
            reason = it.reason
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cancelResponse.collectLatest {
                Log.d("EEEEEEE", "onViewCreated: ${it.status}")
            }
        }

        binding.btnContinue.setOnClickListener {
            viewModel.cancelOrder(args.id, OrderCancelRequest("Sababsiz"))
        }

        adapter.submitList(list)

        binding.rvCancelOrder.adapter = adapter

    }

    fun loadData() {
        list = ArrayList()
        list.add(OrderCancelData(1, "Sababsiz"))
        list.add(OrderCancelData(2, "Fikrimni o'zgartirdim"))
        list.add(OrderCancelData(3, "Kuryer bilan bog'lana olmadim"))
        list.add(OrderCancelData(4, "Mahsulotlar kerak bo'lmay qoldi"))
    }

}