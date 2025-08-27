package uz.mrx.arigo.presentation.ui.dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.databinding.DialogFeaturesBinding
import uz.mrx.arigo.presentation.adapter.ShopListAdapter
import uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail.MagazineDetailScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail.impl.MagazineDetailScreenViewModelImpl

class FeatureDialogFragment(private val id:Int,   private val magazineId:Int,  private val onShopSelected: (Int) -> Unit // callback qo‘shamiz
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogFeaturesBinding
    private val viewModel: MagazineDetailScreenViewModel by activityViewModels<MagazineDetailScreenViewModelImpl>()
    private lateinit var shopListAdapter: ShopListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogFeaturesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.queryAddShop(id, magazineId)

        shopListAdapter = ShopListAdapter { selectedShop ->
            onShopSelected(selectedShop.id) // tanlangan do‘konning id sini qaytaramiz
            dismiss() // dialogni yopamiz
        }


        lifecycleScope.launch {
            viewModel.queryAddShopResponse.collectLatest { shopList ->
                shopListAdapter.submitList(shopList)
            }
        }

        binding.rvMagazine.adapter = shopListAdapter


        binding.backRes.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { dlg ->
            val bottomSheet = dlg.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }


}
