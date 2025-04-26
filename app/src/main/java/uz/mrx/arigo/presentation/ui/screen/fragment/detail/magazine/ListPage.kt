package uz.mrx.arigo.presentation.ui.screen.fragment.detail.magazine

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.PageListBinding
import uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail.MagazineDetailScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail.impl.MagazineDetailScreenViewModelImpl

@AndroidEntryPoint
class ListPage:Fragment(R.layout.page_list) {


    private val binding:PageListBinding by viewBinding(PageListBinding::bind)
    private val viewModel: MagazineDetailScreenViewModel by viewModels<MagazineDetailScreenViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.repeatBtn.setOnClickListener {
            viewModel.openSearchDeliveryScreen()
        }

    }
}