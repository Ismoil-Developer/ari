package uz.mrx.arigo.presentation.ui.screen.fragment.detail.magazine

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
import uz.mrx.arigo.data.remote.request.order.OrderRequest
import uz.mrx.arigo.databinding.PageListBinding
import uz.mrx.arigo.presentation.ui.screen.fragment.detail.MagazineDetailScreenArgs
import uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail.MagazineDetailScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail.impl.MagazineDetailScreenViewModelImpl

@AndroidEntryPoint
class ListPage(private val id: Int) : Fragment(R.layout.page_list) {

    private val binding: PageListBinding by viewBinding(PageListBinding::bind)

    private val viewModel: MagazineDetailScreenViewModel by viewModels<MagazineDetailScreenViewModelImpl>()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edtOrder.setMovementMethod(android.text.method.ScrollingMovementMethod.getInstance())

        binding.repeatBtn.setOnClickListener {
            val orderItems = binding.edtOrder.text.toString()

            val request = OrderRequest(orderItems)

            viewModel.createOrder(id, request)

        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.createOrderResponse.collectLatest { response ->
                viewModel.openUpdateOrderScreen(response.id)
            }
        }

    }

}
