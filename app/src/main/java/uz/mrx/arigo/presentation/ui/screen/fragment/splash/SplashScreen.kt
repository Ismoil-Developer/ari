package uz.mrx.arigo.presentation.ui.screen.fragment.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.mrx.arigo.R
import uz.mrx.arigo.data.local.shp.MySharedPreference
import uz.mrx.arigo.databinding.ScreenSplashBinding
import uz.mrx.arigo.presentation.ui.viewmodel.splash.SplashScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.splash.impl.SplashScreenViewModelImpl
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreen : Fragment(R.layout.screen_splash) {

    private val viewModel: SplashScreenViewModel by viewModels<SplashScreenViewModelImpl>()

    @Inject
    lateinit var sharedPreferences: MySharedPreference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (sharedPreferences.token.isNotEmpty()){
            Log.d("AAAAAA", "onViewCreated: ${sharedPreferences.token}")
            viewModel.openMainScreen()
        }else{
            viewModel.openLanguageScreen()
        }

    }
}