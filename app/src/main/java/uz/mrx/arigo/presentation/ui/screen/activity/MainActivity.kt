package uz.mrx.arigo.presentation.ui.screen.activity

import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.mrx.arigo.R
import uz.mrx.arigo.broadcasts.ConnectivityListener
import uz.mrx.arigo.broadcasts.ConnectivityReceiver
import uz.mrx.arigo.presentation.navigation.NavigationHandler
import uz.mrx.arigo.presentation.ui.dialog.NoInternetDialog
import javax.inject.Inject


@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ConnectivityListener {

    @Inject
    lateinit var handler: NavigationHandler

    private var noInternetDialog: NoInternetDialog? = null
    private lateinit var connectivityReceiver : ConnectivityReceiver

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectivityReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(connectivityReceiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connectivityReceiver = ConnectivityReceiver(this)

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)

        // Status bar iconlarini qora qilish
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT // Status bar fonini qora qilish

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        handler.navigationStack.onEach {
            it.invoke(navHost.navController)
        }.launchIn(lifecycleScope)

    }

    override fun onNetworkChange(isConnected: Boolean) {

        if (isConnected) {
            noInternetDialog?.dismiss()
            noInternetDialog = null // Dialogni tozalash

        } else {
            if (noInternetDialog == null || noInternetDialog?.isShowing() == false) {
                noInternetDialog = NoInternetDialog(this)
                noInternetDialog?.show()
            }
        }
    }

}