package uz.mrx.arigo.presentation.ui.screen.fragment.news

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.mrx.arigo.R
import uz.mrx.arigo.databinding.ScreenChatBinding
import uz.mrx.arigo.presentation.ui.viewmodel.news.ChatScreenViewModel
import uz.mrx.arigo.presentation.ui.viewmodel.news.impl.ChatScreenViewModelImpl

@AndroidEntryPoint
class ChatScreen:Fragment(R.layout.screen_chat) {

    private val binding:ScreenChatBinding by viewBinding(ScreenChatBinding::bind)
    private val viewModel:ChatScreenViewModel by viewModels<ChatScreenViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        
    }
    
}