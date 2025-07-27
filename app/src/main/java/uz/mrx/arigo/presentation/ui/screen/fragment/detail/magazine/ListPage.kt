package uz.mrx.arigo.presentation.ui.screen.fragment.detail.magazine

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.data.remote.request.order.OrderRequest
import uz.mrx.arigo.databinding.PageListBinding
import uz.mrx.arigo.presentation.ui.dialog.FeatureDialogFragment
import uz.mrx.arigo.presentation.ui.screen.fragment.detail.MagazineDetailScreenArgs
import uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail.MagazineDetailScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail.impl.MagazineDetailScreenViewModelImpl

@AndroidEntryPoint
class ListPage(private val id: Int) : Fragment(R.layout.page_list) {

    private val binding: PageListBinding by viewBinding(PageListBinding::bind)

    private val viewModel: MagazineDetailScreenViewModel by viewModels<MagazineDetailScreenViewModelImpl>()

    private var isChecked = false
    private var additionalShopId: Int? = null // bu tanlangan shopId ni saqlaydi


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edtOrder.setMovementMethod(android.text.method.ScrollingMovementMethod.getInstance())

        if (id != -1){
            viewModel.getAdditionalShop(id)

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.additionalShopResponse.collectLatest {
                    binding.title.text = it.title
                    Glide.with(requireContext()).load(it.image).into(binding.lock)
                }
            }

        }

        binding.imageQuestionsUnCheck.setOnClickListener {
            isChecked = true
            binding.imageQuestionsUnCheck.visibility = View.GONE
            binding.imageQuestionsCheck.visibility = View.VISIBLE

            // isChecked true bo'lsa, dialog ochiladi
            val dialog = FeatureDialogFragment(1) { selectedShopId ->
                additionalShopId = selectedShopId
            }

            dialog.show(parentFragmentManager, "FeatureDialog")


        }


        binding.repeatBtn.isEnabled = false
        binding.repeatBtn.setCardBackgroundColor(
            ContextCompat.getColor(requireContext(), R.color.buttonBgColorFalse)
        )


        binding.edtOrder.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString().trim()
                if (input.isEmpty()) {
                    // Tugma faolsiz holatda
                    binding.repeatBtn.isEnabled = false
                    binding.repeatBtn.setCardBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.buttonBgColorFalse)
                    )
                } else {
                    // Tugma faol holatda
                    binding.repeatBtn.isEnabled = true
                    binding.repeatBtn.setCardBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.buttonBgColor)
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })


        binding.imageQuestionsCheck.setOnClickListener {
            isChecked = false
            binding.imageQuestionsUnCheck.visibility = View.VISIBLE
            binding.imageQuestionsCheck.visibility = View.GONE
        }



        binding.repeatBtn.setOnClickListener {
            if (!it.isEnabled) return@setOnClickListener

            val orderItems = binding.edtOrder.text.toString()
            val additionalShop = additionalShopId ?: 0 // hech narsa tanlanmagan bo‘lsa, yubormaslik

            val request = OrderRequest(
                items = orderItems,
                allow_other_shops = isChecked,
                additional_shop = additionalShop
            )

            viewModel.createOrder(id, request)
        }



        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.createOrderResponse.collectLatest { response ->
                viewModel.openUpdateOrderScreen(response.id)
            }
        }



    }

}
