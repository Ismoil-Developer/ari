package uz.mrx.arigo.presentation.ui.screen.fragment.main.page

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.PageHistoryBinding
import uz.mrx.arigo.presentation.adapter.HistoryAdapter
import uz.mrx.arigo.presentation.ui.viewmodel.history.HistoryPageViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.history.impl.HistoryPageViewModelImpl

@AndroidEntryPoint
class HistoryPage : Fragment(R.layout.page_history) {

    private val binding: PageHistoryBinding by viewBinding(PageHistoryBinding::bind)
    private val viewModel: HistoryPageViewModel by viewModels<HistoryPageViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val historyAdapter = HistoryAdapter {
            viewModel.openHistoryDetailScreen()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getHistory.collectLatest {

                if (it.isEmpty()) {
                    // Show empty views, hide RecyclerView
                    binding.emptyTitle.isVisible = true
                    binding.emptyDetail.isVisible = true
                    binding.emptyHistory.isVisible = true
                    binding.rv.isGone = true
                } else {
                    // Show RecyclerView, hide empty views
                    binding.emptyTitle.isGone = true
                    binding.emptyDetail.isGone = true
                    binding.emptyHistory.isGone = true
                    binding.rv.isVisible = true
                }
                historyAdapter.submitList(it)
            }
        }

        binding.rv.adapter = historyAdapter

    }

}