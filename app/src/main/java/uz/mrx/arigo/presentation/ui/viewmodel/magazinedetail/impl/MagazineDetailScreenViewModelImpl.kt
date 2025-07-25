package uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.data.remote.request.order.OrderRequest
import uz.mrx.arigo.data.remote.response.feature.all.ShopAllResponse
import uz.mrx.arigo.data.remote.response.feature.detail.FeatureDetailResponse
import uz.mrx.arigo.data.remote.response.feature.feedback.FeedBackRequest
import uz.mrx.arigo.data.remote.response.feature.feedback.FeedBackResponse
import uz.mrx.arigo.data.remote.response.feature.shoplist.ShopListResponse
import uz.mrx.arigo.data.remote.response.order.OrderResponse
import uz.mrx.arigo.domain.usecase.feature.FeatureUseCase
import uz.mrx.arigo.domain.usecase.order.OrderUseCase
import uz.mrx.arigo.presentation.direction.magazinedetail.MagazineDetailScreenDirection
import uz.mrx.arigo.presentation.ui.viewmodel.magazinedetail.MagazineDetailScreenViewModel
import uz.mrx.arigo.utils.flow
import javax.inject.Inject

@HiltViewModel
class MagazineDetailScreenViewModelImpl @Inject constructor(private val direction: MagazineDetailScreenDirection, private val useCase: FeatureUseCase, private val orderUseCase: OrderUseCase):MagazineDetailScreenViewModel, ViewModel() {


    override fun postFeedBack(id: Int, feedBackRequest: FeedBackRequest) {
        viewModelScope.launch {
            useCase.postFeedBack(id, feedBackRequest).collectLatest {
                it.onSuccess {
                    feedBackResponse.tryEmit(it)
                }
                it.onError {

                }
            }
        }
    }

    override val feedBackResponse = flow<FeedBackResponse>()

    override fun openUpdateOrderScreen(id: Int) {
        viewModelScope.launch {
            direction.openUpdateOrderScreen(id)
        }
    }

    override val featuresDetailResponse = flow<FeatureDetailResponse>()

    override fun getFeaturesDetail(id: Int) {

        viewModelScope.launch(Dispatchers.IO){
            useCase.getFeaturesDetail(id).collectLatest {
                it.onSuccess {
                    featuresDetailResponse.emit(it)
                }
                it.onError {

                }
            }
        }

    }

    override val createOrderResponse = flow<OrderResponse>()

    override fun createOrder(id: Int, orderRequest: OrderRequest) {

        viewModelScope.launch {
            orderUseCase.createOrder(id, orderRequest).collectLatest {
                it.onSuccess {
                    createOrderResponse.tryEmit(it)
                }
                it.onError {

                }
            }
        }
    }

    override val getFeaturesResponse = flow<List<ShopListResponse>>()

    override fun getFeaturesById(id: Int) {
        viewModelScope.launch {
            useCase.getShopList(id).collectLatest {

                it.onSuccess {

                    getFeaturesResponse.tryEmit(it)

                }
                it.onError {

                }

            }
        }
    }
}