package uz.mrx.arigo.presentation.ui.screen.fragment.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.ScreenMainBinding
import uz.mrx.arigo.presentation.ui.viewmodel.main.MainScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.main.impl.MainScreenViewModelImpl

@AndroidEntryPoint
class MainScreen : Fragment(R.layout.screen_main){

    private val binding: ScreenMainBinding by viewBinding(ScreenMainBinding::bind)
    private val viewModel:MainScreenViewModel by viewModels<MainScreenViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)


        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.orderPage -> {
                    // bu yerda koordinatalar real holatda olinadi
                    val coordinates = "41.311081,69.240562" // test uchun
                    viewModel.openOrderDeliveryScreen(coordinates, -1)
                    true // bu item tanlandi deb qaytadi
                }
                else -> {
                    navController.navigate(item.itemId) // boshqa itemlar uchun navigatsiya
                    true
                }
            }
        }



    }

}