package uz.mrx.arigo.presentation.ui.screen.fragment.history

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.ScreenHistoryDetailBinding

@AndroidEntryPoint
class HistoryDetailScreen:Fragment(R.layout.screen_history_detail) {

    private val binding:ScreenHistoryDetailBinding by viewBinding(ScreenHistoryDetailBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }


    }
}