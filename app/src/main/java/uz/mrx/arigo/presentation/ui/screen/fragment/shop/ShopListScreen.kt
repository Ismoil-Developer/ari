package uz.mrx.arigo.presentation.ui.screen.fragment.shop

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.addTextChangedListener
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
import uz.mrx.arigo.databinding.ScreenListShopBinding
import uz.mrx.arigo.presentation.adapter.ShopListAdapter
import uz.mrx.arigo.presentation.ui.viewmodel.shop.ShopListScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.shop.impl.ShopListScreenViewModelImpl

@AndroidEntryPoint
class ShopListScreen : Fragment(R.layout.screen_list_shop) {

    private val binding: ScreenListShopBinding by viewBinding(ScreenListShopBinding::bind)
    private val viewModel: ShopListScreenViewModel by viewModels<ShopListScreenViewModelImpl>()
    private val args: ShopListScreenArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val adapter = ShopListAdapter {
            viewModel.openShopDetailScreen(it.id)
        }

        binding.icBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Set up the adapter for RecyclerView
        binding.rv.adapter = adapter

        // Collect the results from ViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.responseShopList.collectLatest { shopList ->
                adapter.submitList(shopList)
            }
        }

        // Observe search EditText changes
        binding.searchEdt.addTextChangedListener { editable ->
            val query = editable.toString()
            if (query.isNotBlank()) {
                // Call ViewModel's search method when the text changes
            } else {
                // Optionally, you can clear the adapter when search is empty
                viewModel.openShopList(args.id)
            }
        }

        binding.searchEdt.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    viewModel.openShopSearchList(args.id, it.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        // Handle Map Search Click
        binding.map.setOnClickListener {
            viewModel.openMapSearchScreen(args.id)
        }

        // Initially load the shop list
        viewModel.openShopList(args.id)

    }
}