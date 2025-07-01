package uz.mrx.arigo.presentation.ui.screen.fragment.history

import android.os.Bundle
import android.view.View
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
import uz.mrx.arigo.databinding.ScreenHistoryDetailBinding
import uz.mrx.arigo.presentation.ui.viewmodel.history.HistoryPageViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.history.impl.HistoryPageViewModelImpl

@AndroidEntryPoint
class HistoryDetailScreen:Fragment(R.layout.screen_history_detail) {

    private val binding:ScreenHistoryDetailBinding by viewBinding(ScreenHistoryDetailBinding::bind)
    private val viewModel: HistoryPageViewModel by viewModels<HistoryPageViewModelImpl>()

    private val args:HistoryDetailScreenArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        if (args.id != -1){
            viewModel.getHistoryDetail(args.id)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.historyDetailResponse.collectLatest {

                binding.date.text = it.created_at
                binding.contractNumber.text = it.shop_id.toString()
                binding.shopName.text = it.shop_title
                binding.shopName2.text = it.shop_title
                binding.addressRegion.text = it.user_location_address

            }
        }

    }
}