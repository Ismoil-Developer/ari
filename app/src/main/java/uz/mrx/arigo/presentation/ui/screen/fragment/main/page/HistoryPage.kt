package uz.mrx.arigo.presentation.ui.screen.fragment.main.page

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.PageHistoryBinding

@AndroidEntryPoint
class HistoryPage:Fragment(R.layout.page_history) {


    private val binding:PageHistoryBinding by viewBinding(PageHistoryBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

}