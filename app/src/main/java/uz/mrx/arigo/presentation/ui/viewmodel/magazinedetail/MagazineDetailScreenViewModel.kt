package uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail

import kotlinx.coroutines.flow.Flow
import uz.mrx.arigo.data.remote.request.order.OrderRequest
import uz.mrx.arigo.data.remote.response.feature.detail.FeatureDetailResponse
import uz.mrx.arigo.data.remote.response.feature.feedback.FeedBackRequest
import uz.mrx.arigo.data.remote.response.feature.feedback.FeedBackResponse
import uz.mrx.arigo.data.remote.response.order.OrderResponse

interface MagazineDetailScreenViewModel {

    fun postFeedBack(id: Int, feedBackRequest: FeedBackRequest)

    val feedBackResponse:Flow<FeedBackResponse>

    fun openUpdateOrderScreen(id:Int)

    fun getFeaturesDetail(id: Int)

    val featuresDetailResponse: Flow<FeatureDetailResponse>

    fun createOrder(id: Int, request: OrderRequest)

    val createOrderResponse: Flow<OrderResponse>


}
