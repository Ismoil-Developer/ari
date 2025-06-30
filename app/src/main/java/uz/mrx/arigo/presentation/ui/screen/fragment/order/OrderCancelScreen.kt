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
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.data.model.OrderCancelData
import uz.mrx.arigo.data.remote.request.order.OrderCancelRequest
import uz.mrx.arigo.databinding.ScreenCancelOrderBinding
import uz.mrx.arigo.presentation.adapter.CancelAdapter
import uz.mrx.arigo.presentation.ui.dialog.CancelDialog
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


        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        var reason = ""

        val adapter = CancelAdapter { selectedItem ->
            if (selectedItem.reason == "Boshqa sababdan") {
                binding.editText.visibility = View.VISIBLE
            } else {
                binding.editText.visibility = View.GONE
                reason = selectedItem.reason
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cancelResponse.collectLatest { response ->
                // Agar response muvaffaqiyatli bo'lsa
                if (response.status == "success") {

                    val dialog = CancelDialog(requireContext()) {
                        // "Davom etish" bosilganda nima qilish kerakligini yozing
                        viewModel.openMainScreen()
                    }

                    dialog.show()

                }
            }
        }


        binding.btnContinue.setOnClickListener {
            val finalReason = if (binding.editText.visibility == View.VISIBLE) {
                binding.editText.text.toString().trim()
            } else {
                reason
            }

            if (finalReason.isEmpty()) {
                Toast.makeText(requireContext(), "Iltimos, bekor qilish sababini tanlang yoki yozing", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.cancelOrder(args.id, OrderCancelRequest(finalReason))
            }
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
        list.add(OrderCancelData(5, "Boshqa sababdan")) // Buni kiritish muhim
    }


}