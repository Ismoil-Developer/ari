package uz.mrx.arigo.presentation.ui.screen.fragment.detail.magazine

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.data.remote.request.order.OrderRequest
import uz.mrx.arigo.databinding.PageListBinding
import uz.mrx.arigo.presentation.ui.dialog.FeatureDialogFragment
import uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail.MagazineDetailScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail.impl.MagazineDetailScreenViewModelImpl

@AndroidEntryPoint
class ListPage(private val id: Int, private val roleId:Int) : Fragment(R.layout.page_list) {

    private val binding: PageListBinding by viewBinding(PageListBinding::bind)

    private val viewModel: MagazineDetailScreenViewModel by viewModels<MagazineDetailScreenViewModelImpl>()

    private var isChecked = false

    private var additionalShopId: Int? = null // bu tanlangan shopId ni saqlaydi

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.edtOrder.setMovementMethod(android.text.method.ScrollingMovementMethod.getInstance())


        Log.d("ROLEID", "onViewCreated: $roleId")
        val hintResId = when (roleId) {
            1 -> R.string.hint_magazine_order
            2 -> R.string.hint_pharmacy_order
            else -> R.string.hechnima // yoki boshqa default hint
        }

        binding.edtOrder.setHint(hintResId)


        binding.addShop.setOnClickListener {
            val dialog = FeatureDialogFragment(roleId, id) { selectedShopId ->
                additionalShopId = selectedShopId
                viewModel.getAdditionalShop(selectedShopId) // ✅ tanlangandan keyin chaqiriladi
            }

            dialog.show(parentFragmentManager, "FeatureDialog")
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.additionalShopResponse.collectLatest { response ->
                Log.d("ADDITIONAL", "onViewCreated: ${response.image} ")

                Glide.with(requireContext())
                    .load(response.image)
                    .into(binding.imageView)

                binding.title.text = response.title
            }
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

        binding.repeatBtn.setOnClickListener {

            if (!it.isEnabled) return@setOnClickListener

            if (additionalShopId == null) {
                // ⚠️ Agar shop tanlanmagan bo‘lsa
                Toast.makeText(requireContext(), "Iltimos, avval qo‘shimcha magazin tanlang", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val orderItems = binding.edtOrder.text.toString()

            val request = OrderRequest(
                items = orderItems,
                allow_other_shops = isChecked,
                additional_shop = additionalShopId // ✅ endi null bo‘lishi mumkin emas
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