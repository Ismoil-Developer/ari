package uz.mrx.arigo.presentation.ui.viewmodel.news.impl

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.mrx.arigo.presentation.direction.chat.ChatScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.news.ChatScreenViewModel
import javax.inject.Inject

@HiltViewModel
class ChatScreenViewModelImpl @Inject constructor(private val direction: ChatScreenDirection):ChatScreenViewModel, ViewModel(){



}