package uz.mrx.arigo.presentation.ui.viewmodel.shop.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mrx.arigo.data.remote.response.feature.shoplist.ShopListResponse
import uz.mrx.arigo.domain.usecase.feature.FeatureUseCase
import uz.mrx.arigo.presentation.direction.shop.ShopListScreenDirection
import uz.mrx.arigo.presentation.navigation.Navigator
import uz.mrx.arigo.presentation.ui.viewmodel.shop.ShopListScreenViewModel
import uz.mrx.arigo.utils.flow
import javax.inject.Inject

@HiltViewModel
class ShopListScreenViewModelImpl @Inject constructor(private val direction: ShopListScreenDirection, private val useCase: FeatureUseCase):ShopListScreenViewModel, ViewModel() {

    override fun openMapSearchScreen(id: Int) {
        viewModelScope.launch {
            direction.openMapSearchScreen(id)
        }
    }

    override fun openShopList(id: Int) {
        viewModelScope.launch {
            useCase.getShopList(id).collectLatest {
                it.onSuccess {
                    responseShopList.tryEmit(it)
                }
                it.onError {

                }
            }
        }
    }

    override val responseShopList = flow<List<ShopListResponse>>()

    override fun openShopDetailScreen(id: Int) {

        viewModelScope.launch {
            direction.openShopDetailScreen(id)
        }

    }

    override fun openShopSearchList(id: Int, query: String) {
        viewModelScope.launch {
            useCase.getShopSearchList(id, query).collectLatest { result ->
                result.onSuccess {
                    responseShopList.tryEmit(it)
                }
                result.onError {

                }
            }
        }
    }



}