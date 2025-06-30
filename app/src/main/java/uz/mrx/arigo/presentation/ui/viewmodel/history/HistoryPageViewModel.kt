package uz.mrx.arigo.presentation.ui.viewmodel.history

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.response.history.OrderHistoryResponse

interface HistoryPageViewModel {

    fun openHistoryDetailScreen()

    val getHistory:Flow<List<OrderHistoryResponse>>


}