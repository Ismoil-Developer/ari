package uz.mrx.arigo.presentation.ui.viewmodel.history

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.response.history.OrderHistoryDetailResponse
import uz.mrx.arigo.data.remote.response.history.OrderHistoryResponse

interface HistoryPageViewModel {

    fun openHistoryDetailScreen(id:Int)

    val getHistory:Flow<List<OrderHistoryResponse>>

    fun getHistoryDetail(id: Int)

    val historyDetailResponse:Flow<OrderHistoryDetailResponse>



}